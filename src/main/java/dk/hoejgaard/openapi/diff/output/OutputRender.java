package dk.hoejgaard.openapi.diff.output;


import dk.hoejgaard.openapi.diff.APIDiff;

/**
 * Renders the output from an API difference report into a given format
 */
interface OutputRender {

    /**
     * @param diff the difference report containing the comparison between the existing and the future candidate API
     * @return a complete rendered report
     */
    String render(APIDiff diff);

}
