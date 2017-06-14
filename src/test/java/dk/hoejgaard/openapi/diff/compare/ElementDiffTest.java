package dk.hoejgaard.openapi.diff.compare;

import java.util.Map;

import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class ElementDiffTest {

    private SwaggerParser swaggerParser;
    private Swagger api;

    @Before
    public void setup() {
        swaggerParser = new SwaggerParser();
        api = swaggerParser.read("elaborate_example_v1.json");
    }

    @Test
    public void testCreate() {
        Map<String, Model> models = api.getDefinitions();
        ElementDiff pd = new ElementDiff(models, models, "existingRef", "futureRef" , "scope");
        assertNotNull(pd);
    }

    @Test
    public void testDiffWithNoDifferences() {
        Map<String, Model> models = api.getDefinitions();
        ElementDiff pd = new ElementDiff(models, models, "existingRef", "futureRef" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(0, pd.getChanged().size());
    }

    @Test
    public void testDiffWithDifferences() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = secondaryModel.get("Account");
        Map<String, Property> properties = model.getProperties();
        Property prop = new BooleanProperty();
        prop.setName("testBool");
        prop.setRequired(true);
        prop.description("some description");
        properties.put("testProp", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(1, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(0, pd.getChanged().size());
    }
}
