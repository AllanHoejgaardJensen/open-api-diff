package dk.hoejgaard.openapi.diff.output;

import java.util.Arrays;
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
 * Renders output from a compatibility into a color coded console format for easier readability in terminal window
 * <p>
 * See samples from the report in the readme.md file in this project
 */

public class ConsoleRender implements OutputRender {
    private static Logger logger = LoggerFactory.getLogger(ConsoleRender.class);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final String LINE = "------------------------------------------------------------------------------------------------------";
    private String title = "Console Renderer Output";
    private String subTitle = "The results of the comparison of APIs";
    private String reference;
    private String candidate;

    /**
     * @param title the title of the report
     * @param subTitle the subtitle of the report
     * @param reference the API specification for the existing API
     * @param candidate the API specification for the future candidate API
     */
    public ConsoleRender(String title, String subTitle, String reference, String candidate) {
        this.title = title;
        this.subTitle = subTitle;
        this.reference = reference;
        this.candidate = candidate;
        logger.info("Rendering an Console and TXT Report for API differences having the title: {} and subtitle: {} for reference API: {} " +
            "and comparing to future API: {}", new Object[]{title, subTitle, reference, candidate});

    }

    /**
     * @param diff the difference report containing the comparison between the existing and the future candidate API
     * @return a complete rendered report
     */
    public String render(APIDiff diff) {
        StringBuilder sb = new StringBuilder();
        buildIntroduction(sb);
        buildAddedEndpoints(diff, sb);
        buildRemovedEndpoints(diff, sb);
        buildChangedEndpoints(diff, sb);
        buildElaboratedChangeSection(diff, sb);
        buildElaboratedSection(diff, sb);
        buildConsoleEpilogue(sb);
        return sb.toString();
    }

    private void buildIntroduction(StringBuilder sb) {
        sb.append(ANSI_WHITE).append(ANSI_BLUE_BACKGROUND).append(line());
        sb.append(title).append("\n\n\n");
        sb.append(ANSI_YELLOW).append(ANSI_BLUE_BACKGROUND);
        sb.append(subTitle);
        sb.append(line()).append("\n");
        sb.append(ANSI_RESET);
        sb.append(ANSI_BLUE);
        sb.append("The report layout uses three sections: short, elaborate and compliance\n");
        sb.append("The reports shows the added endpoints, the removed and the changed endpoints on a short form.\n");
        sb.append("The changed endpoints are succeeding that presented in a more elaborate form\n");
        sb.append("The syntax used is:\n");
        sb.append("  +  means added.\n");
        sb.append("  -  means removed.\n");
        sb.append("  @  means altered.\n");
        sb.append("  !  means (in Existing API).\n");
        sb.append("  >  means (in the New API).\n");
        sb.append(" (C) means (Compliance).\n\n");
        sb.append("The APIs compared are:\n");
        sb.append(" - ! ").append(reference).append("\n");
        sb.append(" - > ").append(candidate).append("\n\n");
        sb.append(ANSI_RESET);
    }

    private void buildAddedEndpoints(APIDiff diff, StringBuilder sb) {
        sb.append(line());
        sb.append(ANSI_GREEN).append("Added Endpoints").append(ANSI_RESET);
        sb.append(line());
        List<Endpoint> added = diff.getAddedEndpoints();
        if (added.isEmpty()) {
            sb.append(" - no added endpoints\n");
        }
        for (Endpoint endpoint : added) {
            sb.append(" + ").append(endpoint.getPathUrl()).append('\n');
            sb.append("   ").append(endpoint.getSummary()).append('\n');
        }
        sb.append(LINE).append('\n');
        sb.append(ANSI_RESET);
    }

    private void buildRemovedEndpoints(APIDiff diff, StringBuilder sb) {
        sb.append(line());
        sb.append(ANSI_RED).append("Removed Endpoints").append(ANSI_RESET);
        sb.append(line());
        List<Endpoint> removed = diff.getMissingEndpoints();
        if (removed.isEmpty()) {
            sb.append(" - no removed endpoints\n");
        }
        for (Endpoint endpoint : removed) {
            sb.append(" - ").append(endpoint.getPathUrl()).append('\n');
            sb.append("   ").append(endpoint.getSummary()).append('\n');
        }
        sb.append(LINE).append('\n');
        sb.append(ANSI_RESET);
    }

