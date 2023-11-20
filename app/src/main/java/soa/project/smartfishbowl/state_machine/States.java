package soa.project.smartfishbowl.state_machine;

import androidx.annotation.NonNull;

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

  private static final Map<Integer, States> map = new HashMap<>();

  static
  {
    for (States state : States.values())
    {
      map.put(state.value, state);
    }
  }

  private final int value;

  States(Integer value)
  {
    this.value = value;
  }

  /**
   * Mapeo del valor recibido con el estado correspondiente.
   */
  public static States toEnum(Integer value)
  {
    return map.get(value);
  }

  @NonNull
  @Override
  public String toString()
  {
    return switch (this)
            {
              case INIT -> "Init";
              case IDLE -> "Idle";
              case IDLE_NIGHT -> "Night Mode";
              case IDLE_MANUAL -> "Manual Mode";
              case LOW_ON_WATER -> "Low on Water";
              case DRAWING_WATER -> "Drawing Water";
              case FEEDING_FISHES -> "Feeding Fishes";
            };
  }
}
