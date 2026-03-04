package io.github.fishstiz.packed_packs.config;

public enum ProfileScope {
    GLOBAL,
    PROFILE,
    NONE;

    public boolean exists() {
        return this != NONE;
    }
}
