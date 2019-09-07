package be.thomaswinters.textgeneration.domain.generators.locked;

import be.thomaswinters.random.Picker;
import be.thomaswinters.textgeneration.domain.context.ITextGeneratorContext;
import be.thomaswinters.textgeneration.domain.declarationrequirement.IDeclarationRequirement;
import be.thomaswinters.textgeneration.domain.declarationrequirement.SingleNameRequirement;
import be.thomaswinters.textgeneration.domain.generators.named.NamedTextGenerator;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class LockedGenerator extends NamedTextGenerator {

    private final String identifier;

    protected LockedGenerator(String name, String identifier) {
        super(name);
        if (identifier == null) {
            throw new NullPointerException();
        }
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String generate(ITextGeneratorContext parameters) {

        // Check if already generated:
        Optional<String> previouslyGenerated = parameters.getValueFor(this);
        if (previouslyGenerated.isPresent()) {
            return previouslyGenerated.get();
        }

        // Generate:
        String generated;

        // Check if constrained
        if (parameters.getLockConstraintRegister().hasRestrictions(this)) {
            if (parameters.getLockConstraintRegister().getValuesFor(this).isEmpty()) {
                throw new IllegalStateException("No options left for " + this);
            }
            generated = Picker.pick(parameters.getLockConstraintRegister().getValuesFor(this));
        } else {
            // Just generated otherwise
            generated = super.generate(parameters);
        }

        parameters.registerLock(this, generated, parameters);
        return generated;
    }

    @Override
    public Stream<String> getAllGenerations(ITextGeneratorContext parameters) {
        Optional<String> potentialLock = parameters.getValueFor(this);
        if (potentialLock.isPresent()) {
            return Arrays.asList(potentialLock.get()).stream();
        }

        if (parameters.getLockConstraintRegister().hasRestrictions(this)) {
            return parameters.getLockConstraintRegister().getValuesFor(this).stream();
        }

        return super.getAllGenerations(parameters);
    }

    @Override
    public String toCode() {
        return "<" + getDeclaration() + (getIdentifier().equals("") ? "" : ":" + getIdentifier()) + ">";
    }

    /*-********************************************-*
     *  Overriden object methods (value class)
     *-********************************************-*/
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDeclaration() == null) ? 0 : getDeclaration().hashCode());
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LockedGenerator other = (LockedGenerator) obj;
        if (getDeclaration() == null) {
            if (other.getDeclaration() != null)
                return false;
        } else if (!getDeclaration().equals(other.getDeclaration()))
            return false;
        if (identifier == null) {
            if (other.identifier != null)
                return false;
        } else if (!identifier.equals(other.identifier))
            return false;
        return true;
    }

    /*-********************************************-*/

    @Override
    public IDeclarationRequirement getRequirements() {
        return new SingleNameRequirement(getDeclaration());
    }
}
