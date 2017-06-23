package dk.hoejgaard.openapi.diff.model;

/**
 * The content type is a simplified version of the HTTP specified Content Type
 * including parameters and not including quality parameters
 */
public class ContentType {

    private static final String JSON = "application/json";
    private static final String HAL_JSON = "application/hal\\+json";
    private static final String HAL_SCHEME = "^" + HAL_JSON + ";concept=(.*);v=[0-9]+";
    private static final String JSON_SCHEME = "^" + JSON + ";concept=(.*);v=[0-9]+";
    private static final String HAL_CONCEPT = "^((application\\/hal\\+json)+(, )?(;concept=[a-z][a-z0-9]+)?(;v=[0-9]+)?(, )*)+";
    private static final String JSON_CONCEPT = "^((application\\/json)+(, )?(;concept=[a-z][a-z0-9]+)?(;v=[0-9]+)?(, )*)+";

    private String type = "unknown-Content-type-type";
    private String subtype = "unknown-Content-type-subtype";
    private String asString = "unknown-Content-type-string";
    private String parameters = "";
    private String projection = "";
    private String version = "";
    private boolean isHALScheme;
    private boolean isJSONScheme;
    private boolean isHALConcept;
    private boolean isJSONConcept;
    private boolean isHAL;
    private boolean isJSON;

    public ContentType(String contentType) {
        asString = contentType;
        deriveType(contentType);
        deriveSubtype(contentType);
        deriveParameters(contentType);
        isHALScheme = asString.matches(HAL_SCHEME);
        isJSONScheme = asString.matches(JSON_SCHEME);
        isHALConcept = asString.matches(HAL_CONCEPT);
        isJSONConcept = asString.matches(JSON_CONCEPT);
        isHAL = asString.matches(HAL_JSON);
        isJSON = asString.matches(JSON);
    }

    /**
     * the type part of the content-type, this is:
     * <p>
     * {@code application} of e.g.  {@code application/json}
     * <p>
     *
     * @return the content-type type definition
     */
    public String getType() {
        return type;
    }

    /**
     * the subtype part of the content-type, this is: {@code hal+json} of e.g.  {@code application/hal+json}
     * <p>
     *
     * @return the content-type subtype definition
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * the parameter part of the content-type, this is:
     * <p>
     * {@code concept=projection;v=1} of e.g. {@code application/hal+json;concept=projection;v=1}
     * <p>
     *
     * @return the content-type parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * the version part of the content-type, this is: {@code "1" of e.g. application/hal+json;concept=projection;v=1}
     * <p>
     * the version part of the content-type, this is: {@code "" of e.g. application/hal+json;concept=projection}
     * <p>
     *
     * @return the content-type version
     */
    public String getVersion() {
        return version;
    }

    /**
     * the subtype part of the content-type, this is: {@code      "projection" of e.g. application/hal+json;concept=projection;v=1}
     *
     * <p>
     * note: if its a default projection the version part is a list entry with an empty version so:
     * {@code      application/hal+json;concept=projection}
     * <p>
     * results in a key "projection" having a list with one entry containing an empty version string
     * <p>
     * {@code application/hal+json;concept=projection, application/hal+json;concept=projection;v=1,application/hal+json;concept=projection;v=2}
     * <p>
     * results in a key "projection" having a list with three entries:
     * <p>
     * (1) containing an empty version string for the default, one entry with the number
     * <p>
     * (2) containing a version string with the number "1"
     * <p>
     * (3) containing a version string with the number "2"
     * <p>
     *
     * @return the content-type parameters
     */
    public String getProjection() {
        return projection;
    }


    /**
     * is the content type the default " always newest response please" e.g.
     * <p>
     * {@code "application/hal+json"}
     * <p>
     * or
     * <p>
     * {@code "application/json"}
     * <p>
     * @return true if default found
     */
    public boolean isDefaultContentType() {
        return isHAL || isJSON;
    }

    /**
     *  checks is format is e.g.
     *  <p>
     *  {@code "application/hal+json;concept=(projection);v=(version)"}
     *  <p>
     *    or
     *  <p>
     *  {@code "application/json;concept=(projection);v=(version)"}
     *  <p>
     * @return is the content type is complying to the projections and version scheme and having type "application" and
     * subtypes "hal+json" or "json"
     */
    public boolean isSchemeCompliant() {
        return isHALScheme || isJSONScheme;
    }

    /**
     *  checks is format is e.g.
     *  <p>
     *  {@code "application/hal+json or application/hal+json;concept=(projection) or application/hal+json;concept=(projection);v=(version)"}
     *  <p>
     *    or
     *  <p>
     *  {@code "application/json or application/json;concept=(projection) or application/json;concept=(projection);v=(version)"}
     *  <p>
     * @return is the content type is complying to the definitions on default projection*version tuple
     * specific projections and version scheme and having type "application" and subtypes "hal+json" or "json"
     */
    public boolean isJsonConceptCompliant() {
        return isHALConcept || isJSONConcept;
    }


    @Override
    public String toString() {
        return asString;
    }

    /**
     * simple check for whether string is JSON content type or not e.g. "application/json"
     * @param contentType the content-type to check
     * @return true of found to be json
     */
    public static boolean isJsonOnly(String contentType) {
        return contentType.matches(JSON) || contentType.matches(JSON_CONCEPT);
    }

    /**
     * simple check for whether string is JSON content type or not e.g. "application/json"
     * @param contentType the content-type to check
     * @return true of found to be json
     */
    public static boolean isHALJson(String contentType) {
        return contentType.matches(HAL_JSON) || contentType.matches(HAL_CONCEPT);
    }

    private void deriveType(String contentType) {
        int slash = contentType.indexOf("/");
        if (slash > 0) {
            type = contentType.substring(0, slash);
        }
    }

    private void deriveSubtype(String contentType) {
        int slash = contentType.indexOf("/");
        int parameter = contentType.indexOf(";");
        if (parameter < 0) {
            subtype = contentType.substring(slash + 1, contentType.length());
        } else {
            subtype = contentType.substring(slash + 1, parameter);
        }
    }

    private void deriveParameters(String contentType) {
        String[] params = contentType.split("concept=");
        for (String param : params) {
            if (!param.contains(type) && !param.contains(subtype)) {
                parameters = "concept=" + param;
                int versionStart = param.indexOf(';');
                if (versionStart > 0) {
                    projection = param.substring(0, versionStart);
                } else {
                    projection = param.substring(0, param.length());

                }
                int vs = param.indexOf(";v=");
                version = "";
                if (vs > 0) {
                    version = param.substring(vs + 3, param.length());
                }
            }
        }
    }
}
