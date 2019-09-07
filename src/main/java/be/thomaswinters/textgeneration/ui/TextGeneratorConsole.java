package be.thomaswinters.textgeneration.ui;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;
import be.thomaswinters.textgeneration.domain.parsers.DeclarationsFileParser;
import be.thomaswinters.textgeneration.domain.parsers.TextGeneratorParser;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interactive console for testing and learning purposes.
 *
 * @author Thomas Winters
 */
public class TextGeneratorConsole {

    private Optional<File> location = Optional.empty();
    private NamedGeneratorRegister register = new NamedGeneratorRegister();

    public void run() {
        Scanner sc = new Scanner(System.in);

        boolean exited = false;
        do {
            System.out.print((location.map(File::getPath).orElse("")) + ">");

            Optional<String> response = Optional.empty();
            try {
                response = interpret(sc.nextLine());
            } catch (Exception e) {
                System.err.println("An error occured: " + e.getMessage());
            }
            response.ifPresent(System.out::println);

        } while (!exited);
        sc.close();
    }

    private Optional<String> interpret(String line) throws IOException {
        TextGeneratorParser parser = new TextGeneratorParser(location);

        if (DeclarationsFileParser.isNameDeclaration(line)) {
            Pair<String, ITextGenerator> parsed = DeclarationsFileParser
                    .parseNamedDeclaration(line, parser);
            register.createGenerator(parsed.getKey(), parsed.getValue());
            return Optional.empty();
        } else {
            ITextGenerator generator = parser.parseGenerator(line);
            return Optional.of(generator.generate());
        }

    }

    public static void main(String[] args) {
        new TextGeneratorConsole().run();
    }

    //TODO: \exit \help \cd commandos.
}
