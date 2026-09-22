package jjcompat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/** A server-lifecycle snapshot of only the defaults this mod replaces. */
final class SurfaceDefaultsSession<K, V> {
    private final Function<K, V> read;
    private final BiConsumer<K, V> write;
    private final Map<K, V> previous = new LinkedHashMap<>();

    SurfaceDefaultsSession(Function<K, V> read, BiConsumer<K, V> write) {
        this.read = read;
        this.write = write;
    }

    static boolean matchesOverworld(boolean marker, int minY, int height) {
        return marker && minY == -64 && height == 2096;
    }

    static boolean matchesNether(int minY, int height) {
        return minY == 0 && height == 2032;
    }

    void replace(K category, V rule) {
        // containsKey deliberately preserves a captured null value.
        if (!previous.containsKey(category)) previous.put(category, read.apply(category));
        write.accept(category, rule);
    }

    void restore() {
        previous.forEach(write);
        previous.clear();
    }
}
