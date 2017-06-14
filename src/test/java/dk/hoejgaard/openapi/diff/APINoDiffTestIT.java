package dk.hoejgaard.openapi.diff;

import java.util.List;

import dk.hoejgaard.openapi.diff.model.Endpoint;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import org.junit.Test;


import static org.junit.Assert.*;

public class APINoDiffTestIT {

    @Test
    public void testUnchangedAPI() {
        APIDiff api = new APIDiff("elaborate_example_v2d.json", "elaborate_example_v2e.json", Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }
}
