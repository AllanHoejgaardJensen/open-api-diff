package dk.hoejgaard.openapi.diff.output;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

import dk.hoejgaard.openapi.diff.APIDiff;
import dk.hoejgaard.openapi.diff.compare.OperationDiff;
import dk.hoejgaard.openapi.diff.compare.ParameterChanges;
import dk.hoejgaard.openapi.diff.compare.PropertyChanges;
import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.compare.ResponseChanges;
import dk.hoejgaard.openapi.diff.model.Endpoint;
import dk.hoejgaard.openapi.diff.model.ScopedProperty;
import io.swagger.models.HttpMethod;
import io.swagger.models.Response;
import io.swagger.models.parameters.Parameter;
import j2html.tags.ContainerTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renders output from a compatibility into a HTML format
 * <p>
 * See samples from the report in the readme.md file in this project
 */
public class HtmlRender implements OutputRender {
    private static Logger logger = LoggerFactory.getLogger(HtmlRender.class);
    private final String inlinedStyle;
    private String title = "API change log";
    private String subTitle = "Details";
    private String reference = "Reference";
    private String candidate = "Candidate";

    /**
     * @param title the title of the report
     * @param subTitle the subtitle of the report
     * @param reference the API specification for the existing API
     * @param candidate the API specification for the future candidate API
     */
    public HtmlRender(String title, String subTitle, String reference, String candidate) {
        inlinedStyle = this.getStyles();
        this.title = title;
        this.subTitle = subTitle;
        this.reference = reference;
        this.candidate = candidate;
        logger.info("Rendering an HTML Report for API differences having the title: {} and subtitle: {} for reference API: {} " +
            "and comparing to future API: {}", new Object[] {title, subTitle, reference, candidate});
    }

    /**
     * @param diff the difference report containing the comparison between the existing and the future candidate API
     * @return a complete rendered report
     */
    public String render(APIDiff diff) {
        ContainerTag addedEndpoints = addedEndpoints(diff);
        ContainerTag removedEndpoints = removedEndpoints(diff);
        ContainerTag changedEndpoints = changedEndpoints(diff);
        ContainerTag elaboratedChangedResources = elaboratedChangedResources(diff);
        ContainerTag nonCompliantResources = nonCompliantEndpoints(diff);

        ContainerTag html = html().attr("lang", "en").with(
            head().with(
                meta().withCharset("utf-8"),
                title(title),
                link().withRel("shortcut icon")
                    .withType("image/png")
                    .withHref("https://www.openapis.org/wp-content/uploads/2016/11/favicon.png"),
                style().withText(inlinedStyle)
            ),
            body().with(
                header().with(h1(title)),
                header().with(h2(subTitle).withClass("subtitle")),
                div().withClass("intro").with(
                    p("The report layout uses three sections: short, elaborate and compliance"),
                    p("The reports shows the added endpoints, the removed and the changed endpoints on a short form."),
                    p("The changed endpoints are succeeding that presented in a more elaborate form")
                ),
                p("The syntax used is:").withClass("syntaxheader"),
                div().with(
                    p().with(span(" \" (+) \" means added.")),
                    p().with(span(" \" (-) \" means removed.")),
                    p().with(span(" \" (@) \" means altered.")),
                    p().with(span(" \" (!) \" means issue found in Existing API.")),
                    p().with(span(" \" (>) \" means issues found in the New API.")),
                    p().with(span(" \" (C) \" means (Compliance)."))).withClass("syntax"),
                div().with(
                    p().with(span(" The APIs compared are:")),
                    p().with(span(" \" (!) \" " + reference)),
                    p().with(span(" \" (>) \" " + candidate))).withClass("syntax"),
                div().withClass("overview").with(
                    div().with(h2("Added Endpoints"), hr(), addedEndpoints),
                    div().with(h2("Removed Endpoints"), hr(), removedEndpoints),
                    div().with(h2("Changed or Observable Endpoints"), hr(), changedEndpoints),
                    div().with(h2("The Elaborated Report for Changed or Observable Endpoints").withClass("section"),
                        hr(), elaboratedChangedResources),
                    div().with(h2("The Elaborated Compliance Report for Changed or Observable Endpoints").withClass("section"),
                        hr(), nonCompliantResources)
                )
            )
        );

        return document().render() + html.render();
    }

