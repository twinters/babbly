package be.thomaswinters.textgeneration.domain.functionheader.arguments;

public class Arguments {

	public static final Argument INTEGER_ARG = new Argument(
			ArgumentTypes.INTEGER_TYPE);
	public static final Argument DOUBLE_ARG = new Argument(
			ArgumentTypes.DOUBLE_TYPE);
	public static final Argument DOUBLE_ARG_OPTIONAL = new Argument(
			ArgumentTypes.DOUBLE_TYPE, true, false);
	public static final Argument TEXT_GENERATOR_ARG = new Argument(
			ArgumentTypes.TEXT_GENERATOR_TYPE);
	public static final Argument REPEATING_TEXT_GENERATOR_ARG = new Argument(
			ArgumentTypes.TEXT_GENERATOR_TYPE, false, true);
	public static final Argument LOCKED_GENERATOR_ARG = new Argument(
			ArgumentTypes.LOCKED_GENERATOR_TYPE);
	public static final Argument FILE_ARG = new Argument(
			ArgumentTypes.FILE_TYPE);
}
