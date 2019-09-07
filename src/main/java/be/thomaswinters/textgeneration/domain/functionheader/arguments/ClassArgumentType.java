package be.thomaswinters.textgeneration.domain.functionheader.arguments;


public class ClassArgumentType<E> implements IArgumentType {

	private final Class<E> clazz;

	public ClassArgumentType(Class<E> clazz) {
		this.clazz = clazz;
	}

	public Class<E> getClazz() {
		return clazz;
	}

	public boolean isInstance(Object o) {
		return clazz.isInstance(o);
	}

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}
}
