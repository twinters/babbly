package be.thomaswinters.textgeneration.domain.declarationrequirement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EmptyRequirement implements IDeclarationRequirement {

	@Override
	public boolean canBeFulfilledBy(Collection<String> availableNames) {
		return true;
	}

	@Override
	public Set<String> getAllVariablesUsed() {
		return new HashSet<>();
	}

}
