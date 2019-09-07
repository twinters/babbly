package be.thomaswinters.textgeneration.domain.generators.databases;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.commands.ITextGeneratorCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class WordsFileTextGenerator extends AFileTextGenerationProxy implements ITextGeneratorCommand {

    /*-********************************************-*
     *  Constructor and factory method
     *-********************************************-*/
    public WordsFileTextGenerator(File wordDatabaseFile) throws IOException {
        super(wordDatabaseFile);
    }

    @Override
    protected Optional<ITextGenerator> loadGenerator() {
        try {
            return Optional.of(DisjunctionTextGenerator.fromStrings(Files.readAllLines(getDatabaseFile().toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    protected String getCodeName() {
        return "words";
    }

    /*-********************************************-*/

}
