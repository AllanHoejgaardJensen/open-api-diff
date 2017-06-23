package dk.hoejgaard.openapi.diff.model;


import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EndpointTest {

    @Test
    public void testEndpointInstantiation() {
        Operation operation = new Operation();
        operation.setSummary("This is the summary");
        Endpoint ep = new Endpoint("/some/resource/url", HttpMethod.GET, operation);
        assertNotNull(ep);
        assertEquals(HttpMethod.GET, ep.getVerb());
        assertEquals(operation, ep.getOperation());
        assertEquals("This is the summary", ep.getSummary());
        assertEquals("/some/resource/url", ep.getPathUrl());
    }

}
