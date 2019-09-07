package be.thomaswinters.textgeneration.domain.generators.locked;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockedGeneratorPool {

    private static final Map<String, Map<String, LockedGenerator>> lockPool = new ConcurrentHashMap<>();

    public static LockedGenerator get(String name, String identifier) {

        if (!lockPool.containsKey(name)) {
            lockPool.put(name, new ConcurrentHashMap<>());
        }
        if (!lockPool.get(name).containsKey(identifier)) {
            LockedGenerator generator = new LockedGenerator(name, identifier);
            lockPool.get(name).put(identifier, generator);
            return generator;
        }
        return lockPool.get(name).get(identifier);
    }
}
