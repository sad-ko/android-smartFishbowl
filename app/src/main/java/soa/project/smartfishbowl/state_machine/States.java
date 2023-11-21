package soa.project.smartfishbowl.state_machine;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import soa.project.smartfishbowl.R;

/**
 * Estados que recibe desde la pecera.
 */
public enum States
{
  INIT(0),
  IDLE(1),
  IDLE_NIGHT(2),
  IDLE_MANUAL(3),
  LOW_ON_WATER(4),
  DRAWING_WATER(5),
  FEEDING_FISHES(6);

  private static final Map<Integer, States> map = new HashMap<>();

  static
  {
    for (States state : States.values())
    {
      map.put(state.value, state);
    }
  }

  private final int value;
  public String time;

  States(Integer value)
  {
    this.value = value;
  }

  /**
   * Mapeo del valor recibido con el estado correspondiente.
   */
  public static States toEnum(Integer value, String time)
  {
    States tmp = map.get(value);
    assert tmp != null;
    tmp.time = time;
    return tmp;
  }

  public String toString(Context context)
  {
    return switch (this)
            {
              case INIT -> context.getString(R.string.state_init);
              case IDLE -> context.getString(R.string.state_idle);
              case IDLE_NIGHT -> context.getString(R.string.state_night);
              case IDLE_MANUAL -> context.getString(R.string.state_manual);
              case LOW_ON_WATER -> context.getString(R.string.state_low);
              case DRAWING_WATER -> context.getString(R.string.state_clean);
              case FEEDING_FISHES -> context.getString(R.string.state_feed);
            };
  }
}
