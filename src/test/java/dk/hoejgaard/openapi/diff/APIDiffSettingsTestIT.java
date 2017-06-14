package dk.hoejgaard.openapi.diff;

import java.util.List;

import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.model.Endpoint;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class APIDiffSettingsTestIT {
    private final String REFERENCE_API = "elaborate_example_v2c.json";
    private final String SUBJECT_API = "elaborate_example_v2d.json";


    @Test
    public void testAPIChangesVersionTripleMaturityNoneDiffAll() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL, Maturity.NONE, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(!changedEndPoints.isEmpty());
        assertEquals(13, changedEndPoints.size());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityNoneDiffBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.BREAKING, Maturity.NONE, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityNoneDiffPotentiallyBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.POTENTIALLY_BREAKING, Maturity.NONE, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityNoneDiffLaissezFaire() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.LAISSEZ_FAIRE, Maturity.NONE, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityLowDiffAll() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL, Maturity.LOW, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(!changedEndPoints.isEmpty());
        assertEquals(13, changedEndPoints.size());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityLowDiffBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.BREAKING, Maturity.LOW, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityLowDiffPotentiallyBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.POTENTIALLY_BREAKING, Maturity.LOW, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityLowDiffLaissezFaire() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.LAISSEZ_FAIRE, Maturity.LOW, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHATEOASDiffAll() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL, Maturity.HATEOAS, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(!changedEndPoints.isEmpty());
        assertEquals(13, changedEndPoints.size());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHATEOASDiffBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.BREAKING, Maturity.HATEOAS, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHATEOASDiffPotentiallyBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.POTENTIALLY_BREAKING, Maturity.HATEOAS, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHATEOASDiffLaissezFaire() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.LAISSEZ_FAIRE, Maturity.HATEOAS, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHALDiffAll() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL, Maturity.HAL, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(!changedEndPoints.isEmpty());
        assertEquals(13, changedEndPoints.size());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHALDiffBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.BREAKING, Maturity.HAL, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHALDiffPotentiallyBreaking() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.POTENTIALLY_BREAKING, Maturity.HAL, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testAPIChangesVersionTripleMaturityHALDiffLaissezFaire() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.LAISSEZ_FAIRE, Maturity.HAL, Versions.SINGLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

}