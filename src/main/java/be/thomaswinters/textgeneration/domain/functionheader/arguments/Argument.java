package be.thomaswinters.textgeneration.domain.functionheader.arguments;

public class Argument {
	private final IArgumentType argumentType;
	private final boolean isOptional;
	private final boolean isRepeatable;

	public Argument(IArgumentType argumentType, boolean isOptional,
			boolean isRepeatable) {
		this.argumentType = argumentType;
		this.isOptional = isOptional;
		this.isRepeatable = isRepeatable;
	}

	public Argument(IArgumentType argumentType) {
		this(argumentType, false, false);
	}

	public boolean isOptional() {
		return isOptional;
	}

	public boolean isRepeatable() {
		return isRepeatable;
	}

	public IArgumentType getType() {
		return argumentType;
	}

	public boolean isInstance(Object o) {
		return argumentType.isInstance(o);
	}

	@Override
	public String toString() {
		return getType().toString() + (isRepeatable() ? "..." : "")
				+ (isOptional() ? "?" : "");
	}

}
