package com.example.smartfishbowl;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Button mostrarNotificacionButton = findViewById(R.id.mostrarNotificacionButton);

        mostrarNotificacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarNotificacion();
            }
        });
    }

    private void mostrarNotificacion() {
        MqttHandler mqttHandler = new MqttHandler(getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notificaciónes");
        for (String event:mqttHandler.getEvents()) {
            builder.setMessage(event);
        }

        // Crear y mostrar el cuadro de diálogo de notificación
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