    private void buildChangedEndpoints(APIDiff diff, StringBuilder sb) {
        sb.append(line());
        sb.append(ANSI_BLUE).append("Changed or Observable Endpoints").append(ANSI_RESET);
        sb.append(line());
        List<ResourceDiff> changed = diff.getChangedResourceDiffs();
        if (changed.isEmpty()) {
            sb.append(" - no changed or observable endpoints\n");
        }
        for (ResourceDiff resource : changed) {
            sb.append(" @ ").append(resource.getPathUrl()).append('\n');
        }
        sb.append(LINE).append("\n\n");
        sb.append(ANSI_RESET);
    }

    private void buildElaboratedChangeSection(APIDiff diff, StringBuilder sb) {
        List<ResourceDiff> changed = diff.getChangedResourceDiffs();
        sb.append(ANSI_WHITE).append(ANSI_BLUE_BACKGROUND).append(LINE).append('\n');
        sb.append("The Elaborated Report for changed or observable Endpoints \n");
        sb.append(LINE).append("\n\n").append(ANSI_RESET);
        for (ResourceDiff resource : changed) {
            sb.append(ANSI_YELLOW).append(tildeLine(resource.getPathUrl())).append('\n');
            sb.append(" @ ").append(resource.getPathUrl()).append(" \n");
            sb.append(tildeLine(resource.getPathUrl())).append("\n").append(ANSI_RESET);
            sb.append("    Operations:\n\n");
            Map<HttpMethod, OperationDiff> changedOperations = resource.getChangedOperations();
            for (Map.Entry<HttpMethod, OperationDiff> entry : changedOperations.entrySet()) {
                OperationDiff oprDiff = entry.getValue();
                sb.append(ANSI_BLUE).append("   (c) ").append(resource.getPathUrl()).append("::")
                    .append(entry.getKey()).append(" -> compliance: ")
                    .append(oprDiff.isCompliant()).append(ANSI_RESET).append("\n\n");
                buildBreaking(sb, oprDiff);
                buildPotentiallyBreaking(sb, oprDiff);
            }
            sb.append("\n\n\n");
        }
        sb.append(LINE).append("\n\n").append(ANSI_RESET);
    }

    private void buildBreaking(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(ANSI_RED).append(indent(6)).append("Breaking Changes").append(ANSI_RESET).append("\n");
        sb.append(ANSI_RED).append(indent(5)).append(tildeLine("Breaking Changes  ")).append(ANSI_RESET).append("\n");
        Map<String, List<String>> breaks = oprDiff.getBreakingChanges();
        if (breaks.isEmpty()) {
            sb.append("              - no breaking changes\n");
        }
        for (Map.Entry<String, List<String>> entry : breaks.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(18)).append("- ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildPotentiallyBreaking(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(ANSI_YELLOW).append(indent(6)).append("Potentially Breaking Changes").append(ANSI_RESET).append("\n");
        sb.append(ANSI_YELLOW).append(indent(5)).append(tildeLine("Potentially Breaking Changes")).append(ANSI_RESET).append("\n");
        Map<String, List<String>> potentialBreaks = oprDiff.getPotentiallyBreakingChanges();
        if (potentialBreaks.isEmpty()) {
            sb.append("              - no potentially breaking changes\n");
        }
        for (Map.Entry<String, List<String>> entry : potentialBreaks.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                if (entry.getKey().contains("existing.compliance")) {
                    sb.append(indent(18)).append(ANSI_RED).append(" ! ").append(ANSI_RESET).append(incident).append("\n");
                } else {
                    sb.append(indent(18)).append(ANSI_YELLOW).append(" + ").append(ANSI_RESET).append(incident).append("\n");
                }
            }
            sb.append('\n');
        }
    }

