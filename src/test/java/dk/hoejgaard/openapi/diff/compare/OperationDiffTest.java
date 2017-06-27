package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import org.junit.Test;


import static org.junit.Assert.*;

public class OperationDiffTest {

    @Test
    public void testCheckContentParadigmVersionSingle() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=3");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDual() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionForSingle() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=3");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionForDual() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTriple() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=5");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        common.add("application/hal+json;concept=account;v=4");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTriplePlus() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=6");
        added.add("application/hal+json;concept=account;v=5");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        common.add("application/hal+json;concept=account;v=4");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTripleMinus() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=6");
        added.add("application/hal+json;concept=account;v=7");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        removed.add("application/hal+json;concept=account;v=2");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=3");
        common.add("application/hal+json;concept=account;v=4");
        common.add("application/hal+json;concept=account;v=5");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionSingleMissingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionSingleMissingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDualMissingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertTrue(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionDualMissingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        removed.add("application/hal;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        assertTrue(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionSingleNothingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionSingleNothingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDoubleNothingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        assertTrue(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionDoubleNothingAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        assertTrue(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDoubleNothingAdded2() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        common.add("application/hal+json;concept=account;v=2");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionDoubleNothingAdded2() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        common.add("application/json;concept=accountupdate;v=2");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDoubleNothingAdded3() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionDoubleNothingAdded3() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        common.add("application/json;concept=accountupdate;v=2");
        common.add("application/json;concept=accountupdate;v=3");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTripleNothingAdded2() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        common.add("application/hal+json;concept=account;v=2");
        assertTrue(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionTripleNothingAdded2() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        common.add("application/json;concept=accountupdate;v=2");
        assertTrue(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTripleNothingAdded3() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentCosumerParadigmVersionTripleNothingAdded3() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        common.add("application/json;concept=accountupdate;v=2");
        common.add("application/json;concept=accountupdate;v=3");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTripleNothingAdded4() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        common.add("application/hal+json;concept=account;v=4");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionTripleNothingAdded4() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        common.add("application/json;concept=accountupdate;v=2");
        common.add("application/json;concept=accountupdate;v=3");
        common.add("application/json;concept=accountupdate;v=4");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionTripleNothingAdded5() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=1");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        common.add("application/hal+json;concept=account;v=4");
        common.add("application/hal+json;concept=account;v=6");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentConsumerParadigmVersionTripleNothingAdded5() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=1");
        common.add("application/json;concept=accountupdate;v=2");
        common.add("application/json;concept=accountupdate;v=3");
        common.add("application/json;concept=accountupdate;v=4");
        common.add("application/json;concept=accountupdate;v=6");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDoubleNonSequentialVersionAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=6");
        added.add("application/hal+json;concept=account;v=7");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertTrue(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckConsumerContentParadigmVersionDoubleNonSequentialVersionAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        added.add("application/json;concept=accountupdate;v=6");
        added.add("application/json;concept=accountupdate;v=7");
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        assertTrue(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionSingleSequentialVersionAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=5");
        added.add("application/hal+json;concept=account;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckConsumerContentParadigmVersionSingleSequentialVersionAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/json;concept=accountupdate;v=5");
        added.add("application/json;concept=accountupdate;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        common.add("application/json;concept=accountupdate;v=3");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionDualSequentialVersionAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=5");
        added.add("application/hal+json;concept=account;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        common.add("application/hal+json;concept=account;v=3");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckConsumerContentParadigmVersionDualSequentialVersionAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<String> added = new ArrayList<>();
        added.add("application/json;concept=accountupdate;v=5");
        added.add("application/json;concept=accountupdate;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        common.add("application/json;concept=accountupdate;v=3");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionSingleNonSequentialVersionsAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=3");
        added.add("application/hal+json;concept=account;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckConsumerContentParadigmVersionSingleNonSequentialVersionsAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/json;concept=accountupdate;v=3");
        added.add("application/json;concept=accountupdate;v=4");
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionSingleSequentialVersionsAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=3");
        added.add("application/hal+json;concept=account;v=5");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=2");
        assertFalse(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckConsumerContentParadigmVersionSingleSequentialVersionsAdded() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/json;concept=accountupdate;v=3");
        added.add("application/json;concept=accountupdate;v=5");
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=1");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=2");
        assertFalse(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckContentParadigmVersionSingleNonSequentialVersionRemoved() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/hal+json;concept=account;v=5");
        List<String> removed = new ArrayList<>();
        removed.add("application/hal+json;concept=account;v=2");
        List<String> common = new ArrayList<>();
        common.add("application/hal+json");
        common.add("application/hal+json;concept=account;v=4");
        assertTrue(od.checkContentProducerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testCheckConsumerContentParadigmVersionSingleNonSequentialVersionRemoved() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        List<String> added = new ArrayList<>();
        added.add("application/json;concept=accountupdate;v=5");
        List<String> removed = new ArrayList<>();
        removed.add("application/json;concept=accountupdate;v=2");
        List<String> common = new ArrayList<>();
        common.add("application/json");
        common.add("application/json;concept=accountupdate;v=4");
        assertTrue(od.checkContentConsumerParadigm(common, added, removed, "unit test"));
    }

    @Test
    public void testChangedContentTypes() {
        Operation opr = new Operation();
        List<String> producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        producers.add("application/hal+json;concept=account;v=2");
        producers.add("application/hal+json;concept=account;v=3");
        opr.setProduces(producers);
        List<String> consumers = new ArrayList<>();
        consumers.add("application/json");
        opr.setConsumes(consumers);

        Operation oprNew = new Operation();
        producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=2");
        producers.add("application/hal+json;concept=account;v=3");
        producers.add("application/hal+json;concept=account;v=4");
        oprNew.setProduces(producers);
        oprNew.setConsumes(consumers);
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        OperationDiff odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(1, odr.getAddedContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals(1, odr.getMissingContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));
        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertTrue(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());

        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertTrue(odr.isChanged());
        assertFalse(odr.isCompliant());

        od = new OperationDiff(Diff.BREAKING, Maturity.FULL, Versions.SINGLE);
        odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(1, odr.getAddedContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals(1, odr.getMissingContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));
        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertFalse(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());
        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertFalse(odr.isChanged());
        assertFalse(odr.isCompliant());

        od = new OperationDiff(Diff.POTENTIALLY_BREAKING, Maturity.FULL, Versions.SINGLE);
        odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(1, odr.getAddedContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals(1, odr.getMissingContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));

        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertTrue(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());
        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertTrue(odr.isChanged());
        assertFalse(odr.isCompliant());

        od = new OperationDiff(Diff.LAISSEZ_FAIRE, Maturity.FULL, Versions.SINGLE);
        odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(1, odr.getAddedContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals(1, odr.getMissingContentTypes().size());
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));

        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertTrue(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());
        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertTrue(odr.isChanged());
        assertFalse(odr.isCompliant());
    }

    @Test
    public void testCheckConsumerComplianceOK() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        Operation operation = new Operation();
        List<String> producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        operation.setProduces(producers);
        List<String> consumers = new ArrayList<>();
        consumers.add("application/json");
        operation.setConsumes(consumers);
        od.checkVersionCompliance(operation, HttpMethod.PUT, true, "future");

        assertTrue(od.getDesignFlaws().isEmpty());
        assertTrue(od.getChanges().isEmpty());
        assertTrue(od.getBreakingChanges().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getBreakingChanges().isEmpty());
    }

    @Test
    public void testCheckConsumerComplianceHALisNotOK() {
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        Operation operation = new Operation();
        List<String> producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        operation.setProduces(producers);
        List<String> consumers = new ArrayList<>();
        consumers.add("application/hal+json");
        operation.setConsumes(consumers);
        od.checkVersionCompliance(operation, HttpMethod.PUT, true, "future");

        assertFalse(od.getDesignFlaws().isEmpty());
        assertTrue(od.getChanges().isEmpty());
        assertTrue(od.getBreakingChanges().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getBreakingChanges().isEmpty());

        od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        operation = new Operation();
        producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        operation.setProduces(producers);
        consumers = new ArrayList<>();
        consumers.add("application/hal+json");
        operation.setConsumes(consumers);
        od.checkVersionCompliance(operation, HttpMethod.PUT, false, "existing");

        assertTrue(od.getDesignFlaws().isEmpty());
        assertTrue(od.getChanges().isEmpty());
        assertTrue(od.getBreakingChanges().isEmpty());
        assertFalse(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getBreakingChanges().isEmpty());
    }

    @Test
    public void testMissingContentTypesSingle() {
        Operation opr = new Operation();
        List<String> producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        producers.add("application/hal+json;concept=account;v=2");
        producers.add("application/hal+json;concept=account;v=3");
        opr.setProduces(producers);
        List<String> consumers = new ArrayList<>();
        consumers.add("application/json;concept=accountupdate;v=3");
        consumers.add("application/json");
        opr.setConsumes(consumers);

        Operation oprNew = new Operation();
        producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=4");
        oprNew.setProduces(producers);

        consumers = new ArrayList<>();
        consumers.add("application/json;concept=accountupdate;v=4");
        consumers.add("application/json");
        oprNew.setConsumes(consumers);
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        OperationDiff odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(2, odr.getAddedContentTypes().size()); //1 consumer and 1 producer added
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals("application/json;concept=accountupdate;v=4", odr.getAddedContentTypes().get(1));
        assertEquals(4, odr.getMissingContentTypes().size()); //3 producers and 1 consumer
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));
        assertEquals("application/hal+json;concept=account;v=2", odr.getMissingContentTypes().get(1));
        assertEquals("application/hal+json;concept=account;v=3", odr.getMissingContentTypes().get(2));
        assertEquals("application/json;concept=accountupdate;v=3", odr.getMissingContentTypes().get(3));
        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertTrue(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());

        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertTrue(odr.isChanged());
        assertFalse(odr.isCompliant());

        assertEquals(3, od.getChanges().size());
        assertEquals(3, od.getPotentiallyBreakingChanges().size());
    }

    @Test
    public void testMissingContentTypesDouble() {
        Operation opr = new Operation();
        List<String> producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        producers.add("application/hal+json;concept=account;v=2");
        producers.add("application/hal+json;concept=account;v=3");
        opr.setProduces(producers);
        List<String> consumers = new ArrayList<>();
        consumers.add("application/json;concept=accountupdate;v=3");
        consumers.add("application/json");
        opr.setConsumes(consumers);

        Operation oprNew = new Operation();
        producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=4");
        oprNew.setProduces(producers);

        consumers = new ArrayList<>();
        consumers.add("application/json;concept=accountupdate;v=4");
        consumers.add("application/json");
        oprNew.setConsumes(consumers);
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        OperationDiff odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(2, odr.getAddedContentTypes().size()); //1 consumer and 1 producer added
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals("application/json;concept=accountupdate;v=4", odr.getAddedContentTypes().get(1));
        assertEquals(4, odr.getMissingContentTypes().size()); //3 producers and 1 consumer
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));
        assertEquals("application/hal+json;concept=account;v=2", odr.getMissingContentTypes().get(1));
        assertEquals("application/hal+json;concept=account;v=3", odr.getMissingContentTypes().get(2));
        assertEquals("application/json;concept=accountupdate;v=3", odr.getMissingContentTypes().get(3));
        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertTrue(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());

        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertTrue(odr.isChanged());
        assertFalse(odr.isCompliant());

        assertEquals(3, od.getChanges().size());
        assertEquals(3, od.getPotentiallyBreakingChanges().size());
    }
    @Test
    public void testMissingContentTypesTriple() {
        Operation opr = new Operation();
        List<String> producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=1");
        producers.add("application/hal+json;concept=account;v=2");
        producers.add("application/hal+json;concept=account;v=3");
        opr.setProduces(producers);
        List<String> consumers = new ArrayList<>();
        consumers.add("application/json;concept=accountupdate;v=3");
        consumers.add("application/json");
        opr.setConsumes(consumers);

        Operation oprNew = new Operation();
        producers = new ArrayList<>();
        producers.add("application/hal+json");
        producers.add("application/hal+json;concept=account;v=4");
        oprNew.setProduces(producers);

        consumers = new ArrayList<>();
        consumers.add("application/json;concept=accountupdate;v=4");
        consumers.add("application/json");
        oprNew.setConsumes(consumers);
        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.TRIPLE);
        OperationDiff odr = od.getChangedContentTypes(opr, oprNew, "unit test");
        assertEquals(2, odr.getAddedContentTypes().size()); //1 consumer and 1 producer added
        assertEquals("application/hal+json;concept=account;v=4", odr.getAddedContentTypes().get(0));
        assertEquals("application/json;concept=accountupdate;v=4", odr.getAddedContentTypes().get(1));
        assertEquals(4, odr.getMissingContentTypes().size()); //3 producers and 1 consumer
        assertEquals("application/hal+json;concept=account;v=1", odr.getMissingContentTypes().get(0));
        assertEquals("application/hal+json;concept=account;v=2", odr.getMissingContentTypes().get(1));
        assertEquals("application/hal+json;concept=account;v=3", odr.getMissingContentTypes().get(2));
        assertEquals("application/json;concept=accountupdate;v=3", odr.getMissingContentTypes().get(3));
        assertTrue(od.isDiff());
        assertFalse(od.isBroke());
        assertTrue(od.isChanged());
        assertTrue(od.isPotentiallyBroke());
        assertFalse(od.isCompliant());

        assertTrue(odr.isDiff());
        assertFalse(odr.isBroke());
        assertTrue(odr.isPotentiallyBroke());
        assertTrue(odr.isChanged());
        assertFalse(odr.isCompliant());

        assertEquals(3, od.getChanges().size());
        assertEquals(3, od.getPotentiallyBreakingChanges().size());
    }

    @Test
    public void testAddParameter() {
        Operation opr = new Operation();
        assertTrue(opr.getParameters().isEmpty());

        Operation oprNew = new Operation();
        Parameter parameter = new HeaderParameter();
        parameter.setName("newParameter");
        oprNew.addParameter(parameter);
        assertEquals(1, oprNew.getParameters().size());

        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        assertEquals(1, od.getAddedParameters(opr, oprNew).size());
        assertTrue(od.getRemovedParameters(opr, oprNew).isEmpty());
        assertTrue(od.getCommonParameters(opr, oprNew).isEmpty());
        assertEquals("newParameter", od.getAddedParameters(opr, oprNew).get(0).getName());

    }

    @Test
    public void testAddParameters() {
        Parameter parameter = new HeaderParameter();
        parameter.setName("existingParameter");
        Operation opr = new Operation();
        opr.addParameter(parameter);
        assertEquals(1, opr.getParameters().size());

        Operation oprNew = new Operation();
        oprNew.addParameter(parameter);
        Parameter headerParameter = new HeaderParameter();
        headerParameter.setName("newParameter");
        oprNew.addParameter(headerParameter);
        assertEquals(2, oprNew.getParameters().size());

        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        assertEquals(1, od.getAddedParameters(opr, oprNew).size());
        assertTrue(od.getRemovedParameters(opr, oprNew).isEmpty());
        assertEquals(1, od.getCommonParameters(opr, oprNew).size());
        assertEquals("newParameter", od.getAddedParameters(opr, oprNew).get(0).getName());
    }

    @Test
    public void testRemoveParameter() {
        Parameter parameter = new HeaderParameter();
        parameter.setName("existingParameter");
        Operation opr = new Operation();
        opr.addParameter(parameter);
        assertEquals(1, opr.getParameters().size());

        Operation oprNew = new Operation();
        Parameter headerParameter = new HeaderParameter();
        headerParameter.setName("newParameter");
        oprNew.addParameter(headerParameter);
        assertEquals(1, oprNew.getParameters().size());

        OperationDiff od = new OperationDiff(Diff.ALL, Maturity.FULL, Versions.SINGLE);
        assertEquals(1, od.getAddedParameters(opr, oprNew).size());
        assertEquals(1, od.getRemovedParameters(opr, oprNew).size());
        assertTrue(od.getCommonParameters(opr, oprNew).isEmpty());
        assertEquals("newParameter", od.getAddedParameters(opr, oprNew).get(0).getName());
    }

    @Test
    public void testAddResponse() {
        Response resp = new Response();
        Operation opr = new Operation();
        opr.addResponse("200", resp);
        opr.addResponse("404", resp);
        assertEquals(2, opr.getResponses().size());

        Response respNew = new Response();
        Operation oprNew = new Operation();
        oprNew.addResponse("200", respNew);
        oprNew.addResponse("404", respNew);
        Response resp202 = new Response();
        oprNew.addResponse("202", resp202);
        assertEquals(3, oprNew.getResponses().size());

        Swagger swagger = new Swagger();
        Map<String, Model> definitions = new LinkedHashMap<>();
        Model sample = new RefModel();
        Map<String, Property> pSample = new LinkedHashMap<>();
        sample.setProperties(pSample);
        definitions.put("sample", sample);
        swagger.setDefinitions(definitions);
        OperationDiff od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od = od.getChangedResponses(opr, oprNew, HttpMethod.GET);
        assertEquals(1, od.getAddedResponses().size());
        assertFalse(od.getBreakingChanges().isEmpty());
        assertFalse(od.getDesignFlaws().isEmpty());
        assertEquals(1, od.getBreakingChanges().size()); //202 must be an API breaking change

        OperationDiff odb = new OperationDiff(swagger, swagger, Diff.BREAKING, Maturity.FULL, Versions.SINGLE);
        odb = odb.getChangedResponses(opr, oprNew, HttpMethod.GET);
        assertEquals(1, odb.getAddedResponses().size());
        assertFalse(odb.getBreakingChanges().isEmpty());
        assertTrue(odb.getDesignFlaws().isEmpty());
        assertEquals(1, odb.getBreakingChanges().size()); //202 must be an API breaking change

        OperationDiff odbl = new OperationDiff(swagger, swagger, Diff.BREAKING, Maturity.LOW, Versions.SINGLE);
        odbl = odbl.getChangedResponses(opr, oprNew, HttpMethod.GET);
        assertEquals(1, odbl.getAddedResponses().size());
        assertFalse(odbl.getBreakingChanges().isEmpty());
        assertTrue(odbl.getDesignFlaws().isEmpty());

        OperationDiff odpb = new OperationDiff(swagger, swagger, Diff.POTENTIALLY_BREAKING, Maturity.LOW, Versions.SINGLE);
        odpb = odpb.getChangedResponses(opr, oprNew, HttpMethod.GET);
        assertEquals(1, odpb.getAddedResponses().size());
        assertTrue(odpb.getPotentiallyBreakingChanges().isEmpty());
        assertTrue(odpb.getDesignFlaws().isEmpty());
        assertTrue(odpb.getBreakingChanges().isEmpty()); //202 must be an API breaking change, but is not detected
    }

    @Test
    public void testResponseCompliance() {
        Response resp = new Response();
        Operation opr = new Operation();
        opr.addResponse("200", resp);
        opr.addResponse("404", resp);
        assertEquals(2, opr.getResponses().size());
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("X-Log-Token");
        opr.addParameter(parameter);

        Response respNew = new Response();
        Operation oprNew = new Operation();
        oprNew.addResponse("200", respNew);
        oprNew.addResponse("404", respNew);
        assertEquals(2, oprNew.getResponses().size());

        oprNew.addParameter(parameter);

        Swagger swagger = new Swagger();
        Map<String, Model> definitions = new LinkedHashMap<>();
        Model sample = new RefModel();
        Map<String, Property> pSample = new LinkedHashMap<>();
        sample.setProperties(pSample);
        definitions.put("sample", sample);
        swagger.setDefinitions(definitions);
        OperationDiff od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);

        od = od.getChangedResponses(opr, oprNew, HttpMethod.GET);
        assertTrue(od.getAddedResponses().isEmpty());
        assertTrue(od.getBreakingChanges().isEmpty());
        assertFalse(od.getDesignFlaws().isEmpty());
        assertTrue(od.getBreakingChanges().isEmpty());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od.checkResponseCompliance(opr, HttpMethod.GET, false, "existing");
        assertTrue(od.getDesignFlaws().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertFalse(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od.checkResponseCompliance(oprNew, HttpMethod.GET, true, "future");
        assertFalse(od.getDesignFlaws().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od.checkResponseCompliance(oprNew, HttpMethod.PUT, true, "future");
        assertFalse(od.getDesignFlaws().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());
    }
    @Test
    public void testRemovedResponseCompliance() {
        Response resp = new Response();
        Operation opr = new Operation();
        opr.addResponse("200", resp);
        opr.addResponse("301", resp);
        opr.addResponse("307", resp);
        opr.addResponse("415", resp);
        opr.addResponse("429", resp);
        opr.addResponse("500", resp);
        assertEquals(6, opr.getResponses().size());
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("X-Log-Token");
        opr.addParameter(parameter);

        Response respNew = new Response();
        Operation oprNew = new Operation();
        oprNew.addResponse("200", respNew);
        oprNew.addResponse("404", respNew);
        assertEquals(2, oprNew.getResponses().size());

        oprNew.addParameter(parameter);

        Swagger swagger = new Swagger();
        Map<String, Model> definitions = new LinkedHashMap<>();
        Model sample = new RefModel();
        Map<String, Property> pSample = new LinkedHashMap<>();
        sample.setProperties(pSample);
        definitions.put("sample", sample);
        swagger.setDefinitions(definitions);
        OperationDiff od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);

        od = od.getChangedResponses(opr, oprNew, HttpMethod.GET);
        assertFalse(od.getAddedResponses().isEmpty());
        assertFalse(od.getBreakingChanges().isEmpty());
        assertFalse(od.getDesignFlaws().isEmpty());
        assertFalse(od.getBreakingChanges().isEmpty());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od.checkResponseCompliance(opr, HttpMethod.GET, false, "existing");
        assertTrue(od.getDesignFlaws().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertFalse(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od.checkResponseCompliance(oprNew, HttpMethod.GET, true, "future");
        assertFalse(od.getDesignFlaws().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        od.checkResponseCompliance(oprNew, HttpMethod.PUT, true, "future");
        assertFalse(od.getDesignFlaws().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());
    }

    @Test
    public void testRequestHeaderCompliance() {
        Swagger swagger = new Swagger();
        Map<String, Model> definitions = new LinkedHashMap<>();
        Model sample = new RefModel();
        Map<String, Property> pSample = new LinkedHashMap<>();
        sample.setProperties(pSample);
        definitions.put("sample", sample);
        swagger.setDefinitions(definitions);
        OperationDiff od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);

        Operation operation = new Operation();
        HeaderParameter parameter = new HeaderParameter();
        parameter.setName("X-Log-Token");
        operation.addParameter(parameter);
        parameter.setName("Accept");
        operation.addParameter(parameter);
        od.checkRequestHeaderCompliance(operation, true, "future");
        assertFalse(od.getDesignFlaws().isEmpty());
        assertFalse(od.getChanges().isEmpty());
        assertFalse(od.getBreakingChanges().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertTrue(od.getExistingCompliance().getImprovements().isEmpty());
        assertTrue(od.getExistingCompliance().getChanges().isEmpty());

        assertTrue(od.isBroke());
        assertFalse(od.isPotentiallyBroke());
        assertTrue(od.isDiff());
        assertTrue(od.isChanged());
        assertFalse(od.isCompliant());

        od = new OperationDiff(swagger, swagger, Diff.ALL, Maturity.FULL, Versions.SINGLE);
        operation = new Operation();
        parameter = new HeaderParameter();
        parameter.setName("X-Log-Token");
        operation.addParameter(parameter);
        parameter.setName("Accept");
        operation.addParameter(parameter);
        od.checkRequestHeaderCompliance(operation, false, "existing");
        assertTrue(od.getDesignFlaws().isEmpty());
        assertTrue(od.getChanges().isEmpty());
        assertTrue(od.getBreakingChanges().isEmpty());
        assertTrue(od.getExistingFlaws().isEmpty());
        assertFalse(od.getExistingCompliance().getBreakingChanges().isEmpty());
    }
}
