package com.example.smartfishbowl;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MqttHandler implements MqttCallback{
    private MqttClient client;
    private Context mContext;

    public MqttHandler(Context mContext){

        this.mContext = mContext;
    }
    public void connect( String brokerUrl, String clientId, String user, String pass) {
        try {


            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(user);
            options.setPassword(pass.toCharArray());
            options.setCleanSession(true);


            MemoryPersistence persistence = new MemoryPersistence();

            client = new MqttClient(brokerUrl, clientId, persistence);
            client.connect(options);

            client.setCallback(this);

        } catch (MqttException e) {
            Log.d("Aplicacion",e.getMessage()+ "  "+e.getCause());
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void connectionLost(Throwable cause) {
        Log.d("MAIN ACTIVITY","conexion perdida"+ cause.getMessage().toString());
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msgJson=message.toString();

        JSONObject json = new JSONObject(message.toString());
        //Float valor = Float.parseFloat(json.getString("value"));
        System.out.println(msgJson);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }



}
