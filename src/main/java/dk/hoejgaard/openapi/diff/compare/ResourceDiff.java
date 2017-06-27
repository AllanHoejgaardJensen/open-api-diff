package dk.hoejgaard.openapi.diff.compare;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;

/**
 * contains the differences between the existing and the future candidate resource
 */
public class ResourceDiff {

    private final String pathUrl;
    private final Diff depth;
    private final SortedMap<HttpMethod, Operation> newOperations = new TreeMap<>();
    private final SortedMap<HttpMethod, Operation> missingOperations  = new TreeMap<>();
    private final SortedMap<HttpMethod, OperationDiff> changedOperations  = new TreeMap<>();
    private final SortedMap<String, OperationDiff> nonCompliantExistingOperations  = new TreeMap<>();
    private final SortedMap<String, OperationDiff> nonCompliantFutureOperations  = new TreeMap<>();

    public ResourceDiff(Diff depth, String pathUrl) {
        this.depth = depth;
        this.pathUrl = pathUrl;
    }

    /**
     * @return the operations that have been added to the future API
     */
    public Map<HttpMethod, Operation> getNewOperations() {
        return newOperations;
    }

    public void setNewOperations(Map<HttpMethod, Operation> newOperations) {
        this.newOperations.putAll(newOperations);
    }

    /**
     * @return the operations that would no longer be part of the future API
     */
    public Map<HttpMethod, Operation> getMissingOperations() {
        return missingOperations;
    }

    public void setMissingOperations(Map<HttpMethod, Operation> missing) {
        for (Map.Entry<HttpMethod, Operation> entry : missing.entrySet()) {
            this.missingOperations.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * @return the operations that are found in the existing and the future API, which have changed in the future API
     */
    public Map<HttpMethod, OperationDiff> getChangedOperations() {
        return changedOperations;
    }

    /**
     * @return the operations that are found to be non-compliant with the used comparison depth and maturity
     */
    public Map<HttpMethod, OperationDiff> getNonCompliantOperations() {
        SortedMap<HttpMethod, OperationDiff> result = new TreeMap<>();
        for (Map.Entry<String, OperationDiff> entry : nonCompliantFutureOperations.entrySet()) {
            OperationDiff od = entry.getValue();
            String scope = entry.getKey();
            if (!od.isCompliant()) {
                String substring = scope.substring(scope.lastIndexOf("::") + 2, scope.length());
                result.put(HttpMethod.valueOf(substring), nonCompliantFutureOperations.get(scope));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    public void addChangedOperations(Map<HttpMethod, OperationDiff> changed) {
        if (changed == null) {
            return;
        }
        for (Map.Entry<HttpMethod, OperationDiff> entry : changed.entrySet()) {
            OperationDiff od = entry.getValue();
            if (od != null && od.isChanged()) {
                this.changedOperations.put(entry.getKey(), od);
            }
        }
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public boolean isDiff() {
        if (Diff.ALL.equals(depth)) {
            return !newOperations.isEmpty() || !missingOperations.isEmpty() || !changedOperations.isEmpty() || isNonCompliant();
        } else {
            return !newOperations.isEmpty() || !missingOperations.isEmpty() || !changedOperations.isEmpty();
        }
    }

    private boolean isNonCompliant() {
        return !nonCompliantExistingOperations.isEmpty() || !nonCompliantFutureOperations.isEmpty();
    }

    public void addNonCompliantCandidateOperations(String scope, Map<HttpMethod, OperationDiff> differences) {
        for (Map.Entry<HttpMethod, OperationDiff> entry : differences.entrySet()) {
            addNonCompliantCandidateOperations(scope + "::" + entry.getKey().toString(), entry.getValue());
        }
    }

    public void addNonCompliantExistingOperations(String scope, Map<HttpMethod, OperationDiff> differences) {
        for (Map.Entry<HttpMethod, OperationDiff> entry : differences.entrySet()) {
            addNonCompliantExistingOperations(scope + "::" + entry.getKey().toString(), entry.getValue());
        }
    }

    private void addNonCompliantCandidateOperations(String scope, OperationDiff differences) {
        nonCompliantFutureOperations.put(scope, differences);

    }

    private void addNonCompliantExistingOperations(String scope, OperationDiff differences) {
        nonCompliantExistingOperations.put(scope, differences);
    }
}
