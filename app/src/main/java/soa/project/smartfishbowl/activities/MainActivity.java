package soa.project.smartfishbowl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;
import soa.project.smartfishbowl.state_machine.StateLiveData;
import soa.project.smartfishbowl.state_machine.States;
import soa.project.smartfishbowl.state_machine.TankState;

public class MainActivity extends AppCompatActivity
{
  private static final String BROKER_URL = "tcp://industrial.api.ubidots.com:1883";
  private static final String CLIENT_ID = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj";
  private static final String topic = "/v2.0/devices/esp32-smart-fishbowl/send-state";
  private ImageView tankView;
  private TextView tankStatus;
  private TextView tankDescription;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /* -- Activity Transition -- */
    findViewById(R.id.actionsButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(MainActivity.this, ActionsActivity.class);
      // Le dice a la nueva activity desde cual proviene.
      intent.putExtra("origin", "main");
      startActivity(intent);
      finish();
    });
    findViewById(R.id.notifButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
      // Le dice a la nueva activity desde cual proviene.
      intent.putExtra("origin", "main");
      startActivity(intent);
      finish();
    });

    /* -- Init Variables -- */
    tankView = findViewById(R.id.tankView);
    tankStatus = findViewById(R.id.tankStatus);
    tankDescription = findViewById(R.id.tankDescription);

    // Funcion que se ejecuta al recibir un nuevo estado
    StateLiveData.getState().observe(this, this::changeState);
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    /* -- Animate Transition -- */
    Bundle bundle = getIntent().getExtras();
    if (bundle != null)
    {
      String origin = bundle.getString("origin");
      if (origin != null && origin.equals("actions"))
      {
        findViewById(R.id.focused).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.from_action_to_home));
      }
      else if (origin != null && origin.equals("notifications"))
      {
        findViewById(R.id.focused).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.from_notifications_to_home));
      }
    }

    /* -- Mqtt Connection -- */
    MqttHandler mqttHandler = MqttHandler.getInstance();
    if (!mqttHandler.isConnected())
    {
      // Ejecutamos la conexion en otro hilo para no trabar el actual.
      new Thread(() ->
      {
        mqttHandler.connect(BROKER_URL, CLIENT_ID);
        mqttHandler.subscribe(topic);
      }).start();
    }
  }

  // Cambia la interfaz de la pecera en base al nuevo estado recibido.
  void changeState(States newState)
  {
    TankState tankState = TankState.getState(newState);
    if (tankState != null)
    {
      tankView.setBackgroundResource(tankState.view);
      tankStatus.setText(tankState.status);
      tankDescription.setText(tankState.description);
    }
  }
}
