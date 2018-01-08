package com.tabeldata.android.mqtt.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.LocalDateTime;

public class MainActivity extends AppCompatActivity implements MqttCallback {

    private static final String username = "rtyoepgj";
    private static final String password = "f6kj3JqI7nAv";
    private MqttAndroidClient connection;

    private Switch onOffRoom1;

    public MqttConnectOptions getOptions(String username, String password) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);

        return options;
    }

    public MqttAndroidClient getConnection(String brokerServer) throws MqttException {
        String clientId = MqttClient.generateClientId();
        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), brokerServer, clientId);
        return client;
    }

    public void publish(Boolean onOff) {
        try {
            MqttMessage message;
            if (onOff)
                message = new MqttMessage("halo ini dari mqtt publisher on".getBytes());
            else
                message = new MqttMessage("halo ini dari mqtt publish off".getBytes());

            IMqttDeliveryToken publisher =
                    this.connection.publish(
                            "room1/lamp",
                            message);
            publisher.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("submitPublisher", "Submit publisher");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("submitBublisher", "message erorr");
                }
            });

        } catch (MqttException e) {
            Log.e("message", "submit published error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setup field
        this.onOffRoom1 = findViewById(R.id.onOffRoom1);
        this.onOffRoom1.setOnCheckedChangeListener((compoundButton, checked) -> {
            publish(checked);
        });
        try {
            MqttConnectOptions options = getOptions(this.username, this.password);
            this.connection = getConnection("tcp://m14.cloudmqtt.com:13568");
            connection.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable cause) {
        Log.e("connectionLost", cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i("topicArrived", "topic yang terkirim " + topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i("topicComplete", "token submited");
    }
}
