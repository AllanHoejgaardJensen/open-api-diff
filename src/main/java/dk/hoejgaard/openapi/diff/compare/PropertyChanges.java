package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains a finite set of differences between an existing and a future parameter.
 */
public class PropertyChanges {
    private static Logger logger = LoggerFactory.getLogger(PropertyChanges.class);

    private final boolean isRequiredChanged;
    private final boolean isDescriptionChanged;
    private final Diff diffDepth;
    private final Map<String, List<String>> changes = new HashMap<>();
    private final Map<String, List<String>> flawedDefines = new HashMap<>();
    private final Map<String, List<String>> potentiallyBreaking = new HashMap<>();
    private final Map<String, List<String>> breaking = new HashMap<>();
    private List<ScopedProperty> added = new ArrayList<>();
    private List<ScopedProperty> removed = new ArrayList<>();

    public PropertyChanges(Parameter existing, Parameter future, Diff depth) {
        this.isRequiredChanged = existing.getRequired() != future.getRequired();
        this.isDescriptionChanged = !empty(existing.getDescription()).equals(empty(future.getDescription()));
        this.diffDepth = depth;
    }

    /**
     * @return the property added to the future specification of the API.
     */
    public List<ScopedProperty> getAddedProperties() {
        return added;
    }

    /**
     * @param added the property added to the future specification API
     */
    public void addAddedProperties(List<ScopedProperty> added) {
        this.added = added;
    }

    /**
     * @return the properties removed from the future specification of the API.
     */
    public List<ScopedProperty> getRemovedProperties() {
        return removed;
    }

    /**
     * @param removed the properties removed from the future specification API
     */
    public void addRemovedProperties(List<ScopedProperty> removed) {
        this.removed = removed;
    }

    /**
     * @return true if there were any recorded differences between the existing and the future specification
     */
    public boolean containsDiff() {
        if (Diff.ALL.equals(diffDepth)) {
            return anyPropertyChanges() || anyThingAddedOrRemoved() || anyObservations();
        } else if (Diff.POTENTIALLY_BREAKING.equals(diffDepth)) {
            return anyPropertyChanges() || anyThingAddedOrRemoved() || breakingObserved() || !potentiallyBreaking.isEmpty();
        } else if (Diff.BREAKING.equals(diffDepth)) {
            return anyPropertyChanges() || anyThingAddedOrRemoved() || breakingObserved();
        } else if (Diff.LAISSEZ_FAIRE.equals(diffDepth)) {
            return anyPropertyChanges() || anyThingAddedOrRemoved() || !breaking.isEmpty();
        }
        logger.error("unhandled diff depth");
        return true;
    }

    /**
     * @return a map with change points and the breaking changes recorded for each point
     */
    public Map<String, List<String>> getBreaking() {
        return Collections.unmodifiableMap(breaking);
    }

    /**
     * @return a map with change points and the potentially breaking changes recorded for each point
     */
    public Map<String, List<String>> getPotentiallyBreaking() {
        return Collections.unmodifiableMap(potentiallyBreaking);
    }

    /**
     * @return a map with change points and the changes recorded for each point
     */
    public Map<String, List<String>> getChanges() {
        return changes;
    }

    /**
     * @return a map with change points and the lack of definitions for each point
     */
    public Map<String, List<String>> getFlawedDefines() {
        return flawedDefines;
    }

    /**
     * a breaking change is detected and thus the clients will struggle with the new version of the API
     * @param origin where the flaw is found
     * @param information what the flaw is
     */
    public void addBreakingChange(String origin, String information) {
        if (breaking.containsKey(origin)) {
            breaking.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            breaking.put(origin, originList);
        }
    }

    /**
     * a potentially breaking change is detected and thus some clients will likely struggle with the new version of the API
     * @param origin where the flaw is found
     * @param information what the flaw is
     */
    public void addPotentialBreakingChange(String origin, String information) {
        if (potentiallyBreaking.containsKey(origin)) {
            potentiallyBreaking.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            potentiallyBreaking.put(origin, originList);
        }
    }

    /**
     * a change is detected and thus the API changes at a given point
     * @param change where the flaw is found
     * @param information what the flaw is
     */

    public void addRecordedChange(String change, String information) {
        if (changes.containsKey(change)) {
            if (!changes.get(change).contains(information)) {
                changes.get(change).add(information);
            }
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            changes.put(change, originList);
        }
    }

    private boolean anyObservations() {
        return breakingObserved() || !potentiallyBreaking.isEmpty() ||
            !flawedDefines.isEmpty();
    }

    private boolean breakingObserved() {
        return !breaking.isEmpty() || !changes.isEmpty();
    }

    private boolean anyPropertyChanges() {
        return isRequiredChanged || isDescriptionChanged || !changes.isEmpty();
    }

    private boolean anyThingAddedOrRemoved() {
        return !added.isEmpty() || !removed.isEmpty();
    }

    private String empty(final CharSequence cs) {
        if (StringUtils.isBlank(cs))
            return "";
        return cs.toString();
    }
}