    private ContainerTag addedEndpoints(APIDiff diff) {
        List<Endpoint> added = diff.getAddedEndpoints();
        if (added.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no observations of added endpoints"));
        }
        return buildEndpointTabular(added);
    }

    private ContainerTag buildEndpointTabular(List<Endpoint> added) {
        ContainerTag t = table().withClass("tabular");
        for (Endpoint endpoint : added) {
            String verb = endpoint.getVerb().toString();
            t.with(
                tr().with(
                    td().withClass("verb").with(span(verb).withClass(verb)),
                    td().withText(endpoint.getPathUrl() + " ").withClass("observation"),
                    td().with(span(endpoint.getSummary()).withClass("summary"))
                ));
        }
        return t;
    }

    private ContainerTag removedEndpoints(APIDiff diff) {
        List<Endpoint> missing = diff.getMissingEndpoints();
        if (missing.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no observations of removed endpoints"));
        }
        return buildEndpointTabular(missing);
    }

    private ContainerTag changedEndpoints(APIDiff diff) {
        List<Endpoint> changed = diff.getChangedEndpoints();
        if (changed.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no observations of changed endpoints"));
        }
        return buildEndpointTabular(changed);
    }

    private ContainerTag elaboratedChangedResources(APIDiff diff) {
        List<ResourceDiff> ec = diff.getChangedResourceDiffs();
        if (ec.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no elaborate observation of changed endpoints"));
        }
        ContainerTag div = div();
        for (ResourceDiff resource : ec) {
            Map<HttpMethod, OperationDiff> changedOperations = resource.getChangedOperations();
            if (changedOperations.isEmpty()) {
                return ol().with(li().withClass("noobservation").withText("no observations of changed operations(verbs)"));
            }
            ContainerTag resourceContainer = resourceContainer(changedOperations, resource);
            div.with(
                ol().with(li().withClass("distance")
                    .with(span("Operations for:").withClass("operations"))
                    .withText(" - ")
                    .with(span(resource.getPathUrl()).withClass("operations"))),
                div().with(resourceContainer));
        }
        return div;
    }

    private ContainerTag resourceContainer(Map<HttpMethod, OperationDiff> changedOperations, ResourceDiff resource) {
        ContainerTag div = div();
        for (Map.Entry<HttpMethod, OperationDiff> entry : changedOperations.entrySet()) {
            OperationDiff oprDiff = entry.getValue();
            ContainerTag breaking = breaking(oprDiff);
            ContainerTag potentiallyBreaking = potentiallyBreaking(oprDiff);
            div.with(
                ol().with(li()
                    .with(span(entry.getKey().toString()).withClass(entry.getKey().toString()))
                    .withText(resource.getPathUrl() + "   compliance: ")
                    .with(span(String.valueOf(oprDiff.isCompliant())).withClass(String.valueOf(oprDiff.isCompliant())))),
                div().with(h3("Breaking Changes"), breaking),
                div().with(h3("Potentially Breaking Changes"), potentiallyBreaking)
            );
        }
        return div;
    }

    private ContainerTag breaking(OperationDiff oprDiff) {
        Map<String, List<String>> breaks = oprDiff.getBreakingChanges();
        if (breaks.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no breaking observations"));
        }
        ContainerTag ol = ol().withClass("tabular");
        for (String observation : breaks.keySet()) {
            String style = "observation";
            String reportedObservation;
            if (observation.contains("existing.compliance")) {
                reportedObservation = "(!) " + outX(observation);
                style = "existingobservation";
            } else {
                reportedObservation = "(>) " + out(observation);
            }
            ContainerTag observedIncidents = observedIncidents(breaks, observation);
            ol.with(li(reportedObservation).withClass(style), observedIncidents);
        }
        return ol;
    }

    private ContainerTag observedIncidents(Map<String, List<String>> breaks, String observation) {
        List<String> incidents = breaks.get(observation);
        ContainerTag li = li();
        for (String incident : incidents) {
            li.withText(incident).withClass("observationincident");
        }
        return li;
    }

