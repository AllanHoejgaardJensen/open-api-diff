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

/**
 * Contains a finite set of differences between an existing and a future parameter.
 */
public class ParameterChanges {

    private final boolean isRequiredChanged;
    private final boolean isDescriptionChanged;
    private final Diff diffDepth;
    private List<ScopedProperty> added = new ArrayList<>();
    private List<ScopedProperty> removed = new ArrayList<>();

    private Map<String, List<String>> changes = new HashMap<>();
    private Map<String, List<String>> flawedDefines = new HashMap<>();
    private Map<String, List<String>> potentiallyBreaking = new HashMap<>();
    private Map<String, List<String>> breaking = new HashMap<>();

    public ParameterChanges(Parameter existing, Parameter future, Diff depth) {
        this.isRequiredChanged = existing.getRequired() != future.getRequired();
        this.isDescriptionChanged = !empty(existing.getDescription()).equals(empty(future.getDescription()));
        this.diffDepth = depth;
    }

    public boolean isRequiredChanged() {
        return isRequiredChanged;
    }

    public boolean isDescriptionChanged() {
        return isDescriptionChanged;
    }

    /**
     * @return the parameters added to the future specification of the API.
     */
    public List<ScopedProperty> getAddedParams() {
        return added;
    }

    /**
     * @return the parameters removed from the future specification of the API.
     */
    public List<ScopedProperty> getRemovedParams() {
        return removed;
    }

    /**
     * @return true if there were any recorded differences between the existing and the future specification
     */
    public boolean containsDiff() {
        if (Diff.ALL.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                anyObservations();
        } else if (Diff.POTENTIALLY_BREAKING.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                breakingObserved() || potentiallyBreaking.size() > 0;
        } else if (Diff.BREAKING.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                breakingObserved();
        } else if (Diff.LAISSEZ_FAIRE.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                breaking.size() > 0;
        }
        return anyParameterChanges() || anyThingAddedOrRemoved();
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
     *
     * @param origin      where the flaw is found
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
     *
     * @param origin      where the flaw is found
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
     *
     * @param change      where the flaw is found
     * @param information what the flaw is
     */

    public void addRecordedChange(String change, String information) {
        if (changes.containsKey(change)) {
            changes.get(change).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            changes.put(change, originList);
        }
    }

    /**
     * a definition flaw is characterized by a definition that makes it hard to deliver deterministic future proof design for an API
     *
     * @param origin      where the flaw is found
     * @param information what the flaw is
     */
    public void addDefinitionFlaw(String origin, String information) {
        if (flawedDefines.containsKey(origin)) {
            flawedDefines.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            flawedDefines.put(origin, originList);
        }
    }

    private boolean anyObservations() {
        return breakingObserved() || potentiallyBreaking.size() > 0 ||
            flawedDefines.size() > 0;
    }

    private boolean breakingObserved() {
        return breaking.size() > 0 || changes.size() > 0;
    }

    private boolean anyParameterChanges() {
        return isRequiredChanged || isDescriptionChanged;
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