    private void buildElaboratedSection(APIDiff diff, StringBuilder sb) {
        sb.append(ANSI_CYAN).append(ANSI_BLACK_BACKGROUND).append(LINE + "\n\n\n");
        sb.append("The Elaborated Compliance Report\n\n");
        sb.append(LINE).append("\n\n").append(ANSI_RESET).append("\n\n");

        List<ResourceDiff> nonCompliant = diff.getAllDiffs();
        for (ResourceDiff resource : nonCompliant) {
            sb.append(ANSI_BLACK_BACKGROUND).append(ANSI_YELLOW).append(tildeLine(resource.getPathUrl())).append('\n');
            sb.append(" @ ").append(resource.getPathUrl()).append(" \n");
            sb.append(tildeLine(resource.getPathUrl())).append("\n").append(ANSI_RESET);
            Map<HttpMethod, OperationDiff> changedOperations = resource.getChangedOperations();
            if (changedOperations.isEmpty()) {
                sb.append("   - no compliance issues reported");
            } else {
                sb.append("    Operations:\n\n");
            }
            for (Map.Entry<HttpMethod, OperationDiff> entry : changedOperations.entrySet()) {
                OperationDiff oprDiff = entry.getValue();
                sb.append(ANSI_BLUE).append("   (c) ").append(resource.getPathUrl()).append("::")
                    .append(entry.getKey()).append(" -> compliance: ")
                    .append(oprDiff.isCompliant()).append(ANSI_RESET).append("\n\n");
                buildFlaws(sb, oprDiff);
                buildObservations(sb, oprDiff);
            }
            sb.append("\n\n\n");
        }
    }

