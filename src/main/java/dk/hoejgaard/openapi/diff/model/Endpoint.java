package dk.hoejgaard.openapi.diff.model;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;

/**
 * an Endpoint is a particular a target address which can be reached unambiguously by a URL as a part of an API.
 * <p>
 * The understanding of endpoint used is a representation of an URL target used as a part of a semantic REST API.
 * <p>
 * The API consists of a set of endpoint that are documented in the form of an API doc. These are swagger.json or openapi.json documented APIs.
 * <p>
 * The maturity of the API can go to the highest level of maturity with HATEOAS and versions of content and versions of structure.
 * <p>
 * An Endpoint has an:
 * <ol>
 * <li>address,</li>
 * <li>
 * accepts a number of parameters,
 * </li>
 * <li>supports a number of capabilities,</li>
 * <li>has one verb associated (GET, PUT, DELETE, POST, PATCH) with it and</li>
 * <li>and supports a number of content-types (type/subtype;concept=&lt;projection&gt;v=&lt;version&gt;)
 * - e.g. (application/hal+json;concept=accountsparse;v=3) </li>
 * <li>supports and emits a number of response codes</li>
 * </ol>
 * <p>
 * The projections (potential editions of json in the response) and their versions are determined by a content-type parameters.
 * All the things above must be taken into consideration, to know what the expected contract is between the users of an API and
 * the mechanics of the API. The response codes are taken into consideration because a REST api can use e.g. 301 to move information
 * from one place to another, either within same local API or to other remote API and that should result in a warning, but it does
 * not break the contract if the clients are tolerant (and wise) readers that can react to this change.
 * If a principle like the one shown in the (https://github.com/Nykredit/HATEOAS - branch:allegedContentTypeSupport) is used
 * the methods behind the API can be deprecated, but it will not do much good for the client, thus this needs to be documented
 * in the text.
 */
public class Endpoint {

    private String pathUrl;
    private HttpMethod verb;
    private Operation operation;

    public Endpoint(String pathUrl, HttpMethod verb, Operation operation) {
        this.pathUrl = pathUrl;
        this.verb = verb;
        this.operation = operation;
    }

    /**
     * @return the url for the endpoint
     */
    public String getPathUrl() {
        return pathUrl;
    }

    /**
     * @return the verb e.g GET, POST, PUT, DELETE, PATCH
     */
    public HttpMethod getVerb() {
        return verb;
    }

    /**
     * @return a summarized description of the endpoint (currently derived from Operation)
     */
    public String getSummary() {
        return operation.getSummary();
    }

    /**
     * @return the operation (defined by Swagger 2)
     */
    public Operation getOperation() {
        return operation;
    }

}
