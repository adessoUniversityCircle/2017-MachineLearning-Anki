package de.adesso.anki.sdk.advertisement;

import java.util.HashMap;
import java.util.Map;

public enum AnkiModel {
  KOURAI(0x01),
  BOSON(0x02),
  RHO(0x03),
  KATAL(0x04),
  HADION(0x05),
  SPEKTRIX(0x06),
  CORAX(0x07),
  GROUNDSHOCK(0x08, "#2994f1"),
  SKULL(0x09, "#df3232"),
  THERMO(0x0a, "#a11c20"),
  NUKE(0x0b, "#bed62f"),
  GUARDIAN(0x0c, "#42b1d7"),
  BIGBANG(0x0e, "#4e674d");
  
  private int id;
  private String color = "#f00";
  
  private AnkiModel(int id) { this.id = id; }
  private AnkiModel(int id, String color) { this.id = id; this.color = color; }
  
  public String getColor() {
    return color;
  }
  
  public static AnkiModel fromId(int id) {
    return idToModel.get(id);
  }
  
  public int getId() {
    return id;
  }
  
  private static final Map<Integer, AnkiModel> idToModel = new HashMap<Integer, AnkiModel>() {{
    for (AnkiModel m : AnkiModel.values()) {
      put(m.id, m);
    }
  }};

}
