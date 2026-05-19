package com.mossshade.soullink.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LocalizedText {

    private LocalizedText() {
        throw new UnsupportedOperationException("Cannot instantiate LocalizedText");
    }

    public static MutableComponent getTranslatableWithFallback(String key, Object... args) {
        return Component.translatableWithFallback(key, Component.translatable(key, args).getString(), args);
//        return Component.translatable(key, args);
    }

}
