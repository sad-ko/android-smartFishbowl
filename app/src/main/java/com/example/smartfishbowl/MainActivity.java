package com.example.smartfishbowl;


import static java.sql.DriverManager.println;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.ConsoleHandler;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MainActivity<MqttMessage> extends AppCompatActivity
{
  private static final String BROKER_URL = "tcp://industrial.api.ubidots.com:1883";
  private static final String CLIENT_ID = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj";

  private static final String USER = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj";

  private static final String PASS = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj";

  private MqttHandler mqttHandler;
  private Button cmdFeedFishes;
  private Button cmdCleanTank;
  private Queue<Integer> mqttToQueue;

  private MqttClient client;//borrar
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.actionsButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(MainActivity.this, ActionsActivity.class);
      intent.putExtra("origin", "main");
      startActivity(intent);
      finish();
    });

    Bundle bundle = getIntent().getExtras();
    if (bundle != null)
    {
      String origin = bundle.getString("origin");
      if (origin != null && origin.equals("actions"))
      {
        findViewById(R.id.focused).startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_action_to_home));
      }
    }
    mqttHandler = new MqttHandler(getApplicationContext());
    mqttHandler.connect(BROKER_URL,CLIENT_ID, USER, PASS);
    String message = "hola";
    //MqttMessage mqttMessage = new MqttMessage(message.getBytes());


   // client.publish("/v2.0/devices/android-smart-fishbowl/send-event", mqttMessage);
    subscribeToTopic("/v2.0/devices/android-smart-fishbowl/send-event");
    publishMessage("/v2.0/devices/android-smart-fishbowl/send-event","5");
    //publishMessage("/v2.0/devices/android-smart-fishbowl/send-event",mqttMessage);
    communicationMqtt();
    pruebamqtt();


  }


    private void pruebamqtt() {
      String clientId = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj"; // Un identificador único para tu cliente MQTT
      String broker = "tcp://industrial.api.ubidots.com:1883"; // URL del servidor MQTT de Ubidots
      String token = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj"; // Tu token de Ubidots
      String topic = "/v2.0/devices/esp32-smart-fishbowl/send-state"; // La etiqueta de tu dispositivo en Ubidots

      MemoryPersistence persistence = new MemoryPersistence();
      try {
        MqttClient client = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(token);

        client.connect(connOpts);

        String payload = "yourMessage"; // El mensaje que deseas enviar
        org.eclipse.paho.client.mqttv3.MqttMessage message = new org.eclipse.paho.client.mqttv3.MqttMessage(payload.getBytes());
        client.publish(topic, (org.eclipse.paho.client.mqttv3.MqttMessage) message);

        client.disconnect();
      } catch (MqttException me) {
        me.printStackTrace();
      }
    }

  private void publishMessage(String topic, String message){
    Toast.makeText(this, "Publishing message: " + message, Toast.LENGTH_SHORT).show();
    mqttHandler.publish(topic,message);
  }

  private void subscribeToTopic(String topic) {
      Toast.makeText(this, "Subscribing to topic "+ topic, Toast.LENGTH_SHORT).show();
      mqttHandler.subscribe(topic);
    }

  public void communicationMqtt(){
    //mqttToQueue = new LinkedList<>();
    LinkedBlockingQueue<Integer> queueToMqtt = new LinkedBlockingQueue<>(5);
    boolean success = queueToMqtt.offer(1);
  }



}
