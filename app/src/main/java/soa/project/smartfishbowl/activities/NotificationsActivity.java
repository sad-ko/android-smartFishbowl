package soa.project.smartfishbowl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;

public class NotificationsActivity extends AppCompatActivity
{
  private final Handler handler = new Handler();
  private TextView logger;
  private ScrollView scrollView;
  private MqttHandler mqttHandler;
  // Runnable para la actualización periódica
  private final Runnable actualizarDatosRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      try
      {
        // Actualiza el TextView
        logger.setText(String.join("\n", mqttHandler.getEvents()));

        // Desplaza automáticamente hacia abajo
        scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      // Programa la próxima ejecución después de un intervalo
      handler.postDelayed(this, 1000); // 1000 milisegundos (1 segundo)
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notifications);

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
                AnimationUtils.loadAnimation(this, R.anim.from_home_to_notifications));
      }
      else if (origin != null && origin.equals("actions"))
      {
        findViewById(R.id.focused).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.from_action_to_notifications));
      }
    }

    scrollView = findViewById(R.id.scrollView);
    logger = findViewById(R.id.logger);
    mqttHandler = MqttHandler.getInstance();

    // Inicia la tarea de actualización
    handler.post(actualizarDatosRunnable);
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    // Detén el handler cuando la actividad se destruye
    handler.removeCallbacks(actualizarDatosRunnable);
  }
}