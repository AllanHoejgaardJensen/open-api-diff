package dk.hoejgaard.openapi.diff.compare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains a finite set of differences between an existing and a future parameter.
 */
public class ParameterChanges {
    private static Logger logger = LoggerFactory.getLogger(ParameterChanges.class);

    private final boolean isRequiredChanged;
    private final boolean isDescriptionChanged;
    private final boolean isAccessChanged;
    private final boolean isPatternChanged;
    private final boolean isMaximumChanged;
    private final boolean isMinimumChanged;
    private final boolean isMaximumLengthChanged;
    private final boolean isMinimumLengthChanged;
    private final boolean isDefaultChanged;
    private final boolean isAllowEmptyChanged;
    private final boolean isTypeChanged;
    private final boolean isFormatChanged;

    private final Parameter existing;
    private final Parameter future;
    private final String scope;
    private final Diff diffDepth;
    private List<ScopedProperty> added = new ArrayList<>();
    private List<ScopedProperty> removed = new ArrayList<>();

    private Map<String, List<String>> changes = new HashMap<>();
    private Map<String, List<String>> flawedDefines = new HashMap<>();
    private Map<String, List<String>> potentiallyBreaking = new HashMap<>();
    private Map<String, List<String>> breaking = new HashMap<>();

    /**
     * @param existing parameter, which is non null
     * @param future   parameter, which is non null
     * @param depth    which is non null
     */
    public ParameterChanges(Parameter existing, Parameter future, Diff depth) {
        Objects.requireNonNull(existing);
        Objects.requireNonNull(future);
        Objects.requireNonNull(depth);
        this.existing = existing;
        this.future = future;
        this.scope = findScope();
        this.isRequiredChanged = hasRequiredChanged();
        this.isDescriptionChanged = hasDescriptionChanged();
        this.isPatternChanged = hasPatternChanged();
        this.isMaximumChanged = hasMaximumChanged();
        this.isMinimumChanged = hasMinimumChanged();
        this.isMaximumLengthChanged = hasMaximumLengthChanged();
        this.isMinimumLengthChanged = hasMinimumLengthChanged();
        this.isDefaultChanged = hasDefaultChanged();
        this.isAllowEmptyChanged = hasAllowEmptyChanged();
        this.isTypeChanged = hasTypeChanged();
        this.isAccessChanged = hasAccessChanged();
        this.isFormatChanged = hasFormatChanged();
        this.diffDepth = depth;
    }

    public Parameter getExisting() {
        return existing;
    }

    public Parameter getFuture() {
        return future;
    }

    public boolean isRequiredChanged() {
        return isRequiredChanged;
    }

    public boolean isDescriptionChanged() {
        return isDescriptionChanged;
    }

    public boolean isPatternChanged() {
        return isPatternChanged;
    }

    public boolean isMaximumChanged() {
        return isMaximumChanged;
    }

    public boolean isMinimumChanged() {
        return isMinimumChanged;
    }

    public boolean isMaximumLengthChanged() {
        return isMaximumLengthChanged;
    }

    public boolean isMinimumLengthChanged() {
        return isMinimumLengthChanged;
    }

    public boolean isDefaultChanged() {
        return isDefaultChanged;
    }

    public boolean isAllowEmptyChanged() {
        return isAllowEmptyChanged;
    }

    public boolean isTypeChanged() {
        return isTypeChanged;
    }

    public boolean isAccessChanged() {
        return isAccessChanged;
    }

    public boolean isFormatChanged() {
        return isFormatChanged;
    }

    /**
     * @return the parameters added to the future specification of the API.
     */
    public List<ScopedProperty> getAddedParams() {
        return added;
    }

    /**
     * @return the parameters removed from the future specification of the API.
     */
    public List<ScopedProperty> getRemovedParams() {
        return removed;
    }

    /**
     * @return true if there were any recorded differences between the existing and the future specification
     */
    public boolean containsDiff() {
        if (Diff.POTENTIALLY_BREAKING.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                breakingObserved() || !potentiallyBreaking.isEmpty();
        } else if (Diff.BREAKING.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                breakingObserved();
        } else if (Diff.LAISSEZ_FAIRE.equals(diffDepth)) {
            return anyParameterChanges() || anyThingAddedOrRemoved() ||
                !breaking.isEmpty();
        }
        return anyParameterChanges() || anyThingAddedOrRemoved() || anyObservations();
    }

