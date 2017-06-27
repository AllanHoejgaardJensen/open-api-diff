package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dk.hoejgaard.openapi.diff.compare.util.Lists;
import dk.hoejgaard.openapi.diff.compare.util.Maps;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import dk.hoejgaard.openapi.diff.model.ContentType;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;


/**
 * Contains the changes between the versions of the same operation in existing and the future API.
 */
public class OperationDiff {

    private final Diff depths;
    private final Maturity maturity;
    private final Versions versions;
    private Swagger referenceAPI;
    private Swagger candidateAPI;
    private final OperationChanges operationChanges = new OperationChanges();
    private final OperationChanges existingCompliance = new OperationChanges();
    private final List<ContentType> addedContentTypes = new ArrayList<>();
    private final List<ContentType> missingContentTypes = new ArrayList<>();
    private final List<ResponseChanges> changedResponses = new ArrayList<>();
    private final List<PropertyChanges> changedProperties = new ArrayList<>();
    private final Map<String, Boolean> defaultRequestHeaders = new LinkedHashMap<>();

    private List<Parameter> addedParameters = new ArrayList<>();
    private List<Parameter> missingParameters = new ArrayList<>();
    private List<ParameterChanges> changedParameters = new ArrayList<>();

    private Map<String, Response> addedResponses = new HashMap<>();
    private Map<String, Response> missingResponses = new HashMap<>();

    private List<ScopedProperty> addedProperties = new ArrayList<>();
    private List<ScopedProperty> missingProperties = new ArrayList<>();

    /**
     * @param referenceAPI the existing API
     * @param candidateAPI the future candidate API
     * @param depths       the level of detail and depth used in the comparison
     * @param maturity     the level of REST service maturity used in the comparison
     * @param versions     the number of overlapping versions using the content-type as a versioning mechanism
     */
    public OperationDiff(Swagger referenceAPI, Swagger candidateAPI, Diff depths, Maturity maturity, Versions versions) {
        this(depths, maturity, versions);
        this.referenceAPI = referenceAPI;
        this.candidateAPI = candidateAPI;
        this.defaultRequestHeaders.put("Accept", true);
        this.defaultRequestHeaders.put("X-Service-Generation", true);
        this.defaultRequestHeaders.put("X-Client-Version", true);
        this.defaultRequestHeaders.put("X-Log-Token", false);
    }

    /**
     * @param depths   the level of detail and depth used in the comparison
     * @param maturity the level of REST service maturity used in the comparison
     * @param versions the number of overlapping versions using the content-type as a versioning mechanism
     */
    OperationDiff(Diff depths, Maturity maturity, Versions versions) {
        this.depths = depths;
        this.maturity = maturity;
        this.versions = versions;
    }

    /**
     * analyzes the differences between an existing operation in the current API and its successor in a future API.
     *
     * @param existingOpr the existing operation
     * @param futureOpr   the future candidate operation
     * @param method      the verb (GET, POST, PUT, DELETE,...)
     * @param context     the context in which the observed differences should be reported according to
     * @return the result of the comparison as a complete object with the changes found
     */
    public OperationDiff analyze(Operation existingOpr, Operation futureOpr, HttpMethod method, String context) {
        if (Diff.ALL.equals(depths)) {
            checkRequestHeaderCompliance(existingOpr, false, "existing.operation.had.");
            checkRequestHeaderCompliance(futureOpr, true, "future.operation.had");
            getChangedContentTypes(existingOpr, futureOpr, context);
            getChangedResponses(existingOpr, futureOpr, method);
        }
        getChangedItems(existingOpr, futureOpr);
        return this;
    }

