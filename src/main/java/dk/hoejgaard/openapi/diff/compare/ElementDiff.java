package dk.hoejgaard.openapi.diff.compare;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dk.hoejgaard.openapi.diff.compare.util.Maps;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.Model;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.AbstractNumericProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;

/**
 * Determines differences in the API model for properties/references
 */
class ElementDiff {

    private Map<String, Model> existingAPI;
    private Map<String, Model> futureAPI;

    private List<ScopedProperty> added;
    private List<ScopedProperty> removed;
    private List<ScopedProperty> changed;
    private Map<String, String> changeCause;
    private Map<String, String> breaking;
    private Map<String, String> potentiallyBreaking;


    /**
     * @param existing definition model for the existing API
     * @param future definition model for the future candidate API
     * @param existingRef name for the reference/property in the existing API
     * @param nextRef name for the reference/property in the candidate API
     * @param scope the scope in which this should be seen and reported
     */
    ElementDiff(Map<String, Model> existing, Map<String, Model> future, String existingRef, String nextRef, String scope) {
        added = new ArrayList<>();
        removed = new ArrayList<>();
        changed = new ArrayList<>();
        changeCause = new LinkedHashMap<>();
        breaking = new LinkedHashMap<>();
        potentiallyBreaking = new LinkedHashMap<>();
        existingAPI = existing;
        futureAPI = future;
        diff(existingAPI.get(existingRef), futureAPI.get(nextRef), scope);
    }

    /**
     * @return added scopedProperties in the future candidate API compared to what was included in the existing API
     */
    public List<ScopedProperty> getAdded() {
        return Collections.unmodifiableList(added);
    }

    /**
     * @return removed scopedProperties in the future candidate API comparing to the existing API
     */
    public List<ScopedProperty> getRemoved() {
        return Collections.unmodifiableList(removed);
    }

    /**
     * @return changed scopedProperties in the future candidate API comparing to the existing API
     */
    public List<ScopedProperty> getChanged() {
        return Collections.unmodifiableList(changed);
    }

    /**
     * @return reasons for a given change e.g. the regExp pattern was changed
     */
    public Map<String, String> getChangeCause() {
        return Collections.unmodifiableMap(changeCause);
    }

    /**
     * @return the breaking changes
     */
    public Map<String, String> getBreaking() {
        return Collections.unmodifiableMap(breaking);
    }

    /**
     * @return the potentially breaking changes
     */
    public Map<String, String> getPotentiallyBreaking() {
        return Collections.unmodifiableMap(potentiallyBreaking);
    }

    private void diff(Model existingAPI, Model nextAPI, String parentEl) {
        if (null == existingAPI && null == nextAPI) return;
        Map<String, Property> existingProperties = null == existingAPI ? null : existingAPI.getProperties();
        Map<String, Property> futureProperties = null == nextAPI ? null : nextAPI.getProperties();
        Maps<String, Property> diff = Maps.diff(existingProperties, futureProperties);
        Map<String, Property> increasedProp = diff.getAdded();
        Map<String, Property> missingProp = diff.getRemoved();

        added.addAll(convert2ElProperties(increasedProp, parentEl, false));
        removed.addAll(convert2ElProperties(missingProp, parentEl, true));
        if (existingProperties != null && futureProperties != null) {
            List<String> sharedKey = diff.getCommon();
            for (String key : sharedKey) {
                Property existing = existingProperties.get(key);
                Property future = futureProperties.get(key);
                if (existing instanceof RefProperty
                    && future instanceof RefProperty) {
                    String existingRef = ((RefProperty) existing).getSimpleRef();
                    String futureRef = ((RefProperty) future).getSimpleRef();
                    diff(this.existingAPI.get(existingRef),
                        futureAPI.get(futureRef),
                        null == parentEl ? key : (parentEl + "." + key));
                } else if (!isCompliant(existing, future)) {
                    ScopedProperty fpWithPath = convert2ELProperty(parentEl, key, future);
                    changed.add(fpWithPath);
                    String context = parentEl + "." + key + ".";
                    changeCause.put(context, investigateAndReport(existing, future, context));
                }
            }
        }
    }

