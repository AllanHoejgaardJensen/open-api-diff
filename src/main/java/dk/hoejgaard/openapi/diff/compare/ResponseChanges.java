package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dk.hoejgaard.openapi.diff.compare.util.Maps;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Response;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains a finite set of differences between a response in the existing API and the future candidate API responses
 * <p>
 * Furthermore it has functionality to verify compliance for responses with respect to their headers and the effect that has on the
 * ability to create a future proof API, using particular headers in the existing API make it easier to produce compliant APIs.
 */
public class ResponseChanges {
    private static final Map<String, String> HEADERS = new LinkedHashMap<>();
    private static final Map<String, String> CODES = new LinkedHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(ResponseChanges.class);

    private final Map<String, Boolean> responsesRequired = new HashMap<>();
    private final Map<String, String> defaultHeaders = new LinkedHashMap<>();
    private final Map<String, List<String>> specificHeaders = new LinkedHashMap<>();
    private final Map<String, List<String>> changes = new HashMap<>();
    private final Map<String, List<String>> flawedDefines = new HashMap<>();
    private final Map<String, List<String>> existingFlaws = new HashMap<>();
    private final Map<String, List<String>> potentiallyBreaking = new HashMap<>();
    private final Map<String, List<String>> breaking = new HashMap<>();

    private Diff depth;
    private boolean includeHeadersCheck = true;
    private Map<String, Model> existingDefinition;
    private Map<String, Model> futureDefinition;

    /**
     * @param requiredResponses a list of response codes that must be present for all verbs
     * @param headerChecks      if true the headers associated with the response code are checked
     * @param less              if true the headers associated with the response code are checked
     */
    ResponseChanges(List<String> requiredResponses, boolean headerChecks, boolean less) {
        for (String requiredResponse : requiredResponses) {
            responsesRequired.put(requiredResponse, false);
        }
        this.includeHeadersCheck = headerChecks;
        this.depth = includeHeadersCheck ? Diff.ALL : !less ? Diff.BREAKING : Diff.POTENTIALLY_BREAKING;
    }

    /**
     * @param existing model of the existing API
     * @param future model for the future candidate API
     * @param checkHeaders include checking headers to get a deep comparison for each response
     * @param less              if true the headers associated with the response code are checked
     */
    private ResponseChanges(Map<String, Model> existing, Map<String, Model> future, boolean checkHeaders, boolean less) {
        this.existingDefinition = existing;
        this.futureDefinition = future;
        initRequiredResponses();
        initHeadersInfo();
        initCodeInfo();
        this.includeHeadersCheck = checkHeaders;
        this.depth = checkHeaders ? Diff.ALL : !less ? Diff.BREAKING : Diff.POTENTIALLY_BREAKING;
        if (includeHeadersCheck) {
            initHeaderSetup();
        }
    }

    /**
     * @param existing model of the existing API
     * @param future model for the future candidate API
     * @param checkHeaders include checking headers to get a deep comparison for each response
     */
    ResponseChanges(Map<String, Model> existing, Map<String, Model> future, boolean checkHeaders) {
        this(existing, future, checkHeaders, false);
    }

    /**
     * @param requiredResponses a list of response codes that must be present for all verbs
     * @param headerChecks      if true the headers associated with the response code are checked
     */
    ResponseChanges(List<String> requiredResponses, boolean headerChecks) {
        this(requiredResponses, headerChecks, false);
    }

    /**
     * default set of response codes are used with default header checking for each response
     */
    ResponseChanges(boolean checkHeaders, boolean less) {
        this(null, null, checkHeaders, less);
    }

    /**
     * default set of response codes are used with default header checking for each response
     */
    ResponseChanges(boolean checkHeaders) {
        this(null, null, checkHeaders);
    }

    /**
     * default set of response codes are used without header checking for each response
     */
    ResponseChanges() {
        this(false);
    }

    /**
     * @param requiredResponses a list of response codes that must be present for all verbs, headers are not checked.
     */
    ResponseChanges(List<String> requiredResponses) {
        this(requiredResponses, false);
    }

