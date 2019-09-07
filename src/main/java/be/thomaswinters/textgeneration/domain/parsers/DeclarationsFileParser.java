package be.thomaswinters.textgeneration.domain.parsers;

import be.thomaswinters.textgeneration.domain.constraints.LockConstraint;
import be.thomaswinters.textgeneration.domain.exceptions.DeclarationFileSyntaxError;
import be.thomaswinters.textgeneration.domain.factories.command.CommandFactory;
import be.thomaswinters.textgeneration.domain.factories.constraints.AliterateConstraintFactory;
import be.thomaswinters.textgeneration.domain.factories.constraints.ConstraintFactory;
import be.thomaswinters.textgeneration.domain.functionheader.FunctionHeader;
import be.thomaswinters.textgeneration.domain.functionheader.arguments.IArgumentType;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.databases.DeclarationsFileTextGeneratorProxy;
import be.thomaswinters.textgeneration.domain.generators.databases.WordsFileTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.named.NamedGeneratorRegister;
import be.thomaswinters.textgeneration.domain.generators.named.NamedTextGenerator;
import be.thomaswinters.textgeneration.domain.parsers.argumenttypes.*;
import be.thomaswinters.util.DataLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DeclarationsFileParser {

    private static final String MAIN_DECLARATION = "main";

    /*-********************************************-*
     *  Prefixes
     *-********************************************-*/
    private static final String CONSTRAINT_PREFIX = "#constraint";

    /*-********************************************-*/

    /*-********************************************-*
     *  Constraint factories
     *-********************************************-*/

    private static final Map<String, ConstraintFactory> DEFAULT_CONSTRAINT_FACTORIES = new HashMap<>();

    private static void addConstraintFactory(ConstraintFactory factory) {
        DEFAULT_CONSTRAINT_FACTORIES.put(factory.getName(), factory);
    }

    static {
        addConstraintFactory(new AliterateConstraintFactory());
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Instance
     *-********************************************-*/
    private final NamedGeneratorRegister namedRegister;
    private final Collection<LockConstraint> constraints;
    private final URL file;
    private final Optional<File> relativeFile;
    private final FileTypeParser fileTypeParser;
    private final TextGeneratorParser lineParser;
    private final DeclarationFileTextGenerator parsedGenerator;
    /*-********************************************-*/

    public DeclarationsFileParser(URL file, Optional<File> parentFile, NamedGeneratorRegister namedRegister,
                                  boolean ignoreGeneratorsUsingMissingDeclarations, Collection<CommandFactory> commands) throws IOException {
        this.file = file;
        this.namedRegister = namedRegister;
        this.constraints = new HashSet<>();
        this.relativeFile = parentFile;
        this.fileTypeParser = new FileTypeParser(relativeFile);
        this.lineParser = new TextGeneratorParser(relativeFile, commands);

        initArgumentParsers();

        ITextGenerator parsedGenerator = getParsedTextGenerator();

        if (!ignoreGeneratorsUsingMissingDeclarations) {
            // TODO: improve "get requirements" check to not include doubles.

            // Collection<String> completeDeclarationNames =
            // namedRegister.getCompleteDeclarationNames();
            // Collection<String> missingDeclarations =
            // getMissingDeclarations(parsedGenerator, completeDeclarationNames);
            // if (!missingDeclarations.isEmpty()) {
            // throw new DeclarationFileSyntaxError(file, "There are missing declarations: "
            // + missingDeclarations);
            //
            // }
        }

        this.parsedGenerator = new DeclarationFileTextGenerator(parsedGenerator, namedRegister, constraints,
                ignoreGeneratorsUsingMissingDeclarations);
    }

    public DeclarationsFileParser(File file, NamedGeneratorRegister namedRegister,
                                  boolean ignoreGeneratorsUsingMissingDeclarations, Collection<CommandFactory> commands) throws IOException {
        this(file.toURI().toURL(), Optional.ofNullable(file.getParentFile()), namedRegister, ignoreGeneratorsUsingMissingDeclarations, commands);
    }

    public DeclarationsFileParser(URL file, NamedGeneratorRegister namedRegister,
                                  boolean ignoreGeneratorsUsingMissingDeclarations, Collection<CommandFactory> commands) throws IOException {
        this(file, Optional.empty(), namedRegister, ignoreGeneratorsUsingMissingDeclarations, commands);
    }

    public DeclarationsFileParser(File file, NamedGeneratorRegister namedRegister,
                                  boolean ignoreGeneratorsUsingMissingDeclarations) throws IOException {
        this(file, namedRegister, ignoreGeneratorsUsingMissingDeclarations, new HashSet<>());
    }

    public DeclarationsFileParser(URL file, NamedGeneratorRegister namedRegister,
                                  boolean ignoreGeneratorsUsingMissingDeclarations) throws IOException {
        this(file, namedRegister, ignoreGeneratorsUsingMissingDeclarations, new HashSet<>());
    }

    public DeclarationsFileParser(File file, NamedGeneratorRegister namedRegister) throws IOException {
        this(file, namedRegister, false);
    }

    public DeclarationsFileParser(URL file, NamedGeneratorRegister namedRegister) throws IOException {
        this(file, namedRegister, false);
    }

    public DeclarationsFileParser(File file) throws IOException {
        this(file, new NamedGeneratorRegister());
    }

    public DeclarationsFileParser(URL file) throws IOException {
        this(file, new NamedGeneratorRegister());
    }



    /*-********************************************-*
     *  Check validity
     *-********************************************-*/

    public static Collection<String> getMissingDeclarations(ITextGenerator generator, Collection<String> declarations) {
        Set<String> result = new HashSet<>(generator.getRequirements().getAllVariablesUsed());
        result.removeAll(declarations);
        return result;
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Strings reading
     *-********************************************-*/
    private void parseStrings() throws IOException {

        List<String> readStrings = DataLoader.readLines(file);

        List<String> commentsRemoved = removeComments(readStrings);

        List<String> trimmed = trimLines(commentsRemoved);

        List<String> removedConstraints = removeConstraintsDeclarations(trimmed);

        List<String> removerImports = removeImports(removedConstraints);

        List<String> removedMultiLineNamedDeclarations = processMultiLineDeclarations(removerImports);

        List<String> removedNamedDeclarations = processSingleLineDeclarations(removedMultiLineNamedDeclarations);

        List<String> remainingLines = removeBlankLines(removedNamedDeclarations);

        if (!remainingLines.isEmpty()) {
            throw new DeclarationFileSyntaxError(file,
                    "Not empty file. Unused lines:\n" + remainingLines.stream().collect(Collectors.joining("\n")));
        }
    }

    private ITextGenerator getParsedTextGenerator() throws IOException {
        // List<ITextGenerator> generators = parseStrings().modifiers().map(e -> new
        // TextGeneratorTextProxy(e, lineParser))
        // .collect(Collectors.toList());
        // return generators;
        parseStrings();
        return new NamedTextGenerator(MAIN_DECLARATION);
    }

    public DeclarationFileTextGenerator getGenerator() {
        return parsedGenerator;
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Comments
     *-********************************************-*/

    private List<String> removeComments(List<String> lines) {
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            if (line.contains("//")) {
                result.add(line.substring(0, line.indexOf("//")));
            } else {
                result.add(line);
            }
        }
        return result;
    }

    private List<String> removeBlankLines(List<String> lines) {
        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            if (!line.equals("")) {
                result.add(line);
            }
        }
        return result;
    }

    private List<String> trimLines(List<String> lines) {
        return lines.stream().map(e -> e.trim()).collect(Collectors.toList());
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Declarations
     *-********************************************-*/

    public static final String DECLARATION_START_REGEX = "^[a-zA-Z_][a-zA-Z0-9_]*\\s*[=]";
    public static final String MULTILINE_DECLARATION_REGEX = DECLARATION_START_REGEX + "\\s*\\{.*$";
    public static final String DECLARATION_REGEX = DECLARATION_START_REGEX + ".*$";

    private List<String> processMultiLineDeclarations(List<String> lines) throws IOException {

        List<String> newLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            if (line.matches(MULTILINE_DECLARATION_REGEX)) {
                i = new MultilineDeclarationParser(this, lines, i).processMultiLineDeclaration();
            } else {
                newLines.add(line);
            }

        }

        return newLines;
    }

    private static String WEIGHT_DECLARATION_REGEX = "^\\s*(\\d+|x): .*$";

    /*-********************************************-*
     *  Multiline processor
     *-********************************************-*/

    /*-********************************************-*/

    boolean isWeightDeclaration(String line) {
        return line.matches(WEIGHT_DECLARATION_REGEX);
    }

    private List<String> processSingleLineDeclarations(List<String> lines) throws IOException {

        List<String> result = new ArrayList<String>();
        for (String line : lines) {
            if (isNameDeclaration(line)) {
                Pair<String, ITextGenerator> parsed = parseNamedDeclaration(line, lineParser);
                addNamedGeneratorDeclaration(parsed.getKey(), parsed.getValue());
            } else {
                result.add(line);
            }
        }
        return result;
    }

    public static Pair<String, ITextGenerator> parseNamedDeclaration(String line, TextGeneratorParser lineParser)
            throws IOException {
        String[] splitted = line.split("=", 2);
        return Pair.of(splitted[0].trim(), lineParser.parseGenerator(splitted[1].trim()));
    }

    public static boolean isNameDeclaration(String line) {
        return line.matches(DECLARATION_REGEX);
    }

    void addNamedGeneratorDeclaration(String name, ITextGenerator generator) {
        namedRegister.createGenerator(name, generator);
    }

    /*-********************************************-*/

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
        addArgumentParser(new LockedGeneratorTypeParser());
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Imports
     *-********************************************-*/

    private List<String> removeImports(List<String> lines) throws IOException {

        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            if (isImportLine(line)) {
                processImportLine(line);
            } else {
                newLines.add(line);
            }
        }
        return newLines;

    }

    private static final String IMPORT_PREFIX = "import ";

    private void processImportLine(String line) throws IOException {
        String location = line.substring(IMPORT_PREFIX.length());

        // Check if contains a star
        List<File> toImport;
        if (location.endsWith("*")) {
            location = location.substring(0, location.length() - 1);
            toImport = Arrays.asList(fileTypeParser.parse(location).listFiles());
        } else {
            toImport = Arrays.asList(fileTypeParser.parse(location));
        }

        // Import all
        for (File file : toImport) {
            importFile(file);
        }

    }

    private void importFile(File importFile) throws IOException {
        String fileName = importFile.getName();
        int pointIndex = fileName.contains(".") ? fileName.indexOf('.') : fileName.length();

        String name = fileName.substring(0, pointIndex);
        String extension = fileName.substring(pointIndex + 1);

        switch (extension) {
            case "words":
                addNamedGeneratorDeclaration(name, new WordsFileTextGenerator(importFile));
                break;
            case "decl":
                addNamedGeneratorDeclaration(name, new DeclarationsFileTextGeneratorProxy(importFile));
                break;
        }

    }

    private boolean isImportLine(String line) {
        return line.startsWith(IMPORT_PREFIX);
    }

    /*-********************************************-*/

    /*-********************************************-*
     *  Constraints
     *-********************************************-*/

    private List<String> removeConstraintsDeclarations(List<String> linesList) {
        List<String> result = new ArrayList<>();
        for (String line : linesList) {
            if (isConstraint(line)) {
                constraints.addAll(extractConstraints(line));
            } else {
                result.add(line);
            }
        }
        return result;
    }

    public static boolean isConstraint(String line) {
        return line.trim().startsWith(CONSTRAINT_PREFIX);
    }

    private Collection<LockConstraint> extractConstraints(String line) {
        String[] splitted = line.split(" ");
        if (splitted.length < 4) {
            throw new DeclarationFileSyntaxError(file, "Not a valid constraint: " + line + "\nSpaces required.");
        }
        assert splitted[0].equals(CONSTRAINT_PREFIX);
        String constraintName = splitted[1];

        if (!DEFAULT_CONSTRAINT_FACTORIES.containsKey(constraintName)) {
            throw new DeclarationFileSyntaxError(file, "Unknown constraintname: " + constraintName);
        }

        ConstraintFactory constraint = DEFAULT_CONSTRAINT_FACTORIES.get(constraintName);
        FunctionHeader header = constraint.getHeader();

        List<String> stringArguments = Arrays.asList(splitted).subList(2, splitted.length);

        List<Object> arguments = new ArrayList<Object>();
        for (int i = 0; i < stringArguments.size(); i++) {

            String toParse = stringArguments.get(i);
            ArgumentTypeParser parser = argumentParsers.get(header.get(i).getType());

            arguments.add(parser.parse(toParse));
        }

        return constraint.create(arguments);

    }

    public static DeclarationFileTextGenerator createTemplatedGenerator(URL templateFile, Collection<CommandFactory> customCommands)
            throws IOException {
        DeclarationsFileParser parser = new DeclarationsFileParser(templateFile, new NamedGeneratorRegister(), true,
                customCommands);
        return parser.getGenerator();
    }

    public URL getFile() {
        return file;
    }

    public TextGeneratorParser getLineParser() {
        return lineParser;
    }
}
