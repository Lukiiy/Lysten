package me.lukiiy.lysten;

import java.util.function.Function;

public record ConfigKey<T>(String key, T defaultValue, Function<String, T> parser, Function<T, String> serializer) {
    public ConfigKey {
        writeDefault();
    }

    public void writeDefault() {
        if (defaultValue() == null || serializer() == null) return;

        Lysten.CONFIG.setIfAbsent(key, serializer.apply(defaultValue));
    }

    public T get() {
        String raw = Lysten.CONFIG.getOrDefault(key, serializer.apply(defaultValue));

        try {
            return parser.apply(raw);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public void set(T value) {
        Lysten.CONFIG.set(key, serializer.apply(value));
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
