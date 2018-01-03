package com.tabeldata.android.mqtt.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    public MqttAndroidClient getConnection(String brokerServer, String username, char[] password) throws MqttException {
        String clientId = MqttClient.generateClientId();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password);

        MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), brokerServer, clientId);

        IMqttToken token = client.connect(options);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // We are connected
                Log.i("connection", "onSuccess");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                // Something went wrong e.g. connection timeout or firewall problems
                Log.e("comunitationError", exception.getMessage());

            }
        });
        return client;
    }

    public void connectTo(View view) {
        try {
            MqttAndroidClient connection = getConnection(
                    "tcp://m14.cloudmqtt.com:13568",
                    "rtyoepgj",
                    "f6kj3JqI7nAv".toCharArray());
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
