package soa.project.smartfishbowl.activities;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import soa.project.smartfishbowl.networking.MqttHandler;
import soa.project.smartfishbowl.R;

public class NotificationsActivity extends AppCompatActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notifications);
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
