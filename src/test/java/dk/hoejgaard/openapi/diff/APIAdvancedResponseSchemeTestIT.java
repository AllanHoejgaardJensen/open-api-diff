package dk.hoejgaard.openapi.diff;

import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.compare.OperationDiff;
import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import io.swagger.models.HttpMethod;
import org.junit.Test;


import static org.junit.Assert.*;

public class APIAdvancedResponseSchemeTestIT {

    @Test
    public void testAPICompliance() {
        APIDiff api = new APIDiff("elaborate_example_v2c.json", "elaborate_example_v2d.json", Diff.ALL, Maturity.FULL, Versions.SINGLE);
        assertEquals(0, api.getAddedEndpoints().size());
        assertEquals(0, api.getMissingEndpoints().size());
        List<ResourceDiff> observables = api.getAllDiffs();
        assertTrue(!observables.isEmpty());
        assertEquals(13, observables.size());

        ResourceDiff resourceCommon = getResourceDiffByPath(observables, "/accounts/{regNo}-{accountNo}");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/accounts");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/account-events");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/account-events/{category}/{id}");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/accounts/{regNo}-{accountNo}/transactions");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/accounts/{regNo}-{accountNo}/transactions/{id}");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/accounts/{regNo}-{accountNo}/reconciled-transactions");
        assertNotNull(resourceCommon);

        resourceCommon = getResourceDiffByPath(observables, "/accounts/{regNo}-{accountNo}/reconciled-transactions/{id}");
        assertNotNull(resourceCommon);

        ResourceDiff virtualAccount = getResourceDiffByPath(observables, "/virtual-accounts");
        assertNotNull(virtualAccount);

        virtualAccount = getResourceDiffByPath(observables, "/virtual-accounts/{regNo}-{accountNo}");
        assertNotNull(virtualAccount);

        ResourceDiff resourceNew = getResourceDiffByPath(observables, "/account-events-new-resource");
        assertNotNull(resourceNew);
        Map<HttpMethod, OperationDiff> nonCompliant = resourceNew.getNonCompliantOperations();
        assertEquals(0, nonCompliant.size());

    }

    private ResourceDiff getResourceDiffByPath(List<ResourceDiff> resourceDiffs, String path) {
        for (ResourceDiff resource : resourceDiffs) {
            if (resource.getPathUrl().equals(path)) {
                return resource;
            }
        }
        return null;
    }

}
