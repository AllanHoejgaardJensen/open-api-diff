package dk.hoejgaard.openapi.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import dk.hoejgaard.openapi.diff.compare.OperationDiff;
import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.compare.util.Maps;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import dk.hoejgaard.openapi.diff.model.Endpoint;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compares an existing API based on Open API version 2 with a future candidate API in the same format.
 * <p>
 * The comparison can be simple just looking at resources, parameters etc. or can be more complex.
 * <p>
 * In the more complex mode the compliance comprises content-types, response headers, projections,
 * versions for a given endpoint or for the api as such according to the rules outlined by github.com/allanhoejgaardjensen
 * <p>
 * The comparison may include HATEOAS (HAL) compatibility issues for backwards compliance as well.
 */
public class APIDiff {

    private static Logger logger = LoggerFactory.getLogger(APIDiff.class);
    private final List<ResourceDiff> resourceDiffs = new ArrayList<>();

    private Swagger referenceAPI;
    private Swagger candidateAPI;
    private Diff depths = Diff.LAISSEZ_FAIRE;
    private Maturity maturity = Maturity.LOW;
    private Versions versions = Versions.DOUBLE;
    private List<Endpoint> newEndpointList = new ArrayList<>();
    private List<Endpoint> missingEndpointList = new ArrayList<>();
    private List<Endpoint> changedEndpointList = new ArrayList<>();


    /**
     * @param existing the current and thus existing version of the API
     * @param future   the API that is going to replace the current API
     */
    public APIDiff(String existing, String future) {
        this(existing, future, Diff.ALL, Maturity.LOW, Versions.DOUBLE);
    }

    /**
     * @param existing the current and thus existing version of the API
     * @param future   the API that is going to replace the current API
     * @param depths   the level if depth used in the diff
     */
    public APIDiff(String existing, String future, Diff depths) {
        this(existing, future, depths,
            Diff.LAISSEZ_FAIRE.equals(depths) ? Maturity.NONE : Maturity.LOW,
            Diff.LAISSEZ_FAIRE.equals(depths) ? Versions.NONE : Versions.SINGLE);
    }

    /**
     * @param existing the current and thus existing version of the API
     * @param future   the API that is going to replace the current API
     * @param depths   the level if depth used in the diff (all, hal, laissez-faire) @see Diff
     * @param maturity the level of maturity (full, hal, low, none) @see Maturity
     * @param versions the number of overlapping versions @see Versions
     */
    public APIDiff(String existing, String future, Diff depths, Maturity maturity, Versions versions) {
        this.depths = depths;
        this.maturity = maturity;
        this.versions = versions;
        SwaggerParser swaggerParser = new SwaggerParser();
        referenceAPI = swaggerParser.read(existing);
        candidateAPI = swaggerParser.read(future);
        if (null == referenceAPI || null == candidateAPI) {
            throw new RuntimeException("cannot read api-doc from spec.");
        }
        Map<String, Path> currentResources = referenceAPI.getPaths();
        Map<String, Path> futureResources = candidateAPI.getPaths();
        Maps<String, Path> pathDelta = Maps.diff(currentResources, futureResources);
        this.newEndpointList = convert2EndpointList(pathDelta.getAdded());
        if (Diff.ALL.equals(depths)) {
            checkCompliance(futureResources, pathDelta.getAdded());
        }
        this.missingEndpointList = convert2EndpointList(pathDelta.getRemoved());
        boolean ok = compareResources(currentResources, futureResources, pathDelta);
        if (!resourceDiffs.isEmpty()) {
            this.changedEndpointList = convert2EndpointList(pathDelta.getCommon(), futureResources);
        }
        if (ok) {
            logger.info(" New API most likely compliant to the comparison criteria - CAUTION: this is not an exhaustive check");
        } else {
            logger.info(" New API most likely not compliant to the comparison criteria - please have a second look at the API");
        }
    }

    public List<Endpoint> getAddedEndpoints() {
        return Collections.unmodifiableList(newEndpointList);
    }

    public List<Endpoint> getMissingEndpoints() {
        return Collections.unmodifiableList(missingEndpointList);
    }

    public List<Endpoint> getChangedEndpoints() {
        return Collections.unmodifiableList(changedEndpointList);
    }