    /**
     * @return diff says if there were differences between responses
     */
    public boolean isDiff() {
        if (Diff.ALL.equals(depth)) {
            return anyChange() || isPotentiallyBreaking() || isBreaking();
        } else if (Diff.BREAKING.equals(depth)) {
            return isBreaking();
        } else if (Diff.POTENTIALLY_BREAKING.equals(depth)) {
            return isBreaking() || isPotentiallyBreaking();
        }
        return false;
    }

    /**
     * @return a map containing the breaking changes observed
     */
    public Map<String, List<String>> getBreaking() {
        return breaking;
    }

    /**
     * @return a map containing the potentially breaking changes observed
     */
    public Map<String, List<String>> getPotentiallyBreaking() {
        return potentiallyBreaking;
    }

    /**
     * @return a map containing the design shortcomings observed
     */
    public Map<String, List<String>> getFlawedDefines() {
        return flawedDefines;
    }

    /**
     * @return a map containing the changes observed
     */
    public Map<String, List<String>> getChanges() {
        return changes;
    }

    /**
     * @param existing the response as defined in the current and existing API
     * @param future the response as defined in the future API
     * @param responseCode the http response code 200, 201, 202,...
     * @param method the http verb POST, PUT, GET, DELETE ...
     * @return a ResponseChanges object containing all changes observed
     */
    public ResponseChanges diff(Response existing, Response future, String responseCode, HttpMethod method) {
        if (existing.equals(future)) {
            logger.trace("Found equal ({} - {}, {}, {}, {})",
                new Object[]{existing.getDescription(), responseCode, method, future.getSchema(), existing.getSchema()});
        } else {
            logger.trace("Found !equal ({} - {}, {}, {}, {})",
                new Object[] {existing.getDescription(), responseCode, method, future.getSchema(), existing.getSchema()});
        }

        diffSchema(existing, future);


        if (Diff.ALL.equals(depth)) {
            if (!checkCompliance(responseCode, existing, method, false, "difference.recorded")) {
                List<String> pList = potentiallyBreaking.containsKey(responseCode) ? potentiallyBreaking.get(responseCode) : new ArrayList<>();
                pList.add("improvement suggestion - important status code or headers missing for existing API, " +
                    "potentially breaking clients ahead, see compliance section for further information if full depth is used");
                potentiallyBreaking.put("existing.compliance.for." + method + ".response.code." + responseCode + ".observation", pList);
            }
            if (!checkCompliance(responseCode, future, method, true, "difference.recorded")) {
                List<String> pList = flawedDefines.containsKey(responseCode) ? flawedDefines.get(responseCode) : new ArrayList<>();
                pList.add("improvement suggestion - important status code or headers missing for new API, " +
                    "may break future APIs for consumers, see compliance section for further information if full depth is used");
                flawedDefines.put("future.compliance.for." + method + ".response.code." + responseCode + ".observation", pList);
            }
        }
        Map<String, Property> existingHeaders = existing.getHeaders();
        Map<String, Property> futureHeaders = future.getHeaders();
        Maps<String, Property> headerDiff = Maps.diff(existingHeaders, futureHeaders);
        handleHeaders(responseCode, method, headerDiff);
        return this;
    }

