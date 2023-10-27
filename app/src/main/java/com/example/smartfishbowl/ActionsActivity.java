package com.example.smartfishbowl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActionsActivity extends AppCompatActivity
{
  private ImageButton mainButton;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actions);

    mainButton = findViewById(R.id.homeButton);
    mainButton.setOnClickListener(v ->
    {
      Intent intent = new Intent(ActionsActivity.this, MainActivity.class);
      startActivity(intent);
      overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
      finish();
    });
  }
}