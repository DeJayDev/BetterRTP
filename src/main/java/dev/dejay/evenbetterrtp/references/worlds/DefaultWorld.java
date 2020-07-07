package dev.dejay.evenbetterrtp.references.worlds;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import java.util.List;
import org.bukkit.Bukkit;

public class DefaultWorld implements RTPWorld {

    private boolean useWorldBorder;
    private int centerX, centerZ, maxBorderRad, minBorderRad, price;
    private List<String> Biomes;

    public void setup() {
        //Setups
        String pre = "DefaultWorld";
        FileBasics.FileType config = Main.get().getFiles().getType(FileBasics.FileType.CONFIG);
        //Booleans
        useWorldBorder = config.getBoolean(pre + ".UseWorldBorder");
        //Integers
        centerX = config.getInt(pre + ".centerX");
        centerZ = config.getInt(pre + ".centerZ");
        maxBorderRad = config.getInt(pre + ".MaxRadius");
        if (maxBorderRad <= 0) {
            Main.get().getMessages().sms(Bukkit.getConsoleSender(),
                    "WARNING! DefaultWorld Maximum radius of '" + maxBorderRad + "' is not allowed! Set to '1000'");
            maxBorderRad = 1000;
        }
        minBorderRad = config.getInt(pre + ".MinRadius");
        if (minBorderRad < 0 || minBorderRad >= maxBorderRad) {
            Main.get().getMessages().sms(Bukkit.getConsoleSender(),
                    "WARNING! DefaultWorld Minimum radius of '" + minBorderRad + "' is not allowed! Set to '0'");
            minBorderRad = 0;
        }
        if (Main.get().getFiles().getType(FileBasics.FileType.ECO).getBoolean("Economy.Enabled"))
            price = Main.get().getFiles().getType(FileBasics.FileType.ECO).getInt("Economy.Price");
        //Other
        this.Biomes = config.getStringList(pre + ".Biomes");
    }

    @Override
    public boolean getUseWorldBorder() {
        return useWorldBorder;
    }

    @Override
    public int getCenterX() {
        return centerX;
    }

    @Override
    public int getCenterZ() {
        return centerZ;
    }

    @Override
    public int getMaxRad() {
        return maxBorderRad;
    }

    @Override
    public int getMinRad() {
        return minBorderRad;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public List<String> getBiomes() {
        return Biomes;
    }

    @Override
    public String getWorld() {
        return null;
    }
}