    private ContainerTag potentiallyBreaking(OperationDiff oprDiff) {
        Map<String, List<String>> breaks = oprDiff.getPotentiallyBreakingChanges();
        if (breaks.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no potentially breaking observations "));
        }
        ContainerTag ol = ol().withClass("tabular");
        for (String observation : breaks.keySet()) {
            String style = "observation";
            String reportedObservation;
            if (observation.contains("existing.compliance")) {
                reportedObservation = "(!) " + outX(observation);
                style = "existingobservation";
                ContainerTag observedIncidents = observedIncidents(breaks, observation);
                ol.with(li().with(span(reportedObservation).withClass(style)), observedIncidents);
            } else {
                reportedObservation = "(>) " + out(observation);
                ContainerTag observedIncidents = observedIncidents(breaks, observation);
                ol.with(li().withClass(style).with(span(reportedObservation)), observedIncidents);
            }
        }
        return ol;
    }

    private ContainerTag nonCompliantEndpoints(APIDiff diff) {
        List<ResourceDiff> ec = diff.getAllDiffs();
        if (ec.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no compliance observations"));
        }
        ContainerTag div = div();
        for (ResourceDiff resource : ec) {
            Map<HttpMethod, OperationDiff> changedOperations = resource.getChangedOperations();
            ContainerTag resourceContainer = elaboratedResourceContainer(changedOperations, resource);
            div.with(
                ol().with(li()
                    .with(span(resource.getPathUrl()).withClass("operations"))
                    .withText(" - ")
                    .with(span("operations:").withClass("operations"))),
                div().with(resourceContainer));
        }
        return div;
    }

    private ContainerTag elaboratedResourceContainer(Map<HttpMethod, OperationDiff> changedOperations, ResourceDiff resource) {
        ContainerTag div = div().withClass("improvements");
        if (changedOperations.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no observations"));
        }
        for (Map.Entry<HttpMethod, OperationDiff> entry : changedOperations.entrySet()) {
            OperationDiff oprDiff = entry.getValue();
            ContainerTag breaking = breaking(oprDiff);
            ContainerTag potentiallyBreaking = potentiallyBreaking(oprDiff);
            ContainerTag recordedChanges = recordedChanges(oprDiff);
            ContainerTag recordedFlawObservations = recordedFlawObservations(oprDiff);
            ContainerTag recordedContentTypeObservations = recordedContentTypeObservations(oprDiff);
            ContainerTag recordedParameterObservations = recordedParameterObservations(oprDiff);
            ContainerTag recordedPropertiesObservations = recordedPropertiesObservations(oprDiff);
            ContainerTag recordedResponsesObservations = recordedResponsesObservations(oprDiff);
            div.with(
                h2("Design Issues"),
                ol().with(li()
                    .withText(" for ")
                    .with(span(entry.getKey().toString()).withClass(entry.getKey().toString()))
                    .withText(resource.getPathUrl() + "   compliance: ")
                    .with(span(String.valueOf(oprDiff.isCompliant())).withClass(String.valueOf(oprDiff.isCompliant())))),
                div().with(h3("Breaking Changes"), breaking),
                div().with(h3("Potentially Breaking Changes"), potentiallyBreaking),
                div().with(h3("Changes"), recordedChanges),
                div().with(h3("Improvements to existing API"), recordedFlawObservations),
                div().with(h3("Recorded Changes to Content-Types in API"), recordedContentTypeObservations),
                div().with(h3("Recorded Changes to Parameters in API"), recordedParameterObservations),
                div().with(h3("Recorded Changes to Properties in API"), recordedPropertiesObservations),
                div().with(h3("Recorded Changes to Responses in API"), recordedResponsesObservations)
            ).withClass("designissues");
        }
        return div;
    }

