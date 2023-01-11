package com.kyuuzinbr.gldcmod.items.data.elemental;

public enum Element {
    DEFAULT(0),
    FIRE(1),
    ICE(2),
    EARTH(3),
    LIGHTNING(4);

    private final int value;

    private Element(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public boolean equals(int i) {
        return toInt() == i;
    }
}
