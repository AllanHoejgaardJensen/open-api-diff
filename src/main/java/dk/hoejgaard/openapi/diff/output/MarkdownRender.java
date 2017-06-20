package dk.hoejgaard.openapi.diff.output;

import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renders output from a compatibility into a markdown format
 * <p>
 * See samples from the report in the readme.md file in this project
 */
public class MarkdownRender implements OutputRender {
    private static final String H1 = "# ";
    private static final String H2 = "## ";
    private static final String H3 = "### ";
    private static final String H4 = "#### ";
    private static final String H5 = "##### ";
    private static Logger logger = LoggerFactory.getLogger(MarkdownRender.class);
    private final String title;
    private final String subTitle;
    private final String reference;
    private final String candidate;

    /**
     * @param title the title of the report
     * @param subTitle the subtitle of the report
     * @param reference the API specification for the existing API
     * @param candidate the API specification for the future candidate API
     */
    public MarkdownRender(String title, String subTitle, String reference, String candidate) {
        this.title = title;
        this.subTitle = subTitle;
        this.reference = reference;
        this.candidate = candidate;
        logger.info("Rendering an Console and TXT Report for API differences having the title: {} and subtitle: {} for reference API: {} " +
            "and comparing to future API: {}", new Object[] {title, subTitle, reference, candidate});
    }

    /**
     * @param diff the difference report containing the comparison between the existing and the future candidate API
     * @return a complete rendered report
     */
    public String render(APIDiff diff) {
        StringBuilder sb = new StringBuilder();
        sb.append(H1).append(title).append("\n");
        sb.append(H2).append(subTitle).append("\n").append("\n\n");
        sb.append("The report layout uses three sections: short, elaborate and compliance\n");
        sb.append("The reports shows the added endpoints, the removed and the changed endpoints on a short form.\n");
        sb.append("The changed endpoints are succeeding that presented in a more elaborate form\n\n");
        sb.append("The syntax used is:\n\n");
        sb.append(" - ` + ` means added.\n\n");
        sb.append(" - ` - ` means removed.\n\n");
        sb.append(" - ` @ ` means altered.\n\n");
        sb.append(" - ` ! ` means issue found in Existing API.\n\n");
        sb.append(" - ` > ` means issues found in the New API.\n\n");
        sb.append(" - *` C   `* means (Compliance).\n\n");
        sb.append("\n\n");
        sb.append("The APIs compared are:\n");
        sb.append(" - ` ! ` ").append(reference).append("\n");
        sb.append(" - ` > ` ").append(candidate).append("\n\n");
        buildShortPart(diff, sb);
        buildElaboratedChanges(diff, sb);
        buildElaboratedCompliance(diff, sb);
        sb.append("\n");
        return sb.toString();
    }


    private void buildShortPart(APIDiff diff, StringBuilder sb) {
        buildAdded(diff, sb);
        buildRemoved(diff, sb);
        buildChanges(diff, sb);
    }

    private void buildAdded(APIDiff diff, StringBuilder sb) {
        sb.append(H2).append("Added Endpoints\n");
        List<Endpoint> added = diff.getAddedEndpoints();
        if (added.isEmpty()) {
            sb.append(" `   No Added Endpoints  `\n");
        } else {
            sb.append("  Added Endpoints  |  Info\n");
            sb.append("----------------- | -------\n");
        }
        for (Endpoint endpoint : added) {
            sb.append(" + ` ").append(endpoint.getVerb()).append(" `  `").append(endpoint.getPathUrl())
                .append("`    | ").append(endpoint.getSummary()).append('\n');
        }
        sb.append("\n\n");
    }

    private void buildRemoved(APIDiff diff, StringBuilder sb) {
        sb.append(H2).append("Removed Endpoints\n");
        List<Endpoint> missing = diff.getMissingEndpoints();
        if (missing.isEmpty()) {
            sb.append(" `   No Removed Endpoints  `\n");
        } else {
            sb.append("  Removed Endpoints  |  Info\n");
            sb.append("------------------- | -------\n");
        }
        for (Endpoint endpoint : missing) {
            sb.append(" - ` ").append(endpoint.getVerb()).append(" `  `").append(endpoint.getPathUrl())
                .append("`  | ").append(endpoint.getSummary()).append('\n');
        }
        sb.append("\n\n");
    }

