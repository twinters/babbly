package be.thomaswinters.textgeneration.domain.declarationrequirement;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleNameRequirement implements IDeclarationRequirement {
	private final String name;

	public SingleNameRequirement(String name) {
		this.name = name;
	}

	@Override
	public boolean canBeFulfilledBy(Collection<String> availableNames) {
		return availableNames.contains(name);
	}

	@Override
	public Set<String> getAllVariablesUsed() {
		return Stream.of(name).collect(Collectors.toSet());
	}

}
