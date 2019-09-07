package be.thomaswinters.textgeneration.domain.generators.locked;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

public class LockRegister {
    private final HashSet<ILockObserver> observers = new HashSet<>();
    private final Map<LockedGenerator, String> lockMap = new HashMap<>();

    public void registerLock(LockedGenerator lock, String generated, ITextGeneratorContext parameters) {
        this.lockMap.put(lock, generated);
        observers.stream().forEach(e -> e.update(lock, parameters));
    }

    public Optional<String> getValueFor(LockedGenerator lock) {
        if (lockMap.get(lock) == null) {
            return Optional.empty();
        }
        return Optional.of(lockMap.get(lock));
    }

    public void addObserver(ILockObserver observer) {
        observers.add(observer);
    }
}
