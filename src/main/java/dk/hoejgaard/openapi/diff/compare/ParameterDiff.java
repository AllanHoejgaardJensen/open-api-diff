package dk.hoejgaard.openapi.diff.compare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.RefParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Differences between sets of parameters (and  currently properties as well).
 */
class ParameterDiff {

    private static Logger logger = LoggerFactory.getLogger(ParameterDiff.class);
    Map<String, Model> existingDefinition;
    Map<String, Model> futureDefinition;

    private List<Parameter> addedParams;
    private List<Parameter> missingParams;
    private List<ParameterChanges> changedParams;
    private List<ScopedProperty> addedProps;
    private List<ScopedProperty> missingProps;
    private List<PropertyChanges> changedProps;
    private Diff depth;

    ParameterDiff() {
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

    ParameterChanges investigateFurtherChanges(Parameter existingParam,
                                               Parameter futureParam,
                                               ParameterChanges parameterChanges) {
        if (existingParam.equals(futureParam)) {
            logger.trace("Found equal ({})", futureParam.getName());
        }
        if (existingParam instanceof PathParameter && futureParam instanceof PathParameter) { //Path
            AbstractSerializableParameter pathParam = (PathParameter) existingParam;
            AbstractSerializableParameter futurePathParam = (PathParameter) futureParam;
            String origin = "path." + futureParam.getName();
            parameterChanges = getDetailedChanges(origin, parameterChanges, pathParam, futurePathParam);
        } else if (existingParam instanceof HeaderParameter && futureParam instanceof HeaderParameter) { //Header
            AbstractSerializableParameter headerParam = (HeaderParameter) existingParam;
            AbstractSerializableParameter futureHeaderParam = (HeaderParameter) futureParam;
            String header = "header." + futureParam.getName();
            parameterChanges = getDetailedChanges(header, parameterChanges, headerParam, futureHeaderParam);
        } else if (existingParam instanceof QueryParameter && futureParam instanceof QueryParameter) { //Query
            AbstractSerializableParameter queryParam = (QueryParameter) existingParam;
            AbstractSerializableParameter futureQueryParam = (QueryParameter) futureParam;
            String header = "query." + futureParam.getName();
            parameterChanges = getDetailedChanges(header, parameterChanges, queryParam, futureQueryParam);
        } else if (existingParam instanceof FormParameter && futureParam instanceof FormParameter) { //Form
            AbstractSerializableParameter formParam = (FormParameter) existingParam;
            AbstractSerializableParameter futureFormParam = (FormParameter) futureParam;
            String header = "form." + futureParam.getName();
            parameterChanges = getDetailedChanges(header, parameterChanges, formParam, futureFormParam);
        } else if (existingParam instanceof CookieParameter && futureParam instanceof CookieParameter) { //Cookie
            AbstractSerializableParameter cookieParam = (CookieParameter) existingParam;
            AbstractSerializableParameter futureCookieParam = (CookieParameter) futureParam;
            String header = "cookie." + futureParam.getName();
            parameterChanges = getDetailedChanges(header, parameterChanges, cookieParam, futureCookieParam);
        } else if (existingParam instanceof RefParameter && futureParam instanceof RefParameter) { //Cookie
            RefParameter refParam = (RefParameter) existingParam;
            RefParameter futureRefParam = (RefParameter) futureParam;
            logger.warn("-----> REF checks are not implemented yet");
            parameterChanges.addDefinitionFlaw("Ref Parameter", "Parameters definition check: for" + refParam + " and " + futureRefParam +
                " ........Ref check implementation lacks in tool");
        }
        return parameterChanges;
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
                parameterChanges = investigateFurtherChanges(existingParam, futureParam, parameterChanges);
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

    /**
     * checks within the individual parameter to see if there are any changes that potentially
     * means the future api is not compliant with the existing
     */
    private ParameterChanges getDetailedChanges(String origin,
                                        ParameterChanges recordedChanges,
                                        AbstractSerializableParameter existingParam,
                                        AbstractSerializableParameter futureParam) {
        recordedChanges = checkParamType(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamPattern(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamMinimum(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamMaximum(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamDefaultValue(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamMinLength(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamMaxLength(origin, recordedChanges, existingParam, futureParam);
        recordedChanges = checkParamAllowEmptyValue(origin, recordedChanges, existingParam, futureParam);
        return recordedChanges;
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

    private ParameterChanges checkParamDefaultValue(String origin, ParameterChanges recordedChanges,
                                                    AbstractSerializableParameter existingParam,
                                                    AbstractSerializableParameter futureParam) {
        if (existingParam.getDefaultValue() != null) {
            if (!(existingParam.getAllowEmptyValue().equals(futureParam.getAllowEmptyValue()))) {
                recordedChanges = handleDefaultValue(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getDefaultValue() != null) {
            if (!(futureParam.getDefaultValue().equals(existingParam.getDefaultValue()))) {
                recordedChanges = handleDefaultValue(origin, recordedChanges, existingParam, futureParam);
            }
        }
        return recordedChanges;
    }

    private ParameterChanges checkParamAllowEmptyValue(String origin, ParameterChanges recordedChanges,
                                               AbstractSerializableParameter existingParam,
                                               AbstractSerializableParameter futureParam) {
        if (existingParam.getAllowEmptyValue() != null) {
            if (!(existingParam.getAllowEmptyValue().equals(futureParam.getAllowEmptyValue()))) {
                recordedChanges = handleAllowEmptyValue(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getAllowEmptyValue() != null) {
            recordedChanges = handleAllowEmptyValue(origin, recordedChanges, existingParam, futureParam);
        }
        return recordedChanges;
    }


    private ParameterChanges checkParamMinLength(String origin, ParameterChanges recordedChanges,
                                         AbstractSerializableParameter existingParam,
                                         AbstractSerializableParameter futureParam) {
        if (existingParam.getMinLength() != null) {
            if (!(existingParam.getMinLength().equals(futureParam.getMinLength()))) {
                recordedChanges = handleMinLength(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getMinLength() != null) {
            recordedChanges = handleMinLength(origin, recordedChanges, existingParam, futureParam);
        }
        return recordedChanges;
    }

    private ParameterChanges checkParamMaxLength(String origin, ParameterChanges recordedChanges,
                                         AbstractSerializableParameter existingParam,
                                         AbstractSerializableParameter futureParam) {
        if (existingParam.getMaxLength() != null) {
            if (!(existingParam.getMaxLength().equals(futureParam.getMaxLength()))) {
                recordedChanges = handleMaxLength(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getMaxLength() != null) {
            recordedChanges = handleMaxLength(origin, recordedChanges, existingParam, futureParam);
        }
        return recordedChanges;
    }

    private ParameterChanges checkParamMinimum(String origin, ParameterChanges recordedChanges,
                                       AbstractSerializableParameter existingParam,
                                       AbstractSerializableParameter futureParam) {
        if (existingParam.getMinimum() != null) {
            if (!existingParam.getMinimum().equals(futureParam.getMinimum())) {
                recordedChanges = handleMinimum(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getMinimum() != null) {
            if (!futureParam.getMinimum().equals(existingParam.getMinimum())) {
                recordedChanges = handleMinimum(origin, recordedChanges, existingParam, futureParam);
            }
        }
        return recordedChanges;
    }


    private ParameterChanges checkParamMaximum(String origin, ParameterChanges recordedChanges,
                                       AbstractSerializableParameter existingParam,
                                       AbstractSerializableParameter futureParam) {
        if (existingParam.getMaximum() != null) {
            if (!existingParam.getMaximum().equals(futureParam.getMaximum())) {
                recordedChanges = handleMaximum(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getMaximum() != null) {
            if (!futureParam.getMaximum().equals(existingParam.getMaximum())) {
                recordedChanges = handleMaximum(origin, recordedChanges, existingParam, futureParam);
            }
        }
        return recordedChanges;
    }

    private ParameterChanges checkParamPattern(String origin, ParameterChanges recordedChanges,
                                       AbstractSerializableParameter existingParam,
                                       AbstractSerializableParameter futureParam) {
        if (existingParam.getPattern() != null) {
            if (!existingParam.getPattern().equals(futureParam.getPattern())) {
                recordedChanges = handlePattern(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getPattern() != null) {
            if (!futureParam.getPattern().equals(existingParam.getPattern())) {
                recordedChanges = handlePattern(origin, recordedChanges, existingParam, futureParam);
            }
        }
        return recordedChanges;
    }


    private ParameterChanges checkParamType(String origin, ParameterChanges recordedChanges,
                                    AbstractSerializableParameter existingParam,
                                    AbstractSerializableParameter futureParam) {
        if (existingParam.getType() != null) {
            if (!existingParam.getType().equals(futureParam.getType())) {
                recordedChanges = handleType(origin, recordedChanges, existingParam, futureParam);
            }
        } else if (futureParam.getType() != null) {
            if (!futureParam.getType().equals(existingParam.getType())) {
                recordedChanges = handleType(origin, recordedChanges, existingParam, futureParam);
            }
        }
        return recordedChanges;
    }

    private ParameterChanges handleType(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                        AbstractSerializableParameter futureParam) {
        String cause = ".type.changed.from." +
            existingParam.getType() + ".to." + futureParam.getType();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause); //difficult to know when a type is compatible with the existing one
        if (existingParam.getType() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.type.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getType() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .type.defined.as.null::makes future proof api design difficult");
        }

        return recordedChanges;
    }

    private ParameterChanges handlePattern(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                           AbstractSerializableParameter futureParam) {
        String cause = "pattern.changed.from." +
            existingParam.getPattern() + ".to." + futureParam.getPattern();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause); //difficult to know when a regexp is a subset of an existing one
        if (existingParam.getPattern() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.pattern.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getPattern() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .pattern.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }


    private ParameterChanges handleMinimum(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                           AbstractSerializableParameter futureParam) {
        String cause = ".mimimumvalue.changed.from." +
            existingParam.getMinimum() + ".to." + futureParam.getMaximum();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause);
        if (futureParam.getMinimum() != null && (existingParam.getMinimum() != null)) {
            if (futureParam.getMinimum().subtract(existingParam.getMinimum()).longValue() > 0) { //it now demands longer min
                recordedChanges.addBreakingChange(origin, cause);
            }
        }
        if (existingParam.getMinimum() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.mimimumvalue.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getMinimum() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .mimimumvalue.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }

    private ParameterChanges handleDefaultValue(String origin, ParameterChanges recordedChanges,
                                                AbstractSerializableParameter existingParam,
                                                AbstractSerializableParameter futureParam) {
        String cause = ".defaultvalue.changed.from." +
            existingParam.getDefaultValue() + ".to." + futureParam.getDefaultValue();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause);
        if (futureParam.getDefaultValue() != null && (existingParam.getDefaultValue() != null)) {
            BigDecimal fd = getValue(futureParam);
            if (fd.subtract(existingParam.getMinimum()).longValue() > 0) { //default lower than min
                recordedChanges.addBreakingChange(origin, cause);
            }
            if (existingParam.getMaximum().subtract(fd).longValue() > 0) { //default higher than max
                recordedChanges.addBreakingChange(origin, cause);
            }
        }

        if (existingParam.getMinimum() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.defaultvalue.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getMinimum() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .defaultvalue.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }

    private ParameterChanges handleMaximum(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                           AbstractSerializableParameter futureParam) {
        String cause = ".maximumvalue.changed.from." +
            existingParam.getMaximum() + ".to." + futureParam.getMaximum();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause);
        if (futureParam.getMaximum() != null && existingParam.getMaximum() != null) {
            if (futureParam.getMaximum().subtract(existingParam.getMaximum()).longValue() < 0) { //it now demands lesser max
                recordedChanges.addBreakingChange(origin, cause);
            }
        }
        if (existingParam.getMaximum() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.maximumvalue.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getMaximum() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .maximumvalue.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }

    private ParameterChanges handleMinLength(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                             AbstractSerializableParameter futureParam) {
        String cause = ".minlength.changed.from." +
            existingParam.getMinLength() + ".to." + futureParam.getMinLength();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause);
        if (futureParam.getMinLength() != null) {
            if (futureParam.getMinLength() > existingParam.getMinLength()) { //it now demands longer min
                recordedChanges.addBreakingChange(origin, cause);
            }
        }
        if (existingParam.getMinLength() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.minlength.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getMinLength() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .minlength.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }

    private ParameterChanges handleMaxLength(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                             AbstractSerializableParameter futureParam) {
        String cause = ".maxlength.changed.from." + existingParam.getMaxLength()
            + ".to." + futureParam.getMaxLength();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause);
        if (futureParam.getMaxLength() != null && existingParam.getMaxLength() != null) {
            if (existingParam.getMaxLength() > futureParam.getMaxLength()) { //it now demands lesser max
                recordedChanges.addBreakingChange(origin, cause);
            }
        }
        if (existingParam.getMaxLength() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.maxlength.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getMaxLength() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .maxlength.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }

    private ParameterChanges handleAllowEmptyValue(String origin, ParameterChanges recordedChanges, AbstractSerializableParameter existingParam,
                                                   AbstractSerializableParameter futureParam) {
        String cause = ".allowemptyvalue.changed.from." +
            existingParam.getAllowEmptyValue() + ".to." + futureParam.getAllowEmptyValue();
        recordedChanges.addRecordedChange(origin, cause);
        recordedChanges.addPotentialBreakingChange(origin, cause);
        logger.trace("{} - {}", origin, cause);
        if (existingParam.getAllowEmptyValue() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin +
                " .existing.compliance.allowemptyvalue.defined.as.null::makes future proof api design difficult");
        }
        if (futureParam.getAllowEmptyValue() == null) {
            recordedChanges.addDefinitionFlaw(origin, origin + " .allowemptyvalue.defined.as.null::makes future proof api design difficult");
        }
        return recordedChanges;
    }

    private BigDecimal getValue(AbstractSerializableParameter param) {
        Object value = param.getDefaultValue();
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Integer) {
            return new BigDecimal((Integer) value);
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        if (value instanceof Number) {
            return new BigDecimal(((Number) value).longValue());
        }
        String errMsg = "new.type.from.default.value::could not convert that " + param.getDefaultValue();
        logger.error(errMsg);
        throw new NumberFormatException(errMsg);
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
