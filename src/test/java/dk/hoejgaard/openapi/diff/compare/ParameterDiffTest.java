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
        ParameterDiff pd = new ParameterDiff();
        pd.investigateFurtherChanges(param, param, changes);

        assertEquals(0, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(false, changes.containsDiff());
        assertEquals(false, changes.isDescriptionChanged());
        assertEquals(false, changes.isRequiredChanged());

        PathParameter newParam = new PathParameter();
        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");

        changes = new ParameterChanges(param, newParam, Diff.ALL);
        pd = new ParameterDiff();
        pd.investigateFurtherChanges(param, newParam, changes);
        assertEquals(0, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(true, changes.containsDiff());
        assertEquals(false, changes.isDescriptionChanged());
        assertEquals(false, changes.isRequiredChanged());

        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");
        changes = new ParameterChanges(param, newParam, Diff.ALL);
        pd.investigateFurtherChanges(param, newParam, changes);
        assertEquals(0, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(true, changes.containsDiff());
        assertEquals(false, changes.isDescriptionChanged());
        assertEquals(false, changes.isRequiredChanged());

        assertEquals(8, changes.getPotentiallyBreaking().get("path.testPathParam").size());
        assertEquals(8, changes.getChanges().get("path.testPathParam").size());
        assertEquals(8, changes.getFlawedDefines().get("path.testPathParam").size());


        newParam.setName("testPathParam");
        newParam.setDescription("a simple test pathParam for test purposes");
        newParam.setDefaultValue("222");
        changes = new ParameterChanges(param, newParam, Diff.ALL);
        pd.investigateFurtherChanges(param, newParam, changes);
        assertEquals(1, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(true, changes.containsDiff());
        assertEquals(false, changes.isDescriptionChanged());
        assertEquals(false, changes.isRequiredChanged());

        assertEquals(8, changes.getPotentiallyBreaking().get("path.testPathParam").size());
        assertEquals(8, changes.getChanges().get("path.testPathParam").size());
        assertEquals(8, changes.getFlawedDefines().get("path.testPathParam").size());
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
        ParameterDiff pd = new ParameterDiff();
        pd.investigateFurtherChanges(qp, fqp, changes);
        assertEquals(0, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(true, changes.containsDiff());
        assertEquals(false, changes.isDescriptionChanged());
        assertEquals(true, changes.isRequiredChanged());

        assertEquals(1, changes.getPotentiallyBreaking().get("query.select").size());
        assertEquals(1, changes.getChanges().get("query.select").size());
        assertEquals(0, changes.getFlawedDefines().size());
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
        ParameterDiff pd = new ParameterDiff();
        pd.investigateFurtherChanges(qp, fqp, changes);
        assertEquals(0, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(true, changes.containsDiff());
        assertEquals(true, changes.isDescriptionChanged());
        assertEquals(false, changes.isRequiredChanged());

        assertEquals(1, changes.getPotentiallyBreaking().get("form.firstName").size());
        assertEquals(1, changes.getChanges().get("form.firstName").size());
        assertEquals(0, changes.getFlawedDefines().size());
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
        ParameterDiff pd = new ParameterDiff();
        pd.investigateFurtherChanges(cp, fdp, changes);
        assertEquals(0, changes.getBreaking().size());
        assertEquals(0, changes.getAddedParams().size());
        assertEquals(0, changes.getRemovedParams().size());
        assertEquals(true, changes.containsDiff());
        assertEquals(false, changes.isDescriptionChanged());
        assertEquals(false, changes.isRequiredChanged());

        assertEquals(3, changes.getPotentiallyBreaking().get("cookie.preference").size());
        assertEquals(3, changes.getChanges().get("cookie.preference").size());
        assertEquals(0, changes.getFlawedDefines().size());

        fdp.setMaxLength(30);
        changes = new ParameterChanges(cp, fdp, Diff.ALL);
        pd.investigateFurtherChanges(cp, fdp, changes);
        assertEquals(1, changes.getBreaking().size());
    }

}