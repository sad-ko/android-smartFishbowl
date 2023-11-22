package soa.project.smartfishbowl.state_machine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import soa.project.smartfishbowl.R;

public class StateAdapter extends RecyclerView.Adapter<StateViewHolder>
{
  @SuppressLint("StaticFieldLeak")
  private static volatile StateAdapter instance;
  private static final List<States> messages = new ArrayList<>();
  private final Context context;

  private StateAdapter(LifecycleOwner owner, Context context)
  {
    // Funcion que se ejecuta al recibir un nuevo estado
    StateLiveData.getState().observe(owner, this::newState);
    this.context = context;
  }

  @SuppressLint("NotifyDataSetChanged")
  private void newState(States newState)
  {
    messages.add(newState);
    notifyDataSetChanged();
  }

  /**
   * Devuelve una instancia de este singleton.
   */
  public static StateAdapter getInstance(LifecycleOwner owner, Context context)
  {
    StateAdapter result = instance;
    if (result != null)
    {
      return result;
    }

    synchronized (StateAdapter.class)
    {
      if (instance == null)
      {
        instance = new StateAdapter(owner, context.getApplicationContext());
      }
      return instance;
    }
  }

  public static void addMessage(States newState)
  {
    if (newState != States.INIT)
    {
      messages.add(newState);
    }
  }

  @NonNull
  @Override
  public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    View view =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.state_message, parent, false);
    return new StateViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull StateViewHolder holder, int position)
  {
    States currentState = messages.get(position);
    if (currentState != null)
    {
      holder.timeView.setText(currentState.time);
      holder.stateView.setText(currentState.toString(context));
    }
  }

  @Override
  public int getItemCount()
  {
    return messages.size();
  }
}
