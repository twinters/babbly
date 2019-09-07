package be.thomaswinters.textgeneration.domain.generators.databases;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.commands.ITextGeneratorCommand;
import be.thomaswinters.textgeneration.domain.parsers.DeclarationsFileParser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class DeclarationsFileTextGeneratorProxy extends
        AFileTextGenerationProxy implements ITextGeneratorCommand {

    // private TextGeneratorParameters preconditions; //TODO: in disjunction
    // eventueel?

    /*-********************************************-*
     *  Constructor and factory method
     *-********************************************-*/

    public DeclarationsFileTextGeneratorProxy(File combinationDatabaseFile)
            throws IOException {
        super(combinationDatabaseFile);
    }

    /*-********************************************-*/

    @Override
    protected Optional<ITextGenerator> loadGenerator() {
        try {
            DeclarationsFileParser parser = new DeclarationsFileParser(
                    getDatabaseFile());

            return Optional.of(parser.getGenerator());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    protected String getCodeName() {
        return "decl";
    }

}
