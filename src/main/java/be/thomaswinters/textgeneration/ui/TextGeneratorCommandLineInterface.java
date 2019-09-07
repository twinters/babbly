package be.thomaswinters.textgeneration.ui;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class TextGeneratorCommandLineInterface {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        ITextGenerator generator = parseCombinationDatabase(
                args.length > 0 ? Optional.of(args[0]) : Optional.empty(), sc);
        int amountOfGenerations = parseAmount(
                args.length > 1 ? Optional.of(args[1]) : Optional.empty(), sc);

        for (int i = 0; i < amountOfGenerations; i++) {
            System.out.println(generator.generate());
        }
    }

    private static ITextGenerator parseCombinationDatabase(
            Optional<String> givenArgument, Scanner sc) throws IOException {

        File file;
        if (givenArgument.isPresent()) {
            file = new File(givenArgument.get());

            if (!file.exists()) {
                System.err
                        .println("Given file does not exist. Please specify another.");
            }
        } else {
            file = promptForFile(sc);
        }

        ITextGenerator generator = TextGeneratorPool
                .getDeclarationFile(file);

        return generator;
    }

    private static File promptForFile(Scanner sc) {
        System.out.println("What file would you like to use to generate?");
        try {
            File file = new File(sc.nextLine());
            if (!file.exists()) {
                System.err
                        .println("Given file does not exist. Please specify another.");
                return promptForFile(sc);
            } else {
                return file;
            }
        } catch (Exception e) {
            System.err.println("An error occured: " + e.getMessage());
            return promptForFile(sc);
        }
    }

    private static int parseAmount(Optional<String> givenArgument, Scanner sc) {

        int amount = 1;
        if (givenArgument.isPresent()) {
            if (!givenArgument.get().matches("[0-9]+")) {
                System.err
                        .println("Given amount is not a valid amount. Please specify a new amount.");
                amount = promptForAmount(sc);
            }
            amount = Integer.parseInt(givenArgument.get());
        } else {
            amount = promptForAmount(sc);
        }

        return amount;
    }

    private static int promptForAmount(Scanner sc) {
        System.out.println("Please specify an amount of generations.");
        String answer = sc.nextLine();
        if (!answer.matches("[0-9]+")) {
            System.err.println("Please specify a valid amount.");
            return promptForAmount(sc);
        } else {
            return Integer.parseInt(answer);
        }
    }
}
