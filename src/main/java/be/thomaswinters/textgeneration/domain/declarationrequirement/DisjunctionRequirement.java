package be.thomaswinters.textgeneration.domain.declarationrequirement;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class DisjunctionRequirement implements IDeclarationRequirement {
    private final ImmutableSet<IDeclarationRequirement> required;

    public DisjunctionRequirement(Collection<IDeclarationRequirement> required) {
        this.required = ImmutableSet.copyOf(required);
    }

    public DisjunctionRequirement(IDeclarationRequirement... required) {
        this(ImmutableSet.copyOf(required));
    }

    @Override
    public boolean canBeFulfilledBy(Collection<String> availableNames) {
        return required.stream().anyMatch(e -> e.canBeFulfilledBy(availableNames));
    }

    @Override
    public Set<String> getAllVariablesUsed() {
        return required.stream().flatMap(e -> e.getAllVariablesUsed().stream()).collect(Collectors.toSet());
    }
}
