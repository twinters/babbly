package be.thomaswinters.textgeneration.domain.context;

import be.thomaswinters.textgeneration.domain.constraints.LockConstraint;
import be.thomaswinters.textgeneration.domain.constraints.LockConstraintRegister;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockRegister;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorDeclaration;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class TextGeneratorContext implements ITextGeneratorContext {
    private final LockConstraintRegister constraintsRegister;
    private final LockRegister lockRegister;
    private final NamedGeneratorRegister namedRegister;
    private final boolean ignoreGeneratorsUsingMissingDeclarations;

    /*-********************************************-*
     *  Constructors
     *-********************************************-*/

    public TextGeneratorContext(Collection<LockConstraint> constraints, NamedGeneratorRegister namedRegister,
                                LockRegister lockRegister, boolean ignoreGeneratorsUsingMissingDeclarations) {
        this.namedRegister = namedRegister;
        this.ignoreGeneratorsUsingMissingDeclarations = ignoreGeneratorsUsingMissingDeclarations;
        this.lockRegister = lockRegister;
        this.constraintsRegister = new LockConstraintRegister(this, constraints);
        constraintsRegister.renewConstraints(this);
        this.lockRegister.addObserver(constraintsRegister);

    }

    public TextGeneratorContext(Collection<LockConstraint> constraints, NamedGeneratorRegister namedRegister,
                                boolean ignoreGeneratorsUsingMissingDeclarations) {
        this(constraints, namedRegister, new LockRegister(), ignoreGeneratorsUsingMissingDeclarations);
    }

    public TextGeneratorContext(NamedGeneratorRegister namedRegister,
                                boolean ignoreGeneratorsUsingMissingDeclarations) {
        this(new HashSet<>(), namedRegister, ignoreGeneratorsUsingMissingDeclarations);
    }

    public TextGeneratorContext(Collection<LockConstraint> constraints) {
        this(constraints, new NamedGeneratorRegister(), false);
    }

    public TextGeneratorContext() {
        this(new HashSet<>());
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Named Register facade
     *-********************************************-*/
    @Override
    public NamedGeneratorDeclaration createGenerator(String name, ITextGenerator generator) {
        return namedRegister.createGenerator(name, generator);
    }

    @Override
    public Collection<String> getAllNames() {
        return namedRegister.getAllNames();
    }

    @Override
    public ITextGenerator getGenerator(String name) {
        return namedRegister.getGenerator(name);
    }

    @Override
    public boolean hasGenerator(String name) {
        return namedRegister.hasGenerator(name);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Lock register facade
     *-********************************************-*/
    @Override
    public void registerLock(LockedGenerator lock, String generated, ITextGeneratorContext parameters) {
        lockRegister.registerLock(lock, generated, parameters);
    }

    @Override
    public Optional<String> getValueFor(LockedGenerator lock) {
        return lockRegister.getValueFor(lock);
    }

    /*-********************************************-*/

    @Override
    public LockConstraintRegister getLockConstraintRegister() {
        return constraintsRegister;
    }

    @Override
    public boolean ignoreGeneratorsUsingMissingDeclarations() {
        return ignoreGeneratorsUsingMissingDeclarations;
    }

    @Override
    public Collection<String> getCompleteDeclarationNames() {
        return null;
    }
}
