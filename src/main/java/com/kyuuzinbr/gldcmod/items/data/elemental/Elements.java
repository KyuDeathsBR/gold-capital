package com.kyuuzinbr.gldcmod.items.data.elemental;

public class Elements {
    public static final Element from(int value) {
        for (Element element:
                Element.values()) {
            if (element.toInt() == value) {
                return element;
            }
        }
        return Element.DEFAULT;
    }
    public static final boolean contains(int value) {
        for (Element element:
                Element.values()) {
            if (element.toInt() == value) {
                return true;
            }
        }
        return false;
    }
}
