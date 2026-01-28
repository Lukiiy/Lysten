package me.lukiiy.lysten;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ConfigKey<T> {
    public final String key;
    public final T defaultValue;
    public final Function<String, T> parser;
    public final Function<T, String> serializer;
    private T stored;

    private static final Set<ConfigKey<?>> RUN_RELOADABLE = new HashSet<>();

    private ConfigKey(String key, T defaultValue, Function<String, T> parser, Function<T, String> serializer) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.parser = parser;
        this.serializer = serializer;

        RUN_RELOADABLE.add(this);
        writeDefault();
    }

    public void writeDefault() {
        if (defaultValue == null) return;

        Lysten.CONFIG.setIfAbsent(key, serializer.apply(defaultValue));
    }

    public void load() {
        String raw = Lysten.CONFIG.getOrDefault(key, serializer.apply(defaultValue));

        try {
            stored = parser.apply(raw);
        } catch (Exception e) {
            stored = defaultValue;
        }
    }

    public T get() {
        return stored != null ? stored : defaultValue;
    }

    public void set(T value) {
        stored = value;

        Lysten.CONFIG.set(key, serializer.apply(value));
    }

    public ConfigKey<T> setUnreloadable() {
        RUN_RELOADABLE.remove(this);

        return this;
    }

    public static void reloadItAll() {
        RUN_RELOADABLE.forEach(ConfigKey::load);
    }

    public static ConfigKey<Boolean> bool(String key, boolean def) {
        return new ConfigKey<>(key, def, Boolean::parseBoolean, Object::toString);
    }

    public static ConfigKey<Integer> integer(String key, int def) {
        return new ConfigKey<>(key, def, Integer::parseInt, Object::toString);
    }

    public static ConfigKey<Long> longVal(String key, long def) {
        return new ConfigKey<>(key, def, Long::parseLong, Object::toString);
    }

    public static ConfigKey<Float> floatVal(String key, float def) {
        return new ConfigKey<>(key, def, Float::parseFloat, Object::toString);
    }

    public static ConfigKey<Double> doubleVal(String key, double def) {
        return new ConfigKey<>(key, def, Double::parseDouble, Object::toString);
    }

    public static ConfigKey<String> string(String key, String def) {
        return new ConfigKey<>(key, def, s -> s, s -> s);
    }

    public static <E extends Enum<E>> ConfigKey<E> enumKey(String key, Class<E> type, E def) {
        return new ConfigKey<>(key, def,
                s -> {
                    try {
                        return Enum.valueOf(type, s);
                    } catch (IllegalArgumentException e) {
                        return Enum.valueOf(type, s.toUpperCase());
                    }
                }, Enum::name);
    }
}