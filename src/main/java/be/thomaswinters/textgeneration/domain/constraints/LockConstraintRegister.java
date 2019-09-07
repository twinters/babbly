package be.thomaswinters.textgeneration.domain.constraints;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.locked.ILockObserver;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LockConstraintRegister implements ILockObserver {
    private final Collection<LockConstraint> constraints;
    private final Map<LockedGenerator, Collection<String>> lockPossibilities = new HashMap<>();

    public LockConstraintRegister(ITextGeneratorContext parameters, Collection<LockConstraint> constraints) {
        this.constraints = constraints;
    }

    public LockConstraintRegister(ITextGeneratorContext parameters) {
        this(parameters, new HashSet<>());
    }

    public void renewConstraints(ITextGeneratorContext parameters) {
        constraints.stream().forEach(e -> e.applyConstraints(this, parameters));
    }

    /**
     * Takes intersection of what was possible with given set and sets it as
     * possible values
     *
     * @param locked
     * @param values
     */
    void registerPotentialValues(LockedGenerator locked, Collection<String> values) {
        if (!lockPossibilities.containsKey(locked)) {
            this.lockPossibilities.put(locked, new HashSet<>(values));
        } else {
            this.lockPossibilities.get(locked).retainAll(values);
        }

    }

    public boolean hasRestrictions(LockedGenerator locked) {
        return lockPossibilities.containsKey(locked);
    }

    public Collection<String> getValuesFor(LockedGenerator locked) {
        if (lockPossibilities.get(locked) == null) {
            return ImmutableSet.of();
        }
        return ImmutableSet.copyOf(lockPossibilities.get(locked));
    }

    @Override
    public void update(LockedGenerator lock, ITextGeneratorContext parameters) {
        renewConstraints(parameters);
    }

    @Override
    public String toString() {
        return "LockConstraintRegister: " + constraints;
    }

}
