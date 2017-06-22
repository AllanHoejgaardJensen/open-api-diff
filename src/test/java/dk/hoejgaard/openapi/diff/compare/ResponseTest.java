package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.swagger.models.HttpMethod;
import io.swagger.models.Response;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class ResponseTest {

    @Test
    public void test200ResponseNonCompliance() {
        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);
        ResponseChanges rc = new ResponseChanges();
        List<String> observations = new ArrayList<>();
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));
        assertEquals(0, observations.size());
    }

    @Test
    public void test200ResponseCompliance() {
        List<String> requiredResponses = new ArrayList<>();
        requiredResponses.add("200");

        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);
        ResponseChanges rc = new ResponseChanges(requiredResponses);
        List<String> observations = new ArrayList<>();
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));
    }

    @Test
    public void test200ResponseComplianceIncludingHeaders() {
        List<String> requiredResponses = new ArrayList<>();
        requiredResponses.add("200");

        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);
        ResponseChanges rc = new ResponseChanges(requiredResponses, true);
        List<String> observations = new ArrayList<>();
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));
    }

    @Test
    public void test201ResponseCompliance() {
        List<String> requiredResponses = new ArrayList<>();
        requiredResponses.add("200");
        requiredResponses.add("201");

        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);
        ResponseChanges rc = new ResponseChanges(requiredResponses);
        List<String> observations = new ArrayList<>();
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));

        responses = new LinkedHashMap<>();
        responses.put("201", r);
        rc = new ResponseChanges(requiredResponses);
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PUT, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.POST, true, "scope", observations));

        responses = new LinkedHashMap<>();
        responses.put("200", r);
        rc = new ResponseChanges(requiredResponses);
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PUT, true, "scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.POST, true, "scope", observations));

        responses = new LinkedHashMap<>();
        responses.put("204", r);
        rc = new ResponseChanges(requiredResponses);
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.DELETE, true, "scope", observations));

        responses = new LinkedHashMap<>();
        responses.put("200", r);
        rc = new ResponseChanges(requiredResponses);
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.DELETE, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PATCH, true, "scope", observations));
    }

    @Test
    public void testFullResponseCompliance() {
        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r); responses.put("201", r); responses.put("202", r); responses.put("203", r); responses.put("204", r); responses.put("206", r);
        responses.put("301", r); responses.put("304", r); responses.put("307", r);
        responses.put("400", r); responses.put("401", r); responses.put("403", r); responses.put("404", r); responses.put("405", r);
        responses.put("410", r); responses.put("412", r); responses.put("415", r); responses.put("429", r);
        responses.put("500", r); responses.put("501", r); responses.put("503", r); responses.put("505", r);
        ResponseChanges rc = new ResponseChanges();
        List<String> observations = new ArrayList<>();
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PUT, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.POST, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.DELETE, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PATCH, true, "scope", observations));
    }

    @Test
    public void testFullDefaultResponseCompliance() {
        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r); responses.put("201", r); responses.put("202", r); responses.put("203", r); responses.put("204", r); responses.put("206", r);
        responses.put("301", r); responses.put("304", r); responses.put("307", r);
        responses.put("400", r); responses.put("401", r); responses.put("403", r);
        // 404 is not added for test purpose
        responses.put("405", r); responses.put("410", r); responses.put("412", r); responses.put("415", r); responses.put("429", r);
        responses.put("500", r); responses.put("501", r); responses.put("503", r); responses.put("505", r);
        ResponseChanges rc = new ResponseChanges();
        List<String> observations = new ArrayList<>();
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PUT, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.POST, true, "scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.DELETE, true, "scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PATCH, true, "scope", observations));
    }

    @Test
    public void testNonFullDefaultResponseCompliance() {
        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);
        // responses.put("201", r); is not added
        responses.put("202", r); responses.put("203", r); responses.put("204", r); responses.put("206", r);
        responses.put("301", r); responses.put("304", r); responses.put("307", r);
        responses.put("400", r); responses.put("401", r); responses.put("403", r);
        // 404 is not added for test purpose
        responses.put("405", r); responses.put("410", r); responses.put("412", r); responses.put("415", r); responses.put("429", r);
        responses.put("500", r); responses.put("501", r); responses.put("503", r); responses.put("505", r);
        ResponseChanges rc = new ResponseChanges();
        List<String> observations = new ArrayList<>();

        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PUT, true, "future", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.POST, true, "future", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PUT, true, "existing", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.POST, true, "existing", observations));

    }

    @Test
    public void testNonFullResponseCompliance() {
        Response r = new Response();
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);
        //201 is not added for test purpose
        responses.put("202", r); responses.put("203", r); responses.put("204", r); responses.put("206", r);
        responses.put("301", r); responses.put("304", r); responses.put("307", r);
        responses.put("400", r); responses.put("401", r); responses.put("403", r); responses.put("404", r); responses.put("405", r);
        responses.put("410", r); responses.put("412", r); responses.put("415", r); responses.put("429", r);
        responses.put("500", r); responses.put("501", r); responses.put("503", r); responses.put("505", r);
        ResponseChanges rc = new ResponseChanges();
        List<String> observations = new ArrayList<>();
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.GET, true, "scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PUT, true, "scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.POST, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.DELETE, true, "scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PATCH, true, "scope", observations));
    }

    @Test
    public void testFullResponseComplianceWithHeaders() {
        Response r = new Response();
        addDefaultResponseHeaders(r);

        Response r200 = new Response();

        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r200);
        addDefaultResponseHeaders(r200);
        addResponseHeader(r200, "Content-Type", "the content-type (e.g. application/json) including projection (concept) and version (v)");
        addResponseHeader(r200, "X-RateLimit-Limit", "Request limit per minute");
        addResponseHeader(r200, "X-RateLimit-Limit-24h", "Request limit per 24h");
        addResponseHeader(r200, "X-RateLimit-Remaining", "Requests left for the domain/resource for the 24h (locally determined)");
        addResponseHeader(r200, "X-RateLimit-Reset", "The remaining window before the rate limit resets in UTC epoch seconds");
        addResponseHeader(r200, "Content-Encoding", "is it compressed or not e.g. gzip");

        Response r201 = new Response();
        addDefaultResponseHeaders(r201);
        addResponseHeader(r201, "Location", "the location, where the created resource can be found");
        addResponseHeader(r201, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("201", r201);

        Response r202 = new Response();
        addDefaultResponseHeaders(r202);
        addResponseHeader(r202, "Location", "the location, where the created resource can be found");
        addResponseHeader(r202, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("202", r202);

        responses.put("203", r); responses.put("204", r); responses.put("206", r);

        Response r301 = new Response();
        addDefaultResponseHeaders(r301);
        addResponseHeader(r301, "Location", "the location, where the created resource can be found");
        responses.put("301", r301);
        responses.put("304", r);
        responses.put("307", r301);

        responses.put("400", r); responses.put("401", r); responses.put("403", r); responses.put("404", r); responses.put("405", r);
        responses.put("410", r); responses.put("412", r); responses.put("415", r);

        Response r429 = new Response();
        addDefaultResponseHeaders(r429);
        addResponseHeader(r429, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("429", r429);

        responses.put("500", r); responses.put("501", r);

        Response r503 = new Response();
        addDefaultResponseHeaders(r503);
        addResponseHeader(r503, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("503", r503);

        responses.put("505", r);
        ResponseChanges rc = new ResponseChanges(true);
        List<String> observations = new ArrayList<>();
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.GET, true,"scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PUT, true,"scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.POST, true,"scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.DELETE, true,"scope", observations));
        assertEquals(true, rc.checkCompliance(responses, HttpMethod.PATCH, true,"scope", observations));
    }

    @Test
    public void testNonFullResponseComplianceWithHeaders() {
        Response r = new Response();
        Map<String, String> defaultHeaders = new LinkedHashMap<>();
        defaultHeaders.put("Content-Type", "the content-type (e.g. application/json) including projection (concept) and version (v)");
        defaultHeaders.put("Cache-Control", "sets the boundaries for caching e.g. max-age=##### ");
        defaultHeaders.put("Expires", "sets the expiry time for the information retrieved in the response");
        defaultHeaders.put("Content-Encoding", "is it compressed or not e.g. gzip");
        defaultHeaders.put("ETag", "as long as the ETag is the same there is no need to get the value from a resource again");
        // missing the Last-Modified header
        for (String key : defaultHeaders.keySet()) {
            Property property = new StringProperty();
            property.setName(key);
            property.setRequired(true);
            property.setDescription(defaultHeaders.get(key));
            property.setAllowEmptyValue(false);
            r.addHeader(key, property);
        }
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);

        Response r201 = new Response();
        addDefaultResponseHeaders(r201);
        addResponseHeader(r201, "Location", "the location, where the created resource can be found");
        addResponseHeader(r201, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("201", r201);

        Response r202 = new Response();
        addDefaultResponseHeaders(r202);
        addResponseHeader(r202, "Location", "the location, where the created resource can be found");
        addResponseHeader(r202, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("202", r202);

        responses.put("203", r); responses.put("204", r); responses.put("206", r);

        Response r301 = new Response();
        addDefaultResponseHeaders(r301);
        addResponseHeader(r301, "Location", "the location, where the created resource can be found");
        responses.put("301", r301);

        responses.put("304", r);
        responses.put("307", r301);

        responses.put("400", r); responses.put("401", r); responses.put("403", r); responses.put("404", r); responses.put("405", r);
        responses.put("410", r); responses.put("412", r); responses.put("415", r);

        Response r429 = new Response();
        addDefaultResponseHeaders(r429);
        addResponseHeader(r429, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("429", r429);

        responses.put("500", r); responses.put("501", r);

        Response r503 = new Response();
        addDefaultResponseHeaders(r503);
        addResponseHeader(r503, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("503", r503);

        responses.put("505", r);
        ResponseChanges rc = new ResponseChanges(true);
        List<String> observations = new ArrayList<>();
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.GET, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PUT, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.POST, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.DELETE, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PATCH, true,"scope", observations));
    }

    @Test
    public void testAddedHeaders() {
        Response r = new Response();
        addDefaultResponseHeaders(r);
        Map<String, Response> responses = new LinkedHashMap<>();
        responses.put("200", r);

        Response r201 = new Response();
        addDefaultResponseHeaders(r201);
        addResponseHeader(r201, "Location", "the location, where the created resource can be found");
        addResponseHeader(r201, "Retry-After", "the time from now when the newly created resource can be expected");
        responses.put("201", r201);

        Response r201a = new Response();
        addDefaultResponseHeaders(r201a);
        addResponseHeader(r201a, "Location", "the location, where the created resource can be found");
        addResponseHeader(r201a, "Retry-After", "the time from now when the newly created resource can be expected");
        addResponseHeader(r201a, "X-Custom-Header", "added a custom header");
        responses.put("201", r201a);

        ResponseChanges rc = new ResponseChanges(true);
        assertEquals(0, rc.diff(r, r, "200", HttpMethod.GET).getBreaking().size());
        assertEquals(1, rc.diff(r201, r201a, "201", HttpMethod.GET).getBreaking().size());
        assertEquals(1, rc.diff(r201a, r201, "201", HttpMethod.GET).getBreaking().size());
        assertEquals(2, rc.diff(r201a, r201, "201", HttpMethod.PUT).getBreaking().size());

        List<String> observations = new ArrayList<>();
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.GET, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PUT, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.POST, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.DELETE, true,"scope", observations));
        assertEquals(false, rc.checkCompliance(responses, HttpMethod.PATCH, true,"scope", observations));
    }

    private void addDefaultResponseHeaders(Response response) {
        Map<String, String> defaultHeaders = new LinkedHashMap<>();
        defaultHeaders.put("Content-Type", "the content-type (e.g. application/json) including projection (concept) and version (v)");
        defaultHeaders.put("Cache-Control", "sets the boundaries for caching e.g. max-age=##### ");
        defaultHeaders.put("Expires", "sets the expiry time for the information retrieved in the response");
        defaultHeaders.put("Content-Encoding", "is it compressed or not e.g. gzip");
        defaultHeaders.put("ETag", "as long as the ETag is the same there is no need to get the value from a resource again");
        defaultHeaders.put("Last-Modified", "this tells the client when the information was last updated and thus the client knows the age of" +
            "the information, the API needs to state explicit what parts are comprised is it the complete response that was updated or could " +
            "it be a small part only");
        defaultHeaders.put("X-RateLimit-Limit", "Request limit per minute");
        defaultHeaders.put("X-RateLimit-Limit-24h", "Request limit per 24h");
        defaultHeaders.put("X-RateLimit-Remaining", "Requests left for the domain/resource for the 24h (locally determined)");
        defaultHeaders.put("X-RateLimit-Reset", "The remaining window before the rate limit resets in UTC epoch seconds");
        defaultHeaders.put("X-Log-Token", "the client side reference to activities and logging on the serverside, if added to the " +
            "Request as a header using the same name X-Log-Token it should be reused on the client side");

        for (String key : defaultHeaders.keySet()) {
            Property property = new StringProperty();
            property.setName(key);
            property.setRequired(true);
            property.setDescription(defaultHeaders.get(key));
            property.setAllowEmptyValue(false);
            response.addHeader(key, property);
        }
    }

    private void addResponseHeader(Response response, String name, String description) {
        Property property = new StringProperty();
        property.setName(name);
        property.setRequired(true);
        property.setDescription(description);
        property.setAllowEmptyValue(false);
        response.addHeader(name, property);
    }
}