    /**
     * @return a map with change points and the breaking changes recorded for each point
     */
    public Map<String, List<String>> getBreaking() {
        return Collections.unmodifiableMap(breaking);
    }

    /**
     * @return a map with change points and the potentially breaking changes recorded for each point
     */
    public Map<String, List<String>> getPotentiallyBreaking() {
        return Collections.unmodifiableMap(potentiallyBreaking);
    }

    /**
     * @return a map with change points and the changes recorded for each point
     */
    public Map<String, List<String>> getChanges() {
        return changes;
    }

    /**
     * @return a map with change points and the lack of definitions for each point
     */
    public Map<String, List<String>> getFlawedDefines() {
        return flawedDefines;
    }

    /**
     * a breaking change is detected and thus the clients will struggle with the new version of the API
     *
     * @param origin      where the flaw is found
     * @param information what the flaw is
     */
    public void addBreakingChange(String origin, String information) {
        if (breaking.containsKey(origin)) {
            if (!breaking.get(origin).contains(information))
                breaking.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            breaking.put(origin, originList);
        }
    }

    /**
     * a potentially breaking change is detected and thus some clients will likely struggle with the new version of the API
     *
     * @param origin      where the flaw is found
     * @param information what the flaw is
     */
    public void addPotentialBreakingChange(String origin, String information) {
        if (potentiallyBreaking.containsKey(origin)) {
            if (!potentiallyBreaking.get(origin).contains(information))
                potentiallyBreaking.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            potentiallyBreaking.put(origin, originList);
        }
    }

    /**
     * a change is detected and thus the API changes at a given point
     *
     * @param change      where the flaw is found
     * @param information what the flaw is
     */

    public void addRecordedChange(String change, String information) {
        if (changes.containsKey(change)) {
            if (!changes.get(change).contains(information)) {
                changes.get(change).add(information);
            }
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            changes.put(change, originList);
        }
    }

    /**
     * a definition flaw is characterized by a definition that makes it hard to deliver deterministic future proof design for an API
     *
     * @param origin      where the flaw is found
     * @param information what the flaw is
     */
    public void addDefinitionFlaw(String origin, String information) {
        if (flawedDefines.containsKey(origin)) {
            if (!flawedDefines.get(origin).contains(information))
                flawedDefines.get(origin).add(information);
        } else {
            List<String> originList = new ArrayList<>();
            originList.add(information);
            flawedDefines.put(origin, originList);
        }
    }

    private boolean anyObservations() {
        return breakingObserved() || !potentiallyBreaking.isEmpty() ||
            !flawedDefines.isEmpty();
    }

    private boolean breakingObserved() {
        return !breaking.isEmpty() || !changes.isEmpty();
    }

    private boolean anyParameterChanges() {
        return isRequiredChanged || isDescriptionChanged;
    }

    private boolean anyThingAddedOrRemoved() {
        return !added.isEmpty() || !removed.isEmpty();
    }

    private String empty(final CharSequence cs) {
        if (StringUtils.isBlank(cs))
            return "";
        return cs.toString();
    }

    private boolean hasRequiredChanged() {
        return existing.getRequired() != future.getRequired();
    }

    private boolean hasDescriptionChanged() {
        return !empty(existing.getDescription()).equals(empty(future.getDescription()));
    }

    public boolean hasPatternChanged() {
        if (existing.getPattern() != null) {
            if (!existing.getPattern().equals(future.getPattern())) {
                handlePattern();
                return true;
            }
        } else if (future.getPattern() != null) {
            if (!future.getPattern().equals(existing.getPattern())) {
                handlePattern();
                return true;
            }
        }
        return false;
    }