    private ContainerTag recordedChanges(OperationDiff oprDiff) {
        Map<String, List<String>> changes = oprDiff.getChanges();
        if (changes.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no recorded change observations "));
        }
        ContainerTag ol = ol().withClass("tabular");
        for (String observation : changes.keySet()) {
            String style = "observation";
            String reportedObservation;
            if (observation.contains("existing.compliance")) {
                reportedObservation = "(!) " + outX(observation);
                style = "existingobservation";
            } else {
                reportedObservation = "(>) " + out(observation);
            }
            ContainerTag observedIncidents = observedIncidents(changes, observation);
            ol.with(li().with(span(reportedObservation).withClass(style)), observedIncidents);
        }
        return ol;
    }

    private ContainerTag recordedFlawObservations(OperationDiff oprDiff) {
        Map<String, List<String>> changes = oprDiff.getExistingFlaws();
        if (changes.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no potential design flaw observations "));
        }
        ContainerTag ol = ol().withClass("tabular");
        for (String observation : changes.keySet()) {
            String style = "observation";
            String difKey = "existing.difference.recorded";
            String reportedObservation;
            if (observation.contains("existing.compliance")) {
                reportedObservation = "(!) " + outX(observation);
                style = "existingobservation";
            } else if (observation.contains(difKey)) {
                reportedObservation = "(!) " + out(observation.substring(difKey.length()));
                style = "existingobservation";
            } else {
                reportedObservation = "(>) " + out(observation);
            }
            ContainerTag observedIncidents = observedIncidents(changes, observation);
            ol.with(li().with(span(reportedObservation).withClass(style)), observedIncidents);
        }
        return ol;
    }

    private ContainerTag recordedContentTypeObservations(OperationDiff oprDiff) {
        List<String> added = oprDiff.getAddedContentTypes();
        List<String> removed = oprDiff.getMissingContentTypes();
        if (added.isEmpty() && removed.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no Content-Type observations "));
        }
        ContainerTag contentTypes = div();
        ContainerTag addedIncidents = observedIncidents(added, "(>) Content-Type Added");
        ContainerTag removedIncidents = observedIncidents(removed, "(!) Content-Type Removed");
        contentTypes.with(addedIncidents, removedIncidents);
        return contentTypes;
    }

    private ContainerTag observedIncidents(List<String> incidents, String scope) {
        ContainerTag ol = ol();
        if (!incidents.isEmpty()) {
            String style = "incident";
            if (scope.contains("(!)")) {
                style = "existingobservation";
            }
            ol.withClass("tabular").with(li(scope).withClass(style));
        }
        for (String incident : incidents) {
            ol.with(li().withText(incident).withClass("observationincident"));
        }
        return ol;
    }

    private ContainerTag recordedParameterObservations(OperationDiff oprDiff) {
        List<Parameter> added = oprDiff.getAddedParameters();
        List<Parameter> removed = oprDiff.getMissingParameters();
        List<ParameterChanges> changed = oprDiff.getChangedParameters();
        if (added.isEmpty() && removed.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no Parameter observations "));
        }
        ContainerTag contentTypes = div();
        ContainerTag addedParameters = observedParameterIncidents(added, " (>) Parameter Added");
        ContainerTag removedParameters = observedParameterIncidents(removed, " (!) Parameter Removed");
        ContainerTag changedParameters = observedParameterChanges(changed, " (@) Parameter Changed");
        contentTypes.with(addedParameters, removedParameters, changedParameters);
        return contentTypes;
    }

    private ContainerTag observedParameterIncidents(List<Parameter> incidents, String scope) {
        ContainerTag ol = ol();
        String style = "incident";
        if (scope.contains("(!)")) {
            style = "existingobservation";
        }
        if (!incidents.isEmpty()) {
            ol.withClass("tabular").with(li(scope).withClass(style));
            for (Parameter incident : incidents) {
                ol.with(
                    li().with(span(incident.getName()).withClass("observation")),
                    li().withClass("observationincident").with(span(incident.getName() + " is " +
                        (incident.getRequired() ? "is Required" : " is Optional"))));
            }
        }
        return ol;
    }

