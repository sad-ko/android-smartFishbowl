package soa.project.smartfishbowl.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;

public class NotificationsActivity extends AppCompatActivity {
  private Handler handler = new Handler();
  private TextView textViewDatos;

  private ScrollView scrollView;

  private MqttHandler mqttHandler;

  private static final String topic = "/v2.0/devices/esp32-smart-fishbowl/send-state";

  private static final String BROKER_URL = "tcp://industrial.api.ubidots.com:1883";
  private static final String CLIENT_ID = "BBFF-VfVWtbeDj6PUpfZmWTcyqCfiqUOTRj";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notifications);
    scrollView = findViewById(R.id.scrollView);
    textViewDatos = findViewById(R.id.logger);

    // Inicia la tarea de actualización
    handler.post(actualizarDatosRunnable);
    /* -- Activity Transition -- */
    findViewById(R.id.homeButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(NotificationsActivity.this, MainActivity.class);
      // Le dice a la nueva activity desde cual proviene.
      intent.putExtra("origin", "notifications");
      startActivity(intent);
      finish();
    });
    findViewById(R.id.actionsButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(NotificationsActivity.this, ActionsActivity.class);
      // Le dice a la nueva activity desde cual proviene.
      intent.putExtra("origin", "notifications");
      startActivity(intent);
      finish();
    });

    /* -- Animate Transition -- */
    Bundle bundle = getIntent().getExtras();
    String origin = bundle.getString("origin");
    if (origin != null && origin.equals("main")) {
      findViewById(R.id.focused).startAnimation(
              AnimationUtils.loadAnimation(this, R.anim.from_home_to_notifications));
    } else if (origin != null && origin.equals("actions")) {
      findViewById(R.id.focused).startAnimation(
              AnimationUtils.loadAnimation(this, R.anim.from_action_to_notifications));
    }
  }
  @Override
  protected void onStart() {
    super.onStart();
    mqttHandler = MqttHandler.getInstance();

    if (!mqttHandler.isConnected())
    {
      // Ejecutamos la conexion en otro hilo para no trabar el actual.
      new Thread(() ->
      {
        mqttHandler.connect(BROKER_URL, CLIENT_ID);
        mqttHandler.subscribe(topic);
      }).start();
    }
    //mqttHandler.subscribe(topic);

  }

  // Runnable para la actualización periódica
  private Runnable actualizarDatosRunnable = new Runnable() {
    @Override
    public void run() {
      try {

        // Actualiza el TextView
        textViewDatos.setText(String.join("\n", mqttHandler.getEvents()));

        // Desplaza automáticamente hacia abajo
        scrollView.post(new Runnable() {
          @Override
          public void run() {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
          }
        });
      }catch (Exception e){
        e.printStackTrace();
      }
      // Programa la próxima ejecución después de un intervalo
      handler.postDelayed(this, 1000); // 1000 milisegundos (1 segundo)
    }

  };

  @Override
  protected void onDestroy() {
    // Detén el handler cuando la actividad se destruye
    handler.removeCallbacks(actualizarDatosRunnable);
    super.onDestroy();
  }


  private void mostrarNotificacion() {
    MqttHandler mqttHandler = MqttHandler.getInstance();

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Notificaciónes");
    for (String event : mqttHandler.getEvents()) {
      builder.setMessage(event);
    }

    // Crear y mostrar el cuadro de diálogo de notificación
    AlertDialog dialog = builder.create();
    dialog.show();
  }

}