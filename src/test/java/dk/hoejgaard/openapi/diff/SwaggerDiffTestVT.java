package dk.hoejgaard.openapi.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.model.Endpoint;
import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import dk.hoejgaard.openapi.diff.output.HtmlRender;
import dk.hoejgaard.openapi.diff.output.MarkdownRender;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class SwaggerDiffTestVT {

    private static final String TARGET_RESULTS_REPORT = "target/results/reports";
    private final String REFERENCE_API = "elaborate_example_v2.json";
    private final String SUBJECT_API = "elaborate_example_v2a.json";

    @Test
    public void testDiffHTML() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL, Maturity.HATEOAS, Versions.DOUBLE);
        String html = new HtmlRender("ChangeLog", "", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testReportDiff.html";
        try {
            File dir = new File(TARGET_RESULTS_REPORT);
            dir.mkdirs();
            FileWriter fw = new FileWriter(fileName);
            fw.write(html);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testDiffMarkdown() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL);
        String render = new MarkdownRender("TestTitle", "TestSubTitle", "reference", "candidate").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testReportDiff.md";
        try {
            File dir = new File(TARGET_RESULTS_REPORT);
            dir.mkdirs();
            FileWriter fw = new FileWriter(fileName);
            fw.write(render);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testDeeper() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL, Maturity.FULL, Versions.DOUBLE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(!changedEndPoints.isEmpty());
        //should find at least one content-type breaking change
    }

}
