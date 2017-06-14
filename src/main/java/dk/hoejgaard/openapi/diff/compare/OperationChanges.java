package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains a finite set of differences between an existing and a future parameter.
 */
public class OperationChanges {

    private static Logger logger = LoggerFactory.getLogger(OperationChanges.class);

    private Map<String, List<String>> changes = new HashMap<>();
    private Map<String, List<String>> flawedDefines = new HashMap<>();
    private Map<String, List<String>> existingFlaws = new HashMap<>();
    private Map<String, List<String>> potentiallyBreaking = new HashMap<>();
    private Map<String, List<String>> breaking = new HashMap<>();

    /**
     * @return a map with change points and the breaking changes recorded for each point
     */
    public Map<String, List<String>> getBreakingChanges() {
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
     * @return a map with change points and the lack of definitions for each point in the existing operation
     */
    public Map<String, List<String>> getExistingFlaws() {
        return existingFlaws;
    }

    /**
     * a breaking change is detected and thus the clients will struggle with the new version of the API
     * @param origin where the flaw is found
     * @param information what the flaw is
     */
    public void addBreakingChange(String origin, String information) {
        if (breaking.containsKey(origin)) {
            if (!breaking.get(origin).contains(information)) {
                breaking.get(origin).add(information);
            }
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
            if (!potentiallyBreaking.get(origin).contains(information)) {
                potentiallyBreaking.get(origin).add(information);
            } else {
                logger.trace("potential breaking change ({}, {}) was already registered", origin, information);
            }
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

    /**
     * a definition flaw is characterized by a definition that makes it hard to deliver deterministic future proof design for an API
     * @param origin where the flaw is found
     * @param information what the flaw is
     */
    public void addDefinitionFlaw(String origin, String information) {
        if (flawedDefines.containsKey(origin)) {
            flawedDefines.get(origin).add(information);
        } else if (existingFlaws.containsKey(origin)) {
            existingFlaws.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            if (origin.contains("existing")) {
                existingFlaws.put(origin, originList);
            } else if (origin.contains("future")) {
                flawedDefines.put(origin, originList);
            } else {
                flawedDefines.put("change" + origin, originList);
                changes.put(origin, originList);
            }
        }
    }

    void addRecordedChange(Map<String, List<String>> changes) {
        for (Map.Entry<String, List<String>> entry : changes.entrySet()) {
            List<String> observations = entry.getValue();
            for (String observation : observations) {
                addRecordedChange(entry.getKey(), observation);
            }
        }
    }

    void addBreakingChange(Map<String, List<String>> changes) {
        for (Map.Entry<String, List<String>> entry : changes.entrySet()) {
            List<String> observations = entry.getValue();
            for (String observation : observations) {
                addBreakingChange(entry.getKey(), observation);
            }
        }
    }

    void addPotentialBreakingChange(Map<String, List<String>> changes) {
        for (Map.Entry<String, List<String>> entry : changes.entrySet()) {
            List<String> observations = entry.getValue();
            for (String observation : observations) {
                addPotentialBreakingChange(entry.getKey(), observation);
            }
        }
    }

    void addDefinitionFlaw(Map<String, List<String>> changes) {
        for (Map.Entry<String, List<String>> entry : changes.entrySet()) {
            List<String> observations = entry.getValue();
            for (String observation : observations) {
                addDefinitionFlaw(entry.getKey(), observation);
            }
        }
    }

}