    /**
     * checks the request headers and evaluates the headers to be compliant or not
     *
     * @param operation the operation for which the headers are evaluated
     * @param future    is this the future candidate operation or from the existing API
     * @param scope     the scope for the evaluation and reporting
     */
    public void checkRequestHeaderCompliance(Operation operation, boolean future, String scope) {
        List<Parameter> parameters = operation.getParameters();
        List<HeaderParameter> headers = parameters.stream()
            .filter(parameter -> parameter instanceof HeaderParameter)
            .map(header -> (HeaderParameter) header)
            .collect(Collectors.toList());

        for (Map.Entry<String, Boolean> entry : defaultRequestHeaders.entrySet()) {
            boolean required = entry.getValue();
            boolean found = false;
            for (HeaderParameter header : headers) {
                if (entry.getKey().equals(header.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found && required) {
                String key = ".non-compliant.required.header.setup";
                String value = "a required header " + entry.getKey() + " was not found";
                if (future) {
                    operationChanges.addBreakingChange(key, value);
                } else {
                    existingCompliance.addBreakingChange(scope + key, value);

                }
            } else if (!found) {
                String key = ".non-compliant header setup";
                String value = "a header " + entry.getKey() + " was not found";
                if (future) {
                    operationChanges.addDefinitionFlaw(":::non-optimal.header.setup", "the header " + entry.getKey() + " was not found");
                } else {
                    existingCompliance.addBreakingChange(scope + key, value);
                }
            }
        }
    }

    /**
     * checks the responses and their headers and evaluates them to be compliant or not
     *
     * @param opr    the operation for which the headers are evaluated
     * @param method the verb used (as response compliance varies according to verb)
     * @param future is this the future candidate operation or from the existing API
     * @param scope  the scope for the evaluation and reporting
     */
    public void checkResponseCompliance(Operation opr, HttpMethod method, boolean future, String scope) {
        ResponseChanges rc = getResponseChangesInstance(
            Diff.ALL.equals(depths) || Diff.BREAKING.equals(depths) || Diff.POTENTIALLY_BREAKING.equals(depths));
        List<String> missing = new ArrayList<>();
        boolean compliant = rc.checkCompliance(opr.getResponses(), method, future, scope, missing);
        if (!compliant) {
            String key = ":::non-optimal.response.setup";
            String value = "the following response codes:  " + missing + "  were either missing or may not include required response " +
                "headers and thus do not support a future proof API";
            if (future) {
                operationChanges.addDefinitionFlaw(scope + key, value);
            } else {
                existingCompliance.addDefinitionFlaw(scope + key, value);
            }
        }
    }

    /**
     * checks the versions and evaluates the headers to be compliant or not
     *
     * @param opr    the operation for which the headers are evaluated
     * @param method the verb used (as compliance varies according to verb)
     * @param future is this the future candidate operation or from the existing API
     * @param scope  the scope for the evaluation and reporting
     */
    public void checkVersionCompliance(Operation opr, HttpMethod method, boolean future, String scope) {
        boolean isConsumer = HttpMethod.PUT.equals(method) || HttpMethod.POST.equals(method);
        boolean consumesCompliant;
        boolean consumesOK = false;
        if (isConsumer) {
            consumesOK = getConsumerCompliance(opr);
        }
        consumesCompliant = !isConsumer || consumesOK;

        if (isConsumer && !consumesCompliant) {
            String key = scope + ":::non-optimal.consumer.setup";
            String value = "the consumers defined did not meet the future prof setup for content-types";
            if (future) {
                operationChanges.addDefinitionFlaw(scope + key, value);
            } else {
                existingCompliance.addDefinitionFlaw(key, value);
            }
        }

        boolean produceCompliant = getProducerCompliance(opr);
        if (!produceCompliant) {
            String key = scope + ":::non-optimal.producer.setup";
            String value = "the producers defined did not meet the future prof setup for content-types";
            if (future) {
                operationChanges.addDefinitionFlaw(scope + key, value);
            } else {
                existingCompliance.addDefinitionFlaw(key, value);
            }
        }
    }

    /**
     * @return the compliance for the existing API
     */
    public OperationChanges getExistingCompliance() {
        return existingCompliance;
    }

    public List<String> getAddedContentTypes() {
        return addedContentTypes.stream()
            .map(ContentType::toString)
            .collect(Collectors.toList());
    }

    /**
     * @return the content-types no longer in the future candidate API when comparing that to the existing API for the same operation
     */
    public List<String> getMissingContentTypes() {
        return missingContentTypes.stream()
            .map(ContentType::toString)
            .collect(Collectors.toList());
    }

    /**
     * @return the added parameters for the operation when comparing the existing and the future candidate API
     */
    public List<Parameter> getAddedParameters() {
        return Collections.unmodifiableList(addedParameters);
    }

    /**
     * @return the removed parameters for the operation when comparing the existing and the future candidate API
     */
    public List<Parameter> getMissingParameters() {
        return Collections.unmodifiableList(missingParameters);
    }

    /**
     * @return the changed parameters for the operation when comparing the existing and the future candidate API
     */
    public List<ParameterChanges> getChangedParameters() {
        return Collections.unmodifiableList(changedParameters);
    }

    /**
     * @return the added properties for the operation when comparing the existing and the future candidate API
     */
    public List<ScopedProperty> getAddedProperties() {
        return Collections.unmodifiableList(addedProperties);
    }

    /**
     * @return the removed properties for the operation when comparing the existing and the future candidate API
     */
    public List<ScopedProperty> getMissingProperties() {
        return Collections.unmodifiableList(missingProperties);
    }

    /**
     * @return the changed properties for the operation when comparing the existing and the future candidate API
     */
    public List<PropertyChanges> getChangedProperties() {
        return changedProperties;
    }

    /**
     * @return the added responses for the operation when comparing the existing and the future candidate API
     */
    public Map<String, Response> getAddedResponses() {
        return Collections.unmodifiableMap(addedResponses);
    }

    /**
     * @return the removed responses for the operation when comparing the existing and the future candidate API
     */
    public Map<String, Response> getMissingResponses() {
        return Collections.unmodifiableMap(missingResponses);
    }

    /**
     * @return the changed responses for the operation when comparing the existing and the future candidate API
     */
    public List<ResponseChanges> getChangedResponses() {
        return Collections.unmodifiableList(changedResponses);
    }

    /**
     * @return are there significant differences
     */
    public boolean isDiff() {
        if (Diff.ALL.equals(depths)) {
            return isBasicDiff() || isDiffCompliance();
        } else if (Diff.BREAKING.equals(depths)) {
            return isBasicDiff() || !operationChanges.getBreakingChanges().isEmpty();
        } else if (Diff.POTENTIALLY_BREAKING.equals(depths)) {
            return isBasicDiff() || !operationChanges.getBreakingChanges().isEmpty() || !operationChanges.getPotentiallyBreaking().isEmpty();
        } else {
            return isBasicDiff();
        }
    }

    /**
     * @return is it broken
     */
    public boolean isBroke() {
        return !operationChanges.getBreakingChanges().isEmpty();
    }

    /**
     * @return is it potentially broken
     */
    public boolean isPotentiallyBroke() {
        return !operationChanges.getPotentiallyBreaking().isEmpty();
    }

    /**
     * @return is it compliant
     */
    public boolean isCompliant() {
        boolean ppr = arePropertiesUnAffected() && areParametersUnAffected() && areResponsesUnAffected();
        return ppr && areOperationsUnAffected() && areContentTypesUnAffected();
    }

    /**
     * @return is it changed
     */
    public boolean isChanged() {
        if (Diff.ALL.equals(depths)) {
            return !isCompliant();
        } else if (Diff.POTENTIALLY_BREAKING.equals(depths)) {
            return !arePropertiesUnAffected() || !areParametersUnAffected() || !operationChanges.getBreakingChanges().isEmpty()
                || !operationChanges.getPotentiallyBreaking().isEmpty();
        } else if (Diff.BREAKING.equals(depths)) {
            return !arePropertiesUnAffected() || !areParametersUnAffected() || !operationChanges.getBreakingChanges().isEmpty();

        }
        return true;
    }

    /**
     * @return the breaking changes
     */
    public Map<String, List<String>> getBreakingChanges() {
        return Collections.unmodifiableMap(operationChanges.getBreakingChanges());
    }

    /**
     * @return the potentially breaking changes
     */
    public Map<String, List<String>> getPotentiallyBreakingChanges() {
        return Collections.unmodifiableMap(operationChanges.getPotentiallyBreaking());
    }

    /**
     * @return the detected opinionated flaws for the future candidate operation
     */
    public Map<String, List<String>> getDesignFlaws() {
        return Collections.unmodifiableMap(operationChanges.getFlawedDefines());
    }

    /**
     * @return the detected opinionated flaws/improvements for the existing operation
     */
    public Map<String, List<String>> getExistingFlaws() {
        return Collections.unmodifiableMap(operationChanges.getImprovements());
    }

    /**
     * @return changes
     */
    public Map<String, List<String>> getChanges() {
        return Collections.unmodifiableMap(operationChanges.getChanges());
    }

    OperationDiff getChangedContentTypes(Operation existingOpr, Operation futureOpr, String context) {
        Lists<String> produces = Lists.diff(existingOpr.getProduces(), futureOpr.getProduces());
        if (checkContentProducerParadigm(produces.getCommon(), produces.getAdded(), produces.getRemoved(), context)) {
            if (Maturity.FULL.equals(maturity) || Maturity.HAL.equals(maturity)) {
                maturityProducerVersioningReport(context, produces);
            } else {
                genuineProducerReport(context, produces);
            }
        }
        Lists<String> consumes = Lists.diff(existingOpr.getConsumes(), futureOpr.getConsumes());
        if (isConsumerCheckNeeded(consumes)) {
            if (checkContentConsumerParadigm(consumes.getCommon(), consumes.getAdded(), consumes.getRemoved(), context)) {
                if (Maturity.FULL.equals(maturity) || Maturity.HAL.equals(maturity) || Maturity.HATEOAS.equals(maturity)) {
                    maturityConsumerVersioningReport(context);
                } else {
                    genuineConsumerReport(context);
                }
            }
        }
        addAddedContentTypes(produces.getAdded());
        if (!produces.getRemoved().isEmpty()) {
            missingProducersReport(context, produces);
        }
        addMissingContentTypes(produces.getRemoved());
        addAddedContentTypes(consumes.getAdded());
        addMissingContentTypes(consumes.getRemoved());
        if (!consumes.getRemoved().isEmpty()) {
            missingConsumersReport(context, consumes);
        }
        return this;
    }

    List<Parameter> getAddedParameters(Operation opr, Operation oprNew) {
        Lists<Parameter> paramDiff = Lists.diff(opr.getParameters(), oprNew.getParameters());
        return paramDiff.getAdded();
    }

    List<Parameter> getRemovedParameters(Operation opr, Operation oprNew) {
        Lists<Parameter> paramDiff = Lists.diff(opr.getParameters(), oprNew.getParameters());
        return paramDiff.getRemoved();
    }

    List<Parameter> getCommonParameters(Operation opr, Operation oprNew) {
        Lists<Parameter> paramDiff = Lists.diff(opr.getParameters(), oprNew.getParameters());
        return paramDiff.getCommon();
    }

    /**
     * checks whether the application/hal+json is used as a default content setup and versions are added
     * and thus whether it is possible to create a future proof api or not.
     * This does not yet support "labelled releases" only consecutive overlapping versions.
     *
     * @param common  content-types that is the types present in the current and the future api
     * @param added   content-types that is the types not present in the current but present in the future api
     * @param removed content-types that is the types present in the current and not in the future api
     * @return true if the paradigm is found compliant
     */
    boolean checkContentProducerParadigm(List<String> common, List<String> added, List<String> removed, String context) {
        boolean defaultContentTypeFound = false;
        Map<String, List<String>> conceptsVersions = new LinkedHashMap<>();
        for (String commonContentType : common) {
            if (Maturity.HAL.equals(maturity) || (Maturity.FULL.equals(maturity))) {
                ContentType contentType = new ContentType(commonContentType);
                if (contentType.isDefaultContentType()) {
                    defaultContentTypeFound = true;
                }
                conceptsVersions = getConceptVersions(conceptsVersions, contentType);
            }
        }
        if (defaultContentTypeFound) {
            if (Versions.SINGLE.equals(versions)) {
                return inAdequateCommonVersions(conceptsVersions, added, removed, 1);
            } else if (Versions.DOUBLE.equals(versions)) {
                return inAdequateCommonVersions(conceptsVersions, added, removed, 2);
            } else if (Versions.TRIPLE.equals(versions)) {
                return inAdequateCommonVersions(conceptsVersions, added, removed, 3);
            }
            return false;
        } else {
            operationChanges.addRecordedChange(context + ".content-type.scheme.unknown",
                "difficult to state facts on breaking or not " + conceptsVersions);
            return true;
        }
    }
    /**
     * checks whether the application/json is used as a default content setup and versions are added
     * and thus whether it is possible to create a future proof api or not.
     * This does not yet support "labelled releases" only consecutive overlapping versions.
     *
     * @param common  content-types that is the types present in the current and the future api
     * @param added   content-types that is the types not present in the current but present in the future api
     * @param removed content-types that is the types present in the current and not in the future api
     * @return true if the paradigm is found compliant
     */
    boolean checkContentConsumerParadigm(List<String> common, List<String> added, List<String> removed, String context) {
        boolean defaultContentTypeFound = false;
        Map<String, List<String>> conceptsVersions = new LinkedHashMap<>();
        for (String commonContentType : common) {
            if (Maturity.HAL.equals(maturity) || (Maturity.FULL.equals(maturity))) {
                ContentType contentType = new ContentType(commonContentType);
                if (contentType.isDefaultContentType()) {
                    defaultContentTypeFound = true;
                }
                conceptsVersions = getConceptVersions(conceptsVersions, contentType);
            }
        }
        if (defaultContentTypeFound) {
            if (Versions.SINGLE.equals(versions)) {
                return inAdequateConsumerVersions(conceptsVersions, added, removed, 1);
            } else if (Versions.DOUBLE.equals(versions)) {
                return inAdequateConsumerVersions(conceptsVersions, added, removed, 2);
            } else if (Versions.TRIPLE.equals(versions)) {
                return inAdequateConsumerVersions(conceptsVersions, added, removed, 3);
            }
            return false;
        } else {
            operationChanges.addRecordedChange(context + ".content-type.scheme.unknown",
                "difficult to state facts on breaking or not " + conceptsVersions);
            return true;
        }
    }

    OperationDiff getChangedResponses(Operation existingOpr, Operation futureOpr, HttpMethod method) {
        Maps<String, Response> responses = Maps.diff(existingOpr.getResponses(), futureOpr.getResponses());
        if (Diff.ALL.equals(depths) || Diff.BREAKING.equals(depths)) {
            checkAddedResponseCompliance(responses.getAdded());
            checkRemovedResponseCompliance(responses.getRemoved());
        }
        addAddedResponses(responses.getAdded());
        addMissingResponses(responses.getRemoved());
        List<String> delta = responses.getCommon();
        for (int i = 0; i < delta.size(); i++) {
            Response existing = getResponse(existingOpr, delta, i);
            Response future = getResponse(futureOpr, delta, i);
            ResponseChanges changes = getResponseChangesInstance(Diff.ALL.equals(depths));
            changes = changes.diff(existing, future, getResponseCode(delta, i), method);
            if (changes.isDiff()) {
                addChangedResponse(changes);
                operationChanges.addBreakingChange(changes.getBreaking());
                operationChanges.addPotentialBreakingChange(changes.getPotentiallyBreaking());
                operationChanges.addRecordedChange(changes.getChanges());
                operationChanges.addDefinitionFlaw(changes.getFlawedDefines());
                operationChanges.addDefinitionFlaw(changes.getExistingFlaws());
            }
        }
        return this;
    }

    private boolean isDiffOperation() {
        return !addedContentTypes.isEmpty() || !missingContentTypes.isEmpty();
    }

    private void getChangedItems(Operation existingOpr, Operation futureOpr) {
        List<Parameter> oldParameters = existingOpr.getParameters();
        List<Parameter> newParameters = futureOpr.getParameters();
        ParameterDiff differences = new ParameterDiff(referenceAPI.getDefinitions(), candidateAPI.getDefinitions(), depths,
            oldParameters, newParameters);
        getChangedParameters(differences);
        getChangedProperties(differences);
    }

    private void getChangedProperties(ParameterDiff differences) {
        addAddedProperties(differences.getAddedProps());
        addMissingProperties(differences.getMissingProps());
        addPropertyChanges(differences.getChangedProperties());
        List<PropertyChanges> changedProps = differences.getChangedProperties();
        for (PropertyChanges change : changedProps) {
            operationChanges.addBreakingChange(change.getBreaking());
            operationChanges.addPotentialBreakingChange(change.getPotentiallyBreaking());
            operationChanges.addDefinitionFlaw(change.getFlawedDefines());
        }
    }

    private void getChangedParameters(ParameterDiff differences) {
        addAddedParameters(differences.getAddedParams());
        addMissingParameters(differences.getMissingParams());
        addParameterChanges(differences.getChangedParams());
        List<ParameterChanges> changes = differences.getChangedParams();
        for (ParameterChanges change : changes) {
            operationChanges.addBreakingChange(change.getBreaking());
            operationChanges.addPotentialBreakingChange(change.getPotentiallyBreaking());
            operationChanges.addDefinitionFlaw(change.getFlawedDefines());
        }
    }

    private void addAddedContentTypes(List<String> added) {
        this.addedContentTypes.addAll(added.stream().map(ContentType::new).collect(Collectors.toList()));
    }

    private void addMissingContentTypes(List<String> removed) {
        this.missingContentTypes.addAll(removed.stream().map(ContentType::new).collect(Collectors.toList()));
    }

    private void addAddedParameters(List<Parameter> addParameters) {
        this.addedParameters = addParameters;
    }

    private void addMissingParameters(List<Parameter> missingParameters) {
        this.missingParameters = missingParameters;
    }

    private void addParameterChanges(List<ParameterChanges> parameterChanges) {
        this.changedParameters = parameterChanges;
    }

    private void addAddedProperties(List<ScopedProperty> addProps) {
        this.addedProperties = addProps;
    }

    private void addMissingProperties(List<ScopedProperty> missingProps) {
        this.missingProperties = missingProps;
    }

    private void addAddedResponses(Map<String, Response> added) {
        addedResponses = added;
    }

    private void addMissingResponses(Map<String, Response> removed) {
        missingResponses = removed;
    }

    private void addChangedResponse(ResponseChanges common) {
        changedResponses.add(common);
    }

    private boolean isBasicDiff() {
        return isDiffParam() || isDiffProp() || isDiffOperation();
    }

    private boolean isDiffProp() {
        return !addedProperties.isEmpty()
            || !missingProperties.isEmpty();
    }

    private boolean isDiffParam() {
        return !addedParameters.isEmpty() || !missingParameters.isEmpty()
            || !changedParameters.isEmpty();
    }

    private boolean isDiffCompliance() {
        return !operationChanges.getBreakingChanges().isEmpty() || !operationChanges.getPotentiallyBreaking().isEmpty()
            || !operationChanges.getChanges().isEmpty() || operationChanges.getFlawedDefines().isEmpty();
    }

    private void missingConsumersReport(String context, Lists<String> consumers) {
        operationChanges.addRecordedChange(context + ".content-type.consumers.removed", "this may cause problem for some clients" +
            "removed are: [" + consumers.getRemoved() + "]");
        operationChanges.addPotentialBreakingChange(context + ".content-type.consumers.removed", "this may cause problem for " +
            "some clients if support is removed in the endpoint - removed are: [" + consumers.getRemoved() + "]");
    }

    private void missingProducersReport(String context, Lists<String> produces) {
        operationChanges.addRecordedChange(context + ".content-type.producers.removed", "this may cause problem for some clients " +
            "removed are: [" + produces.getRemoved() + "]");
        operationChanges.addPotentialBreakingChange(context + ".content-type.producers.removed", "this may cause problem for some clients " +
            "removed are: [" + produces.getRemoved() + "]");
    }

    private void genuineConsumerReport(String context) {
        operationChanges.addDefinitionFlaw(context + ".producer.version.definition.flaw", "the versioning scheme used for the producer " +
            "is unknown and creation of a future proof versioning scheme is very difficult and changes will hurt the consumers of " +
            "the service as well as the implementation team for the service");
    }

    private void maturityConsumerVersioningReport(String context) {
        operationChanges.addPotentialBreakingChange(context + ".producer.versioning.non-compliant", "content consumers are" +
            " not following the scheme of having application/hal+json;concept=[projection];v=[version] with a " +
            versions + " version available");
        operationChanges.addRecordedChange(context + ".producer.version.scheme.conflict", "versions do not overlap correctly or " +
            "and/do not use default version");
    }

    private void genuineProducerReport(String context, Lists<String> produces) {
        operationChanges.addDefinitionFlaw(context + ".producer.version.definition.flaw", "the versioning scheme used is unknown and " +
            "creation of a future proof versioning scheme is very difficult and changes will hurt consumers of the service as they " +
            "have to change as a reaction of the change and have no default way to revert to the previous version on the content in" +
            " the endpoint. The observed producers are: [common][added][removed] [" + produces.getCommon() + "] ["
            + produces.getAdded() + "] [" + produces.getRemoved() + "]");
    }

    private void maturityProducerVersioningReport(String context, Lists<String> produces) {
        operationChanges.addPotentialBreakingChange(context + ".producer.versioning.non-compliant",
            "content producers are not following the scheme of having application/hal+json;concept=[projection];v=[version] with a " +
                versions + " version available. The observed changed and added producers are: [common][added] [" +
                produces.getCommon() + "] [" + produces.getAdded() + "] [" + produces.getRemoved() + "]");
        operationChanges.addRecordedChange(context + ".producer.version.scheme.conflict", "versions do not overlap correctly or " +
            "and/do not use default version. Content-types are [common][added] " +
            "[" + produces.getCommon() + "] [" + produces.getAdded() + "] [" + produces.getRemoved() + "]");
    }

    private void addPropertyChanges(List<PropertyChanges> changed) {
        this.changedProperties.addAll(changed);
    }

    private Map<String, List<String>> getConceptVersions(Map<String, List<String>> conceptsNVersions, ContentType c) {
        if (c.isSchemeCompliant()) {
            String concept = c.getProjection();
            String version = c.getVersion();
            if (conceptsNVersions.containsKey(concept)) {
                conceptsNVersions.get(concept).add(version);
            } else {
                List<String> vl = new ArrayList<>();
                vl.add(version);
                conceptsNVersions.put(concept, vl);
            }
        }
        return conceptsNVersions;
    }

    private Response getResponse(Operation opr, List<String> delta, int i) {
        return opr.getResponses().get(getResponseCode(delta, i));
    }

    private String getResponseCode(List<String> delta, int i) {
        return delta.get(i);
    }

    private boolean inAdequateCommonVersions(
        Map<String, List<String>> conceptsNVersions, List<String> added, List<String> removed, int versions) {
        if (conceptsNVersions.isEmpty()) return true;
        for (Map.Entry<String, List<String>> entry : conceptsNVersions.entrySet()) {
            List<String> vl = entry.getValue();
            int next = 0;
            int previous = 0;
            if (vl.size() < versions) {
                return true;
            } else if (added.isEmpty() || removed.isEmpty()) {
                if (!(versions <= vl.size())) {
                    return true;
                }
            } else {
                for (String v : vl) {
                    int n = Integer.parseInt(v);
                    if (n > next) next = n;
                    if (n < previous) previous = n;
                }
                next++;
                previous++;
                String concept = entry.getKey();
                if (!added.contains("application/hal+json;concept=" + concept + ";v=" + next)
                    || !removed.contains("application/hal+json;concept=" + concept + ";v=" + previous)
                    ) {
                    if (Maturity.FULL.equals(maturity) || Maturity.HAL.equals(maturity))
                        operationChanges.addBreakingChange("non-compliant concept versioning for HAL content-types",
                            "                            content producers are not following the scheme of having" +
                                " application/hal+json;concept=" + concept + ";v=[version] with a " + versions + " version available");
                    operationChanges.addRecordedChange("producer.hal.version.scheme.conflict",
                        "the projections versions do not overlap correctly");
                    return true;
                }
            }
        }
        return false;
    }
    private boolean inAdequateConsumerVersions(
        Map<String, List<String>> conceptsNVersions, List<String> added, List<String> removed, int versions) {
        if (conceptsNVersions.isEmpty()) return true;
        for (Map.Entry<String, List<String>> entry : conceptsNVersions.entrySet()) {
            List<String> vl = entry.getValue();
            int next = 0;
            int previous = 0;
            if (vl.size() < versions) {
                return true;
            } else if (added.isEmpty() || removed.isEmpty()) {
                if (!(versions <= vl.size())) {
                    return true;
                }
            } else {
                for (String v : vl) {
                    int n = Integer.parseInt(v);
                    if (n > next) next = n;
                    if (n < previous) previous = n;
                }
                next++;
                previous++;
                String concept = entry.getKey();
                if (!added.contains("application/json;concept=" + concept + ";v=" + next)
                    || !removed.contains("application/json;concept=" + concept + ";v=" + previous)
                    ) {
                    if (Maturity.FULL.equals(maturity) || Maturity.HAL.equals(maturity) || Maturity.HATEOAS.equals(maturity))
                        operationChanges.addBreakingChange("non-compliant concept versioning for content-types",
                            "                            content consumers are not following the scheme of having" +
                                " application/json;concept=" + concept + ";v=[version] with a " + versions + " version available");
                    operationChanges.addRecordedChange("consumer.version.scheme.conflict",
                        "the projections and versions do not overlap correctly");
                    return true;
                }
            }
        }
        return false;
    }

    private void checkAddedResponseCompliance(Map<String, Response> added) {
        if (added.keySet().contains("202")) {
            operationChanges.addBreakingChange("response.202.added", ResponseChanges.getCodeMsg("202"));
        }
        if (added.keySet().contains("301")) {
            operationChanges.addPotentialBreakingChange("response.301.added", "adding 301 may break the client programming model, clients " +
                "may not be able to follow the location if not implemented and thus experience service as being down");
        }
        if (added.keySet().contains("307")) {
            operationChanges.addPotentialBreakingChange("response.307.added", "adding 307 may break the client programming model, clients " +
                "may not be able to follow the location if not implemented and thus experience service as being down");
        }
        if (added.keySet().contains("429")) {
            operationChanges.addPotentialBreakingChange("response.429.added", "adding 429 is not harming the client, clients " +
                "may however perceive service as being poor");
        }
        //SUGGEST: add more to (or deduct from) the list as the experience with the tool shows the needs
    }

    private void checkRemovedResponseCompliance(Map<String, Response> removed) {
        if (removed.keySet().contains("429")) {
            String msg429 = "response.429.removed";
            String explanation = "removing 429 may result in clients not backing off and thus this may result in higher load and congestion" +
                " on the server side resulting in a bad client experience";
            operationChanges.addPotentialBreakingChange(msg429, explanation);
            operationChanges.addRecordedChange(msg429, explanation);
        }
        if (removed.keySet().contains("301")) {
            String msg301 = "response.301.removed";
            String explanation = "removing 301 means clients cannot follow change of location and experience service as being down";
            operationChanges.addBreakingChange(msg301, explanation);
            operationChanges.addPotentialBreakingChange(msg301, explanation);
            operationChanges.addRecordedChange(msg301, explanation);
        }
        if (removed.keySet().contains("307")) {
            String msg301 = "response.307.removed";
            String explanation = "removing 307 means clients cannot follow change of location and experience service as being down";
            operationChanges.addBreakingChange(msg301, explanation);
            operationChanges.addPotentialBreakingChange(msg301, explanation);
            operationChanges.addRecordedChange(msg301, explanation);
        }
        //SUGGEST: add more to (or deduct from) the list as the experience with the tool shows the needs
    }

    private Boolean getProducerCompliance(Operation opr) {
        boolean defaultProducerFound = false;
        List<String> producer = opr.getProduces();
        if (producer != null) {
            for (String produce : producer) {
                ContentType ct = new ContentType(produce);
                if (ct.isDefaultContentType()) {
                    defaultProducerFound = true;
                }
            }
        }
        if (defaultProducerFound) {
            for (String produce : producer) {
                ContentType ct = new ContentType(produce);
                if (ct.isSchemeCompliant()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getConsumerCompliance(Operation opr) {
        boolean defaultConsumerFound = false;
        List<String> consumes = opr.getConsumes();
        if (consumes != null) {
            for (String consume : consumes) {
                if (ContentType.isJsonOnly(consume)) {
                    defaultConsumerFound = true;
                    break;
                }
            }
        }
        if (defaultConsumerFound) {
            for (String consume : consumes) {
                ContentType ct = new ContentType(consume);
                if (ct.isJsonConceptCompliant()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areResponsesUnAffected() {
        return addedResponses.isEmpty() && missingResponses.isEmpty() && changedResponses.isEmpty();
    }

    private boolean areContentTypesUnAffected() {
        return addedContentTypes.isEmpty() && missingContentTypes.isEmpty();
    }

    private boolean areOperationsUnAffected() {
        return operationChanges.getBreakingChanges().isEmpty() && operationChanges.getPotentiallyBreaking().isEmpty() &&
            operationChanges.getFlawedDefines().isEmpty() && operationChanges.getChanges().isEmpty();
    }

    private boolean arePropertiesUnAffected() {
        return addedProperties.isEmpty() && missingProperties.isEmpty();
    }

    private boolean areParametersUnAffected() {
        return addedParameters.isEmpty() && missingParameters.isEmpty() && changedParameters.isEmpty();
    }

    private boolean isConsumerCheckNeeded(Lists<String> consumes) {
        return !(consumes.getCommon().isEmpty() || consumes.getAdded().isEmpty() || consumes.getRemoved().isEmpty());
    }

    private ResponseChanges getResponseChangesInstance(boolean includeHeaders) {
        return new ResponseChanges(referenceAPI.getDefinitions(), candidateAPI.getDefinitions(), includeHeaders);
    }

}
