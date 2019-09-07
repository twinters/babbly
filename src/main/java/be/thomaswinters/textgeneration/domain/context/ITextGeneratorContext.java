package be.thomaswinters.textgeneration.domain.context;

import be.thomaswinters.textgeneration.domain.constraints.LockConstraintRegister;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.StaticTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.INamedGeneratorRegister;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorDeclaration;

import java.util.Collection;
import java.util.Optional;

public interface ITextGeneratorContext extends INamedGeneratorRegister {

    /*-********************************************-*
     *  Named register
     *-********************************************-*/

    boolean hasGenerator(String name);

    ITextGenerator getGenerator(String name);

    Collection<String> getAllNames();

    NamedGeneratorDeclaration createGenerator(String name, ITextGenerator generator);

    default NamedGeneratorDeclaration createGenerator(String name, String staticText) {
        return this.createGenerator(name,new StaticTextGenerator(staticText));
    }


    /*-********************************************-*/

    /*-********************************************-*
     *  Lock
     *-********************************************-*/
    LockConstraintRegister getLockConstraintRegister();

    Optional<String> getValueFor(LockedGenerator lock);

    void registerLock(LockedGenerator lock, String generated, ITextGeneratorContext parameters);

    /*-********************************************-*/
    boolean ignoreGeneratorsUsingMissingDeclarations();

}