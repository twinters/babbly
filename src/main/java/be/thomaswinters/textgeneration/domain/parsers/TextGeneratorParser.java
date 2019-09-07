package be.thomaswinters.textgeneration.domain.parsers;

import be.thomaswinters.textgeneration.domain.exceptions.TextGeneratorSyntaxError;
import be.thomaswinters.textgeneration.domain.factories.command.CommandFactory;
import be.thomaswinters.textgeneration.domain.factories.command.SingleTextGeneratorArgumentCommandFactory;
import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.Arguments;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.IArgumentType;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.RepeaterTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool;
import be.thomaswinters.textgeneration.domain.generators.commands.*;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationsFileTextGeneratorProxy;
import be.thomaswinters.textgeneration.domain.generators.databases.WordsFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGeneratorPool;
import be.thomaswinters.textgeneration.domain.generators.named.NamedTextGenerator;
import be.thomaswinters.textgeneration.domain.parsers.argumenttypes.*;
import be.thomaswinters.textgeneration.domain.parsers.util.JunctionBuilder;
import be.thomaswinters.textgeneration.domain.parsers.util.StaticStringReader;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class TextGeneratorParser {

    private static final ImmutableCollection<Character> PROHIBITED_CHARACTERS = ImmutableSet.of('\\', '/', '|', '=', '<', '>',
            '(', ')');

    /*-********************************************-*
     *  Default commands
     *-********************************************-*/
    private static final Collection<CommandFactory> DEFAULT_SINGLE_ARGUMENT_COMMANDS = Arrays.asList(
            // Capitalise
            new SingleTextGeneratorArgumentCommandFactory("capitalise", CapitaliseCommand::new),
            new SingleTextGeneratorArgumentCommandFactory("capitaliseAll", CapitaliseAllCommand::new),
            new SingleTextGeneratorArgumentCommandFactory("decapitalise", DecapitaliseCommand::new),

            // Lowercase
            new SingleTextGeneratorArgumentCommandFactory("lower", LowerCaseCommand::new),

            // Uppercase
            new SingleTextGeneratorArgumentCommandFactory("upper", UpperCaseCommand::new),
            // Synonym
//            new SingleTextGeneratorArgumentCommandFactory("synonym", SynonymCommand::new),
            // Ifdefined
            new SingleTextGeneratorArgumentCommandFactory("ifdefined", IfDefinedCommand::new),
            // Maybe
            new CommandFactory("maybe",
                    new FunctionHeader(Arguments.TEXT_GENERATOR_ARG, Arguments.DOUBLE_ARG_OPTIONAL)) {
                @Override
                protected ITextGeneratorCommand createFunction(List<Object> arguments) {
                    ITextGenerator generator = (ITextGenerator) arguments.get(0);
                    double chance = arguments.size() > 1 ? (double) arguments.get(1) : 0.5d;
                    return new MaybeCommand(generator, chance);
                }
            },

            // Declaration file
            new CommandFactory("decl", new FunctionHeader(Arguments.FILE_ARG)) {
                @Override
                protected ITextGeneratorCommand createFunction(List<Object> arguments) {
                    try {
                        return new DeclarationsFileTextGeneratorProxy((File) arguments.get(0));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Illegal file: " + e.getMessage());
                    }
                }
            },

            // Words file
            new CommandFactory("words", new FunctionHeader(Arguments.FILE_ARG)) {
                @Override
                protected ITextGeneratorCommand createFunction(List<Object> arguments) {
                    try {
                        return new WordsFileTextGenerator((File) arguments.get(0));
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Illegal file: " + e.getMessage());
                    }
                }
            });

    private static Map<String, CommandFactory> toCommandFactoryMap(Collection<CommandFactory> factories) {
        Map<String, CommandFactory> result = new HashMap<>();
        factories.forEach(e -> result.put(e.getName(), e));
        return result;
    }

    /*-********************************************-*/

    private final Optional<File> relativeLocation;
    private final Map<String, CommandFactory> commandFactories;
    private final TextGeneratorParser self = this;

    // private

    /*-********************************************-*
     *  Constructors
     *-********************************************-*/
    public TextGeneratorParser(Optional<File> relativeLocation, Collection<CommandFactory> commands) {
        this.relativeLocation = relativeLocation;

        Builder<String, CommandFactory> commandFactoriesBuilder = new Builder<>();
        commandFactoriesBuilder.putAll(toCommandFactoryMap(DEFAULT_SINGLE_ARGUMENT_COMMANDS));
        commandFactoriesBuilder.putAll(toCommandFactoryMap(commands));

        this.commandFactories = commandFactoriesBuilder.build();
    }

    public TextGeneratorParser(Optional<File> relativeLocation) {
        this(relativeLocation, new HashSet<>());
    }

    public TextGeneratorParser() {
        this(Optional.empty());
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  General main parse function
     *-********************************************-*/

    public ITextGenerator parseGenerator(String input) throws IOException {
        return createStringReadingTextGenerator(input).getResult();
    }

    public StringReadingTextGeneratorParser createStringReadingTextGenerator(String input) throws IOException {
        return new StringReadingTextGeneratorParser(new StaticStringReader(input));
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Easy access
     *-********************************************-*/
    public static ITextGenerator parse(String input) throws IOException {
        return (new TextGeneratorParser()).parseGenerator(input);
    }

    /*-********************************************-*/

    public class StringReadingTextGeneratorParser {

        private final StaticStringReader inputStream;
        private final ITextGenerator parsed;
        private Optional<String> remainingCharacters = Optional.empty();

        public StringReadingTextGeneratorParser(StaticStringReader stringReader) throws IOException {
            this.inputStream = stringReader;
            initArgumentParsers();
            this.parsed = parseStream();
        }

        private ITextGenerator parseStream() throws IOException {
            JunctionBuilder buffer = new JunctionBuilder();

            while (inputStream.hasNext()) {
                char lastCharacter = inputStream.next();

                // Check last character
                switch (lastCharacter) {
                    case '\\':
                        buffer.add(parseCommand());
                        break;
                    // case '+':
                    // buffer.markConjunction();
                    // break;
                    case '|':
                        if (inputStream.hasNext() && inputStream.peek() == '|') {
                            inputStream.next();
                            buffer.markEqualDisjunction();
                        } else {
                            buffer.markDisjunction();
                        }
                        break;
                    case '/':
                        buffer.markCascade();
                        break;
                    case '(':
                        buffer.add(parseStream());
                        break;
                    case '<':
                        buffer.add(parseNamed());
                        break;
                    case '{':
                        buffer.modifyLast(parseRepeater());
                        break;
                    case ')':
                        return buffer.poll();
                    default:
                        buffer.add(lastCharacter);
                        break;
                }
            }

            ITextGenerator generator = buffer.poll();
            remainingCharacters = Optional.of(buffer.pollRemainingBuffer());
            return generator;
        }

        private Function<ITextGenerator, ITextGenerator> parseRepeater() {

            boolean isRepeater = false;
            boolean isChance = false;

            int firstNumber = 0;

            StringBuilder builder = new StringBuilder();
            while (inputStream.hasNext() && inputStream.peek() != '}') {
                char next = inputStream.next();
                if (Character.isDigit(next)) {
                    builder.append(next);
                } else if (next == '.') {
                    if (isRepeater) {
                        throw new TextGeneratorSyntaxError(inputStream, "Can't have decimal points in repeater.");
                    } else if (isChance) {
                        throw new TextGeneratorSyntaxError(inputStream, "Can't have double decimal points");
                    }
                    if (!builder.toString().trim().equals("") && Integer.parseInt(builder.toString()) != 0) {
                        throw new TextGeneratorSyntaxError(inputStream, "Can't have numbers higher than 1 in a chance.");
                    }
                    isChance = true;
                    builder.append(next);
                } else if (next == ',') {
                    if (isRepeater) {
                        throw new TextGeneratorSyntaxError(inputStream, "Can only have two numbers in a repeater");
                    } else if (isChance) {
                        throw new TextGeneratorSyntaxError(inputStream, "Can't use a repeater in combination with a chance");
                    }
                    isRepeater = true;
                    // Extract the minimum and start parsing the maximum
                    firstNumber = Integer.parseInt(builder.toString());
                    builder = new StringBuilder();
                }
            }
            if (inputStream.next() != '}') {
                throw new TextGeneratorSyntaxError(inputStream,
                        "Repeating modifiers should end with }");
            }
            if (isRepeater) {
                int minimum = firstNumber;
                int maximum = Integer.parseInt(builder.toString());
                return e -> new RepeaterTextGenerator(e, minimum, maximum);
            } else if (isChance) {
                double chance = Double.parseDouble(builder.toString());
                return e -> new MaybeCommand(e, chance);
            }

            throw new TextGeneratorSyntaxError(inputStream, "Repeaters need to specify a minimum and a maximum, or a chance");

        }


        public Optional<String> getRemainingCharacters() {
            return remainingCharacters;
        }

        public ITextGenerator getResult() {
            return parsed;
        }

        /*-********************************************-*
         *  Argument parsers
         *-********************************************-*/

        private final Map<IArgumentType, ArgumentTypeParser> argumentParsers = new HashMap<>();

        private void addArgumentParser(ArgumentTypeParser parser) {
            argumentParsers.put(parser.getType(), parser);
        }

        private void initArgumentParsers() {
            addArgumentParser(new IntegerTypeParser());
            addArgumentParser(new DoubleTypeParser());
            addArgumentParser(new TextGeneratorTypeParser(self));
            addArgumentParser(new LockedGeneratorTypeParser());
            addArgumentParser(new FileTypeParser(relativeLocation));
        }

        /*-********************************************-*/

        /*-********************************************-*
         *  Command
         *-********************************************-*/
        private ITextGenerator parseCommand() throws IOException {
            StringBuilder read = new StringBuilder();
            if (!inputStream.hasNext()) {
                throw new TextGeneratorSyntaxError(inputStream,
                        "The escape/command character must be followed by something.");
            }

            // Check if escaped special character
            char lastCharacter = inputStream.next();
            read.append(lastCharacter);
            if (PROHIBITED_CHARACTERS.contains(lastCharacter)) {
                return TextGeneratorPool.getStatic(lastCharacter + "");
            }

            // Empty line
            if (lastCharacter == ' ') {
                return TextGeneratorPool.getStatic(" ");
            }

            // Commandsupport
            read.append(inputStream.next());

            // Find the commando name
            while (inputStream.hasNext() && Character.isAlphabetic(inputStream.peek())) {
                lastCharacter = inputStream.next();
                read.append(lastCharacter);
            }

            // Check errors
            if (!inputStream.hasNext()) {
                throw new TextGeneratorSyntaxError(inputStream, "Illegal end of line in command");
            }
            if (inputStream.peek() != '(') {
                throw new TextGeneratorSyntaxError(inputStream, "A commandname should always be followed by a '('");
            }

            // Namestuff
            String commandName = read.toString();
            CommandFactory commandFactory = getCommandFactory(commandName);

            List<String> argumentStrings = getCommandArguments();
            List<Object> arguments = parseArguments(argumentStrings, commandFactory.getHeader());

            return commandFactory.create(arguments);
        }

        private CommandFactory getCommandFactory(String commandName) {
            CommandFactory commandFactory = commandFactories.get(commandName);

            if (commandFactory == null) {
                throw new IllegalStateException("The given command does not exist: " + commandName);
            }
            return commandFactory;
        }

        private List<Object> parseArguments(List<String> stringArguments, FunctionHeader header) {

            List<Object> arguments = new ArrayList<>();
            for (int i = 0; i < stringArguments.size(); i++) {

                String toParse = stringArguments.get(i);
                ArgumentTypeParser parser = argumentParsers.get(header.get(i).getType());

                arguments.add(parser.parse(toParse));
            }

            return arguments;
        }

        private List<String> getCommandArguments() {
            if (inputStream.next() != '(') {
                throw new IllegalStateException("Can not start reading command arguments: Does not start with a '('");
            }

            List<String> results = new ArrayList<>();
            StringBuilder currentStringBuilder = new StringBuilder();

            int bracketsDeep = 1;

            while (inputStream.hasNext() && bracketsDeep > 0) {
                char ch = inputStream.next();

                switch (ch) {
                    case ';':
                        results.add(currentStringBuilder.toString());
                        currentStringBuilder = new StringBuilder();
                        break;
                    case '(':
                        bracketsDeep++;
                        currentStringBuilder.append(ch);
                        break;
                    case ')':
                        bracketsDeep--;
                        if (bracketsDeep > 0) {
                            currentStringBuilder.append(ch);
                        }
                        break;
                    default:
                        currentStringBuilder.append(ch);
                }
            }
            results.add(currentStringBuilder.toString());

            return results;
        }

        /*-********************************************-*/

        /*-********************************************-*
         *  Named
         *-********************************************-*/
        private ITextGenerator parseNamed() throws IOException {

            Optional<ITextGenerator> result = Optional.empty();

            // READ NAME
            StringBuilder nameBuilder = new StringBuilder();
            while (
                    inputStream.hasNext() && (Character.isAlphabetic(inputStream.peek()) || Character.isDigit(inputStream.peek()) ||
                            inputStream.peek() == '_')) {
                nameBuilder.append(inputStream.next());
            }
            String referencedName;
            result = Optional.of(new NamedTextGenerator(nameBuilder.toString()));

            // Identifier start
            if (inputStream.hasNext() && inputStream.peek() == ':') {
                // Poll the colon
                inputStream.next();
                result = Optional.of(parseNamedIdentifier(nameBuilder.toString()));
            }
            while (inputStream.hasNext() && inputStream.peek() == '.') {
                // Poll of the dot
                inputStream.next();
                result = Optional.of(parseTextGeneratorCommand(result));
            }

            // Poll >
            if (inputStream.hasNext() && inputStream.peek() == '>') {
                // Poll ending
                inputStream.next();

                if (result.isPresent()) {
                    return result.get();
                } else {
                    throw new TextGeneratorSyntaxError(inputStream, "No named reference was properly made");
                }
            }
            throw new TextGeneratorSyntaxError(inputStream, "Declaration reference was not properly enclosed: " + inputStream.peek());
        }


        private ITextGenerator parseNamedIdentifier(String name) throws IOException {
            StringBuilder identifier = new StringBuilder();
            while (inputStream.hasNext() && (Character.isAlphabetic(inputStream.peek()) || Character.isDigit(inputStream.peek()))) {
                identifier.append(inputStream.next());
            }
            return LockedGeneratorPool.get(name, identifier.toString());
        }

        private ITextGenerator parseTextGeneratorCommand(Optional<ITextGenerator> argumentGenerator) {
            if (!argumentGenerator.isPresent()) {
                throw new TextGeneratorSyntaxError(inputStream, "Can't apply function on empty text generator: " + argumentGenerator);
            }
            StringBuilder commandReader = new StringBuilder();
            while (inputStream.hasNext() && Character.isAlphabetic(inputStream.peek())) {
                commandReader.append(inputStream.next());
            }
            String commandName = commandReader.toString();
            return getCommandFactory(commandName).create(Arrays.asList(argumentGenerator.get()));
        }

        /*-********************************************-*/
    }

    protected Optional<File> getRelativeLocation() {
        return relativeLocation;
    }

}
