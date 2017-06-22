package dk.hoejgaard.openapi.diff.output;

import dk.hoejgaard.openapi.diff.APIDiff;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


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
        assertEquals(true, consoleContent.contains("API Comparison Results"));
        assertEquals(true, consoleContent.contains("Delivered by Open API Diff tooling"));
        assertEquals(true, consoleContent.contains("Breaking Changes"));
        assertEquals(true, consoleContent.contains("Potentially Breaking Changes"));
        assertEquals(true, consoleContent.contains("+  means added"));
        assertEquals(true, consoleContent.contains("-  means removed"));
        assertEquals(true, consoleContent.contains("@  means altered"));
        assertEquals(true, consoleContent.contains("!  means (in Existing API)"));
        assertEquals(true, consoleContent.contains(">  means (in the New API)"));
        assertEquals(true, consoleContent.contains("(C) means (Compliance)"));
        assertEquals(true, consoleContent.contains(existing));
        assertEquals(true, consoleContent.contains(future));
    }

    @Test
    public void testMarkdown() {
        APIDiff api = new APIDiff(existing, future, diffLevel, maturity, versions);
        String mdContent = new MarkdownRender(
            "API Comparison Results", "Delivered by Open API Diff tooling", existing, future).render(api);
        assertNotNull(mdContent);
        assertEquals(true, mdContent.contains("API Comparison Results"));
        assertEquals(true, mdContent.contains("Delivered by Open API Diff tooling"));
        assertEquals(true, mdContent.contains("Breaking Changes"));
        assertEquals(true, mdContent.contains("Potentially Breaking Changes"));
        assertEquals(true, mdContent.contains("` + ` means added"));
        assertEquals(true, mdContent.contains("` - ` means removed"));
        assertEquals(true, mdContent.contains("` @ ` means altered"));
        assertEquals(true, mdContent.contains("` ! ` means issue found in Existing API"));
        assertEquals(true, mdContent.contains("` > ` means issues found in the New API"));
        assertEquals(true, mdContent.contains("*` C   `* means (Compliance)"));
        assertEquals(true, mdContent.contains(existing));
        assertEquals(true, mdContent.contains(future));
    }

    @Test
    public void testHTML() {
        APIDiff api = new APIDiff(existing, future, diffLevel, maturity, versions);
        String htmlContent = new HtmlRender(
            "API Comparison Results", "Delivered by Open API Diff tooling", existing, future).render(api);
        assertNotNull(htmlContent);
        assertEquals(true, htmlContent.contains("API Comparison Results"));
        assertEquals(true, htmlContent.contains("Delivered by Open API Diff tooling"));
        assertEquals(true, htmlContent.contains("Breaking Changes"));
        assertEquals(true, htmlContent.contains("Potentially Breaking Changes"));
        assertEquals(true, htmlContent.contains("(+)"));
        assertEquals(true, htmlContent.contains("(-)"));
        assertEquals(true, htmlContent.contains("(@)"));
        assertEquals(true, htmlContent.contains("(!)"));
        assertEquals(true, htmlContent.contains("(C)"));
        assertEquals(true, htmlContent.contains(existing));
        assertEquals(true, htmlContent.contains(future));
    }

}
