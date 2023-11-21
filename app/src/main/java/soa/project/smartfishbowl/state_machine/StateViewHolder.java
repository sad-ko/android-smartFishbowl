package soa.project.smartfishbowl.state_machine;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import soa.project.smartfishbowl.R;

public class StateViewHolder extends RecyclerView.ViewHolder
{
  TextView timeView;
  TextView stateView;

  public StateViewHolder(@NonNull View itemView)
  {
    super(itemView);
    timeView = itemView.findViewById(R.id.timeView);
    stateView = itemView.findViewById(R.id.stateView);
  }
}
