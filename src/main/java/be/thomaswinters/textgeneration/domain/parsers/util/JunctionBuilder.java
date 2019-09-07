package be.thomaswinters.textgeneration.domain.parsers.util;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.TextGeneratorPool;
import be.thomaswinters.textgeneration.domain.generators.collection.CascadingTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.ConjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.DisjunctionTextGenerator;
import be.thomaswinters.textgeneration.domain.generators.collection.WeightedDisjunctionTextGenerator;

import java.util.Optional;
import java.util.function.Function;

public class JunctionBuilder {
    private TextGeneratorBuffer cascadeBuffer = new TextGeneratorBuffer(e -> new CascadingTextGenerator(e));
    private TextGeneratorBuffer equalDisjunctionBuffer = new TextGeneratorBuffer(e -> new DisjunctionTextGenerator(e));
    private TextGeneratorBuffer generationWeightedDisjunctionBuffer = new TextGeneratorBuffer(
            e -> WeightedDisjunctionTextGenerator.fromGenerations(e));
    private TextGeneratorBuffer conjunctionBuffer = new TextGeneratorBuffer(e -> new ConjunctionTextGenerator(e));
    private StringBuffer stringBuffer = new StringBuffer();

    public void add(ITextGenerator generator) {
        if (generator == null) {
            throw new NullPointerException("Can't add null to the buffer");
        }
        addStringToConjunctions();
        conjunctionBuffer.append(generator);
    }

    /**
     * Adds a modifier to the last conjunction
     *
     * @param mapper
     */
    public void modifyLast(Function<ITextGenerator, ITextGenerator> mapper) {
        if (mapper == null) {
            throw new NullPointerException("Given mapper is invalid");
        }
        addStringToConjunctionsButLastOneSeperate();
        conjunctionBuffer.modifyLast(mapper);
    }

    public void add(String string) {
        if (string == null) {
            throw new NullPointerException("Can't add null to the buffer");
        }
        stringBuffer.append(string);
    }

    public void add(char ch) {
        stringBuffer.append(ch);
    }

    public void markConjunction() {
        addStringToConjunctions();
    }

    public void markDisjunction() {
        addStringToConjunctions();
        addConjunctsToDisjuncts();
    }

    public void markEqualDisjunction() {
        addStringToConjunctions();
        addConjunctsToDisjuncts();
        addDisjunctsToEqualDisjuncts();
    }

    public void markCascade() {
        addStringToConjunctions();
        addConjunctsToDisjuncts();
        addDisjunctsToEqualDisjuncts();
        addEqualDisjunctsToCascade();
    }

    private void addStringToConjunctions() {
        if (!stringBuffer.isEmpty()) {
            conjunctionBuffer.append(TextGeneratorPool.getStatic(stringBuffer.poll()));
        }
    }

    private Optional<Character> addStringToConjunctionsButLastOneSeperate() {
        if (!stringBuffer.isEmpty()) {
            String toAdd = stringBuffer.poll();
            char lastChar = toAdd.charAt(toAdd.length() - 1);
            toAdd = toAdd.substring(0, toAdd.length() - 1);
            if (!toAdd.isEmpty()) {
                conjunctionBuffer.append(TextGeneratorPool.getStatic(toAdd));
            }
            conjunctionBuffer.append(TextGeneratorPool.getStatic(lastChar + ""));
        }
        return Optional.empty();
    }

    private void addConjunctsToDisjuncts() {
        if (!conjunctionBuffer.isEmpty()) {
            generationWeightedDisjunctionBuffer.append(conjunctionBuffer.poll());
        }
    }

    private void addDisjunctsToEqualDisjuncts() {
        if (!generationWeightedDisjunctionBuffer.isEmpty()) {
            equalDisjunctionBuffer.append(generationWeightedDisjunctionBuffer.poll());
        }
    }

    private void addEqualDisjunctsToCascade() {
        if (!equalDisjunctionBuffer.isEmpty()) {
            cascadeBuffer.append(equalDisjunctionBuffer.poll());
        }
    }

    public String pollRemainingBuffer() {
        return stringBuffer.poll();
    }

    public ITextGenerator poll() {
        addStringToConjunctions();
        addConjunctsToDisjuncts();
        addDisjunctsToEqualDisjuncts();
        addEqualDisjunctsToCascade();
        ITextGenerator result = cascadeBuffer.poll();
        return result;
    }

    @Override
    public String toString() {
        return "ConjunctionBuilder [" + stringBuffer + ", " + conjunctionBuffer + "]";
    }

}