    private String investigateAndReport(Property existing, Property future, String context) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (existing instanceof StringProperty && future instanceof StringProperty) {
            appendStringPropertyDiffs(sb, existing, future, context);
        }
        if (existing instanceof BodyParameter && future instanceof BodyParameter) {
            appendBodyParameterDiffs(sb, existing, future);
        }
        if (existing instanceof AbstractNumericProperty && future instanceof AbstractNumericProperty) {
            appendNumericParameterDiffs(sb, existing, future, context);
        }
        appendPropertyDiffs(sb, existing, future, context);
        if (sb.lastIndexOf(",") > 0) {
            int index = sb.lastIndexOf(",");
            int length = sb.length();
            sb.delete(index, length);
        }
        sb.append("]");
        return sb.toString();
    }

    private StringBuilder appendNumericParameterDiffs(StringBuilder sb, Property existing, Property future, String context) {
        AbstractNumericProperty f = (AbstractNumericProperty) future;
        AbstractNumericProperty e = (AbstractNumericProperty) existing;
        appendMinimum(sb, context, f, e);
        appendExclusiveMinimum(sb, context, f, e);
        appendMaximum(sb, context, f, e);
        appendExclusiveMaximum(sb, context, f, e);
        return sb;
    }

    private void appendMinimum(StringBuilder sb, String context, AbstractNumericProperty f, AbstractNumericProperty e) {
        if (f.getMinimum() != null && !f.getMinimum().equals(e.getMinimum())) {
            String str = "minimum.changed.from." + e.getMinimum() + ".to." + f.getMinimum();
            sb.append(str).append(", ");
            if ((f.getMinimum().subtract(e.getMinimum()).longValue() > 0)) {
                breaking.put(context, context + str);
            }
        }
    }

    private void appendMaximum(StringBuilder sb, String context, AbstractNumericProperty f, AbstractNumericProperty e) {
        if (f.getMaximum() != null && !f.getMaximum().equals(e.getMaximum())) {
            String str = "maximum.changed.from." + e.getMaximum() + ".to." + f.getMaximum();
            sb.append(str).append(", ");
            if ((f.getMaximum().subtract(e.getMaximum()).longValue() < 0)) {
                breaking.put(context, context + str);
            }
        }
    }

    private void appendExclusiveMinimum(StringBuilder sb, String context, AbstractNumericProperty f, AbstractNumericProperty e) {
        if (f.getExclusiveMinimum() != null && !f.getExclusiveMinimum().equals(e.getExclusiveMinimum())) {
            String str = "minimum.exclusive.changed.from." + e.getExclusiveMinimum() + ".to." + f.getExclusiveMinimum();
            sb.append(str).append(", ");
            if (f.getExclusiveMinimum() && !e.getExclusiveMinimum()) {
                breaking.put(context, context + str);
            }
        }
    }

    private void appendExclusiveMaximum(StringBuilder sb, String context, AbstractNumericProperty f, AbstractNumericProperty e) {
        if (f.getExclusiveMaximum() != null && !f.getExclusiveMaximum().equals(e.getExclusiveMaximum())) {
            String str = "maximum.exclusive.changed.from." + e.getExclusiveMaximum() + ".to." + f.getExclusiveMaximum();
            sb.append(str).append(", ");
            if (f.getExclusiveMaximum() && !e.getExclusiveMaximum()) {
                breaking.put(context, context + str);
            }
        }
    }

    private StringBuilder appendBodyParameterDiffs(StringBuilder sb, Property existing, Property future) {
        BodyParameter current = (BodyParameter) existing;
        BodyParameter coming = (BodyParameter) future;
        if (coming.getIn() != null && !coming.getIn().equals(current.getIn())) {
            sb.append("position.changed.from.").append(current.getIn())
                .append(".to.").append(coming.getIn()).append(", ");
        }
        if (coming.getSchema() != null && current.getSchema() != null && !coming.getSchema().equals(current.getSchema())) {
            sb.append("schema.changed.from.").append(current.getSchema().getReference())
                .append(".to.").append(coming.getSchema().getReference()).append(", ");
        } else {
            sb.append("schema.changed.from.").append(current.getSchema() == null ? "defined" : "undefined")
                .append(".to.").append(coming.getSchema() == null ? "defined" : "undefined").append(", ");
        }
        return sb;
    }

    //SUGGEST: make null explained in the same way as for parameters null is undefined and as such it does not state client expectations clearly
    private StringBuilder appendPropertyDiffs(StringBuilder sb, Property existing, Property future, String context) {
        appendRequired(sb, existing, future, context);
        appendType(sb, existing, future, context);
        appendAllowEmpty(sb, existing, future, context);
        appendReadOnly(sb, existing, future);
        appendAccess(sb, existing, future);
        appendFormat(sb, existing, future);
        return sb;
    }

    private void appendFormat(StringBuilder sb, Property existing, Property future) {
        if (future.getFormat() != null && !future.getFormat().equals(existing.getFormat())) {
            sb.append("format.changed.from.").append(existing.getFormat()).append(".to.").append(future.getFormat()).append(", ");
        }
    }

    private void appendAccess(StringBuilder sb, Property existing, Property future) {
        if (future.getAccess() != null && !future.getAccess().equals(existing.getAccess())) {
            sb.append("access.changed.from.").append(existing.getAccess()).append(".to.").append(future.getAccess()).append(", ");
        }
    }

    private void appendReadOnly(StringBuilder sb, Property existing, Property future) {
        if (future.getReadOnly() != null && existing.getReadOnly() != null && !future.getReadOnly() == (existing.getReadOnly())) {
            sb.append("readonly.changed.from.").append(existing.getReadOnly()).append(".to.").append(future.getReadOnly()).append(", ");
        } else if ((future.getReadOnly() == null) && existing.getReadOnly() != null) {
            sb.append("readonly.changed.from.").append(existing.getReadOnly()).append(".to.").append(future.getReadOnly()).append(", ");
        }
    }

    private void appendAllowEmpty(StringBuilder sb, Property existing, Property future, String context) {
        if (future.getAllowEmptyValue() != null && !future.getAllowEmptyValue().equals(existing.getAllowEmptyValue())) {
            String str = "emptyAllowed.changed.from." + existing.getAllowEmptyValue() + ".to." + future.getAllowEmptyValue();
            sb.append(str).append(", ");
            if (!future.getAllowEmptyValue() && existing.getAllowEmptyValue()) {
                breaking.put(context, context + str);
            }
        }
    }

    private void appendType(StringBuilder sb, Property existing, Property future, String context) {
        if (future.getType() != null && !future.getType().equals(existing.getType())) {
            String str = "type.changed.from." + existing.getType() + ".to." + future.getType();
            sb.append(str).append(", ");
            potentiallyBreaking.put(context, context + str);
        }
    }

    private void appendRequired(StringBuilder sb, Property existing, Property future, String context) {
        if (!future.getRequired() == (existing.getRequired())) {
            String str = "required.changed.from." + existing.getRequired() + ".to." + future.getRequired();
            sb.append(str).append(", ");
            if (future.getRequired() && !existing.getRequired()) {
                breaking.put(context, context + str);
            }
        }
    }

    private StringBuilder appendStringPropertyDiffs(StringBuilder sb, Property existing, Property future, String context) {
        StringProperty xisting = (StringProperty) existing;
        StringProperty coming = (StringProperty) future;
        if (coming.getPattern() != null && !coming.getPattern().equals(xisting.getPattern())) {
            String str = "pattern.changed.from." + xisting.getPattern() + ".to." + coming.getPattern();
            sb.append(str).append(", ");
            potentiallyBreaking.put(context, context + str);
        }
        if (coming.getMaxLength() != null && xisting.getMaxLength() != null && coming.getMaxLength() > xisting.getMaxLength()) {
            String str = "maxlength.changed.from." + xisting.getMaxLength() + ".to." + coming.getMaxLength();
            sb.append(str).append(", ");
            potentiallyBreaking.put(context, context + str);
        }
        if (coming.getMaxLength() != null && xisting.getMaxLength() != null && coming.getMaxLength() < xisting.getMaxLength()) {
            String str = "maxlength.changed.from." + xisting.getMaxLength() + ".to." + coming.getMaxLength();
            sb.append(str).append(", ");
            breaking.put(context, context + str);
        }
        if (coming.getMinLength() != null && xisting.getMinLength() != null && coming.getMinLength() < xisting.getMinLength()) {
            String str = "minlength.changed.from." + xisting.getMinLength() + ".to." + coming.getMinLength();
            sb.append(str).append(", ");
            potentiallyBreaking.put(context, context + str);
        }
        if (coming.getMinLength() != null && xisting.getMinLength() != null && coming.getMinLength() > xisting.getMinLength()) {
            String str = "minlength.changed.from." + xisting.getMinLength() + ".to." + coming.getMinLength();
            sb.append(str).append(", ");
            breaking.put(context, context + str);
        }
        return sb;
    }

    private boolean isCompliant(Property existing, Property future) {
        boolean isCompliant = existing.equals(future);
        if (isCompliant) {
            isCompliant = existing.getRequired() == future.getRequired();
            //SUGGEST: this could be suggested as a part of the equals method in the swagger implementation
        }
        return isCompliant;
    }

    private Collection<? extends ScopedProperty> convert2ElProperties(
        Map<String, Property> propMap, String parentEl, boolean existingProperty) {
        List<ScopedProperty> result = new ArrayList<>();
        if (null == propMap) return result;
        for (Entry<String, Property> entry : propMap.entrySet()) {
            String propName = entry.getKey();
            Property property = entry.getValue();
            if (property instanceof RefProperty) {
                String ref = ((RefProperty) property).getSimpleRef();
                io.swagger.models.Model model = existingProperty ? existingAPI.get(ref)
                    : futureAPI.get(ref);
                if (model != null) {
                    Map<String, Property> properties = model.getProperties();
                    result.addAll(
                        convert2ElProperties(properties,
                            null == parentEl ? propName
                                : (parentEl + "." + propName),
                            existingProperty));
                }
            } else {
                ScopedProperty pWithPath = convert2ELProperty(parentEl, propName, property);
                result.add(pWithPath);
            }
        }
        return result;
    }

    private ScopedProperty convert2ELProperty(String parentEl, String propName, Property property) {
        String el = null == parentEl ? propName : (parentEl + "." + propName);
        return new ScopedProperty(el, property);
    }

}
