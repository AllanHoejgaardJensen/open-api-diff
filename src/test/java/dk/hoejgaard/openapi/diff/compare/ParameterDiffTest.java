package dk.hoejgaard.openapi.diff.compare;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ParameterDiffTest{

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
        ParameterDiff pd = new ParameterDiff(models, models, Diff.ALL);
        assertNotNull(pd);
        assertEquals(models, pd.existingDefinition);
        assertEquals(models, pd.futureDefinition);
    }

    @Test
    public void testDiffWithNoDifferences() {
        Map<String, Model> models = api.getDefinitions();
        Map<String, Path> paths = api.getPaths();
        Path path = paths.get("/accounts/{regNo}-{accountNo}/transactions/{id}");
        Map<HttpMethod, Operation> operations = path.getOperationMap();
        Operation operation = operations.get(HttpMethod.GET);
        List<Parameter> parameters = operation.getParameters();
        ParameterDiff pd = new ParameterDiff(models, models, Diff.BREAKING, parameters, parameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());
    }

    @Test
    public void testDiffWithSingleDifference() {
        Map<String, Model> models = api.getDefinitions();
        Map<String, Path> paths = api.getPaths();
        Path path = paths.get("/accounts/{regNo}-{accountNo}/transactions/{id}");
        Map<HttpMethod, Operation> operations = path.getOperationMap();
        Operation operation = operations.get(HttpMethod.GET);
        List<Parameter> parameters = operation.getParameters();

        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> changedModels = secondApi.getDefinitions();
        Map<String, Path> changedPaths = secondApi.getPaths();
        Path changedPath = changedPaths.get("/accounts/{regNo}-{accountNo}/transactions/{id}");
        Map<HttpMethod, Operation> changedOperations = changedPath.getOperationMap();
        Operation changedOperation = changedOperations.get(HttpMethod.GET);
        List<Parameter> changedParameters = changedOperation.getParameters();

        HeaderParameter hpc = new HeaderParameter();
        hpc.setName("TestingHeader");
        changedParameters.add(hpc);

        ParameterDiff pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(1, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());

        HeaderParameter hp = new HeaderParameter();
        hp.setName("TestingHeader");
        parameters.add(hp);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());


        hp.setName("TestingHeaderChanged");
        changedParameters.add(hp);
        parameters.add(hp);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(1, pd.getAddedParams().size());
        assertEquals(1, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());

        hp.setName("TestingHeader");
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());

        hp.setRequired(false);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());

        hp.setRequired(true);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(1, pd.getChangedParams().size());

        hp.setRequired(false);
        hp.setDescription(hp.getDescription() + " A Change");
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(1, pd.getChangedParams().size());
    }

    @Test
    public void testDiffWithDifferenceInBody() {
        Map<String, Model> models = api.getDefinitions();
        Map<String, Path> paths = api.getPaths();
        Path path = paths.get("/accounts/{regNo}-{accountNo}/transactions/{id}");
        Map<HttpMethod, Operation> operations = path.getOperationMap();
        Operation operation = operations.get(HttpMethod.PUT);
        List<Parameter> parameters = operation.getParameters();

        Swagger secondApi = swaggerParser.read("elaborate_example_v1.json");
        Map<String, Model> changedModels = secondApi.getDefinitions();
        Map<String, Path> changedPaths = secondApi.getPaths();
        Path changedPath = changedPaths.get("/accounts/{regNo}-{accountNo}/transactions/{id}");
        Map<HttpMethod, Operation> changedOperations = changedPath.getOperationMap();
        Operation changedOperation = changedOperations.get(HttpMethod.PUT);
        List<Parameter> changedParameters = changedOperation.getParameters();

        ParameterDiff pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());

        //parameters
        Model changedModel = changedModels.get("TransactionUpdate");
        Map<String, Property> changedProperties = changedModel.getProperties();
        StringProperty changedProperty =  new StringProperty();
        changedProperty.setName("currency");
        changedProperty.setRequired(true);
        changedProperty.setAllowEmptyValue(false);
        changedProperty.setPattern("[A-Z]*");
        changedProperty.setMaxLength(3);
        changedProperty.setMinLength(3);
        changedProperties.put(changedProperty.getName(), changedProperty);


        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(1, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(0, pd.getChangedProperties().size());


        //properties - pattern
        Model model = models.get("TransactionUpdate");
        Map<String, Property> properties = model.getProperties();
        StringProperty property =  new StringProperty();
        property.setName("currency");
        property.setRequired(true);
        property.setAllowEmptyValue(false);
        property.setPattern("[A-Z]*");
        property.setMaxLength(3);
        property.setMinLength(3);
        properties.put(property.getName(), property);

        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedParams().size());
        assertEquals(0, pd.getMissingParams().size());
        assertEquals(0, pd.getChangedParams().size());

        changedProperty.setPattern("[a-Z]{3}");
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(1, pd.getChangedProperties().size());

        property.setPattern("[a-Z]{3}");
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(0, pd.getChangedProperties().size());

        //properties - type
        changedProperty.setType("number");
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(1, pd.getChangedProperties().size());

        changedProperty.setType("string");
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(0, pd.getChangedProperties().size());

        //properties - minLength
        changedProperty.setMinLength(property.getMinLength() + 1);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(1, pd.getChangedProperties().size());

        changedProperty.setMinLength(property.getMinLength() - 1);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(1, pd.getChangedProperties().size());

        changedProperty.setMinLength(property.getMinLength());
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(0, pd.getChangedProperties().size());

        //properties - maxLength
        changedProperty.setMaxLength(property.getMaxLength() + 1);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(1, pd.getChangedProperties().size());


        changedProperty.setMaxLength(property.getMaxLength() - 1);
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(1, pd.getChangedProperties().size());

        changedProperty.setMaxLength(property.getMaxLength());
        pd = new ParameterDiff(models, changedModels, Diff.BREAKING, parameters, changedParameters);
        assertEquals(0, pd.getAddedProps().size());
        assertEquals(0, pd.getMissingProps().size());
        assertEquals(0, pd.getChangedProperties().size());
    }

}