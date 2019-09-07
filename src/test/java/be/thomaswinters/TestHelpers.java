package be.thomaswinters;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;


public class TestHelpers {
    public static <E> void assertEqualContent(Collection<? extends E> expected,
                                              Collection<E> actual) {
        Collection<E> expectedNew = new HashSet<E>(expected);
        Collection<E> actualNew = new HashSet<E>(actual);

        if (!expectedNew.equals(actualNew)) {
            fail("Expected contents: "
                    + collectionToString(createStringSortedCollection(expectedNew))
                    + ", but got "
                    + collectionToString(createStringSortedCollection(actualNew)));
        }

    }

    public static <E> void assertEqualContentSingleElement(E expectedElement,
                                                           Collection<? super E> collection) {
        if (!collection.contains(expectedElement)) {
            fail("The collection "
                    + collectionToString(collection)
                    + " does not contain the element "
                    + expectedElement);
        }
        if (collection.size() > 1) {
            fail("The collection "
                    + collectionToString(collection)
                    + " contains more elements than only "
                    + expectedElement);
        }

    }

    public static <E> void assertIn(E element, Collection<? super E> collection) {
        if (!collection.contains(element)) {
            fail("The given expected element \""
                    + element
                    + "\" was not in the collection "
                    + collectionToString(createStringSortedCollection(collection)));
        }
    }

    private static String collectionToString(Collection<?> collection) {
        return "["
                + collection.stream().map(e -> e.toString())
                .collect(Collectors.joining(",")) + "]";
    }

    private static Collection<String> createStringSortedCollection(
            Collection<?> collection) {
        List<String> strings = collection.stream().map(e -> e.toString())
                .collect(Collectors.toList());
        strings.sort(String.CASE_INSENSITIVE_ORDER);

        return strings;
    }
}
