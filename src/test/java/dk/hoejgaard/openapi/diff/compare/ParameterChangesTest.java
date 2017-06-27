package dk.hoejgaard.openapi.diff.compare;

import java.math.BigDecimal;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import org.junit.Test;


import static org.junit.Assert.*;

public class ParameterChangesTest {

    @Test
    public void testContainsDiffFlawAll(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.ALL);
        assertFalse(ps.containsDiff());
        ps.addDefinitionFlaw("flaw", "a simulated flaw");
        assertTrue(ps.containsDiff());
    }

    @Test
    public void testContainsDiffChangeAll(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.ALL);
        assertFalse(ps.containsDiff());
        ps.addRecordedChange("simulated", "a simulated change");
        assertTrue(ps.containsDiff());
        assertEquals(existing, ps.getExisting());
        assertEquals(future, ps.getFuture());
    }

    @Test
    public void testContainsDiffBreakingOnChange(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.BREAKING);
        assertFalse(ps.containsDiff());
        ps.addDefinitionFlaw("flaw", "a simulated flaw");
        assertFalse(ps.containsDiff());
        ps.addPotentialBreakingChange("potentialbreak", "a simulated potential break");
        assertFalse(ps.containsDiff());
        ps.addRecordedChange("change", "a simulated change");
        assertTrue(ps.containsDiff());
    }

    @Test
    public void testContainsDiffBreakingOnBreaking(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.BREAKING);
        assertFalse(ps.containsDiff());
        ps.addDefinitionFlaw("flaw", "a simulated flaw");
        assertFalse(ps.containsDiff());
        ps.addPotentialBreakingChange("potentialbreak", "a simulated potential break");
        assertFalse(ps.containsDiff());
        ps.addBreakingChange("breaking", "a simulated breaking change");
        assertTrue(ps.containsDiff());
    }

    @Test
    public void testContainsDiffPotentiallyBreakingOnChange(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.POTENTIALLY_BREAKING);
        assertFalse(ps.containsDiff());
        ps.addDefinitionFlaw("flaw", "a simulated flaw");
        assertFalse(ps.containsDiff());
        ps.addPotentialBreakingChange("potentialbreak", "a simulated potential break");
        assertTrue(ps.containsDiff());
    }

    @Test
    public void testContainsDiffPotentiallyBreakingOnBreak(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.POTENTIALLY_BREAKING);
        assertFalse(ps.containsDiff());
        ps.addDefinitionFlaw("flaw", "a simulated flaw");
        assertFalse(ps.containsDiff());
        ps.addPotentialBreakingChange("potentialbreak", "a simulated potential break");
        assertTrue(ps.containsDiff());
    }

    @Test
    public void testContainsDiffLF(){
        Parameter existing = new HeaderParameter();
        existing.setName("testParameter");
        Parameter future = new HeaderParameter();
        existing.setName("testParameter");
        ParameterChanges ps = new ParameterChanges(existing, future, Diff.LAISSEZ_FAIRE);
        assertFalse(ps.containsDiff());
        ps.addDefinitionFlaw("flaw", "a simulated flaw");
        assertFalse(ps.containsDiff());
        ps.addPotentialBreakingChange("potentialbreak", "a simulated potential break");
        assertFalse(ps.containsDiff());
        ps.addBreakingChange("break", "a simulated break");
        assertTrue(ps.containsDiff());
    }

    @Test
    public void testCookieParameter() {
        CookieParameter cp = new CookieParameter();
        cp.setName("preference");
        cp.setType("string");
        cp.setDescription("a persons profile preferences");
        cp.setPattern("(a-zA-Z0-9){50}");
        cp.setMaxLength(50);
        cp.setRequired(false);
        cp.setAllowEmptyValue(false);

        CookieParameter fdp = new CookieParameter();
        fdp.setName("preference");
        fdp.setType("string");
        fdp.setDescription("a persons profile preferences");
        fdp.setPattern("(a-zA-Z0-9){255}");
        fdp.setRequired(false);
        fdp.setMaxLength(255);
        fdp.setAllowEmptyValue(true);
        ParameterChanges changes = new ParameterChanges(cp, fdp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());

        assertEquals(3, changes.getPotentiallyBreaking().get("cookie.preference").size());
        assertEquals(3, changes.getChanges().get("cookie.preference").size());
        assertTrue(changes.getFlawedDefines().isEmpty());

        fdp.setMaxLength(30);
        changes = new ParameterChanges(cp, fdp, Diff.ALL);
        assertEquals(1, changes.getBreaking().size());
    }

    @Test
    public void testPathParameter() {
        PathParameter param = new PathParameter();
        param.setName("testPathParam");
        param.setDescription("a simple test pathParam for test purposes");
        param.setPattern("[0-9]{3}");
        param.setMinimum(new BigDecimal(333));
        param.setMaximum(new BigDecimal(666));
        param.setMinLength(3);
        param.setMaxLength(3);
        param.setAllowEmptyValue(false);
        param.setDefault("444");
        param.setFormat("format");
        param.setAccess("public");
        param.setRequired(true);
        param.setType("string");
        ParameterChanges changes = new ParameterChanges(param, param, Diff.ALL);

        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertFalse(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());
        assertFalse(changes.isMaximumChanged());
        assertFalse(changes.isMinimumChanged());
        assertFalse(changes.isMinimumLengthChanged());
        assertFalse(changes.isMaximumLengthChanged());
        assertFalse(changes.isPatternChanged());
        assertFalse(changes.isAllowEmptyChanged());
        assertFalse(changes.isDefaultChanged());
        assertFalse(changes.isTypeChanged());


        PathParameter newParam = new PathParameter();
        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");

        changes = new ParameterChanges(param, newParam, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());

        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());

        assertTrue(changes.isTypeChanged());
        assertTrue(changes.isMinimumLengthChanged());
        assertTrue(changes.isMaximumLengthChanged());
        assertTrue(changes.isAllowEmptyChanged());
        assertTrue(changes.isDefaultChanged());
        assertTrue(changes.isPatternChanged());
        assertTrue(changes.isMaximumChanged());
        assertTrue(changes.isMinimumChanged());
        assertTrue(changes.isAccessChanged());
        assertTrue(changes.isFormatChanged());

        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");
        changes = new ParameterChanges(param, newParam, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());

        assertTrue(changes.isTypeChanged());
        assertTrue(changes.isMinimumLengthChanged());
        assertTrue(changes.isMaximumLengthChanged());
        assertTrue(changes.isAllowEmptyChanged());
        assertTrue(changes.isDefaultChanged());
        assertTrue(changes.isPatternChanged());
        assertTrue(changes.isMaximumChanged());
        assertTrue(changes.isMinimumChanged());
        assertTrue(changes.isAccessChanged());
        assertTrue(changes.isFormatChanged());

        assertEquals(8, changes.getPotentiallyBreaking().get("path.testPathParam").size());
        assertEquals(8, changes.getChanges().get("path.testPathParam").size());
        assertEquals(8, changes.getFlawedDefines().get("path.testPathParam").size());


        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");
        newParam.setDefaultValue("222");

        changes = new ParameterChanges(param, newParam, Diff.ALL);
        assertEquals(1, changes.getBreaking().size());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());
        assertTrue(changes.isDefaultChanged());

        assertEquals(8, changes.getPotentiallyBreaking().get("path.testPathParam").size());
        assertEquals(8, changes.getChanges().get("path.testPathParam").size());
        assertEquals(8, changes.getFlawedDefines().get("path.testPathParam").size());
        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");
        param.setMinimum(new BigDecimal(332));
        param.setMaximum(new BigDecimal(334));
        param.setDefaultValue("333");
        newParam.setMinimum(new BigDecimal(332));
        newParam.setMaximum(new BigDecimal(334));
        newParam.setDefaultValue("333");
        newParam.setType("string");
        newParam.setMinLength(3);
        newParam.setMaxLength(3);
        newParam.setAllowEmptyValue(false);
        newParam.setPattern("[0-9]{3}");
        newParam.setAccess("public");
        newParam.setFormat("format");


        changes = new ParameterChanges(param, newParam, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertFalse(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());
        assertFalse(changes.isDefaultChanged());

        assertFalse(changes.isTypeChanged());
        assertFalse(changes.isMinimumLengthChanged());
        assertFalse(changes.isMaximumLengthChanged());
        assertFalse(changes.isAllowEmptyChanged());
        assertFalse(changes.isDefaultChanged());
        assertFalse(changes.isPatternChanged());
        assertFalse(changes.isMaximumChanged());
        assertFalse(changes.isMinimumChanged());
        assertFalse(changes.isAccessChanged());
        assertFalse(changes.isFormatChanged());

        assertTrue(changes.getPotentiallyBreaking().isEmpty());
        assertTrue(changes.getChanges().isEmpty());
        assertTrue(changes.getFlawedDefines().isEmpty());

        param.setType(null);
        newParam.setType("aType");
        changes = new ParameterChanges(param, newParam, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertTrue(changes.isTypeChanged());
        assertEquals(1, changes.getPotentiallyBreaking().size());
        assertEquals(1, changes.getChanges().size());
        assertEquals(1, changes.getFlawedDefines().size());

        param.setDefaultValue(null);
        newParam.setDefaultValue("333");
        changes = new ParameterChanges(param, newParam, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertTrue(changes.isDefaultChanged());
        assertEquals(1, changes.getPotentiallyBreaking().size());
        assertEquals(1, changes.getChanges().size());
        assertEquals(1, changes.getFlawedDefines().size());
    }

    @Test
    public void testQueryParameter() {
        QueryParameter qp = new QueryParameter();
        qp.setName("select");
        qp.setType("string");
        qp.setDescription("part of api capabilities");
        qp.setPattern("(a-zA-Z0-9)*");
        qp.setRequired(false);
        qp.setAllowEmptyValue(false);

        QueryParameter fqp = new QueryParameter();
        fqp.setName("select");
        fqp.setType("string");
        fqp.setDescription("part of api capabilities");
        fqp.setPattern("(a-zA-Z0-9)+");
        fqp.setRequired(true);
        fqp.setAllowEmptyValue(false);
        ParameterChanges changes = new ParameterChanges(qp, fqp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertTrue(changes.isRequiredChanged());

        assertEquals(1, changes.getPotentiallyBreaking().get("query.select").size());
        assertEquals(1, changes.getChanges().get("query.select").size());
        assertTrue(changes.getFlawedDefines().isEmpty());

        fqp = new QueryParameter();
        fqp.setName("select");
        fqp.setType("string");
        fqp.setDescription("part of api capabilities");
        fqp.setPattern("(a-zA-Z0-9)*");
        fqp.setRequired(false);
        fqp.setAllowEmptyValue(false);
        changes = new ParameterChanges(qp, fqp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertFalse(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());

        assertTrue(changes.getPotentiallyBreaking().isEmpty());
        assertTrue(changes.getChanges().isEmpty());
        assertTrue(changes.getFlawedDefines().isEmpty());

        fqp.setPattern(null);
        fqp.setRequired(false);
        fqp.setAllowEmptyValue(false);
        changes = new ParameterChanges(qp, fqp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertTrue(changes.isPatternChanged());

        assertEquals(1, changes.getPotentiallyBreaking().size());
        assertEquals(1, changes.getChanges().size());
        assertEquals(1, changes.getFlawedDefines().size());


        fqp = new QueryParameter();
        fqp.setName("select");
        fqp.setType("string");
        fqp.setDescription("part of api capabilities");
        fqp.setPattern("(a-zA-Z0-9)*");
        qp.setPattern(null);
        fqp.setRequired(false);
        fqp.setAllowEmptyValue(false);
        changes = new ParameterChanges(qp, fqp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertTrue(changes.isPatternChanged());
        assertEquals(1, changes.getPotentiallyBreaking().size());
        assertEquals(1, changes.getChanges().size());
        assertEquals(1, changes.getFlawedDefines().size());
    }

    @Test
    public void testFormParameter() {
        FormParameter qp = new FormParameter();
        qp.setName("firstName");
        qp.setType("string");
        qp.setDescription("persons first name");
        qp.setPattern("(a-zA-Z){30}");
        qp.setRequired(true);
        qp.setAllowEmptyValue(false);

        FormParameter fqp = new FormParameter();
        fqp.setName("firstName");
        fqp.setType("string");
        fqp.setDescription("a persons first name");
        fqp.setPattern("(a-zA-Z0-9){25}");
        fqp.setRequired(true);
        fqp.setAllowEmptyValue(false);
        ParameterChanges changes = new ParameterChanges(qp, fqp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertTrue(changes.containsDiff());
        assertTrue(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());

        assertEquals(1, changes.getPotentiallyBreaking().get("form.firstName").size());
        assertEquals(1, changes.getChanges().get("form.firstName").size());
        assertTrue(changes.getFlawedDefines().isEmpty());
    }


    @Test
    public void testCollections() {
        FormParameter qp = new FormParameter();
        qp.setName("firstName");
        qp.setType("string");
        qp.setDescription("a persons first name");
        qp.setPattern("(a-zA-Z0-9){30}");
        qp.setRequired(true);
        qp.setAllowEmptyValue(false);

        FormParameter fqp = new FormParameter();
        fqp.setName("firstName");
        fqp.setType("string");
        fqp.setDescription("a persons first name");
        fqp.setPattern("(a-zA-Z0-9){30}");
        fqp.setRequired(true);
        fqp.setAllowEmptyValue(false);
        ParameterChanges changes = new ParameterChanges(qp, fqp, Diff.ALL);
        assertTrue(changes.getBreaking().isEmpty());
        assertTrue(changes.getAddedParams().isEmpty());
        assertTrue(changes.getRemovedParams().isEmpty());
        assertFalse(changes.containsDiff());
        assertFalse(changes.isDescriptionChanged());
        assertFalse(changes.isRequiredChanged());

        assertTrue(changes.getPotentiallyBreaking().isEmpty());
        assertTrue(changes.getChanges().isEmpty());
        assertTrue(changes.getFlawedDefines().isEmpty());

        changes.addBreakingChange("breaking", "breaking 1");
        changes.addBreakingChange("breaking", "breaking 1");
        assertEquals(1, changes.getBreaking().size());
        assertEquals("breaking 1", changes.getBreaking().get("breaking").get(0));
        changes.addBreakingChange("breaking", "breaking 2");
        assertEquals(1, changes.getBreaking().size());
        assertEquals(2, changes.getBreaking().get("breaking").size());
        assertEquals("breaking 2", changes.getBreaking().get("breaking").get(1));

        changes.addBreakingChange("also breaking", "also breaking 1");
        changes.addBreakingChange("also breaking", "also breaking 1");
        assertEquals(2, changes.getBreaking().size());

        changes.addPotentialBreakingChange("potentially breaking", "potentially breaking 1");
        changes.addPotentialBreakingChange("potentially breaking", "potentially breaking 1");
        assertEquals(1, changes.getPotentiallyBreaking().size());
        assertEquals("potentially breaking 1", changes.getPotentiallyBreaking().get("potentially breaking").get(0));

        changes.addPotentialBreakingChange("potentially breaking", "potentially breaking 2");
        assertEquals(2, changes.getPotentiallyBreaking().get("potentially breaking").size());
        assertEquals("potentially breaking 2", changes.getPotentiallyBreaking().get("potentially breaking").get(1));

        changes.addPotentialBreakingChange("also potentially breaking", "also potentially breaking 2");
        changes.addPotentialBreakingChange("also potentially breaking", "also potentially breaking 2");
        assertEquals(2, changes.getPotentiallyBreaking().size());
        assertEquals("also potentially breaking 2", changes.getPotentiallyBreaking().get("also potentially breaking").get(0));

    }


    @Test
    public void testNulls() {
        try {
            ParameterChanges pc = new ParameterChanges(null, null, Diff.ALL);
            fail();
        } catch (NullPointerException npe) {
            //expected
        }
    }
}
