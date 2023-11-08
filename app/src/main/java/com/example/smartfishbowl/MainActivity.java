package com.example.smartfishbowl;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity<MqttMessage> extends AppCompatActivity
{
  private static final String BROKER_URL = "tcp://industrial.api.ubidots.com:1883";
  private static final String CLIENT_ID = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj";

  private MqttHandler mqttHandler;
  private Button cmdFeedFishes;
  private Button cmdCleanTank;
  private Queue<Integer> mqttToQueue;

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
    mqttHandler.connect(BROKER_URL, CLIENT_ID);
   // subscribeToTopic("/v2.0/devices/android-smart-fishbowl/send-event");
    subscribeToTopic("/v2.0/devices/esp32-smart-fishbowl/send-state");
    // client.publish("/v2.0/devices/android-smart-fishbowl/send-event", mqttMessage);
    mqttHandler.publishMessage("/v2.0/devices/android-smart-fishbowl/send-event", 9);
    // publishMessage("/v2.0/devices/android-smart-fishbowl/send-event",mqttMessage);

  }

 // private void publishMessagetest(String topic, String message)
  //{
    //Toast.makeText(this, "Publishing message: " + message, Toast.LENGTH_SHORT).show();
    //mqttHandler.publish(topic, message);
  //}

  private void subscribeToTopic(String topic)
  {
    Toast.makeText(this, "Subscribing to topic " + topic, Toast.LENGTH_SHORT).show();
    mqttHandler.subscribe(topic);
  }

  public void communicationMqtt()
  {
    LinkedBlockingQueue<Integer> queueToMqtt = new LinkedBlockingQueue<>(5);
    boolean success = queueToMqtt.offer(1);
  }
}
