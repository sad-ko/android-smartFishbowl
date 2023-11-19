package soa.project.smartfishbowl.state_machine;

import java.util.HashMap;
import java.util.Map;

import soa.project.smartfishbowl.R;

/**
 * Estado de la interfaz de la pecera.
 */
public class TankState
{
  /**
   * Mapeo del estado con la interfaz de la pecera correspondiente.
   */
  private static final Map<States, TankState> stateTable = new HashMap<>(Map.of(States.INIT,
          new TankState(R.drawable.tank_not_connected, R.string.tank_err, R.string.tank_desc_err),
          States.IDLE, new TankState(R.drawable.tank_okay, R.string.tank_okay), States.IDLE_NIGHT,
          new TankState(R.drawable.tank_okay_night, R.string.tank_okay, R.string.tank_desc_night),
          States.IDLE_MANUAL,
          new TankState(R.drawable.tank_okay, R.string.tank_okay, R.string.tank_desc_manual),
          States.LOW_ON_WATER, new TankState(R.drawable.tank_empty, R.string.tank_empty),
          States.DRAWING_WATER, new TankState(R.drawable.tank_cleaning, R.string.tank_cleaning),
          States.FEEDING_FISHES,
          new TankState(R.drawable.tank_feeding, R.string.tank_okay, R.string.tank_desc_fish_eating)
  ));
  public int view;
  public int status;
  public int description = R.string.desc_empty;

  private TankState(int view, int status)
  {
    this.view = view;
    this.status = status;
  }

  private TankState(int view, int status, int description)
  {
    this.view = view;
    this.status = status;
    this.description = description;
  }

  public static TankState getState(States newState)
  {
    return stateTable.get(newState);
  }
}
