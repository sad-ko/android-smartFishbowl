package com.example.smartfishbowl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MqttHandler implements MqttCallback
{
  private static MqttClient client;
  private final Context mContext;

  private List<String> events = new ArrayList<String>();

  public List<String> getEvents()
  {
    return this.events;
  }
  public MqttHandler(Context mContext)
  {
    this.mContext = mContext;
  }

  public void connect(String brokerUrl, String clientId)
  {
    try
    {
      MqttConnectOptions options = new MqttConnectOptions();
      options.setUserName(clientId);
      options.setPassword("a".toCharArray());
      options.setCleanSession(true);

      MemoryPersistence persistence = new MemoryPersistence();

      client = new MqttClient(brokerUrl, clientId, persistence);
      client.connect(options);
      client.setCallback(this);
    } catch (MqttException e)
    {
      Log.d("Ubidots", e.getMessage() + "  " + e.getCause());
    }
  }

  public void disconnect()
  {
    try
    {
      client.disconnect();
    } catch (MqttException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void connectionLost(Throwable cause)
  {
    Log.d("Ubidots", "Conexion perdida: " + cause.getMessage());
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception
  {
    String msgJson = message.toString();
    JSONObject json = new JSONObject(message.toString());
    //Float valor = Float.parseFloat(json.getString("value"));
    Log.i("Ubidots", msgJson);
    this.events.add(msgJson);
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token)
  {

  }

  public void subscribe(String topic)
  {
    try
    {
      client.subscribe(topic);
    } catch (MqttException e)
    {
      e.printStackTrace();
    }
  }

  public void publishMessage(String topic, Integer message)
  {

    Toast.makeText(mContext, "Publishing message: " + message, Toast.LENGTH_SHORT).show();
    this.publish(topic, message);
  }

  public void publish(String topic, Integer message)
  {
    try
    {
      MqttMessage mqttMessage = new MqttMessage(message.toString().getBytes());
      mqttMessage.setQos(0);
      client.publish(topic, mqttMessage);
    } catch (MqttException e)
    {
      e.printStackTrace();
    }
  }
}
