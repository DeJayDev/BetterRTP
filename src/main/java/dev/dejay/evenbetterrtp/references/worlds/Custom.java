package dev.dejay.evenbetterrtp.references.worlds;

import dev.dejay.evenbetterrtp.Main;
import dev.dejay.evenbetterrtp.references.file.FileBasics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;

public class Custom implements RTPWorld {
    public String world;
    private boolean useWorldBorder = false;
    private int centerX, centerZ, maxBorderRad, minBorderRad, price;
    private List<String> Biomes;

    public Custom(String world) {
        String pre = "CustomWorlds.";
        FileBasics.FileType config = Main.get().getFiles().getType(FileBasics.FileType.CONFIG);
        List<Map<?, ?>> map = config.getMapList("CustomWorlds");
        this.world = world;

        //Find Custom World and cache values
        for (Map<?, ?> m : map) {
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                String key = entry.getKey().toString();
                if (!key.equals(world))
                    continue;
                Map<?, ?> test = ((Map<?, ?>) m.get(key));
                if (test == null)
                    continue;
                if (test.get("UseWorldBorder") != null) {
                    if (test.get("UseWorldBorder").getClass() == Boolean.class)
                        useWorldBorder = Boolean.valueOf(test.get("UseWorldBorder").toString());
                }
                if (test.get("centerX") != null) {
                    if (test.get("centerX").getClass() == Integer.class)
                        centerX = Integer.valueOf((test.get("centerX")).toString());
                }
                if (test.get("centerZ") != null) {
                    if (test.get("centerZ").getClass() == Integer.class)
                        centerZ = Integer.valueOf((test.get("centerZ")).toString());
                }
                if (test.get("MaxRadius") != null) {
                    if (test.get("MaxRadius").getClass() == Integer.class)
                        maxBorderRad = Integer.valueOf((test.get("MaxRadius")).toString());
                    if (maxBorderRad <= 0) {
                        Main.get().getMessages().sms(Bukkit.getConsoleSender(),
                                "WARNING! Custom world '" + world + "' Maximum radius of '" + maxBorderRad + "' is not allowed! Set to default value!");
                        maxBorderRad = Main.get().getRTP().DefaultWorld.getMaxRad();
                    }
                }
                if (test.get("MinRadius") != null) {
                    if (test.get("MinRadius").getClass() == Integer.class)
                        minBorderRad = Integer.valueOf((test.get("MinRadius")).toString());
                    if (minBorderRad < 0 || minBorderRad >= maxBorderRad) {
                        Main.get().getMessages().sms(Bukkit.getConsoleSender(),
                                "WARNING! Custom world '" + world + "' Minimum radius of '" + minBorderRad + "' is not allowed! Set to default value!");
                        minBorderRad = Main.get().getRTP().DefaultWorld.getMinRad();
                        if (minBorderRad >= maxBorderRad)
                            maxBorderRad = Main.get().getRTP().DefaultWorld.getMaxRad();
                    }
                }
                if (test.get("Biomes") != null) {
                    if (test.get("Biomes").getClass() == ArrayList.class)
                        this.Biomes = new ArrayList<>((ArrayList) test.get("Biomes"));
                }
            }
        }
        //Booleans
        /*useWorldBorder = config.getBoolean(pre + world + ".UseWorldBorder");
        //Integers
        centerX = config.getInt(pre + world + ".centerX");
        centerZ = config.getInt(pre + world + ".centerZ");
        maxBorderRad = config.getInt(pre + world + ".MaxRadius");
        if (maxBorderRad <= 0) {
            Main.get().getMessages().sms(Bukkit.getConsoleSender(),
                    "WARNING! Custom world '" + world + "' Maximum radius of '" + maxBorderRad + "' is not allowed! Set to default value!");
            maxBorderRad = Main.get().getRTP().DefaultWorld.getMaxRad();
        }
        minBorderRad = config.getInt(pre + world + ".MinRadius");
        if (minBorderRad <= 0 || minBorderRad >= maxBorderRad) {
            Main.get().getMessages().sms(Bukkit.getConsoleSender(),
                    "WARNING! Custom world '" + world + "' Minimum radius of '" + minBorderRad + "' is not allowed! Set to default value!");
            minBorderRad = Main.get().getRTP().DefaultWorld.getMinRad();
        }
        */
        if (Main.get().getFiles().getType(FileBasics.FileType.ECO).getBoolean("Economy.Enabled"))
            if (Main.get().getFiles().getType(FileBasics.FileType.ECO).getBoolean(pre + "Enabled")) {
                map.clear();
                map = Main.get().getFiles().getType(FileBasics.FileType.ECO).getMapList("CustomWorlds");
                for (Map<?, ?> m : map)
                    for (Map.Entry<?, ?> entry : m.entrySet()) {
                        String key = entry.getKey().toString();
                        Map<?, ?> test = ((Map<?, ?>) m.get(key));
                        if (!key.equals(world))
                            continue;
                        if (test.get("Price") != null)
                            if (test.get("Price").getClass() == Integer.class)
                            price = Integer.valueOf((test.get("Price")).toString());
                    }
            } else
                price = Main.get().getRTP().DefaultWorld.getPrice();
        //Other
        this.Biomes = config.getStringList(pre + world + ".Biomes");
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
        return world;
    }
}