    private ContainerTag observedParameterChanges(List<ParameterChanges> changed, String scope) {
        ContainerTag container = div();
        if (changed.isEmpty()) {
            return div();
        }
        ContainerTag breakingCT = observedBreakingParameterChanges(changed);
        ContainerTag potentialBreakingCT = observedPotentialBreakingParameterChanges(changed);
        ContainerTag flawsCT = observedFlawedParameterChanges(changed);
        String style = "incident";
        if (scope.contains("(!)")) {
            style = "existingobservation";
        }
        container.with(
            div().with(ol().withClass("tabular").with(li(scope).withClass(style))),
            div().with(breakingCT, potentialBreakingCT, flawsCT));
        return container;
    }

    private ContainerTag observedFlawedParameterChanges(List<ParameterChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Design Improvements Observed"),
            inner);

        for (ParameterChanges parameterChanges : changed) {
            Map<String, List<String>> pChanges = parameterChanges.getFlawedDefines();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            } else {
                return container;
            }
        }
        return container;
    }

    private ContainerTag observedPotentialBreakingParameterChanges(List<ParameterChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Potentially Breaking Changes Observed"),
            inner);
        for (ParameterChanges parameterChanges : changed) {
            Map<String, List<String>> pChanges = parameterChanges.getPotentiallyBreaking();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            }
        }
        return container;
    }

    private ContainerTag observedBreakingParameterChanges(List<ParameterChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Breaking Changes Observed"),
            inner);
        for (ParameterChanges parameterChanges : changed) {
            Map<String, List<String>> pChanges = parameterChanges.getBreaking();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            }
        }
        return container;
    }

    private ContainerTag buildChangeObservation(Map<String, List<String>> pChanges) {
        ContainerTag div = div();
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            String difKey = "existing.difference.recorded";
            String observation = entry.getKey();
            String reportedObservation;
            if (observation.contains("existing.compliance")) {
                reportedObservation = "(!) " + outX(observation);
            } else if (observation.contains(difKey)) {
                reportedObservation = "(!) " + out(observation.substring(difKey.length()));
            } else {
                reportedObservation = "(>) " + out(observation);
            }
            List<String> incidents = entry.getValue();
            if (!incidents.isEmpty()) {
                div.with(observedParameterChangeIncidents(incidents, reportedObservation));
            }
        }
        return div;
    }

    private ContainerTag observedParameterChangeIncidents(List<String> incidents, String scope) {
        ContainerTag ol = ol();
        String style = "observation";
        if (scope.contains("(!)")) {
            style = "existingobservation";
        }
        ol.with(li().withText(scope).withClass(style));
        for (String incident : incidents) {
            ol.with(li().withText(incident).withClass("observationincident"));
        }
        return ol;
    }


    private ContainerTag recordedPropertiesObservations(OperationDiff oprDiff) {
        List<ScopedProperty> added = oprDiff.getAddedProperties();
        List<ScopedProperty> removed = oprDiff.getMissingProperties();
        List<PropertyChanges> changed = oprDiff.getChangedProperties();
        if (added.isEmpty() && removed.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no Properties observations "));
        }
        ContainerTag contentTypes = div();
        ContainerTag addedProperties = observedPropertyIncidents(added, " (>) Property Added");
        ContainerTag removedProperties = observedPropertyIncidents(removed, " (!) Property Removed");
        ContainerTag changedProperties = observedPropertyChanges(changed, " (@) Property Changed");
        contentTypes.with(addedProperties, removedProperties, changedProperties);
        return contentTypes;
    }

    private ContainerTag observedPropertyChanges(List<PropertyChanges> changed, String scope) {
        ContainerTag container = div();
        if (changed.isEmpty()) {
            return container;
        }
        ContainerTag breakingCT = observedBreakingPropertyChanges(changed);
        ContainerTag potentialBreakingCT = observedPotentialBreakingPropertyChanges(changed);
        ContainerTag flawsCT = observedFlawedPropertyChanges(changed);
        String style = "incident";
        if (scope.contains("(!)")) {
            style = "existingobservation";
        }
        container.with(
            div().with(ol().withClass("tabular").with(li(scope).withClass(style))),
            div().with(breakingCT, potentialBreakingCT, flawsCT));
        return container;
    }

    private ContainerTag observedFlawedPropertyChanges(List<PropertyChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Design Improvements Observed"),
            inner);

        for (PropertyChanges parameterChanges : changed) {
            Map<String, List<String>> pChanges = parameterChanges.getFlawedDefines();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            } else {
                return div();
            }
        }
        return container;
    }

    private ContainerTag observedPotentialBreakingPropertyChanges(List<PropertyChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Potentially Breaking Changes Observed"),
            inner);
        for (PropertyChanges parameterChanges : changed) {
            Map<String, List<String>> pChanges = parameterChanges.getPotentiallyBreaking();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            }
        }
        return container;
    }

    private ContainerTag observedBreakingPropertyChanges(List<PropertyChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Breaking Changes Observed"),
            inner);
        for (PropertyChanges parameterChanges : changed) {
            Map<String, List<String>> pChanges = parameterChanges.getBreaking();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            }
        }
        return container;
    }

    private ContainerTag observedPropertyIncidents(List<ScopedProperty> incidents, String scope) {
        ContainerTag ol = ol();
        String style = "incident";
        if (!incidents.isEmpty()) {
            if (scope.contains("(!)")) {
                style = "existingobservation";
            }
            ol.withClass("tabular").with(li(scope).withClass(style));
            for (ScopedProperty incident : incidents) {
                ol.with(
                    li().with(span(incident.getEl()).withClass(style)),
                    li().withClass("observationincident").with(span(incident.getEl() + " is " +
                        (incident.getProperty().getRequired() ? "is Required" : " is Optional"))));
            }
        }
        return ol;
    }

    private ContainerTag recordedResponsesObservations(OperationDiff oprDiff) {
        Map<String, Response> added = oprDiff.getAddedResponses();
        Map<String, Response> removed = oprDiff.getMissingResponses();
        List<ResponseChanges> changed = oprDiff.getChangedResponses();
        if (added.isEmpty() && removed.isEmpty() && changed.isEmpty()) {
            return ol().with(li().withClass("noobservation").withText("no Response observations "));
        }
        ContainerTag contentTypes = div();
        ContainerTag addedResponses = observedResponseIncidents(added, " (>) Responses Added");
        ContainerTag removedResponses = observedResponseIncidents(removed, " (!) Responses Removed");
        ContainerTag changedResponses = observedResponseChanges(changed, " (@) Responses Changed");
        contentTypes.with(addedResponses, removedResponses, changedResponses);
        return contentTypes;
    }

    private ContainerTag observedResponseChanges(List<ResponseChanges> changed, String scope) {
        if (changed.isEmpty()) {
            return div();
        }
        ContainerTag div = div();
        ContainerTag breakingCT = observedBreakingResponseChanges(changed);
        ContainerTag potentialBreakingCT = observedPotentialBreakingResponseChanges(changed);
        ContainerTag flawsCT = observedNewFlawedResponseChanges(changed);
        ContainerTag xflawsCT = observedExistingFlawedResponseChanges(changed);
        String style = "incident";
        if (scope.contains("(!)")) {
            style = "existingobservation";
        }
        div.with(
            div().with(ol().withClass("tabular").with(li(scope).withClass(style))),
            div().with(breakingCT, potentialBreakingCT, flawsCT, xflawsCT));
        return div;
    }


    private ContainerTag observedNewFlawedResponseChanges(List<ResponseChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Design Improvements Observed"),
            inner);

        for (ResponseChanges responseChanges : changed) {
            Map<String, List<String>> pChanges = responseChanges.getFlawedDefines();
            if (!pChanges.isEmpty()) {
                inner.with(buildChangeObservation(pChanges));
            } else {
                return div();
            }
        }
        return container;
    }

    private ContainerTag observedExistingFlawedResponseChanges(List<ResponseChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Design Improvements Observed"),
            inner);

        for (ResponseChanges responseChanges : changed) {
            Map<String, List<String>> rChanges = responseChanges.getExistingFlaws();
            if (!rChanges.isEmpty()) {
                inner.with(buildChangeObservation(rChanges));
            } else {
                return div();
            }
        }
        return container;
    }

    private ContainerTag observedPotentialBreakingResponseChanges(List<ResponseChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Potentially Breaking Changes Observed"),
            inner);
        for (ResponseChanges responseChanges : changed) {
            Map<String, List<String>> rChanges = responseChanges.getPotentiallyBreaking();
            if (!rChanges.isEmpty()) {
                inner.with(buildChangeObservation(rChanges));
            }
        }
        return container;
    }

    private ContainerTag observedBreakingResponseChanges(List<ResponseChanges> changed) {
        ContainerTag inner = div();
        ContainerTag container = div().with(
            h4("Breaking Changes Observed"),
            inner);
        for (ResponseChanges responseChanges : changed) {
            Map<String, List<String>> rChanges = responseChanges.getBreaking();
            if (!rChanges.isEmpty()) {
                inner.with(buildChangeObservation(rChanges));
            }
        }
        return container;
    }

    private ContainerTag observedResponseIncidents(Map<String, Response> incidents, String scope) {
        ContainerTag div = div();
        String style = "observation";
        if (!incidents.isEmpty()) {
            if (scope.contains("(!)")) {
                style = "existingobservation";
            }
            ContainerTag ol = ol();
            ol.with(li(scope).withClass(style));
            for (String incident : incidents.keySet()) {
                ol.with(li().with(span(incident).withClass(style)),
                    li().with(span(ResponseChanges.getCodeMsg(incident))).withClass("observationincident"));
            }
            div.with(ol);
        }
        return div;
    }

    private String out(String incident) {
        String result = incident.replace('.', ' ');
        result = toUpperCamelCase(result);
        return result;
    }

    private String outX(String incident) {
        int index = incident.indexOf("existing.compliance");
        int indexFor = incident.indexOf("existing.compliance.for");
        int indexObservation = incident.indexOf(".observation");
        String result;
        if ((indexFor >= 0) && (indexObservation >= 0) && indexFor + 23 < incident.length()) {
            result = incident.substring(indexFor + 23, indexObservation);
        } else if (index >= 0 && index + 19 < incident.length()) {
            result = incident.substring(index + 19, incident.length());
        } else {
            result = incident;
        }
        result = result.replace('.', ' ');
        result = toUpperCamelCase(result);
        return result;
    }

    private String toUpperCamelCase(String input) {
        String[] words = input.trim().split("\\W");
        for (String word : words) {
            if (word.length() < 1) {
                return input;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1, word.length())).append(" ");
        }
        return sb.toString();
    }

    private String getStyles() {
        return "body, h1, h2, h3, h4, h5, h6, hr, p, blockquote, form, fieldset, legend, button, input, textarea, " +
            "{ margin: 0; padding: 0; list-style: none;}  " +
            "body, button, input, select, textarea  { " +
            "    font: 16px/1.5 sans-serif, tahoma, arial, \\\\5b8b\\\\4f53; " +
            "    font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-weight: normal;}" +
            "hr { color: #dfdfdf;} " +
            "h1 {border-radius: 5px; margin: 10px 5px 10px 10px; padding: 70px 20px 50px 20px; color: #FEFEFE; background-color: #8BC34A;} " +
            "h2.subtitle {border-radius: 5px; margin: 5px 5px 5px 10px; padding: 50px 10px 40px 20px; " +
            "    color: #222222; background-color: #8BC34A;} " +
            "h2.section {border-radius: 5px; margin: 35px 1px 10px 10px; padding: 40px; color: #222222; background-color: #8BC34A;} " +
            "h3 {margin-left: 45px; margin-top: 30px;} " +
            "h4, h5, h6 {margin-left: 60px;}" +
            "ol { list-style: none; } " +
            "table {border-collapse: collapse; border-spacing: 0;} " +
            getVerbStyles() +
            "ol > li { margin: 24px;} " +
            "ol > li.noobservation { padding-left: 30px;} " +
            getReportStyles();
    }

    private String getReportStyles() {
        return ".intro { padding: 20px 20px 20px 20px; max-width: 960px; margin: 10px auto; " +
            "    box-shadow: 0 0 4px #ccc; font-size: medium; color: #222266; background-color: #DDDDDD;} " +
            ".syntaxheader { padding: 20px; max-width: 960px; margin: 10px auto; box-shadow: 0 0 4px #ccc; " +
            "    font-size: smaller; font-weight: 400; color: #227722; background-color: #EEEEEE;} " +
            ".syntax { padding: 2px 20px 5px 20px; max-width: 960px; margin: 10px auto; " +
            "    box-shadow: 0 0 4px #ccc;font-size: smaller; color: #225522; background-color: #EEEEEE;} " +
            ".overview { padding: 20px; max-width: 960px; margin: 10px auto; box-shadow: 0 0 4px #ccc;} " +
            ".overview > div { padding: 10px 10px;} " +
            ".resource { color: black; font-family: courier new, courier, monospace; font-size: smaller} " +
            "span.operations { padding: 15px 15px 15px 15px; max-width: 960px; margin: 20px auto; " +
            "    box-shadow: 0 0 4px #ccc; font-size: large; font-weight: 400; color: #227799; background-color: #EEEEEE;} " +
            ".operations { padding: 15px 15px 15px 15px; max-width: 960px; margin: 20px auto; " +
            "    box-shadow: 0 0 4px #ccc; font-size: larger; font-weight: 400; color: #227799; background-color: #EEEEEE;} " +
            ".distance { padding: 20px 0 10px 0; max-width: 960px; margin-top: 45px; margin-bottom: 20px} " +
            ".summary { color: grey; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: x-small} " +
            "li.incident { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 500; margin: 45px 5px 5px 30px; padding: 2px 2px 2px 2px;} " +
            "span.incident { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 500; margin: 45px 5px 5px 80px; padding: 2px 2px 2px 2px;} " +
            ".observation { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 900; margin: 45px 5px 5px 30px; padding: 2px 2px 2px 2px;} " +
            "span.observation { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 900; margin: 45px 5px 5px 50px; padding: 2px 2px 2px 2px;} " +
            "li.observationtight { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 900; margin: 5px 5px 5px 50px; padding: 2px 2px 2px 2px;} " +
            "span.observationtight { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 900; margin: 5px 5px 5px 70px; padding: 2px 2px 2px 2px;} " +
            ".designissues { padding: 20px 20px 20px 20px; max-width: 960px; margin: 20px 10px 80px 40px; box-shadow: 0 0 4px #ccc; " +
            "    font-size: smaller; font-weight: 400; color: #545454; background-color: #EEEEEE;} " +
            ".existingobservation { color: #886767; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: small; font-weight: 100; margin: 45px 5px 5px 30px; padding: 2px 2px 2px 2px;} " +
            ".improvements { margin-left: 30px} " +
            "li.observationincident { margin: 5px 25px 5px 200px; padding: 2px 2px 20px 20px;} " +
            ".observationincident { margin: 2px 5px 5px 125px; padding: 2px 50px 2px 2px; color: grey; " +
            "    font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; font-size: 11px; " +
            "    word-break: break-word;} " +
            ".noobservation { color: #555555; font-family: Monaco, Bitstream Vera Sans Mono, Lucida Console, Terminal, sans-serif; " +
            "    font-size: smaller; margin: 5px 15px 5px 30px; padding: 2px 2px 20px 20px;} " +
            ".tabular {width: 100%} " +
            ".verb { width: 65px;}\n";
    }

    private String getVerbStyles() {
        return ".POST, .post, .get, .GET, .put, .PUT, .delete, .DELETE { color: #fff; padding: 4px; " +
            "    border-radius: 2px; font-size: small; margin: 0 4px; width: 40px; display: inline-block; text-align: center;} " +
            ".POST, .post { background-color: #0B7CAF;} " +
            ".get, .GET { background-color: #009688;} .put, .PUT { background-color: #c5862b;} " +
            ".delete, .DELETE { background-color: #F44336;}" +
            ".false { color: #600; padding: 4px; border-radius: 2px; font-size: small; background-color: #EEBBBB;} " +
            ".true { color: #070; padding: 4px; border-radius: 2px; font-size: small; background-color: #BBEEBB;} ";
    }
}
