package dk.hoejgaard.openapi.diff.compare.util;

import java.util.ArrayList;
import java.util.List;

/**
 * compares two lists and detects what has been added and what has been removed from these lists.
 * <p>
 * The metaphor used is the diffEm operation from tools like git in order to detect a simple overview
 * over differences between these lists.
 * <p>
 * The maps are considered to be a reference list, and the list to check (the subject) to the reference.
 * <p>
 * The differences found are mapped into 3 lists:
 * <ol>
 * <li>common - the elements shared between the reference and the subject lists</li>
 * <li>removed - the elements from the reference list not found in the subject list</li>
 * <li>added - additional elements found that was not part of the reference list </li>
 * </ol>
 *
 * @param <T> elements
 */
public class Lists<T> {

    private List<T> added;
    private List<T> removed;
    private List<T> common;

    private Lists() {
        common = new ArrayList<>();
    }

    /**
     * Compares two maps and returns whats common, what has been added and what has been removed.
     *
     * @param <T> the contained type in the List
     * @param reference the reference list
     * @param subject the subjects list which will be compared to the reference
     * @return a Lists object with the new added stuff, the old and removed stuff and the stuff that is common between the APIs
     */
    public static <T> Lists<T> diff(List<T> reference, List<T> subject) {
        Lists<T> elements = new Lists<>();
        elements.removed = new ArrayList<>();
        if (isSimpleInput(reference, subject, elements)) {
            elements.added = new ArrayList<>();
            return elements;
        }
        elements.added = new ArrayList<>(subject);

        for (int i = 0; i < reference.size(); i++) {
            T element = reference.get(i);
            if (subject.contains(element) && (reference.contains(element))) {
                elements.added.remove(element);
                elements.common.add(element);
            } else if (reference.contains(element) && !subject.contains(element)) {
                elements.removed.add(element);
            }
        }
        return elements;
    }

    /**
     * the simplest case is where the reference and/or the subject is not initialized
     */
    private static <T> boolean isSimpleInput(List<T> reference, List<T> subject, Lists<T> instance) {
        if (null == reference && null == subject) return true;
        if (null == reference) {
            instance.added = subject;
            return true;
        }
        if (null == subject) {
            instance.removed = reference;
            return true;
        }
        return false;
    }

    /**
     * @return Map containing the added keys compared to the reference
     */
    public List<T> getAdded() {
        return added;
    }

    /**
     * @return Map containing the removed keys compared to the reference
     */
    public List<T> getRemoved() {
        return removed;
    }

    /**
     * @return Map containing the keys that exists in the reference and the subject
     */
    public List<T> getCommon() {
        return common;
    }

}
