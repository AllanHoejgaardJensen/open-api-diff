package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;

/**
 * Differences between sets of parameters (and  currently properties as well).
 */
class ParameterDiff {

    Map<String, Model> existingDefinition;
    Map<String, Model> futureDefinition;

    private List<Parameter> addedParams;
    private List<Parameter> missingParams;
    private List<ParameterChanges> changedParams;
    private List<ScopedProperty> addedProps;
    private List<ScopedProperty> missingProps;
    private List<PropertyChanges> changedProps;
    private Diff depth;

    private ParameterDiff() {
        addedParams = new ArrayList<>();
        missingParams = new ArrayList<>();
        changedParams = new ArrayList<>();
        addedProps = new ArrayList<>();
        missingProps = new ArrayList<>();
        changedProps = new ArrayList<>();
    }

    ParameterDiff(Map<String, Model> existing, Map<String, Model> future, Diff depth) {
        this();
        existingDefinition = existing;
        futureDefinition = future;
        this.depth = depth;

    }

    ParameterDiff(Map<String, Model> existModels, Map<String, Model> futureModels, Diff depth,
                  List<Parameter> existingParams, List<Parameter> futureParams) {
        this();
        existingDefinition = existModels;
        futureDefinition = futureModels;
        this.depth = depth;
        diff(existingParams, futureParams);
    }

    /**
     * @return added parameters to the future candidate API compared to the existing API
     */
    public List<Parameter> getAddedParams() {
        return addedParams;
    }

    /**
     * @return removed parameters missing in the future candidate API compared to the existing API
     */
    public List<Parameter> getMissingParams() {
        return missingParams;
    }

    /**
     * @return changed parameters that exists in both the future candidate API and the existing API
     */
    public List<ParameterChanges> getChangedParams() {
        return changedParams;
    }

    /**
     * @return added properties to the future candidate API compared to the existing API
     */
    public List<ScopedProperty> getAddedProps() {
        return addedProps;
    }

    /**
     * @return removed properties missing in the future candidate API compared to the existing API
     */
    public List<ScopedProperty> getMissingProps() {
        return missingProps;
    }

    /**
     * @return changed properties that exists in both the future candidate API and the existing API
     */
    public List<PropertyChanges> getChangedProperties() {
        return changedProps;
    }

    private void diff(List<Parameter> existing, List<Parameter> future) {
        addedParams.addAll(future);
        if (null == existing) existing = new ArrayList<>();
        for (Parameter existingParam : existing) {
            String name = existingParam.getName();
            int index = index(addedParams, name);
            if (-1 == index) {
                missingParams.add(existingParam);
            } else {
                Parameter futureParam = addedParams.get(index);
                addedParams.remove(index);

                ParameterChanges parameterChanges = new ParameterChanges(existingParam, futureParam, depth);
                if (parameterChanges.containsDiff()) {
                    changedParams.add(parameterChanges);
                }

                PropertyChanges propertyChanges = new PropertyChanges(existingParam, futureParam, depth);
                propertyChanges = checkBodyParam(name, existingParam, futureParam, propertyChanges);
                if (propertyChanges.containsDiff()) {
                    if (!propertyChanges.getAddedProperties().isEmpty()) {
                        addAddedProperties(propertyChanges.getAddedProperties());
                    }
                    if (!propertyChanges.getChanges().isEmpty()) {
                        changedProps.add(propertyChanges);
                    }
                    if (!parameterChanges.getRemovedParams().isEmpty()) {
                        addMissingProperties(propertyChanges.getRemovedProperties());
                    }
                }
            }
        }
    }

    private PropertyChanges checkBodyParam(String name, Parameter existingParam, Parameter futureParam, PropertyChanges propertyChanges) {
        if (existingParam instanceof BodyParameter && futureParam instanceof BodyParameter) { //BODY
            BodyParameter existingBodyParam = (BodyParameter) existingParam;
            Model existingSchema = existingBodyParam.getSchema();
            BodyParameter futureBodyParam = (BodyParameter) futureParam;
            Model futureSchema = futureBodyParam.getSchema();
            if (existingSchema instanceof RefModel && futureSchema instanceof RefModel) { // it refers a type definition
                String existingRef = ((RefModel) existingSchema).getSimpleRef();
                String futureRef = ((RefModel) futureSchema).getSimpleRef();
                ElementDiff diff = new ElementDiff(existingDefinition, futureDefinition, existingRef, futureRef, name);
                propertyChanges.addAddedProperties(diff.getAdded());
                propertyChanges.addRemovedProperties(diff.getRemoved());
                if (diff.getChanged().size() > 0) {
                    List<ScopedProperty> changes = diff.getChanged();
                    for (ScopedProperty change : changes) {
                        String origin = futureRef + "." + change.getEl();
                        String cause = "body.property.changed." + diff.getChangeCause();
                        propertyChanges.addRecordedChange(origin, cause);
                        propertyChanges.addPotentialBreakingChange(origin, cause);
                        propertyChanges.addBreakingChange(origin, cause);
                    }
                }
            }
        }
        return propertyChanges;
    }

    private void addAddedProperties(List<ScopedProperty> added) {
        addedProps.addAll(added);
    }

    private void addMissingProperties(List<ScopedProperty> missing) {
        missingProps.addAll(missing);
    }

    private static int index(List<Parameter> right, String name) {
        for (int i = 0; i < right.size(); i++) {
            Parameter para = right.get(i);
            if (name.equals(para.getName())) {
                return i;
            }
        }
        return -1;
    }

}