    private boolean hasTypeChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getType() != null) {
            if (!e.getType().equals(f.getType())) {
                handleType();
                return true;
            }
        } else if (f.getType() != null) {
            if (!f.getType().equals(e.getType())) {
                handleType();
                return true;
            }
        }
        return false;
    }

    private boolean hasAllowEmptyChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getAllowEmptyValue() != null) {
            if (!(e.getAllowEmptyValue().equals(f.getAllowEmptyValue()))) {
                handleAllowEmptyValue();
                return true;
            }
        } else if (f.getAllowEmptyValue() != null) {
            handleAllowEmptyValue();
            return true;
        }
        return false;
    }

    private boolean hasDefaultChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getDefaultValue() != null) {
            if (!(e.getDefaultValue().equals(f.getDefaultValue()))) {
                handleDefaultValue();
                return true;
            }
        } else if (f.getDefaultValue() != null) {
            if (!(f.getDefaultValue().equals(e.getDefaultValue()))) {
                handleDefaultValue();
                return true;
            }
        }

        return false;
    }

    private boolean hasMinimumLengthChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getMinLength() != null) {
            if (!(e.getMinLength().equals(f.getMinLength()))) {
                handleMinLength();
                return true;
            }
        } else if (f.getMinLength() != null) {
            handleMinLength();
            return true;
        }
        return false;
    }

    private boolean hasMaximumLengthChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getMaxLength() != null) {
            if (!(e.getMaxLength().equals(f.getMaxLength()))) {
                handleMaxLength();
                return true;
            }
        } else if (f.getMaxLength() != null) {
            handleMaxLength();
            return true;
        }
        return false;
    }

    private boolean hasMinimumChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getMinimum() != null) {
            if (!e.getMinimum().equals(f.getMinimum())) {
                handleMinimum();
                return true;
            }
        } else if (f.getMinimum() != null) {
            if (!f.getMinimum().equals(e.getMinimum())) {
                handleMinimum();
                return true;
            }
        }
        return false;
    }

    private boolean hasMaximumChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;

        if (e.getMaximum() != null) {
            if (!e.getMaximum().equals(f.getMaximum())) {
                handleMaximum();
                return true;
            }
        } else if (f.getMaximum() != null) {
            if (!f.getMaximum().equals(e.getMaximum())) {
                handleMaximum();
                return true;
            }
        }
        return false;
    }

    private boolean hasAccessChanged() {
        if (existing.getAccess() != null) {
            return !existing.getAccess().equals(future.getAccess());
        }
        if (future.getAccess() != null) {
            return !future.getAccess().equals(existing.getAccess());
        }
        return false;
    }

    private boolean hasFormatChanged() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return false;
        if (e.getFormat() != null) {
            return !e.getFormat().equals(f.getFormat());
        }
        if (f.getFormat() != null) {
            return !f.getFormat().equals(e.getFormat());
        }
        return false;
    }

    private void handleAllowEmptyValue() {
        String cause = ".allowemptyvalue.changed.from." +
            existing.getAllowEmptyValue() + ".to." + future.getAllowEmptyValue();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause);
        logger.trace("{} - {}", scope, cause);
        if (existing.getAllowEmptyValue() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.allowemptyvalue.defined.as.null::makes future proof api design difficult");
        }
        if (future.getAllowEmptyValue() == null) {
            addDefinitionFlaw(scope, scope + " .allowemptyvalue.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handlePattern() {
        String cause = "pattern.changed.from." + existing.getPattern() + ".to." + future.getPattern();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause); //difficult to know when a regexp is a subset of an existing one
        if (existing.getPattern() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.pattern.defined.as.null::makes future proof api design difficult");
        }
        if (future.getPattern() == null) {
            addDefinitionFlaw(scope, scope + " .pattern.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handleType() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return;
        String cause = ".type.changed.from." + e.getType() + ".to." + f.getType();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause); //difficult to know when a type is compatible with the existing one
        if (e.getType() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.type.defined.as.null::makes future proof api design difficult");
        }
        if (f.getType() == null) {
            addDefinitionFlaw(scope, scope + " .type.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handleMaximum() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return;
        String cause = ".maximumvalue.changed.from." + e.getMaximum() + ".to." + f.getMaximum();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause);
        if (f.getMaximum() != null && e.getMaximum() != null) {
            if (f.getMaximum().subtract(e.getMaximum()).longValue() < 0) { //it now demands lesser max
                addBreakingChange(scope, cause);
            }
        }
        if (e.getMaximum() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.maximumvalue.defined.as.null::makes future proof api design difficult");
        }
        if (f.getMaximum() == null) {
            addDefinitionFlaw(scope, scope + " .maximumvalue.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handleMinimum() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return;

        String cause = ".mimimumvalue.changed.from." + e.getMinimum() + ".to." + f.getMaximum();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause);
        if (f.getMinimum() != null && (e.getMinimum() != null)) {
            if (f.getMinimum().subtract(e.getMinimum()).longValue() > 0) { //it now demands longer min
                addBreakingChange(scope, cause);
            }
        }
        if (e.getMinimum() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.mimimumvalue.defined.as.null::makes future proof api design difficult");
        }
        if (f.getMinimum() == null) {
            addDefinitionFlaw(scope, scope + " .mimimumvalue.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handleMaxLength() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return;

        String cause = ".maxlength.changed.from." + e.getMaxLength()
            + ".to." + f.getMaxLength();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause);
        if (f.getMaxLength() != null && e.getMaxLength() != null) {
            if (e.getMaxLength() > f.getMaxLength()) { //it now demands lesser max
                addBreakingChange(scope, cause);
            }
        }
        if (e.getMaxLength() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.maxlength.defined.as.null::makes future proof api design difficult");
        }
        if (f.getMaxLength() == null) {
            addDefinitionFlaw(scope, scope + " .maxlength.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handleMinLength() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return;
        String cause = ".minlength.changed.from." +
            e.getMinLength() + ".to." + f.getMinLength();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause);
        if (f.getMinLength() != null) {
            if (f.getMinLength() > e.getMinLength()) { //it now demands longer min
                addBreakingChange(scope, cause);
            }
        }
        if (e.getMinLength() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.minlength.defined.as.null::makes future proof api design difficult");
        }
        if (f.getMinLength() == null) {
            addDefinitionFlaw(scope, scope + " .minlength.defined.as.null::makes future proof api design difficult");
        }
    }

    private void handleDefaultValue() {
        AbstractSerializableParameter e;
        AbstractSerializableParameter f;
        if (isAbstractSerializable()) {
            e = (AbstractSerializableParameter) existing;
            f = (AbstractSerializableParameter) future;
        } else return;
        String cause = ".defaultvalue.changed.from." +
            e.getDefaultValue() + ".to." + f.getDefaultValue();
        addRecordedChange(scope, cause);
        addPotentialBreakingChange(scope, cause);
        if (f.getDefaultValue() != null && (e.getDefaultValue() != null)) {
            BigDecimal fd = getValue(f);
            if (fd.subtract(e.getMinimum()).longValue() > 0) { //default lower than min
                addBreakingChange(scope, cause);
            }
            if (e.getMaximum().subtract(fd).longValue() > 0) { //default higher than max
                addBreakingChange(scope, cause);
            }
        }

        if (e.getMinimum() == null) {
            addDefinitionFlaw(scope, scope +
                " .existing.compliance.defaultvalue.defined.as.null::makes future proof api design difficult");
        }
        if (f.getMinimum() == null) {
            addDefinitionFlaw(scope, scope + " .defaultvalue.defined.as.null::makes future proof api design difficult");
        }
    }

    private String findScope() {
        if (existing instanceof PathParameter && future instanceof PathParameter) { //Path
            return "path." + future.getName();
        } else if (existing instanceof HeaderParameter && future instanceof HeaderParameter) { //Header
            return "header." + future.getName();
        } else if (existing instanceof QueryParameter && future instanceof QueryParameter) { //Query
            return "query." + future.getName();
        } else if (existing instanceof FormParameter && future instanceof FormParameter) { //Form
            return "form." + future.getName();
        } else if (existing instanceof CookieParameter && future instanceof CookieParameter) { //Cookie
            return "cookie." + future.getName();
        }
        return "";
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ParameterChanges) {
            if (existing.equals(future)) {
                return existing.getRequired() == future.getRequired();
            }
        }
        return false;
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

    private boolean isAbstractSerializable() {
        return existing instanceof AbstractSerializableParameter && future instanceof AbstractSerializableParameter;
    }
}