    public List<ResourceDiff> getChangedResourceDiffs() {
        List<ResourceDiff> changed = new ArrayList<>(resourceDiffs);
        return changed.stream()
            .filter(Objects::nonNull)
            .filter(p -> p.getChangedOperations() != null)
            .filter(p -> !p.getChangedOperations().isEmpty())
            .collect(Collectors.toList());
    }

    public List<ResourceDiff> getAllDiffs() {
        return Collections.unmodifiableList(resourceDiffs);
    }

    private boolean compareResources(Map<String, Path> currentResources, Map<String, Path> futureResources, Maps<String, Path> pathDelta) {
        List<String> commonURLS = pathDelta.getCommon();
        ResourceDiff resourceDiff;
        for (String url : commonURLS) {
            resourceDiff = new ResourceDiff(depths, url);
            Path currentPath = currentResources.get(url);
            Path futurePath = futureResources.get(url);

            Map<HttpMethod, Operation> currentOperations = currentPath.getOperationMap();
            getCurrentOperationCompliance(resourceDiff, url, currentOperations);
            Map<HttpMethod, Operation> futureOperations = futurePath.getOperationMap();
            getCandidateOperationCompliance(resourceDiff, url, futureOperations);

            Maps<HttpMethod, Operation> operationsDelta = getOperationDelta(resourceDiff, currentOperations, futureOperations);
            Map<HttpMethod, OperationDiff> changedOperations = getChangedOperations(currentOperations, futureOperations,
                operationsDelta, url.replace('/', '.'));
            resourceDiff.addChangedOperations(changedOperations);
            this.newEndpointList.addAll(convert2EndpointList(resourceDiff.getPathUrl(), resourceDiff.getNewOperations()));
            this.missingEndpointList.addAll(convert2EndpointList(resourceDiff.getPathUrl(), resourceDiff.getMissingOperations()));

            if (resourceDiff.isDiff()) {
                resourceDiffs.add(resourceDiff);
            }
        }
        return resourceDiffs.isEmpty();
    }

    private void getCandidateOperationCompliance(ResourceDiff resourceDiff, String url, Map<HttpMethod, Operation> futureOperations) {
        String scope = "future::" + url.replace('/', '.');
        if (Diff.ALL.equals(depths)) {
            Map<HttpMethod, OperationDiff> operas = new EnumMap<>(HttpMethod.class);
            for (Entry<HttpMethod, Operation> entry : futureOperations.entrySet()) {
                HttpMethod method = entry.getKey();
                Operation opr = entry.getValue();
                OperationDiff od = new OperationDiff(referenceAPI, candidateAPI, depths, maturity, versions);
                od.checkRequestHeaderCompliance(opr, true, scope);
                od.checkResponseCompliance(opr, method, true, scope);
                od.checkVersionCompliance(opr, method, true, scope);
                if (!od.isCompliant()) {
                    operas.put(method, od);
                }
            }
            if (!operas.isEmpty()) {
                resourceDiff.addNonCompliantCandidateOperations("observations.future::" + url.replace('/', '.'), operas);
            }
        }
    }

    private void getCurrentOperationCompliance(ResourceDiff resourceDiff, String url, Map<HttpMethod, Operation> currentOperations) {
        String api = "existing";
        String complianceMsg = "observations." + api + "." + url.replace('/', '.');
        if (Diff.ALL.equals(depths)) {
            Map<HttpMethod, OperationDiff> operas = mapOperations(api, false, url, currentOperations);
            resourceDiff.addNonCompliantExistingOperations(complianceMsg, operas);
        }
    }

    private void checkCompliance(Map<String, Path> resources, Map<String, Path> select) {
        ResourceDiff resourceDiff;
        String api = "future";
        Map<String, Path> subset = Maps.intersection(resources, select);
        for (String url : subset.keySet()) {
            String complianceMsg = "observations." + api + "." + url.replace('/', '.');
            Path path = resources.get(url);
            resourceDiff = new ResourceDiff(depths, url);
            Map<HttpMethod, Operation> operations = path.getOperationMap();
            if (Diff.ALL.equals(depths)) {
                Map<HttpMethod, OperationDiff> operas = mapOperations(api, true, url, operations);
                resourceDiff.addNonCompliantCandidateOperations(complianceMsg, operas);
            }
            resourceDiffs.add(resourceDiff);
        }
    }

