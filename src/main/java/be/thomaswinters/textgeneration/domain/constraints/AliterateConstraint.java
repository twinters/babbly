package be.thomaswinters.textgeneration.domain.constraints;

import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;

public class AliterateConstraint extends LockConstraint {

    private final int numberOfCharacters;

    public AliterateConstraint(LockedGenerator lock1, LockedGenerator lock2,
                               int numberOfCharacters) {
        super(lock1, lock2);
        if (numberOfCharacters < 0) {
            throw new IllegalArgumentException(
                    "Illegal amount of characters for an aliteration: "
                            + numberOfCharacters);
        }
        this.numberOfCharacters = numberOfCharacters;
    }

    public AliterateConstraint(LockedGenerator lock1, LockedGenerator lock2) {
        this(lock1, lock2, 1);
    }

    @Override
    protected boolean passesConstraint(String string1, String string2) {
        if (string1.equals(string2)) {
            return false;
        }
        if (string1.length() < numberOfCharacters || string2.length() < numberOfCharacters) {
            return false;
        }
        for (int i = 0; i < numberOfCharacters; i++) {
            if (string1.charAt(i) != string2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "#constraint aliterate " + getLock1() + " " + getLock2();
    }

}
