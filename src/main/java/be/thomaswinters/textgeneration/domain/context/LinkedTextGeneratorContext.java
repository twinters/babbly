package be.thomaswinters.textgeneration.domain.context;

import be.thomaswinters.textgeneration.domain.constraints.LockConstraintRegister;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorDeclaration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class LinkedTextGeneratorContext implements ITextGeneratorContext {

    private final ITextGeneratorContext childContext;
    private final ITextGeneratorContext parentContext;

    public LinkedTextGeneratorContext(ITextGeneratorContext childContext, ITextGeneratorContext parentContext) {
        this.childContext = childContext;
        this.parentContext = parentContext;
    }

    /*-********************************************-*
     *  Named Declarations
     *-********************************************-*/
    @Override
    public boolean hasGenerator(String name) {
        return childContext.hasGenerator(name) || parentContext.hasGenerator(name);
    }

    @Override
    public ITextGenerator getGenerator(String name) {
        if (childContext.hasGenerator(name)) {
            return childContext.getGenerator(name);
        } else {
            return parentContext.getGenerator(name);
        }
    }

    @Override
    public Collection<String> getAllNames() {
        Collection<String> allNames = new HashSet<>(childContext.getAllNames());
        allNames.addAll(parentContext.getAllNames());
        return allNames;
    }

    @Override
    public NamedGeneratorDeclaration createGenerator(String name, ITextGenerator generator) {
        return childContext.createGenerator(name, generator);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Locks
     *-********************************************-*/
    @Override
    public LockConstraintRegister getLockConstraintRegister() {
        return childContext.getLockConstraintRegister();
    }

    @Override
    public Optional<String> getValueFor(LockedGenerator lock) {
        Optional<String> value = childContext.getValueFor(lock);
        return value.isPresent() ? value : parentContext.getValueFor(lock);
    }

    @Override
    public void registerLock(LockedGenerator lock, String generated, ITextGeneratorContext parameters) {
        childContext.registerLock(lock, generated, parameters);
    }
    /*-********************************************-*/

    @Override
    public boolean ignoreGeneratorsUsingMissingDeclarations() {
        return childContext.ignoreGeneratorsUsingMissingDeclarations()
                || parentContext.ignoreGeneratorsUsingMissingDeclarations();
    }

}