    private void handleHeaders(String responseCode, HttpMethod method, Maps<String, Property> headerDiff) {
        Map<String, Property> addedHeaders = headerDiff.getAdded();
        if (!addedHeaders.isEmpty()) {
            List<String> bl = breaking.containsKey(responseCode) ? breaking.get(responseCode) : new ArrayList<>();
            bl.addAll(addedHeaders.keySet().stream()
                .filter(key -> addedHeaders.get(key) != null)
                .filter(key -> addedHeaders.get(key).getAllowEmptyValue() != null)
                .filter(key -> !addedHeaders.get(key).getAllowEmptyValue())
                .map(key -> " added response header: " + getHeaderName(key, addedHeaders.get(key)) + " which does not allow having empty value")
                .collect(Collectors.toList()));

            addBreaking(method + ".response.code." + responseCode + ".observation", bl);
            bl.addAll(addedHeaders.keySet().stream()
                .filter(key -> addedHeaders.get(key) != null)
                .filter(key -> !addedHeaders.get(key).getRequired())
                .map(key -> " added header: " + getHeaderName(key, addedHeaders.get(key)) + " which is required")
                .collect(Collectors.toList()));
            addBreaking(method + ".response.code." + responseCode + ".observation", bl);
        }
        Map<String, Property> removedHeaders = headerDiff.getRemoved();
        if (!removedHeaders.isEmpty()) {
            List<String> bl = breaking.containsKey(responseCode) ? breaking.get(responseCode) : new ArrayList<>();
            bl.addAll(removedHeaders.keySet().stream()
                .map(key -> " verb lacks response code " + responseCode + " : "
                    + key + " - and that breaks the client contract")
                .collect(Collectors.toList()));
            addBreaking(method + ".response.code." + responseCode + ".observation", bl);
        }
    }

    private String getHeaderName(String key, Property property) {
        if (property.getName() != null) {
            return property.getName();
        }
        return key;
    }

