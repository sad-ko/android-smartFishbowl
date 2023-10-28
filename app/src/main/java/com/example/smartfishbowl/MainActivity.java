package com.example.smartfishbowl;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.actionsButton).setOnClickListener(v ->
    {
      Intent intent = new Intent(MainActivity.this, ActionsActivity.class);
      intent.putExtra("origin", "main");
      startActivity(intent);
      finish();
    });

    Bundle bundle = getIntent().getExtras();
    if (bundle != null)
    {
      String origin = bundle.getString("origin");
      if (origin != null && origin.equals("actions"))
      {
        findViewById(R.id.focused).startAnimation(AnimationUtils.loadAnimation(this, R.anim.from_action_to_home));
      }
    }
  }
}