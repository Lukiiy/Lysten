package me.lukiiy.lysten;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lysten implements ModInitializer {
    public static final String MOD_ID = "lysten";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Config CONFIG = new Config("lysten", "Lysten!");

    @Override
    public void onInitialize() {}
}