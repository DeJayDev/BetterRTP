package dev.dejay.evenbetterrtp.references.settings;

public class Settings {

    private SoftDepends depends = new SoftDepends();

    public void load() { //Load Settings
        depends.load();
    }

    public SoftDepends getsDepends() {
        return depends;
    }

}
