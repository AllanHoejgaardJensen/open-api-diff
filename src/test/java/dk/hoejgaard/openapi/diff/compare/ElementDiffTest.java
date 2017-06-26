package dk.hoejgaard.openapi.diff.compare;

import java.math.BigDecimal;
import java.util.Map;

import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
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
    public void testDiffWithAddedProperty() {
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

    @Test
    public void testDiffWithRemovedProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        Property prop = new BooleanProperty();
        prop.setName("testBool");
        prop.setRequired(true);
        prop.description("some description");
        properties.put("testProp", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(1, pd.getRemoved().size());
        assertEquals(0, pd.getChanged().size());
    }

    @Test
    public void testDiffWithChangedBooleanProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        Property prop = new BooleanProperty();
        prop.setName("testBool");
        prop.setRequired(true);
        prop.description("some description");
        properties.put("testProp", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new BooleanProperty();
        prop.setName("testBool");
        prop.setRequired(false);
        prop.description("some description");
        properties.put("testProp", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithBreakingChangedBooleanProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        Property prop = new BooleanProperty();
        prop.setName("testBool");
        prop.setRequired(false);
        prop.description("some description");
        properties.put("testProp", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new BooleanProperty();
        prop.setName("testBool");
        prop.setRequired(true);
        prop.description("some description");
        properties.put("testProp", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithBreakingChangedStringProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        Property prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(false);
        prop.setAllowEmptyValue(true);
        prop.description("some description");
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(true);
        prop.setAllowEmptyValue(false);
        prop.description("some description");
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithNoBreakingChangedInStringProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(false);
        prop.setAllowEmptyValue(false);
        prop.setPattern("[a-z]{2}");
        prop.description("some description");
        properties.put("testProperty", prop);
        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(false);
        prop.setAllowEmptyValue(false);
        prop.setPattern("[a-z]{2}");
        prop.description("some other description");
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }


    @Test
    public void testDiffWithBreakingChangedInStringProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        Property prop = new StringProperty();
        prop.setName("testProperty");
        properties.put("testProperty", prop);
        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(false);
        prop.setAllowEmptyValue(true);
        prop.description("some description");
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithBreakingChangedMaxInStringProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setMaxLength(20);
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setMaxLength(10);
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithPotentiallyBreakingChangedInStringProperty() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(false);
        prop.setAllowEmptyValue(false);
        prop.setPattern("[a-z]{2}");
        prop.description("some description");
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setRequired(false);
        prop.setAllowEmptyValue(false);
        prop.setPattern("[a-z]{4}");
        prop.description("some description");
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(1, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithChangedInStringPropertyReadonly() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setReadOnly(true);
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        StringProperty prop2 = new StringProperty();
        prop2.setName("testProperty");
        prop2.setReadOnly(false);
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithReverseChangedInStringPropertyReadonly() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setReadOnly(false);
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        StringProperty prop2 = new StringProperty();
        prop2.setName("testProperty");
        prop2.setReadOnly(true);
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithPotentiallyBreakingChangedInStringPropertyAccess() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setAccess("full");
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setAccess("limited");
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithPotentiallyBreakingChangedInStringPropertyFomat() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        StringProperty prop = new StringProperty();
        prop.setName("testProperty");
        prop.setFormat("format");
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        prop = new StringProperty();
        prop.setName("testProperty");
        prop.setFormat("other");
        properties.put("testProperty", prop);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithChangedInNumericPropertyMaximum() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        LongProperty prop = new LongProperty();
        prop.setName("testProperty");
        prop.setExclusiveMaximum(true);
        prop.setMaximum(new BigDecimal(100));
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        LongProperty prop2 = new LongProperty();
        prop2.setName("testProperty");
        prop2.setExclusiveMaximum(true);
        prop2.setMaximum(new BigDecimal(200));
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithChangedInNumericPropertyMaximumLowered() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        LongProperty prop = new LongProperty();
        prop.setName("testProperty");
        prop.setExclusiveMaximum(true);
        prop.setMaximum(new BigDecimal(200));
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        LongProperty prop2 = new LongProperty();
        prop2.setName("testProperty");
        prop2.setExclusiveMaximum(true);
        prop2.setMaximum(new BigDecimal(100));
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithChangedInNumericPropertyExclusiveMaximumenabled() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        LongProperty prop = new LongProperty();
        prop.setName("testProperty");
        prop.setExclusiveMaximum(false);
        prop.setMaximum(new BigDecimal(200));
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        LongProperty prop2 = new LongProperty();
        prop2.setName("testProperty");
        prop2.setExclusiveMaximum(true);
        prop2.setMaximum(new BigDecimal(200));
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }
    @Test
    public void testDiffWithChangedInNumericPropertyMinimum() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        LongProperty prop = new LongProperty();
        prop.setName("testProperty");
        prop.setExclusiveMinimum(true);
        prop.setMinimum(new BigDecimal(200));
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        LongProperty prop2 = new LongProperty();
        prop2.setName("testProperty");
        prop2.setExclusiveMinimum(true);
        prop2.setMinimum(new BigDecimal(100));
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(0, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithChangedInNumericPropertyMinimumLowered() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        LongProperty prop = new LongProperty();
        prop.setName("testProperty");
        prop.setExclusiveMinimum(true);
        prop.setMinimum(new BigDecimal(100));
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        LongProperty prop2 = new LongProperty();
        prop2.setName("testProperty");
        prop2.setExclusiveMinimum(true);
        prop2.setMinimum(new BigDecimal(200));
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }

    @Test
    public void testDiffWithChangedInNumericPropertyExclusiveMinimumenabled() {
        Map<String, Model> models = api.getDefinitions();
        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> secondaryModel = secondApi.getDefinitions();
        Model model = models.get("Account");
        Map<String, Property> properties = model.getProperties();
        LongProperty prop = new LongProperty();
        prop.setName("testProperty");
        prop.setExclusiveMinimum(false);
        prop.setMinimum(new BigDecimal(200));
        properties.put("testProperty", prop);

        model = secondaryModel.get("Account");
        properties = model.getProperties();
        LongProperty prop2 = new LongProperty();
        prop2.setName("testProperty");
        prop2.setExclusiveMinimum(true);
        prop2.setMinimum(new BigDecimal(200));
        properties.put("testProperty", prop2);
        ElementDiff pd = new ElementDiff(models, secondaryModel, "Account", "Account" , "scope");
        assertEquals(0, pd.getAdded().size());
        assertEquals(0, pd.getRemoved().size());
        assertEquals(1, pd.getChanged().size());
        assertEquals(1, pd.getBreaking().size());
        assertEquals(0, pd.getPotentiallyBreaking().size());
    }
    
}
