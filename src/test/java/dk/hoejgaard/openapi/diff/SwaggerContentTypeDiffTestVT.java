package dk.hoejgaard.openapi.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.compare.ResourceDiff;
import dk.hoejgaard.openapi.diff.model.Endpoint;
import dk.hoejgaard.openapi.diff.output.HtmlRender;
import dk.hoejgaard.openapi.diff.output.MarkdownRender;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertTrue;

public class SwaggerContentTypeDiffTestVT {

    private static final String TARGET_RESULTS_REPORT = "target/results/reports";
    private final String REFERENCE_API = "elaborate_example_v1.json";
    private final String SUBJECT_API = "elaborate_example_v2.json";
    private final String EMPTY_API = "empty_api.json";

    @Before
    public void createReportFolder() {
        File dir = new File(TARGET_RESULTS_REPORT);
        dir.mkdirs();
    }

    @Test
    public void testEqual() {
        APIDiff api = new APIDiff(REFERENCE_API, REFERENCE_API, Diff.BREAKING);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());

    }

    @Test
    public void testNewApi() {
        APIDiff api = new APIDiff(EMPTY_API, REFERENCE_API, Diff.BREAKING);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        String html = new HtmlRender("ChangeLog", "", "", "").render(api);
        try {
            FileWriter fw = new FileWriter(
                "src/test/resources/testElaboratedNewApi.html");
            fw.write(html);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(newEndpoints.size() > 0);
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());

    }

    @Test
    public void testDeprecatedApi() {
        APIDiff api = new APIDiff(REFERENCE_API, EMPTY_API, Diff.BREAKING);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        String html = new HtmlRender("ChangeLog", "", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testElaboratedDeprecatedApi.html";
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(html);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.size() > 0);
        assertTrue(changedEndPoints.isEmpty());
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testDiffContentTypeRemoved() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.BREAKING);
        String html = new HtmlRender("ChangeLog", "", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testElaboratedDiff.html";
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
    public void testDiffAndMarkdown() {
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.BREAKING);
        String render = new MarkdownRender("TestTitle", "TestSubTitle", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testElaboratedDiff.md";
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
        APIDiff api = new APIDiff(REFERENCE_API, SUBJECT_API, Diff.ALL);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(!changedEndPoints.isEmpty());
        //should find at least one content-type breaking change
    }

}
