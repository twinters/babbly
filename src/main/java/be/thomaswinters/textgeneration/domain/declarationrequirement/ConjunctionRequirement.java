package be.thomaswinters.textgeneration.domain.declarationrequirement;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ConjunctionRequirement implements IDeclarationRequirement {
    private final ImmutableSet<IDeclarationRequirement> required;

    public ConjunctionRequirement(Collection<IDeclarationRequirement> required) {
        this.required = ImmutableSet.copyOf(required);
    }

    @Override
    public boolean canBeFulfilledBy(Collection<String> availableNames) {
        return required.stream().allMatch(e -> e.canBeFulfilledBy(availableNames));
    }

    @Override
    public Set<String> getAllVariablesUsed() {
        return required.stream().flatMap(e -> e.getAllVariablesUsed().stream()).collect(Collectors.toSet());
    }
}
