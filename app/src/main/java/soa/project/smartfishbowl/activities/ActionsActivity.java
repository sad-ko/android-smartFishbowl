package soa.project.smartfishbowl.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;
import soa.project.smartfishbowl.state_machine.Events;

public class ActionsActivity extends AppCompatActivity implements SensorEventListener
{
  private static final String topic = "/v2.0/devices/android-smart-fishbowl/send-event";
  private static final float umbral = 30;
  private SensorManager mSensorManager;

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

    findViewById(R.id.notifButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(ActionsActivity.this, NotificationsActivity.class);
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
    Bundle bundle = getIntent().getExtras();
    if (bundle != null)
    {
      String origin = bundle.getString("origin");
      if (origin != null && origin.equals("main"))
      {
        findViewById(R.id.focused).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.from_home_to_action));
      }
      else if (origin != null && origin.equals("notifications"))
      {
        findViewById(R.id.focused).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.from_notifications_to_actions));
      }
    }

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

    // Accedemos al servicio de sensores
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
  }

  /**
   * Metodo para iniciar el acceso a los sensores
   */
  protected void iniSensores()
  {
    mSensorManager.registerListener(this,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
    );
  }

  /**
   * Metodo para parar la escucha de los sensores
   */
  private void pararSensores()
  {
    mSensorManager.unregisterListener(
            this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
  }

  @Override
  public void onSensorChanged(SensorEvent event)
  {
    MqttHandler mqttHandler = MqttHandler.getInstance();
    // Se utiliza synchronized para asegurar que el acceso a este bloque de código sea
    // atómico, evitando posibles problemas de concurrencia
    synchronized (this)
    {
      // Verificación de tipo de sensor (solo si es necesario)
      if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
      {
        // Se verifica si alguno de los valores supera el umbral para hacer prender el led en el embebido
        if ((event.values[0] > umbral) || (event.values[1] > umbral) || (event.values[2] > umbral))
        {
          mqttHandler.publish(topic, Events.SWITCH_LIGHTS.value);
          Toast.makeText(getApplicationContext(), R.string.sensor, Toast.LENGTH_SHORT).show();

          // Detenemos los sensores por un tiempo para evitar el constante spameo.
          pararSensores();
          new Timer().schedule(new TimerTask()
          {
            @Override
            public void run()
            {
              Log.i("ubidots", "....");
              iniSensores();
              cancel();
            }
          }, 2000);
        }
      }
    }
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    pararSensores();
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    pararSensores();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    pararSensores();
  }

  @Override
  protected void onRestart()
  {
    super.onRestart();
    iniSensores();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    iniSensores();
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {}
}