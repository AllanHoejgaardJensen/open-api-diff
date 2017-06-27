package dk.hoejgaard.openapi.diff.output;

import dk.hoejgaard.openapi.diff.APIDiff;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 *
 */
public class RendererTest {

    private static String existing = "./sample-api/elaborate_example_v1.json";
    private static String future = "./sample-api/elaborate_example_v3f.json";
    private static Versions versions = Versions.SINGLE;
    private static Maturity maturity = Maturity.HAL;
    private static Diff diffLevel = Diff.ALL;

    @Test
    public void testTxt() {
        APIDiff api = new APIDiff(existing, future, diffLevel, maturity, versions);
        String consoleContent = new ConsoleRender(
            "API Comparison Results", "Delivered by Open API Diff tooling", existing, future).render(api);
        assertNotNull(consoleContent);
        assertTrue(consoleContent.contains("API Comparison Results"));
        assertTrue(consoleContent.contains("Delivered by Open API Diff tooling"));
        assertTrue(consoleContent.contains("Breaking Changes"));
        assertTrue(consoleContent.contains("Potentially Breaking Changes"));
        assertTrue(consoleContent.contains("+  means added"));
        assertTrue(consoleContent.contains("-  means removed"));
        assertTrue(consoleContent.contains("@  means altered"));
        assertTrue(consoleContent.contains("!  means (in Existing API)"));
        assertTrue(consoleContent.contains(">  means (in the New API)"));
        assertTrue(consoleContent.contains("(C) means (Compliance)"));
        assertTrue(consoleContent.contains(existing));
        assertTrue(consoleContent.contains(future));
    }

    @Test
    public void testMarkdown() {
        APIDiff api = new APIDiff(existing, future, diffLevel, maturity, versions);
        String mdContent = new MarkdownRender(
            "API Comparison Results", "Delivered by Open API Diff tooling", existing, future).render(api);
        assertNotNull(mdContent);
        assertTrue(mdContent.contains("API Comparison Results"));
        assertTrue(mdContent.contains("Delivered by Open API Diff tooling"));
        assertTrue(mdContent.contains("Breaking Changes"));
        assertTrue(mdContent.contains("Potentially Breaking Changes"));
        assertTrue(mdContent.contains("` + ` means added"));
        assertTrue(mdContent.contains("` - ` means removed"));
        assertTrue(mdContent.contains("` @ ` means altered"));
        assertTrue(mdContent.contains("` ! ` means issue found in Existing API"));
        assertTrue(mdContent.contains("` > ` means issues found in the New API"));
        assertTrue(mdContent.contains("*` C   `* means (Compliance)"));
        assertTrue(mdContent.contains(existing));
        assertTrue(mdContent.contains(future));
    }

    @Test
    public void testHTML() {
        APIDiff api = new APIDiff(existing, future, diffLevel, maturity, versions);
        String htmlContent = new HtmlRender(
            "API Comparison Results", "Delivered by Open API Diff tooling", existing, future).render(api);
        assertNotNull(htmlContent);
        assertTrue(htmlContent.contains("API Comparison Results"));
        assertTrue(htmlContent.contains("Delivered by Open API Diff tooling"));
        assertTrue(htmlContent.contains("Breaking Changes"));
        assertTrue(htmlContent.contains("Potentially Breaking Changes"));
        assertTrue(htmlContent.contains("(+)"));
        assertTrue(htmlContent.contains("(-)"));
        assertTrue(htmlContent.contains("(@)"));
        assertTrue(htmlContent.contains("(!)"));
        assertTrue(htmlContent.contains("(C)"));
        assertTrue(htmlContent.contains(existing));
        assertTrue(htmlContent.contains(future));
    }

}