    private void buildChanges(APIDiff diff, StringBuilder sb) {
        sb.append(H2).append("Changed Endpoints\n");
        List<Endpoint> added = diff.getChangedEndpoints();
        if (!added.isEmpty()) {
            sb.append("  Changed or Observable Endpoint  |  Info\n");
            sb.append("------------------- | -------\n");
        } else {
            sb.append(" `   No Changed or Observable Endpoints  `\n");
        }
        for (Endpoint endpoint : added) {
            sb.append(" @ ` ").append(endpoint.getVerb()).append(" `  ` ").append(endpoint.getPathUrl())
                .append(" ` | ").append(endpoint.getSummary()).append('\n');
        }
        sb.append("\n\n");
    }

    private void buildElaboratedChanges(APIDiff diff, StringBuilder sb) {
        List<ResourceDiff> changed = diff.getChangedResourceDiffs();
        sb.append(H2).append("The Elaborated Report for Changed Endpoints \n");
        if (changed.isEmpty()) {
            sb.append("  `   no observations    `\n");
        }
        for (ResourceDiff resource : changed) {
            sb.append(H3).append(" @ `").append(resource.getPathUrl()).append("`\n\n");
            sb.append(H4).append("    Operations:\n\n");
            Map<HttpMethod, OperationDiff> changedOperations = resource.getChangedOperations();
            for (Map.Entry<HttpMethod, OperationDiff> entry : changedOperations.entrySet()) {
                sb.append(H5).append("  *` C   `* `").append(entry.getKey()).append("`  *`")
                    .append(resource.getPathUrl()).append("`* -> compliance: `")
                    .append(entry.getValue().isCompliant()).append("` \n\n");
                buildBreaking(sb, entry.getValue());
                buildPotentiallyBreaking(sb, entry.getValue(), H4);
            }
            sb.append("\n\n\n");
        }
        sb.append("\n\n");
    }

    private void buildBreaking(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H4).append("Breaking Changes").append("\n");
        Map<String, List<String>> breaks = oprDiff.getBreakingChanges();
        if (!breaks.isEmpty()) {
            sb.append("  Breaking          |  Info\n");
            sb.append("------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (Map.Entry<String, List<String>> entry : breaks.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(" ` > ` ").append(formatBunch(mdOut(entry.getKey()).trim()));
            for (String incident : incidents) {
                sb.append(" | **").append(incident.trim()).append("** \n");
            }
        }
    }

