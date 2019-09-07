package be.thomaswinters.textgeneration.domain.constraints;

import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LockConstraint {
    private final LockedGenerator lock1;
    private final LockedGenerator lock2;

    public LockConstraint(LockedGenerator lock1, LockedGenerator lock2) {
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    protected abstract boolean passesConstraint(String string1, String string2);

    public void applyConstraints(LockConstraintRegister register,
                                 ITextGeneratorContext parameters) {

        List<String> lock1Possibilities = lock1.getAllGenerations(parameters)
                .collect(Collectors.toList());
        List<String> lock2Possibilities = lock2.getAllGenerations(parameters)
                .collect(Collectors.toList());

        Collection<String> accepted1 = new HashSet<>();
        Collection<String> accepted2 = new HashSet<>();

        for (String string1 : lock1Possibilities) {
            for (String string2 : lock2Possibilities) {
                if (passesConstraint(string1, string2)) {
                    accepted1.add(string1);
                    accepted2.add(string2);
                }
            }
        }

        register.registerPotentialValues(lock1, accepted1);
        register.registerPotentialValues(lock2, accepted2);

    }

    protected LockedGenerator getLock1() {
        return lock1;
    }

    protected LockedGenerator getLock2() {
        return lock2;
    }
}
