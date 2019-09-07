package be.thomaswinters.textgeneration.domain.exceptions;

import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;
import be.thomaswinters.textgeneration.domain.parsers.util.StaticStringReader;

public class MissingNamedGeneratorException extends TextGeneratorSyntaxError {

    private static final long serialVersionUID = -1289916460624107336L;
    private final String name;
    private final NamedGeneratorRegister register;

    public MissingNamedGeneratorException(StaticStringReader textGeneratorString, String name,
                                          NamedGeneratorRegister register) {
        super(textGeneratorString, "There was no generator named '" + name + "' declared."
                + "\nDeclared generators:\n" + register);
        this.name = name;
        this.register = register;
    }

    public String getName() {
        return name;
    }

    public NamedGeneratorRegister getRegister() {
        return register;
    }
}