    private void buildFlaws(StringBuilder sb, OperationDiff oprDiff) {
        sb.append(ANSI_YELLOW).append("       Design Issues").append(ANSI_RESET).append("\n");
        sb.append(ANSI_YELLOW).append("     ").append(tildeLine("Design Issues")).append(ANSI_RESET).append('\n');
        Map<String, List<String>> flaws = oprDiff.getDesignFlaws();
        for (Map.Entry<String, List<String>> entry : flaws.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("- ").append(entry.getKey()).append('\n');
            for (String incident : incidents) {
                sb.append(indent(18)).append("- ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildObservations(StringBuilder sb, OperationDiff oprDiff) {
        buildBreakingChanges(sb, oprDiff);
        buildPotentiallyBreakingChanges(sb, oprDiff);
        buildRecordedChanges(sb, oprDiff);
        buildRecordedFlawObservations(sb, oprDiff);
        buildRecordedContentTypeObservations(sb, oprDiff);
        buildRecordedParameterObservations(sb, oprDiff);
        buildRecordedPropertiesObservations(sb, oprDiff);
        buildRecordedResponsesObservations(sb, oprDiff);
    }

    private void buildBreakingChanges(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Breaking changes\n");
        Map<String, List<String>> breaks = oprDiff.getBreakingChanges();
        if (breaks.isEmpty()) {
            sb.append(indent(14)).append("- no breaking changes\n");
        }
        for (Map.Entry<String, List<String>> entry : breaks.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("- ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildPotentiallyBreakingChanges(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Potentially Breaking changes\n");
        Map<String, List<String>> potentialBreaks = oprDiff.getPotentiallyBreakingChanges();
        for (Map.Entry<String, List<String>> entry: potentialBreaks.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("* ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildRecordedChanges(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Recorded changes**\n");
        Map<String, List<String>> changes = oprDiff.getChanges();
        for (Map.Entry<String, List<String>> entry : changes.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("* ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildRecordedFlawObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Recorded Flaws in existing API \n");
        Map<String, List<String>> xFlaws = oprDiff.getExistingFlaws();
        for (Map.Entry<String, List<String>> entry : xFlaws.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("* ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildRecordedContentTypeObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Recorded Changes to Content-Types in API \n");
        List<String> addedContentTypes = oprDiff.getAddedContentTypes();
        sb.append(contentTypes(addedContentTypes, true));
        List<String> removedContentTypes = oprDiff.getMissingContentTypes();
        sb.append(contentTypes(removedContentTypes, false));
    }

    private StringBuilder contentTypes(List<String> contentTypes, boolean added) {
        StringBuilder sb =  new StringBuilder();
        if (!contentTypes.isEmpty()) {
            String scope = added ? "+ added" : "- removed";
            sb.append(indent(16)).append(scope).append("" + ":").append("\n");
            for (String type : contentTypes) {
                sb.append(indent(18)).append("* ").append(type).append("\n");
            }
            sb.append('\n');
        }
        return sb;
    }

    private void buildRecordedParameterObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Recorded Changes to Parameters in API \n");
        List<Parameter> addedParams = oprDiff.getAddedParameters();
        if (!addedParams.isEmpty()) {
            sb.append(indent(16)).append("+ added:").append("\n");
            for (Parameter parameter : addedParams) {
                sb.append(indent(18)).append("- ").append(parameter).append("\n");
            }
            sb.append('\n');
        }
        List<Parameter> removedParams = oprDiff.getMissingParameters();
        if (!removedParams.isEmpty()) {
            sb.append(indent(16)).append("- removed:").append("\n");
            for (Parameter parameter : removedParams) {
                sb.append(indent(18)).append("* ").append(parameter).append("\n");
            }
            sb.append('\n');
        }

        List<ParameterChanges> changedParams = oprDiff.getChangedParameters();
        if (!changedParams.isEmpty()) {
            sb.append(indent(16)).append("@ changed:").append("\n");
            for (ParameterChanges parameterChanges : changedParams) {
                Map<String, List<String>> pChanges = parameterChanges.getBreaking();
                for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
                    List<String> incidents = entry.getValue();
                    sb.append(indent(18)).append("* ").append(entry.getKey()).append("\n");
                    for (String incident : incidents) {
                        sb.append(indent(20)).append(" -> ").append(incident).append("\n");
                    }
                    sb.append('\n');
                }
            }
            sb.append('\n');
        }
    }

    private void buildRecordedPropertiesObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n              Recorded Changes to Properties in API \n");
        List<ScopedProperty> addedProperties = oprDiff.getAddedProperties();
        if (!addedProperties.isEmpty()) {
            sb.append(indent(16)).append("+ added:").append("\n");
            for (ScopedProperty prop : addedProperties) {
                sb.append(indent(18)).append("* ").append(prop.getEl()).append("\n");
            }
            sb.append('\n');
        }
        List<ScopedProperty> missingProperties = oprDiff.getMissingProperties();
        if (!missingProperties.isEmpty()) {
            sb.append(indent(16)).append("- removed:").append("\n");
            for (ScopedProperty property : missingProperties) {
                sb.append(indent(18)).append("* ").append(property).append("\n");
            }
            sb.append('\n');
        }

        List<PropertyChanges> changedProperties = oprDiff.getChangedProperties();
        if (!changedProperties.isEmpty()) {
            sb.append(indent(16)).append("@ changed:").append("\n");
            for (PropertyChanges propertyChanges : changedProperties) {
                Map<String, List<String>> pChanges = propertyChanges.getBreaking();
                for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
                    List<String> incidents = entry.getValue();
                    sb.append(indent(18)).append("* ").append(entry.getKey()).append("\n");
                    for (String incident : incidents) {
                        sb.append(indent(20)).append(" ->> ").append(incident).append("\n");
                    }
                    sb.append('\n');
                }

            }
            sb.append('\n');
        }
    }

    private void buildRecordedResponsesObservations(StringBuilder sb, OperationDiff oprDiff) {
        sb.append("\n").append(indent(14)).append("Recorded Changes to Responses in API \n");
        Map<String, Response> addedResponses = oprDiff.getAddedResponses();
        if (!addedResponses.isEmpty()) {
            sb.append(indent(16)).append("+ added:").append("\n");
            for (String observation : addedResponses.keySet()) {
                sb.append(indent(18)).append("* ").append(observation).append("\n");
                sb.append(indent(20)).append(" -> ").append(ResponseChanges.getCodeMsg(observation)).append("\n");
                sb.append('\n');
            }
            sb.append('\n');
        }

        Map<String, Response> missingResponses = oprDiff.getMissingResponses();
        if (!missingResponses.isEmpty()) {
            sb.append(indent(16)).append("- removed:").append("\n");
            for (String observation : missingResponses.keySet()) {
                sb.append(indent(18)).append("* ").append(observation).append("\n");
                sb.append(indent(20)).append(" -> ").append(ResponseChanges.getCodeMsg(observation)).append("\n");
                sb.append('\n');
            }
            sb.append('\n');
        }
        buildRecordedPropertyChangesObservations(sb, oprDiff);
    }

    private void buildRecordedPropertyChangesObservations(StringBuilder sb, OperationDiff oprDiff) {
        List<ResponseChanges> changedResponses = oprDiff.getChangedResponses();
        if (!changedResponses.isEmpty()) {
            sb.append(indent(16)).append("@ changed:").append("\n");
            for (ResponseChanges propertyChanges : changedResponses) {
                Map<String, List<String>> pChanges = propertyChanges.getBreaking();
                populateBreaking(sb, pChanges);
                pChanges = propertyChanges.getPotentiallyBreaking();
                populatePotentiallyBreaking(sb, pChanges);
                pChanges = propertyChanges.getFlawedDefines();
                populateNewFlaws(sb, pChanges);
                pChanges = propertyChanges.getExistingFlaws();
                populateExistingFlaws(sb, pChanges);
                pChanges = propertyChanges.getChanges();
                populatePropertyChanges(sb, pChanges);
            }
            sb.append('\n');
        }
    }

    private void populateBreaking(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(18)).append(ANSI_RED).append("! ").append(ANSI_RESET).append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(20)).append(" -> ").append(incident).append(" - ")
                    .append(ResponseChanges.getCodeMsg(entry.getKey())).append("\n");
            }
            sb.append('\n');
        }
    }

    private void populatePotentiallyBreaking(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append(ANSI_YELLOW).append("! ").append(ANSI_RESET).append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void populateNewFlaws(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append(ANSI_BLUE).append("* ").append(ANSI_RESET).append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void populateExistingFlaws(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("* ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" ->> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void populatePropertyChanges(StringBuilder sb, Map<String, List<String>> pChanges) {
        for (Map.Entry<String, List<String>> entry : pChanges.entrySet()) {
            List<String> incidents = entry.getValue();
            sb.append(indent(16)).append("* ").append(entry.getKey()).append("\n");
            for (String incident : incidents) {
                sb.append(indent(18)).append(" -> ").append(incident).append("\n");
            }
            sb.append('\n');
        }
    }

    private void buildConsoleEpilogue(StringBuilder sb) {
        sb.append(ANSI_BLUE_BACKGROUND).append(ANSI_WHITE)
            .append("\n\n\n")
            .append(ANSI_WHITE_BACKGROUND).append(ANSI_BLACK)
            .append("\n\n\n")
            .append(ANSI_BLACK_BACKGROUND).append(ANSI_PURPLE)
            .append("\n\n\n")
            .append(ANSI_CYAN_BACKGROUND).append(ANSI_YELLOW)
            .append("\n\n\n")
            .append(ANSI_YELLOW_BACKGROUND).append(ANSI_BLACK)
            .append("\n\n\n")
            .append(ANSI_GREEN_BACKGROUND).append(ANSI_BLACK)
            .append("\n\n\n")
            .append(ANSI_PURPLE_BACKGROUND).append(ANSI_BLACK)
            .append("\n\n\n")
            .append(ANSI_RED_BACKGROUND).append(ANSI_BLACK)
            .append("\n\n\n")
            .append(ANSI_RESET);
    }

    private String tildeLine(String pathUrl) {
        char[] line = new char[pathUrl.length() + 4];
        Arrays.fill(line, '~');
        return String.valueOf(line);

    }

    private String indent(int size) {
        char[] line = new char[size];
        Arrays.fill(line, ' ');
        return String.valueOf(line);
    }

    private String line() {
        return "\n" + LINE + "\n";
    }

}