    private Map<HttpMethod, OperationDiff> mapOperations(String api, boolean futureAPI, String url, Map<HttpMethod, Operation> operations) {
        Map<HttpMethod, OperationDiff> operas = new EnumMap<>(HttpMethod.class);
        for (Entry<HttpMethod, Operation> entry : operations.entrySet()) {
            HttpMethod method = entry.getKey();
            Operation operation = entry.getValue();
            OperationDiff od = new OperationDiff(referenceAPI, candidateAPI, depths, maturity, versions);
            od.checkRequestHeaderCompliance(operation, futureAPI, api + ".request.headers.compliance.for::" + url.replace('/', '.'));
            od.checkResponseCompliance(operation, method, futureAPI, api + ".response.compliance.for::" + url.replace('/', '.'));
            od.checkVersionCompliance(operation, method, futureAPI, api + ".version.compliance.for::" + url.replace('/', '.'));
            if (!od.isCompliant()) {
                operas.put(method, od);
            }
        }
        return operas;
    }


    private Maps<HttpMethod, Operation> getOperationDelta(ResourceDiff resourceDiff,
                                                          Map<HttpMethod, Operation> currentOperations,
                                                          Map<HttpMethod, Operation> futureOperations) {
        Maps<HttpMethod, Operation> operationsDelta = Maps.diff(currentOperations, futureOperations);
        Map<HttpMethod, Operation> increasedOperation = operationsDelta.getAdded();
        Map<HttpMethod, Operation> missingOperation = operationsDelta.getRemoved();
        resourceDiff.setNewOperations(increasedOperation);
        resourceDiff.setMissingOperations(missingOperation);
        return operationsDelta;
    }

    private Map<HttpMethod, OperationDiff> getChangedOperations(Map<HttpMethod, Operation> existing,
                                                                Map<HttpMethod, Operation> future,
                                                                Maps<HttpMethod, Operation> delta,
                                                                String context) {
        List<HttpMethod> common = delta.getCommon();
        Map<HttpMethod, OperationDiff> operas = new EnumMap<>(HttpMethod.class);
        OperationDiff changes;
        for (HttpMethod method : common) {
            Operation existingOpr = existing.get(method);
            Operation futureOpr = future.get(method);
            changes = new OperationDiff(referenceAPI, candidateAPI, depths, maturity, versions);
            changes = changes.analyze(existingOpr, futureOpr, method, context);
            if (changes.isDiff()) {
                operas.put(method, changes);
            }
        }
        return operas;
    }

    private List<Endpoint> convert2EndpointList(List<String> common, Map<String, Path> futureResources) {
        List<Endpoint> endpoints = new ArrayList<>();
        if (null == common) return endpoints;
        for (Entry<String, Path> entry : futureResources.entrySet()) {
            String url = entry.getKey();
            Path path = entry.getValue();
            if (common.contains(url)) {
                Map<HttpMethod, Operation> operationMap = path.getOperationMap();
                mapOperations(endpoints, url, operationMap);
            }
        }
        return endpoints;
    }


    private List<Endpoint> convert2EndpointList(Map<String, Path> map) {
        List<Endpoint> endpoints = new ArrayList<>();
        if (null == map) return endpoints;
        for (Entry<String, Path> entry : map.entrySet()) {
            String url = entry.getKey();
            Path path = entry.getValue();
            Map<HttpMethod, Operation> operationMap = path.getOperationMap();
            mapOperations(endpoints, url, operationMap);
        }
        return endpoints;
    }

    private List<Endpoint> mapOperations(List<Endpoint> endpoints, String url, Map<HttpMethod, Operation> operationMap) {
        for (Entry<HttpMethod, Operation> entry : operationMap.entrySet()) {
            HttpMethod httpMethod = entry.getKey();
            Operation operation = entry.getValue();
            Endpoint endpoint = new Endpoint(url, httpMethod, operation);
            endpoints.add(endpoint);
        }
        return endpoints;
    }

    private Collection<? extends Endpoint> convert2EndpointList(String pathUrl, Map<HttpMethod, Operation> map) {
        List<Endpoint> endpoints = new ArrayList<>();
        if (null == map) return endpoints;
        return mapOperations(endpoints, pathUrl, map);
    }

}
