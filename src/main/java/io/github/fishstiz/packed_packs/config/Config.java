package io.github.fishstiz.packed_packs.config;

public class Config {
    private static final Config INSTANCE = new Config();
    private boolean devMode = false;

    public static Config get() {
        return INSTANCE;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