    /**
     * a change is detected and thus the API changes at a given point
     * @param change where the flaw is found
     * @param information what the flaw is
     */
    private void addChange(String change, String information) {
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
     * a breaking change is detected and thus the clients will struggle with the new version of the API
     * @param origin where the flaw is found
     * @param information what the flaw is
     */
    private void addBreakingChange(String origin, String information) {
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
     * a breaking change is detected and thus the clients will struggle with the new version of the API
     * @param origin where the flaw is found
     * @param observations what the flaws are
     */
    private void addBreaking(String origin, List<String> observations) {
        for (String observation: observations) {
            addBreakingChange(origin, observation);
        }
    }

    /**
     * a potentially breaking change is detected and thus some clients will likely struggle with the new version of the API
     * @param origin where the flaw is found
     * @param information what the flaw is
     */
    private void addPotentialBreakingChange(String origin, String information) {
        if (potentiallyBreaking.containsKey(origin)) {
            if (!potentiallyBreaking.get(origin).contains(information)) {
                potentiallyBreaking.get(origin).add(information);
            }
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            potentiallyBreaking.put(origin, originList);
        }
    }

    private void diffSchema(Response existing, Response future) {
        Property ep = existing.getSchema();
        Property fp = future.getSchema();
        if (ep instanceof RefProperty && fp instanceof RefProperty) {
            RefProperty rep = (RefProperty) ep;
            RefProperty rfp = (RefProperty) fp;
            String existingRef = rep.getSimpleRef();
            String futureRef = rfp.getSimpleRef();
            ElementDiff diff = new ElementDiff(existingDefinition, futureDefinition, existingRef, futureRef, futureRef + ".response.body");
            handleDiffAdded(futureRef, diff);
            handleDiffRemoved(futureRef, diff);
            handleDiffChanged(futureRef, diff);
            if (diff.getBreaking().size() > 0) {
                Map<String, String> breakingChanges = diff.getBreaking();
                for (Map.Entry<String, String> entry : breakingChanges.entrySet()) {
                    addBreakingChange(entry.getKey(), entry.getValue());
                }
            }
            if (diff.getPotentiallyBreaking().size() > 0) {
                Map<String, String> potentiallyBreakingChanges = diff.getPotentiallyBreaking();
                for (Map.Entry<String, String> entry : potentiallyBreakingChanges.entrySet()) {
                    addPotentialBreakingChange(entry.getKey(), entry.getValue());
                }
            }

        }
    }

    private void handleDiffChanged(String futureRef, ElementDiff diff) {
        if (diff.getChanged().size() > 0) {
            List<ScopedProperty> diffChanged = diff.getChanged();
            for (ScopedProperty change : diffChanged) {
                String origin = futureRef + "." + change.getEl();
                String cause = "body.property.changed." + diff.getChangeCause();
                addChange(origin, cause);
            }
        }
    }

    private void handleDiffRemoved(String futureRef, ElementDiff diff) {
        if (diff.getRemoved().size() > 0) {
            List<ScopedProperty> diffRemoved = diff.getAdded();
            for (ScopedProperty gone : diffRemoved) {
                String origin = futureRef + "." + gone.getEl();
                String cause = "body.property.removed." + diff.getChangeCause();
                addChange(origin, cause);
            }
        }
    }

    private void handleDiffAdded(String futureRef, ElementDiff diff) {
        if (diff.getAdded().size() > 0) {
            List<ScopedProperty> diffAdded = diff.getAdded();
            for (ScopedProperty appeared : diffAdded) {
                String origin = futureRef + "." + appeared.getEl();
                String cause = "body.property.added." + diff.getChangeCause();
                addChange(origin, cause);
            }
        }
    }

    /**
     * checks for the compliance towards the used response codes and their corresponding headers
     *
     * @param response a single response to be checked for compliance
     * @param method   the verb GET, POST, PUT, DELETE, PATCH
     * @return compliant or not
     */
    private boolean checkCompliance(String responseCode, Response response, HttpMethod method, boolean future, String scope) {
        Map<String, Boolean> required = getConcreteResponsesRequired(method);
        if (required.containsKey(responseCode)) {
            if (includeHeadersCheck) {
                return checkHeaders(response, method, responseCode, future, scope);
            } else {
                return true;
            }
        } else {
            if ("201".equals(responseCode) && (HttpMethod.POST.equals(method) || (HttpMethod.PUT.equals(method)))) {
                String key = future ? "future." : "existing." + scope + ".response." + responseCode + ".missing.for." + method;
                String msg = "to have a compliant and future proof API you should include " + responseCode +
                    " into the defined responses of a " + method + " in order for clients to be able to react on changes in the API";

                if (scope.contains("future")) {
                    List<String> observation = flawedDefines.containsKey(key) ? flawedDefines.get(key) : new ArrayList<>();
                    if (!observation.contains(msg)) {
                        observation.add(defaultHeaders.get(msg));
                        flawedDefines.put(key, observation);
                    }
                } else if (scope.contains("existing")) {
                    List<String> observation = existingFlaws.containsKey(key) ? existingFlaws.get(key) : new ArrayList<>();
                    if (!observation.contains(msg)) {
                        observation.add(defaultHeaders.get(msg));
                        existingFlaws.put(key, observation);
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * checks for the compliance towards the used response codes and their corresponding headers
     *
     * @param responses     a single response to be checked for compliance
     * @param method        the verb GET, POST, PUT, DELETE, PATCH
     * @param futureAPI     states whether the check is done on the future API or not
     * @param scope         the contextual scope for the reporting of this check
     * @param notObservedOK which response codes were not found to be ok in the check
     *
     * @return compliant or not
     */
    public boolean checkCompliance(Map<String, Response> responses, HttpMethod method,
                                   boolean futureAPI, String scope, List<String> notObservedOK) {
        Map<String, Boolean> required = getConcreteResponsesRequired(method);
        required.keySet().stream()
            .filter(observe -> responses.get(observe) != null)
            .forEach(responseCode -> {
                boolean ok = checkCompliance(responseCode, responses.get(responseCode), method, futureAPI, scope);
                required.put(responseCode, ok);
                if (!ok) {
                    notObservedOK.add(responseCode);
                }
            });
        notObservedOK.sort(Comparator.naturalOrder());
        return !required.containsValue(false);
    }

    private Map<String, Boolean> getConcreteResponsesRequired(HttpMethod method) {
        Map<String, Boolean> required = new HashMap<>(responsesRequired);
        if (!HttpMethod.POST.equals(method) && !HttpMethod.PUT.equals(method)) {
            required.remove("201");
        }
        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method)) {
            required.remove("200");
            required.remove("203");
            required.remove("304");
            required.remove("404");
        }
        return required;
    }

    /**
     * The findings here are potential improvements to the existing API that makes it easier to create the next version without the
     * client breaking, this is always a trade off between how much should be specified up-front and how much freedom is available from
     * the client side in order for you to move your API forward
     *
     * @return observed flaws in responses in the existing API
     */
    public Map<String, List<String>> getExistingFlaws() {
        return existingFlaws;
    }

    /**
     * @param code the concrete return code
     * @return the message written for that response code to explain its significance
     */
    public static String getCodeMsg(String code) {
        String result = CODES.get(code);
        return result != null ? result : "no additional info";
    }

    /**
     *
     * @param response the defined response
     * @param method GET, POST, PUT, ...
     * @param responseCode 200, 201, ...404,...500,...
     * @param future future api or current api
     * @param context the context for the check
     * @return true if headers were found ok
     */
    private Boolean checkHeaders(Response response, HttpMethod method, String responseCode, boolean future, String context) {
        boolean headersOK = true;
        Map<String, Property> responseHeaders = response.getHeaders();
        if (null == responseHeaders || responseHeaders.isEmpty()) return false;
        for (String header : defaultHeaders.keySet()) {
            if (!responseHeaders.containsKey(header)) {
                if (future) headersOK = false;
                recordDesignFlaw(method, responseCode, header, "default.header", future, context);
            }
        }
        if (specificHeaders.containsKey(responseCode)) {
            for (String header : specificHeaders.get(responseCode)) {
                if (!response.getHeaders().containsKey(header)) {
                    if (future) headersOK = false;
                    recordDesignFlaw(method, responseCode, header, "special.header", future, context);
                }
            }
        }
        return headersOK;
    }

    /**
     *
     * @param method GET, POST, PUT, DELETE, PATCH
     * @param responseCode 200,201, 202,...500, ..505
     * @param header Content-Type, X_Log-Token, ...
     * @param missing a container for responseCodes that were either not observed or were missing required headers
     * @param future future states that is was part of the future API, if not then the flaw is found in the existing API
     * @param context the context for the flaw - making the information valuable by placing it in a given context.
     */
    private void recordDesignFlaw(HttpMethod method, String responseCode, String header, String missing, boolean future, String context) {
        String localPreScope = context;
        if (!context.contains("future") && !context.contains("existing")) {
            String preScope = future ? "future." : "existing.";
            localPreScope = preScope + context;
        }
        String key = localPreScope + ".response." + responseCode + ".missing." + missing + "." + header + ".for." + method;
        if (localPreScope.contains("future")) {
            List<String> flaws = flawedDefines.containsKey(key) ? flawedDefines.get(key) : new ArrayList<>();
            if (!flaws.contains(HEADERS.get(header))) {
                flaws.add(HEADERS.get(header));
                flawedDefines.put(key, flaws);
            }
        } else if (localPreScope.contains("existing")) {
            List<String> flaws = existingFlaws.containsKey(key) ? existingFlaws.get(key) : new ArrayList<>();
            if (!flaws.contains(HEADERS.get(header))) {
                flaws.add(HEADERS.get(header));
                existingFlaws.put(key, flaws);
            }
        } else {
            logger.info("missed a flaw! - may be important {} ", key);
        }
    }

    private boolean isBreaking() {
        return !breaking.isEmpty();
    }

    private boolean isPotentiallyBreaking() {
        return !potentiallyBreaking.isEmpty();
    }

    private boolean anyChange() {
        return !changes.isEmpty() || !flawedDefines.isEmpty() || !existingFlaws.isEmpty();
    }

    /**
     * the response headers and what they are good for, is collected here.
     * this would be a //CANDIDATE: for a configurable setup
     */
    private void initHeadersInfo() {
        HEADERS.put("X-Log-Token", " X-Log-Token: allows the client side reference to activities and logging on the serverside, " +
            "if added to the Request as a header using the same name X-Log-Token it should be reused on the client side");
        HEADERS.put("Cache-Control", "Cache-Control: sets the boundaries for caching e.g. max-age=##### ");
        HEADERS.put("Expires", "Expires: sets the expiry time for the information retrieved in the response");
        HEADERS.put("ETag", "ETag: as long as the ETag is the same there is no need to get the value from a resource again");
        HEADERS.put("Last-Modified", "Last-Modified: this tells the client when the information was last updated and thus the client " +
            "knows the age of the information, the API needs to state explicit what parts are comprised is it the complete response that was" +
            " updated or could it be a small part only");
        HEADERS.put("Content-Type", "Content-Type: defines the content-type (e.g. application/json) including projection (concept) and " +
            "version (v)");
        HEADERS.put("Content-Encoding", "Content-Encoding: informs on the body - is it compressed or not e.g. gzip");
        HEADERS.put("X-RateLimit-Limit", "X-RateLimit-Limit: Request limit per minute");
        HEADERS.put("X-RateLimit-Limit-24h", "X-RateLimit-Limit-24h: Request limit per 24h");
        HEADERS.put("X-RateLimit-Remaining", "X-RateLimit-Remaining: Requests left for the domain/resource for the 24h (locally determined)");
        HEADERS.put("X-RateLimit-Reset", "X-RateLimit-Reset: The remaining window before the rate limit resets in UTC epoch seconds");
        HEADERS.put("Retry-After", "Retry-After: The time a client should wait before trying to fetch in epoch seconds");
        HEADERS.put("Location", "Location: The url where a resource can be found");
    }

    private void initCodeInfo() {
        CODES.put("202", "adding 202 Accepted breaks the client programming model and is not compatible");
        CODES.put("203", "adding 203 Non Authoritative means the client gets information from a different source than it anticipated and thus" +
            " the caching headers, modified since etc. may not be as valid as they are when information origins from the resource itself");
        CODES.put("301", "adding 301 Permanently Moved may break the client programming model, clients " +
            "may not be able to follow the location if not implemented and thus experience service as being down");
        CODES.put("304", "adding 304 Modified allows clients to know whether they need to re-get information, beware that the implementation " +
            "in the service must required less effort than the GET would have caused");
        CODES.put("307", "adding 307 Temporarily Moved may break the client programming model, clients " +
            "may not be able to follow the location if not implemented and thus experience service as being down");
        CODES.put("410", "adding 410 Gone is not harming the client, it merely tells the client that at some point after having moved " +
            "the resource to another place you will no longer receive 301's and when this happen a 410 be presented");
        CODES.put("429", "adding 429 Too Many Requests is not harming the client, clients may however perceive service as being poor");
        CODES.put("501", "adding 501 Not Implemented allows the service to publish parts of a service and deliver value for the " +
            "implemented parts ");

    }

    /**
     * initiates the default headers required for the response codes in order for having the potential for a future proof API
     * a word of caution: this is  opinionated
     *
     * this would be a //CANDIDATE: for a configurable setup
     */
    private void initHeaderSetup() {
        defaultHeaders.put("X-Log-Token", "the client side reference to activities and logging on the serverside, if added to the " +
            "Request as a header using the same name X-Log-Token it should be reused on the client side");

        List<String> headerSetup = new ArrayList<>();
        headerSetup.add("Content-Type"); //"the content-type (e.g. application/json) including projection (concept) and version (v)"
        headerSetup.add("Content-Encoding"); //"is it compressed or not e.g. gzip"
        headerSetup.add("Cache-Control"); // "sets the boundaries for caching e.g. max-age=##### "
        headerSetup.add("Expires"); // "sets the expiry time for the information retrieved in the response");
        headerSetup.add("ETag"); //"as long as the ETag is the same there is no need to get the value from a resource again");
        headerSetup.add("Last-Modified"); // "this tells the client when the information was last updated
        headerSetup.add("X-RateLimit-Limit"); // "Request limit per minute"
        headerSetup.add("X-RateLimit-Limit-24h"); // "Request limit per 24h"
        headerSetup.add("X-RateLimit-Remaining"); // "Requests left for the domain/resource/endpoint for the 24h (locally determined)"
        headerSetup.add("X-RateLimit-Reset"); // "The remaining window before the rate limit resets in UTC epoch seconds"


        specificHeaders.put("200", headerSetup);

        headerSetup = new ArrayList<>();
        headerSetup.add("Location");
        headerSetup.add("Content-Type"); //"the content-type (e.g. application/json) including projection (concept) and version (v)"
        headerSetup.add("Content-Encoding"); //"is it compressed or not e.g. gzip"
        headerSetup.add("Cache-Control"); // "sets the boundaries for caching e.g. max-age=##### "
        headerSetup.add("Expires"); // "sets the expiry time for the information retrieved in the response");
        headerSetup.add("ETag"); //"as long as the ETag is the same there is no need to get the value from a resource again");
        headerSetup.add("Last-Modified"); // "this tells the client when the information was last updated
        headerSetup.add("X-RateLimit-Limit"); // "Request limit per minute"
        headerSetup.add("X-RateLimit-Limit-24h"); // "Request limit per 24h"
        headerSetup.add("X-RateLimit-Remaining"); // "Requests left for the domain/resource/endpoint for the 24h (locally determined)"
        headerSetup.add("X-RateLimit-Reset"); // "The remaining window before the rate limit resets in UTC epoch seconds"
        specificHeaders.put("201", headerSetup);

        headerSetup = new ArrayList<>();
        headerSetup.add("Location");
        headerSetup.add("Retry-After");
        specificHeaders.put("202", headerSetup);

        headerSetup = new ArrayList<>();
        headerSetup.add("Location");
        headerSetup.add("Expires"); // "sets the expiry time for the information retrieved in the response");
        specificHeaders.put("301", headerSetup);
        specificHeaders.put("307", headerSetup);

        headerSetup = new ArrayList<>();
        headerSetup.add("Retry-After");
        specificHeaders.put("429", headerSetup);
        specificHeaders.put("503", headerSetup);
    }

    /**
     * initiates the complete set of required responses considered necessary in order to produce
     * a potential future proof API.
     *
     * a word of caution: this is  opinionated
     *
     * this would be a //CANDIDATE: for a configurable setup
     *
     * See the complete description of the return codes at:
     * https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
     */
    private void initRequiredResponses() {
        responsesRequired.put("200", false);  // response stating that everything was handled OK
        responsesRequired.put("201", false);  // response informing about a new item created at a given location
        responsesRequired.put("202", false);  // response stating the request accepted and a response will produced and be available
                                              // at a given location and time
        responsesRequired.put("203", false);  // the response returned is not authoritative and thus this is proxied from e.g. somewhere else
        //SUGGEST: - "potentially enable later" as responsesRequired.put("204", false); // the response does not contain any content in the body
        //SUGGEST: - "discuss whether 206 Partial Content should be enabled as default (interesting and meaningful, but as default)"
        //     - as responsesRequired.put("206", false); // the repose is only a partial portion of the complete response to the request
        responsesRequired.put("301", false);  // response informing about resource/endpoint has been moved permanently to other location
        responsesRequired.put("304", false);  // response informing about resource/endpoint has or has not been modified
        responsesRequired.put("307", false);  // response informing about resource/endpoint has been moved temporarily to other location

        responsesRequired.put("400", false);  // response informing about the receipt of a bad request
        responsesRequired.put("401", false);  // response stating that the request was unauthorized
        responsesRequired.put("403", false);  // response stating that the request was authorized, but no access was given to the resource
        responsesRequired.put("404", false);  // response stating that the requested resource was not found
        responsesRequired.put("410", false);  // response stating that the resource is gone (e.g. moved to an unknown place)
        responsesRequired.put("412", false);  // response stating that the request preconditions failed
        responsesRequired.put("415", false);  // response stating that the request asked for a content-type not supported
        responsesRequired.put("429", false);  // response stating that the clients need to back off as the resource is under load

        responsesRequired.put("500", false);  // response stating that the server failed
        responsesRequired.put("501", false); // response stating that the resource/endpoint is not yet implemented
        responsesRequired.put("503", false);  // response stating that the server/service is under heavy load
        responsesRequired.put("505", false);  // response states that request was using an unsupported version of HTTP
    }
}
