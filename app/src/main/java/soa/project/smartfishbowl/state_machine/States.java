package soa.project.smartfishbowl.state_machine;

import java.util.HashMap;
import java.util.Map;

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

  private final int value;
  private static final Map<Integer, States> map = new HashMap<>();

  States(Integer value)
  {
    this.value = value;
  }

  static
  {
    for (States state : States.values())
    {
      map.put(state.value, state);
    }
  }

  /**
   * Mapeo del valor recibido con el estado correspondiente.
   */
  public static States toEnum(Integer value)
  {
    return map.get(value);
  }
}
