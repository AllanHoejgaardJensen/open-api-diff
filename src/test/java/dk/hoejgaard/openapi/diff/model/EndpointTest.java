package dk.hoejgaard.openapi.diff.model;


import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import org.junit.Test;


import static org.junit.Assert.assertNotNull;

public class EndpointTest {

    @Test
    public void testEndpointInstantiation() {
        Operation operation = new Operation();
        Endpoint ep = new Endpoint("/someresource", HttpMethod.GET, operation);
        assertNotNull(ep);
    }

}
