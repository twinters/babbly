package be.thomaswinters.textgeneration.domain.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public class ConstraintUtil {

	/**
	 * This method will create new objects using the given combiner creator. It
	 * will take every possible combination of the 'toCombine' collection (the
	 * order doesn't matter) and create the object using the combiner.
	 * 
	 * @param toCombine
	 * @param combiner
	 * @return A list of all combinations created with the combiner.
	 */
	public static <G, C> Collection<C> combine(
			Collection<? extends G> toCombine, BiFunction<G, G, C> combiner) {
		if (toCombine.size() < 2) {
			throw new IllegalStateException(
					"Not enough locked generators specified.");
		}

		List<G> locksList = new ArrayList<>(toCombine);

		List<C> constraints = new ArrayList<C>();

		for (int i = 0; i < locksList.size(); i++) {
			for (int j = i + 1; j < locksList.size(); j++) {
				constraints.add(combiner.apply(locksList.get(i),
						locksList.get(j)));
			}
		}

		return constraints;

	}

	public static <G> List<G> convertList(List<?> toConvert, int beginIndex,
			int endIndex, Class<G> clazz) {
		List<G> result = new ArrayList<>();
		for (int i = beginIndex; i < endIndex; i++) {
			result.add(clazz.cast(toConvert.get(i)));
		}

		return result;
	}
}
