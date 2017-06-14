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


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleSwaggerDiffTestVT {

    private static final String TARGET_RESULTS_REPORT = "target/results/reports";
    private final String PETSTORE_IO_V2 = "http://petstore.swagger.io/v2/swagger.json"; // String swagger_v1_doc = "petstore_v1.json";
    private final String PETSTORE_LOCAL_V2_DOC = "petstore_v2.json";

    private final String EMPTY_API = "empty_api.json";

    @Before
    public void createReportFolder() {
        File dir = new File(TARGET_RESULTS_REPORT);
        dir.mkdirs();
    }

    @Test
    public void testEqual() {
        APIDiff api = new APIDiff(PETSTORE_IO_V2, PETSTORE_IO_V2, Diff.LAISSEZ_FAIRE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertTrue(newEndpoints.isEmpty());
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
    }

    @Test
    public void testNewApi() {
        APIDiff api = new APIDiff(EMPTY_API, PETSTORE_IO_V2, Diff.LAISSEZ_FAIRE);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        String html = new HtmlRender("ChangeLog", "", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testNewApi.html";
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(html);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(newEndpoints.size() > 0);
        assertTrue(missingEndpoints.isEmpty());
        assertTrue(changedEndPoints.isEmpty());
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testDeprecatedApi() {
        APIDiff api = new APIDiff(PETSTORE_IO_V2, EMPTY_API, Diff.BREAKING);
        List<Endpoint> newEndpoints = api.getAddedEndpoints();
        List<Endpoint> missingEndpoints = api.getMissingEndpoints();
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        String html = new HtmlRender("ChangeLog", "", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testDeprecatedApi.html";
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
    public void testDiffDefault() {
        APIDiff api = new APIDiff(PETSTORE_IO_V2, PETSTORE_LOCAL_V2_DOC);
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        String html = new HtmlRender("ChangeLog", "DiffReport", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testDiff.html";
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(html);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(changedEndPoints.isEmpty());
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testDiff() {
        APIDiff api = new APIDiff(PETSTORE_IO_V2, PETSTORE_LOCAL_V2_DOC, Diff.BREAKING);
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        String html = new HtmlRender("ChangeLog", "DiffReport", "", "").render(api);
        String fileName = TARGET_RESULTS_REPORT + "/testDiff.html";
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(html);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(changedEndPoints.isEmpty());
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testDiffAndMarkdown() {
        APIDiff api = new APIDiff(PETSTORE_IO_V2, PETSTORE_LOCAL_V2_DOC, Diff.LAISSEZ_FAIRE);
        String render = new MarkdownRender("ChangeLog", "DiffReport", "", "").render(api);
        String fileName = "src/test/resources/testDiff.md";
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(render);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void testChangedEndpoint() {
        APIDiff api = new APIDiff(PETSTORE_LOCAL_V2_DOC, "petstore_v3.json", Diff.LAISSEZ_FAIRE);
        List<ResourceDiff> changedEndPoints = api.getChangedResourceDiffs();
        assertFalse(changedEndPoints.size() == 1);
    }

}