    private void buildPotentiallyBreaking(StringBuilder sb, OperationDiff oprDiff, String header) {
        sb.append(header).append("Potentially Breaking Changes").append("\n");
        Map<String, List<String>> potentialBreaks = oprDiff.getPotentiallyBreakingChanges();
        if (!potentialBreaks.isEmpty()) {
            sb.append("  Potentially Breaking          |  Info\n");
            sb.append("------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (Map.Entry<String, List<String>> entry : potentialBreaks.entrySet()) {
            List<String> incidents = entry.getValue();
            String observation = entry.getKey();
            if (observation.contains("existing.compliance")) {
                sb.append(" ` ! ` ");
            } else {
                sb.append(" ` > ` ");
            }
            if (observation.contains("existing.compliance")) {
                sb.append(mdOutX(observation)).append(" missing");
            } else {
                sb.append(formatBunch(mdOut(observation).trim()));
            }

            for (String incident : incidents) {
                if (observation.contains("existing.compliance")) {
                    sb.append(" | ").append(incident).append(" \n");
                } else {
                    sb.append(" | **").append(incident.trim()).append("** \n");
                }
            }
        }
    }

    private void buildElaboratedCompliance(APIDiff diff, StringBuilder sb) {
        List<ResourceDiff> nonCompliant = diff.getAllDiffs();
        sb.append(H2).append("The Elaborated Compliance Report\n\n");
        for (ResourceDiff resource : nonCompliant) {
            sb.append(H3).append(" @ `").append(resource.getPathUrl()).append("`\n\n");
            Map<HttpMethod, OperationDiff> changedOperations = resource.getChangedOperations();
            if (!changedOperations.isEmpty()) {
                sb.append(H4).append("    Operations:\n\n");
            } else {
                sb.append("  `   no observations    `\n");
            }
            for (Map.Entry<HttpMethod, OperationDiff> entry : changedOperations.entrySet()) {
                OperationDiff oprDiff = entry.getValue();
                sb.append(H5).append("  *` C   `* `").append(entry.getKey()).append("`  *`")
                    .append(resource.getPathUrl()).append("`* -> compliance: `")
                    .append(oprDiff.isCompliant()).append("` \n\n");
                buildFlaws(sb, oprDiff);
                buildObservations(sb, oprDiff);
            }
            sb.append("\n\n\n");
        }
    }

    private void buildFlaws(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H4).append("Design Issues").append("\n");
        Map<String, List<String>> flaws = oprDiff.getDesignFlaws();
        if (!flaws.isEmpty()) {
            sb.append("  Issues          |  Info\n");
            sb.append("----------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (Map.Entry<String, List<String>> entry : flaws.entrySet()) {
            sb.append(" ` > ` ").append(formatBunch(mdOut(entry.getKey()).trim()));
            for (String incident : entry.getValue()) {
                sb.append(" | **").append(incident.trim()).append("** \n");
            }
        }
    }

    private void buildObservations(StringBuilder sb, OperationDiff oprDiff) {
        buildBreakingChanges(sb, oprDiff);
        buildPotentiallyBreaking(sb, oprDiff, H5);
        buildRecordedChanges(sb, oprDiff);
        buildRecordedFlawObservations(sb, oprDiff);
        buildRecordedContentTypeObservations(sb, oprDiff);
        buildRecordedParameterObservations(sb, oprDiff);
        buildRecordedPropertiesObservations(sb, oprDiff);
        buildRecordedResponsesObservations(sb, oprDiff);
    }

    private void buildBreakingChanges(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Breaking Changes").append("\n");
        Map<String, List<String>> breaks = oprDiff.getBreakingChanges();
        if (!breaks.isEmpty()) {
            sb.append("  Breaking          |  Info\n");
            sb.append("------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (Map.Entry<String, List<String>> entry : breaks.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(" ` > ` ").append(formatBunch(mdOut(entry.getKey()).trim()));
            for (String incident : incidents) {
                sb.append(" | **").append(incident.trim()).append("** \n");
            }
        }
    }

    private void buildRecordedChanges(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Recorded Changes").append("\n");
        Map<String, List<String>> changes = oprDiff.getChanges();
        if (!changes.isEmpty()) {
            sb.append("  Changes                       |  Info\n");
            sb.append("------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (Map.Entry<String, List<String>> entry  : changes.entrySet()) {
            List<String> incidents = entry.getValue();
            String observation = entry.getKey();
            if (observation.contains("existing.compliance")) {
                sb.append(" ` ! ` ");
            } else {
                sb.append(" ` > ` ");
            }
            if (observation.contains("existing.compliance")) {
                sb.append(mdOutX(observation)).append(" missing");
            } else {
                sb.append(formatBunch(mdOut(observation).trim()));
            }

            for (String incident : incidents) {
                if (observation.contains("existing.compliance")) {
                    sb.append(" | ").append(incident).append(" \n");
                } else {
                    sb.append(" | ").append(formatBunch(incident).trim()).append(" \n");
                }
            }
        }
    }

    private void buildRecordedFlawObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Recorded Flaws in existing API").append("\n");
        Map<String, List<String>> xFlaws = oprDiff.getExistingFlaws();
        if (!xFlaws.isEmpty()) {
            sb.append("  Improvements to existing API  |  Info\n");
            sb.append("------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (Map.Entry<String, List<String>> entry  : xFlaws.entrySet()) {
            List<String> incidents = entry.getValue();
            String observation = entry.getKey();
            if (observation.contains("existing")) {
                sb.append(" ` ! ` ");
            } else {
                sb.append(" ` > ` ");
            }
            if (observation.contains("existing")) {
                sb.append(mdOutX(observation)).append(" missing");
            } else {
                sb.append(formatBunch(mdOut(observation)).trim());
            }

            for (String incident : incidents) {
                sb.append(" | ").append(incident).append(" \n");
            }
        }
    }

    private void buildRecordedContentTypeObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Recorded Changes to Content-Types in API").append("\n");
        List<String> added = oprDiff.getAddedContentTypes();
        List<String> removed = oprDiff.getMissingContentTypes();
        if (!added.isEmpty() || !removed.isEmpty()) {
            sb.append("  Changes to Content-Types      |  Info\n");
            sb.append("------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        for (String observation : added) {
            sb.append(" ` > ` ` + `");
            sb.append(" *").append(formatBunch(mdOut(observation)).trim()).append("* ");
            sb.append(" | *").append(observation.trim()).append("* \n");
        }
        for (String observation : removed) {
            sb.append(" ` > ` **` - `**");
            sb.append(" **").append(formatBunch(mdOut(observation)).trim()).append("** ");
            sb.append(" | **").append(observation.trim()).append("** \n");
        }
    }

    private void buildRecordedParameterObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Recorded Changes to Parameters in API").append("\n");
        List<Parameter> added = oprDiff.getAddedParameters();
        List<Parameter> removed = oprDiff.getMissingParameters();
        List<ParameterChanges> changed = oprDiff.getChangedParameters();
        if (!added.isEmpty() || !removed.isEmpty() || !changed.isEmpty()) {
            sb.append("  Changes to Parameters (Headers, Query,...)      |  Info\n");
            sb.append("------------------------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        if (!added.isEmpty()) {
            for (Parameter observation : added) {
                sb.append(" ` > ` `+ `");
                sb.append(" *").append(observation.getName()).append("* ");
                sb.append(" | *").append(observation.getRequired() ? "Required" : "Optional").append("* \n");
            }
        }
        if (!removed.isEmpty()) {
            for (Parameter observation : removed) {
                sb.append(" ` > ` **`- `**");
                sb.append(" **").append(observation.getName());
                sb.append(" | **").append(observation).append("** \n");
            }
        }
        if (!changed.isEmpty()) {
            for (ParameterChanges parameterChanges : changed) {
                Map<String, List<String>> pChanges = parameterChanges.getBreaking();
                for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
                    List<String> incidents = entry.getValue();
                    sb.append(" ` > ` **` @ `** *").append(entry.getKey()).append("* ").append("\n");
                    for (String incident : incidents) {
                        sb.append(" | **").append(incident).append("** \n");
                    }
                    sb.append('\n');
                }
            }
        }
    }

    private void buildRecordedPropertiesObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Recorded Changes to Properties in API").append("\n");
        List<ScopedProperty> added = oprDiff.getAddedProperties();
        List<ScopedProperty> removed = oprDiff.getMissingProperties();
        List<PropertyChanges> changed = oprDiff.getChangedProperties();
        if (!added.isEmpty() || !removed.isEmpty() || !changed.isEmpty()) {
            sb.append("  Changes to Properties (Body Definitions)      |  Info\n");
            sb.append("------------------------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        if (!added.isEmpty()) {
            for (ScopedProperty observation : added) {
                sb.append(" ` > ` `+ `");
                sb.append(" *").append(observation.getEl()).append("* ");
                sb.append(" | *").append(observation.getProperty().getRequired() ? "Required" : "Optional").append("* \n");
            }
        }
        if (!removed.isEmpty()) {
            for (ScopedProperty observation : removed) {
                sb.append(" ` > ` **`- `**");
                sb.append(" **").append(observation.getEl()).append(".").append(observation.getProperty().getName()).append("** (CHECK!)");
                sb.append(" | **").append(observation).append("** \n");
            }
        }
        if (!changed.isEmpty()) {
            for (PropertyChanges changedProps : changed) {
                Map<String, List<String>> pChanges = changedProps.getBreaking();
                for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
                    List<String> incidents = entry.getValue();
                    sb.append(" ` > ` **` @ `** *").append(entry.getKey()).append("* ");
                    for (String incident : incidents) {
                        sb.append(" | ").append(incident).append("\n");
                    }
                }
            }
        }
    }

    private void buildRecordedResponsesObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(H5).append("Recorded Changes to Responses in API").append("\n");
        Map<String, Response> added = oprDiff.getAddedResponses();
        Map<String, Response> removed = oprDiff.getMissingResponses();
        List<ResponseChanges> changed = oprDiff.getChangedResponses();
        if (!added.isEmpty() || !removed.isEmpty() || !changed.isEmpty()) {
            sb.append("  Changes to Responses (Status Code and Headers)      |  Info\n");
            sb.append("----------------------------------------------------- | -------\n");
        } else {
            sb.append("  `   no observations    `\n");
        }
        if (!added.isEmpty()) {
            for (String observation : added.keySet()) {
                sb.append(" ` > ` **` @ `** *").append(observation.trim()).append("* ");
                sb.append(" | **").append(ResponseChanges.getCodeMsg(observation).trim()).append("** \n");
            }
        }
        if (!removed.isEmpty()) {
            for (String observation : removed.keySet()) {
                sb.append(" ` > ` **`- `**");
                sb.append(" **").append(observation.trim()).append("** ");
                sb.append(" | **").append(ResponseChanges.getCodeMsg(observation).trim()).append("** \n");
            }
        }
        if (!changed.isEmpty()) {
            for (ResponseChanges responseChanges : changed) {
                Map<String, List<String>> pChanges = responseChanges.getBreaking();
                populateBreaking(sb, pChanges);
                pChanges = responseChanges.getPotentiallyBreaking();
                populatePotentiallyBreaking(sb, pChanges);
                pChanges = responseChanges.getFlawedDefines();
                populateNewFlaws(sb, pChanges);
                pChanges = responseChanges.getExistingFlaws();
                populateExistingFlaws(sb, pChanges);
                pChanges = responseChanges.getChanges();
                populatePropertyChanges(sb, pChanges);
            }
        }
    }


