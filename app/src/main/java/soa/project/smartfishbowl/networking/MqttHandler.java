package soa.project.smartfishbowl.networking;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soa.project.smartfishbowl.state_machine.StateLiveData;

/**
 * Singleton para manejar la conexiones de MQTT.
 */
public final class MqttHandler implements MqttCallback
{
  private static volatile MqttHandler instance;
  private static MqttAsyncClient client;
  private final List<String> events = new ArrayList<>();

  public List<String> getEvents()
  {
    return this.events;
  }

  /**
   * Devuelve una instancia de este singleton.
   */
  public static MqttHandler getInstance()
  {
    MqttHandler result = instance;
    if (result != null)
    {
      return result;
    }

    synchronized (MqttHandler.class)
    {
      if (instance == null)
      {
        instance = new MqttHandler();
      }
      return instance;
    }
  }

  public void connect(String brokerUrl, String clientId)
  {
    try
    {
      MqttConnectOptions options = new MqttConnectOptions();
      options.setUserName(clientId);
      // La contraseña no importa, solo el token como clientId.
      options.setPassword("a".toCharArray());
      // Queremos mantener el estado previo de la sesion.
      options.setCleanSession(false);

      MemoryPersistence persistence = new MemoryPersistence();

      client = new MqttAsyncClient(brokerUrl, clientId, persistence);
      client.connect(options).waitForCompletion();
      client.setCallback(this);
      Log.i("Ubidots", "Conexión establecida!");
    }
    catch (MqttException e)
    {
      Log.e("Ubidots", e.getMessage() + "  " + e.getCause());
    }
  }

  @Override
  public void connectionLost(Throwable cause)
  {
    Log.e("Ubidots", "Conexion perdida: " + cause.getMessage());
    try
    {
      client.reconnect();
      Log.i("Ubidots", "Conexión establecida!");
    }
    catch (MqttException e)
    {
      Log.e("Ubidots", e.getMessage() + "  " + e.getCause());
    }
  }

  @Override
  public void messageArrived(String topic, MqttMessage message)
  {
    String msgJson = message.toString();
    Log.i("Ubidots", "New message: " + msgJson);

    events.add(msgJson);

    try
    {
      // Parseamos el mensaje para obtener el valor (nuevo estado).
      JSONObject json = new JSONObject(message.toString());
      float value = Float.parseFloat(json.getString("value"));
      StateLiveData.setState((int) value);
    }
    catch (Exception e)
    {
      Log.i("Ubidots", e.getMessage() + " " + e.getCause());
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {}

  public void subscribe(String topic)
  {
    try
    {
      client.subscribe(topic, 0);
      Log.i("Ubidots", "Subscribing to topic: " + topic);
    }
    catch (MqttException e)
    {
      Log.e("Ubidots", e.getMessage() + "  " + e.getCause());
    }
  }

  public void publish(String topic, String message)
  {
    try
    {
      MqttMessage mqttMessage = new MqttMessage(message.getBytes());
      mqttMessage.setQos(0);
      client.publish(topic, mqttMessage);
      Log.i("Ubidots", "Publishing message: " + message);
    }
    catch (MqttException e)
    {
      Log.e("Ubidots", e.getMessage() + "  " + e.getCause());
    }
  }

  public boolean isConnected()
  {
    return client != null && client.isConnected();
  }
}
