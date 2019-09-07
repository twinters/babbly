package be.thomaswinters.textgeneration.domain.declarationrequirement;

import java.util.Collection;
import java.util.Set;

public interface IDeclarationRequirement {
    boolean canBeFulfilledBy(Collection<String> availableNames);


    Set<String> getAllVariablesUsed();
}
