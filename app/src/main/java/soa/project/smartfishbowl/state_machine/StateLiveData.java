package soa.project.smartfishbowl.state_machine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Variable en la cual se guarda el nuevo estado recibido desde la pecera
 * y se observa desde la Main Activity para informar al usuario del nuevo estado recibido.
 */
public class StateLiveData
{
  private static MutableLiveData<States> state;

  public static LiveData<States> getState()
  {
    if (state == null)
    {
      state = new MutableLiveData<>(States.INIT);
    }
    return state;
  }

  public static void setState(Integer value, String time)
  {
    if (state == null)
    {
      state = new MutableLiveData<>(States.INIT);
    }
    state.postValue(States.toEnum(value, time));
  }
}
