package de.adesso.anki.util;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class LaneConverter {

  /**
   * @param offset the vehicle's offset from center
   * @return an integer value between -8 and 8
   */
  public static int offsetToLane(double offset) {
    if (offset == 0)
      return -1;

    int lane = (int) Math.round(offset + ((offset > 0) ? 4.5 : -4.5) / 9);

    if (lane < -8)
      return -8;

    if (lane > 8)
      return 8;

    return lane;
  }

  public static double offsetFromLane(int lane) {
    if (lane == 0 || lane < -8 || lane > 8)
      return 0;

    return 9.0 * lane + (lane > 0 ? -4.5 : 4.5);
  }

}
