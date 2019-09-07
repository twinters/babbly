package be.thomaswinters.textgeneration.domain.parsers;

import be.thomaswinters.textgeneration.domain.exceptions.DeclarationFileSyntaxError;
import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.buiders.WeightedDisjunctionTextGeneratorBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MultilineDeclarationParser {

    private final DeclarationsFileParser declarationsFileParser;

    // INPUT
    private final List<String> lines;
    private final int startIdx;

    // DERIVED
    private final String name;

    // STANDARD
    private final WeightedDisjunctionTextGeneratorBuilder disjunctionBuilder = new WeightedDisjunctionTextGeneratorBuilder();
    private StringBuilder possibilityStringBuilder = new StringBuilder();

    // ANSWER
    private Optional<Integer> finalIdx = Optional.empty();

    // CONSTRUCTOR
    public MultilineDeclarationParser(DeclarationsFileParser declarationsFileParser, List<String> lines, int startIdx) {
        this.declarationsFileParser = declarationsFileParser;
        this.lines = lines;
        this.startIdx = startIdx;
        this.name = lines.get(startIdx).split("=")[0].trim();
    }


    // ALGORITHM
    int processMultiLineDeclaration() throws IOException {

        if (finalIdx.isPresent()) {
            return finalIdx.get();
        }

        int idx = startIdx + 1;
        int weight = 1;
        boolean useGenerationsWeight = false;

        while (idx < lines.size()) {
            String line = lines.get(idx);

            boolean isEndingLine = line.trim().equals("}");

            // Check if empty line
            if (line.trim().isEmpty() || isEndingLine) {

                // Add to disjunction
                addBufferToDisjunction(weight, useGenerationsWeight);

                // Check if end
                if (isEndingLine) {
                    declarationsFileParser.addNamedGeneratorDeclaration(name, disjunctionBuilder.build());

                    finalIdx = Optional.of(idx);
                    return idx;
                }

            } else {

                // If weighted declaration: renew weight, process new line
                if (declarationsFileParser.isWeightDeclaration(line)) {
                    // Add precious to buffer
                    addBufferToDisjunction(weight, useGenerationsWeight);

                    // Retrieve weight
                    String[] parts = line.split(": ", 2);
                    String weightString = parts[0].trim();
                    if (weightString.matches("\\d+")) {
                        weight = Integer.parseInt(weightString);
                        useGenerationsWeight = false;
                    } else if (weightString.equals("x")) {
                        weight = -1;
                        useGenerationsWeight = true;
                    } else {
                        throw new DeclarationFileSyntaxError(declarationsFileParser.getFile(),
                                "Invalid weight for multiline declaration: " + weightString);
                    }

                    // Renew line to only contain new part
                    line = parts[1];
                }

                // First line:
                if (possibilityStringBuilder.toString().trim().isEmpty()) {
                    possibilityStringBuilder.append(line);
                } else {
                    possibilityStringBuilder.append("\n" + line);
                }
            }

            idx++;
        }

        if (!lines.get(idx - 1).trim().equals("}")) {
            throw new DeclarationFileSyntaxError(declarationsFileParser.getFile(),
                    "No end for the multiline statement '" + name + "' was found.");
        }

        finalIdx = Optional.of(idx);
        return idx;

    }

    /**
     * Adds the buffer, if it contains anything, to the large disjunction
     *
     * @param weight
     * @throws IOException
     */
    private void addBufferToDisjunction(int weight, boolean useGenerationsWeight) throws IOException {
        String bufferText = possibilityStringBuilder.toString();
        if (!bufferText.trim().isEmpty()) {
            ITextGenerator parsedGenerator = declarationsFileParser.getLineParser().parseGenerator(bufferText);

            // Check if generation weight or specified weight
            if (useGenerationsWeight) {
                disjunctionBuilder.addGenerationWeightGenerator(parsedGenerator);
            } else {
                disjunctionBuilder.addGenerator(parsedGenerator, weight);
            }

            // Reset
            possibilityStringBuilder = new StringBuilder();
            weight = 1;
        }
    }
}