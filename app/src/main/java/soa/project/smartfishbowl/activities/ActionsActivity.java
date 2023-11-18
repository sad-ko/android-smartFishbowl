package soa.project.smartfishbowl.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;
import soa.project.smartfishbowl.state_machine.Events;

public class ActionsActivity extends AppCompatActivity implements SensorEventListener
{
  private static final String topic = "/v2.0/devices/android-smart-fishbowl/send-event";
  private SensorManager mSensorManager;

  private final Integer umbral = 15;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actions);

    // Accedemos al servicio de sensores
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



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


  // Metodo para iniciar el acceso a los sensores
  protected void Ini_Sensores()
  {
    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),   SensorManager.SENSOR_DELAY_NORMAL);
  }

  // Metodo para parar la escucha de los sensores
  private void Parar_Sensores()
  {
    mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
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
          Toast.makeText(getApplicationContext(),"Umbral superado por el acelerometro", Toast.LENGTH_LONG).show();
        }
      }
    }
  }
  @Override
  protected void onStop()
  {

    Parar_Sensores();

    super.onStop();
  }

  @Override
  protected void onDestroy()
  {
    Parar_Sensores();

    super.onDestroy();
  }

  @Override
  protected void onPause()
  {
    Parar_Sensores();

    super.onPause();
  }

  @Override
  protected void onRestart()
  {
    Ini_Sensores();

    super.onRestart();
  }

  @Override
  protected void onResume()
  {
    super.onResume();

    Ini_Sensores();
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }
}