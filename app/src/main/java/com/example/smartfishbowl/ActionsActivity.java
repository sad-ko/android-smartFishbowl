package com.example.smartfishbowl;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class ActionsActivity extends AppCompatActivity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actions);

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