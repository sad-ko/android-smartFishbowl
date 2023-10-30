package com.example.smartfishbowl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActionsActivity extends AppCompatActivity
{
  private Button cmdFeedFishes;
  private Button cmdCleanTank;

  private Button cmdSwitchLights;
  private final View.OnClickListener botonesListeners = new View.OnClickListener()
  {
    @Override
    public void onClick(View view)
    {

      final int ButtonPressedId = view.getId();

      if (ButtonPressedId == R.id.feedButton)
      {
        //("/v2.0/devices/android-smart-fishbowl/send-event","feed");
        //abrir servo
        Toast.makeText(getApplicationContext(), "feed", Toast.LENGTH_LONG).show();

      } else if (ButtonPressedId == R.id.cleanButton)
      {
        //("/v2.0/devices/android-smart-fishbowl/send-event","feed");
        //activar bomba
        Toast.makeText(getApplicationContext(), "clean", Toast.LENGTH_LONG).show();
      } else if (ButtonPressedId == R.id.lightsButton)
      {
        //("/v2.0/devices/android-smart-fishbowl/send-event","feed");
        //apagar-encender luces
        Toast.makeText(getApplicationContext(), "ligths", Toast.LENGTH_LONG).show();
      }

      //publishMessage("/v2.0/devices/asaa/pulsador","clean");//cambiar
      //    Toast.makeText(getApplicationContext(),"cleAN",Toast.LENGTH_LONG).show();
      //   break;
      // default:
      //  Toast.makeText(getApplicationContext(),"Error en Listener de botones",Toast.LENGTH_LONG).show();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actions);

    cmdFeedFishes = (Button) findViewById(R.id.feedButton);
    cmdCleanTank = (Button) findViewById(R.id.cleanButton);
    cmdSwitchLights = (Button) findViewById(R.id.lightsButton);

    cmdFeedFishes.setOnClickListener(botonesListeners);
    cmdCleanTank.setOnClickListener(botonesListeners);
    cmdSwitchLights.setOnClickListener(botonesListeners);


    findViewById(R.id.homeButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(ActionsActivity.this, MainActivity.class);
      intent.putExtra("origin", "actions");
      startActivity(intent);
      finish();
    });

    findViewById(R.id.focused).startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_home_to_action));
  }


}