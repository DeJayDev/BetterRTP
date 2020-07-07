package dev.dejay.evenbetterrtp.references.worlds;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class PlayerWorld implements RTPWorld {

    private boolean useWorldBorder;
    private int centerX, centerZ, maxBorderRad, minBorderRad, price, attempts;
    private List<String> Biomes;
    private Player p;
    private String world;

    public PlayerWorld(Player p, String world) {
        this.p = p;
        this.world = world;
    }

    public void setup(RTPWorld world, int price, List<String> biomes) {
        setUseWorldborder(world.getUseWorldBorder());
        setCenterX(world.getCenterX());
        setCenterZ(world.getCenterZ());
        setMaxRad(world.getMaxRad());
        setMinRad(world.getMinRad());
        setPrice(price);
        List<String> list = new ArrayList<>(world.getBiomes());
        if (biomes != null) {
            list.addAll(biomes);
        }
        setBiomes(list);
    }

    public Player getPlayer() {
        return p;
    }

    public String getWorld() {
        return world;
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

    public int getAttempts() {return attempts; }

    @Override
    public List<String> getBiomes() {
        return Biomes;
    }

    private void setUseWorldborder(boolean bool) {
        useWorldBorder = bool;
    }

    private void setCenterX(int x) {
        centerX = x;
    }

    private void setCenterZ(int z) {
        centerZ = z;
    }

    private void setMaxRad(int max) {
        maxBorderRad = max;
    }

    private void setMinRad(int min) {
        minBorderRad = min;
    }

    private void setPrice(int price) {
        this.price = price;
    }

    public void addAttempt() {
        this.attempts++;
    }

    private void setBiomes(List<String> biomes) {
        Biomes = biomes;
    }
}
