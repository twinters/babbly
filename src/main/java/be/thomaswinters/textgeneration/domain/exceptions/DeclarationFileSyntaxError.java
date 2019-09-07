package be.thomaswinters.textgeneration.domain.exceptions;

import java.io.File;
import java.net.URL;

public class DeclarationFileSyntaxError extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 2116098693669559161L;
    private static String DEFAULT_MESSAGE = "There was a syntax error in the declaration file:\n";

    public DeclarationFileSyntaxError(File combinationDatabaseLocation,
                                      String errorMessage) {
        super(
                DEFAULT_MESSAGE
                        + "DeclarationFile:"
                        + combinationDatabaseLocation
                        + "\n"
                        + ((errorMessage != null && !errorMessage.equals("")) ? "Error: "
                        + errorMessage
                        : "No error specified."));
    }

    public DeclarationFileSyntaxError(URL combinationDatabaseLocation,
                                      String errorMessage) {
        super(
                DEFAULT_MESSAGE
                        + "DeclarationFile:"
                        + combinationDatabaseLocation
                        + "\n"
                        + ((errorMessage != null && !errorMessage.equals("")) ? "Error: "
                        + errorMessage
                        : "No error specified."));
    }


}
