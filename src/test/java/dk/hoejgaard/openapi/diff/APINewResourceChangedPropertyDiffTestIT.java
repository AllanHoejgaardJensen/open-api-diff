package dk.hoejgaard.openapi.diff;

import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.model.Endpoint;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.compare.OperationDiff;
import dk.hoejgaard.openapi.diff.compare.PropertyChanges;
import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import io.swagger.models.HttpMethod;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class APINewResourceChangedPropertyDiffTestIT {
    private APIDiff api;

    @Before
    public void readSpecs() {
        api = new APIDiff("elaborate_example_v2e.json", "elaborate_example_v3e.json", Diff.ALL, Maturity.FULL, Versions.SINGLE);
    }

    @Test
    public void testMissingResource() {
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        assertTrue(!missingEndpoints.isEmpty());
        Endpoint endpoint = missingEndpoints.get(0);
        assertEquals("/account-events-new-resource", endpoint.getPathUrl());
    }

    @Test
    public void testAddedResource() {
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        assertTrue(!newEndpoints.isEmpty());
        assertEquals(1, newEndpoints.size());
        Endpoint endpoint = newEndpoints.get(0);
        assertEquals("/accounts/{regNo}-{accountNo}/cards", endpoint.getPathUrl());
        assertEquals(HttpMethod.GET, endpoint.getVerb());
        assertNull(endpoint.getOperation().getConsumes());
        assertEquals(2, endpoint.getOperation().getProduces().size());
        assertEquals("application/hal+json", endpoint.getOperation().getProduces().get(0));
        assertEquals("application/hal+json;concept=cards;v=1", endpoint.getOperation().getProduces().get(1));
        assertEquals(6, endpoint.getOperation().getParameters().size());
        assertEquals("Accept", endpoint.getOperation().getParameters().get(0).getName());

    }

    @Test
    public void testChangedProperty() {
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(!changedEndPoints.isEmpty());
        assertEquals(1, changedEndPoints.size());
        assertNotNull(changedEndPoints.get(0));
        Map<HttpMethod, OperationDiff> changedOperations = changedEndPoints.get(0).getChangedOperations();
        assertEquals(1, changedOperations.size());
        OperationDiff theOperation = changedOperations.get(HttpMethod.PUT);
        assertNotNull(theOperation);
        assertNull(changedOperations.get(HttpMethod.GET));

        assertEquals(0, theOperation.getMissingProperties().size());
        assertEquals(0, theOperation.getAddedProperties().size());

        assertEquals(0, theOperation.getAddedParameters().size());
        assertEquals(0, theOperation.getMissingParameters().size());
        assertEquals(0, theOperation.getChangedParameters().size());

        assertEquals(0, theOperation.getAddedContentTypes().size());
        assertEquals(0, theOperation.getMissingContentTypes().size());

        assertEquals(0, theOperation.getAddedResponses().size());
        assertEquals(0, theOperation.getMissingResponses().size());
        assertEquals(0, theOperation.getChangedResponses().size());

        assertEquals(1, theOperation.getPotentiallyBreakingChanges().size());
        assertEquals(1, theOperation.getBreakingChanges().size());
        assertEquals(0, theOperation.getChanges().size());

        assertEquals(0, theOperation.getExistingCompliance().getChanges().size());

        List<PropertyChanges> changedProperties = theOperation.getChangedProperties();
        assertEquals(1, changedProperties.size());
        Map<String, List<String>> concretePropertyChanges = changedProperties.get(0).getChanges();
        assertTrue(concretePropertyChanges.containsKey("TransactionUpdate.body.currency"));
        assertNotNull(concretePropertyChanges.get("TransactionUpdate.body.currency"));
        assertEquals("body.property.changed.{body.currency.=[required.changed.from.false.to.true]}",
            concretePropertyChanges.get("TransactionUpdate.body.currency").get(0));
    }
}
