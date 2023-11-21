package soa.project.smartfishbowl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import soa.project.smartfishbowl.R;
import soa.project.smartfishbowl.state_machine.StateAdapter;

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

    RecyclerView recyclerView = findViewById(R.id.recycler);
    recyclerView.setAdapter(StateAdapter.getInstance(this, getApplicationContext()));
  }
}