package soa.project.smartfishbowl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;
import soa.project.smartfishbowl.state_machine.Events;

public class ActionsActivity extends AppCompatActivity
{
  private static final String topic = "/v2.0/devices/android-smart-fishbowl/send-event";

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actions);

    /* -- Activity Transition -- */
    findViewById(R.id.homeButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(ActionsActivity.this, MainActivity.class);
      // Le dice a la nueva activity desde cual proviene.
      intent.putExtra("origin", "actions");
      startActivity(intent);
      finish();
    });
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    /* -- Animate Transition -- */
    findViewById(R.id.focused).startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.from_home_to_action));

    /* -- Actions Logic -- */
    MqttHandler mqttHandler = MqttHandler.getInstance();

    // Vincula cada boton con un evento a publicar.
    findViewById(R.id.feedButton).setOnClickListener(v ->
    {
      mqttHandler.publish(topic, Events.FEED_FISHES.value);
      Toast.makeText(getApplicationContext(), R.string.action_feeder, Toast.LENGTH_LONG).show();
    });

    findViewById(R.id.cleanButton).setOnClickListener(v ->
    {
      mqttHandler.publish(topic, Events.DRAW_WATER.value);
      Toast.makeText(getApplicationContext(), R.string.action_cleaner, Toast.LENGTH_LONG).show();
    });

    findViewById(R.id.lightsButton).setOnClickListener(v ->
    {
      mqttHandler.publish(topic, Events.SWITCH_LIGHTS.value);
      Toast.makeText(getApplicationContext(), R.string.action_lights, Toast.LENGTH_LONG).show();
    });
  }
}