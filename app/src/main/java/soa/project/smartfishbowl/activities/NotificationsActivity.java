package soa.project.smartfishbowl.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.networking.MqttHandler;

public class NotificationsActivity extends AppCompatActivity
{
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

    /* -- Animate Transition -- */
    Bundle bundle = getIntent().getExtras();
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

  private void mostrarNotificacion()
  {
    MqttHandler mqttHandler = MqttHandler.getInstance();

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Notificaciónes");
    for (String event : mqttHandler.getEvents())
    {
      builder.setMessage(event);
    }

    // Crear y mostrar el cuadro de diálogo de notificación
    AlertDialog dialog = builder.create();
    dialog.show();
  }
}
