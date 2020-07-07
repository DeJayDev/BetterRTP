package dev.dejay.evenbetterrtp.references.worlds;

import java.util.List;

public interface RTPWorld {

    boolean getUseWorldBorder();

    int getCenterX();

    int getCenterZ();

    int getMaxRad();

    int getMinRad();

    int getPrice();

    List<String> getBiomes();

    String getWorld();

}
