package soa.project.smartfishbowl.state_machine;

/**
 * Eventos que envia a la pecera.
 */
public enum Events
{
  DRAW_WATER("7"),
  FEED_FISHES("8"),
  SWITCH_LIGHTS("9");

  public final String value;

  Events(String value)
  {
    this.value = value;
  }
}
