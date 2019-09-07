package be.thomaswinters.textgeneration.domain.functionheader.arguments;

import be.thomaswinters.textgeneration.domain.generators.ITextGenerator;
import be.thomaswinters.textgeneration.domain.generators.locked.LockedGenerator;

import java.io.File;

public class ArgumentTypes {

    public static final ClassArgumentType<Integer> INTEGER_TYPE = new ClassArgumentType<>(
            Integer.class);
    public static final ClassArgumentType<File> FILE_TYPE = new ClassArgumentType<>(
            File.class);
    public static final ClassArgumentType<Double> DOUBLE_TYPE = new ClassArgumentType<>(
            Double.class);
    public static final ClassArgumentType<ITextGenerator> TEXT_GENERATOR_TYPE = new ClassArgumentType<>(
            ITextGenerator.class);
    public static final ClassArgumentType<LockedGenerator> LOCKED_GENERATOR_TYPE = new ClassArgumentType<>(
            LockedGenerator.class);
}