    private void populateBreaking(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            String observation = entry.getKey();
            if (observation.contains("existing")) {
                sb.append(" ` ! ` ");
                if (observation.contains("compliance")) {
                    sb.append(" *` C `* ");
                    sb.append(mdOutX(observation)).append(" | ");
                } else {
                    sb.append(formatBunch(mdOut(observation))).append(" | ");
                }
            } else {
                sb.append(" ` > ` ");
                sb.append(formatBunch(mdOut(observation))).append(" | ");
            }
            List<String> incidents = entry.getValue();
            for (String incident : incidents) {
                sb.append(" | ").append(incident).append(" - ").append(ResponseChanges.getCodeMsg(observation)).append(" \n");
            }
        }
    }

    private void populatePotentiallyBreaking(StringBuilder sb, Map<String, List<String>> pChanges) {
        buildMDSection(sb, pChanges);
    }

    private void populateNewFlaws(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            List<String> incidents = entry.getValue();
            String observation =  entry.getKey();
            if (observation.contains("existing")) {
                sb.append(" ` ! ` ");
                if (observation.contains("compliance")) {
                    sb.append(" *` C `* ");
                    sb.append(mdOutX(observation)).append(" | ");
                } else {
                    sb.append(formatBunch(mdOut(observation))).append(" | ");
                }
            } else {
                sb.append(" ` > ` ");
                sb.append(formatBunch(mdOut(observation))).append(" | ");
            }
            for (String incident : incidents) {
                sb.append(formatBunch(incident)).append(" \n");
            }
        }
    }

    private void populateExistingFlaws(StringBuilder sb, Map<String, List<String>> pChanges) {
        buildMDSection(sb, pChanges);
    }

    private void populatePropertyChanges(StringBuilder sb, Map<String, List<String>> pChanges) {
        buildMDSection(sb, pChanges);
    }

    private void buildMDSection(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            String observation = entry.getKey();
            if (observation.contains("existing")) {
                sb.append(" ` ! ` ");
                if (observation.contains("compliance")) {
                    sb.append(" *` C `* ");
                    sb.append(mdOutX(observation)).append(" | ");
                } else {
                    sb.append(formatBunch(mdOut(observation))).append(" | ");
                }
            } else {
                sb.append(" ` > ` ");
                sb.append(formatBunch(mdOut(observation))).append(" | ");
            }
            List<String> incidents = entry.getValue();
            for (String incident : incidents) {
                sb.append(formatBunch(incident)).append("\n");
            }
        }
    }

    private String formatBunch(String incident) {
        String result = "";
        result = result + incident
            .replaceAll("\\.\\{", ": \n | ")
            .replaceAll("\\.=\\[", ".=[ ")
            .replaceAll("\\]\\,", " ],\n | ")
            .replaceAll("\\]\\}", " ] \n | ");
        return result;
    }

    private String mdOut(String incident) {
        String result = incident.replace('.', ' ');
        result = toUpperCamelCase(result);
        return result;
    }

    private String mdOutX(String incident) {
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

}
