package dk.hoejgaard.openapi.diff.compare;

import java.util.LinkedHashMap;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResourceDiffTest {

    @Test
    public void testInstantiation() {
        ResourceDiff rd = new ResourceDiff(Diff.ALL, "some/resource");
        assertEquals("some/resource", rd.getPathUrl());
        assertTrue(rd.getChangedOperations().isEmpty());
        assertTrue(rd.getNonCompliantOperations().isEmpty());
        assertTrue(rd.getMissingOperations().isEmpty());
        assertTrue(rd.getNewOperations().isEmpty());
        assertTrue(rd.getChangedOperations().isEmpty());
    }

    @Test
    public void testNewOperation() {
        ResourceDiff rd = new ResourceDiff(Diff.ALL, "some/resource");
        Map<HttpMethod, Operation> newOperations = new LinkedHashMap<>();
        newOperations.put(HttpMethod.GET, new Operation());
        rd.setNewOperations(newOperations);
        assertEquals("some/resource", rd.getPathUrl());
        assertTrue(rd.getChangedOperations().isEmpty());
        assertTrue(rd.getNonCompliantOperations().isEmpty());
        assertTrue(rd.getMissingOperations().isEmpty());
        assertFalse(rd.getNewOperations().isEmpty());
        assertEquals(1, rd.getNewOperations().size());
        assertTrue(rd.getChangedOperations().isEmpty());
    }

    @Test
    public void testNewOperations() {
        ResourceDiff rd = new ResourceDiff(Diff.ALL, "some/resource");
        Map<HttpMethod, Operation> newOperations = new LinkedHashMap<>();
        newOperations.put(HttpMethod.GET, new Operation());
        newOperations.put(HttpMethod.PUT, new Operation());
        rd.setNewOperations(newOperations);
        assertEquals("some/resource", rd.getPathUrl());
        assertTrue(rd.getChangedOperations().isEmpty());
        assertTrue(rd.getNonCompliantOperations().isEmpty());
        assertTrue(rd.getMissingOperations().isEmpty());
        assertFalse(rd.getNewOperations().isEmpty());
        assertEquals(2, rd.getNewOperations().size());
        assertTrue(rd.getChangedOperations().isEmpty());
    }

    @Test
    public void testMissingOperation() {
        ResourceDiff rd = new ResourceDiff(Diff.ALL, "some/resource");
        Map<HttpMethod, Operation> missingOoperations = new LinkedHashMap<>();
        missingOoperations.put(HttpMethod.GET, new Operation());
        rd.setMissingOperations(missingOoperations);
        assertEquals("some/resource", rd.getPathUrl());
        assertTrue(rd.getChangedOperations().isEmpty());
        assertTrue(rd.getNonCompliantOperations().isEmpty());
        assertFalse(rd.getMissingOperations().isEmpty());
        assertEquals(1, rd.getMissingOperations().size());
        assertTrue(rd.getNewOperations().isEmpty());
        assertTrue(rd.getChangedOperations().isEmpty());
    }

    @Test
    public void testMissingOperations() {
        ResourceDiff rd = new ResourceDiff(Diff.ALL, "some/resource");
        Map<HttpMethod, Operation> missingOperations = new LinkedHashMap<>();
        missingOperations.put(HttpMethod.GET, new Operation());
        missingOperations.put(HttpMethod.PUT, new Operation());
        rd.setMissingOperations(missingOperations);
        assertEquals("some/resource", rd.getPathUrl());
        assertTrue(rd.getChangedOperations().isEmpty());
        assertTrue(rd.getNonCompliantOperations().isEmpty());
        assertFalse(rd.getMissingOperations().isEmpty());
        assertEquals(2, rd.getMissingOperations().size());
        assertTrue(rd.getNewOperations().isEmpty());
        assertTrue(rd.getChangedOperations().isEmpty());
    }
}
