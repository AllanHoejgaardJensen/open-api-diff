package dk.hoejgaard.openapi.diff.output;

import dk.hoejgaard.openapi.diff.APIDiff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renders output from a compatibility into a checkstyle xml format
 *
 * (this is not implemented yet - please indicate if you want that in a checkstyle format)
 */
public class XmlRender  implements OutputRender {
    private static Logger logger = LoggerFactory.getLogger(XmlRender.class);

    /**
     * @param title the title of the report
     * @param subTitle the subtitle of the report
     * @param reference the API specification for the existing API
     * @param candidate the API specification for the future candidate API
     * @param schema the schema used for the XML report
     */
    public XmlRender(String title, String subTitle, String reference, String candidate, String schema) {
        logger.info("the xml renderer has not been implemented yet and thus the report with title: {} and subtitle: {} could not " +
                "be created for comparing the existing API {} with the future {} using the schema {} ",
            new Object[]{title, subTitle, reference, candidate, schema});
    }

    /**
     * @param diff the difference report containing the comparison between the existing and the future candidate API
     * @return a complete rendered report (not implemented currently)
     */
    public String render(APIDiff diff) {
        String s = "The xml checkstyle renderer has not been implemented yet";
        logger.info(s);
        return s;
    }
}
