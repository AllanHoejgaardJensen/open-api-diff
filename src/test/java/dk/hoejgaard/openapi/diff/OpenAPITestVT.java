package dk.hoejgaard.openapi.diff;


import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OpenAPITestVT {

    @Test
    public void testCompareAPIsAndCreateCustomTxtDiff() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-CUSTOM.txt"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-CUSTOM.txt")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDDiff() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-CUSTOM.md"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-CUSTOM.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomHTMLDiff() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-CUSTOM.html"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-CUSTOM.html")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDDiffWithAllFullOne() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-AF1a.md", "all", "full", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-AF1a.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithAllFullOneShort() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-AF1c.md", "a", "f", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-AF1c.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithAllFullTwoShort() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-AF2a.md", "a", "f", "2"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-AF2a.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithAllFullThreeShort() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-AF3a.md", "a", "f", "3"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-AF3a.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithBreakHalTwo() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-BH2a.md", "breaking", "hal", "2"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-BH2a.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithBreakHalTwoShort() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-BH2b.md", "b", "h", "2"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-BH2b.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithPotentialBreakLowOne() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-PL1a.md", "potentiallybreaking", "low", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-PL1a.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithPotentialBreakLowOneShort() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-PL1b.md", "pb", "l", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-PL1b.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithLowNonOne() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-LN1a.md", "laissez-faire", "non", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-LN1a.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDiffWithLowNonOneShort() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-LN1b.md", "l", "n", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-LN1b.md")));
    }

    @Test
    public void testCompareAPIsAndCreateCustomTxtDiffCheck() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-CUSTOM-TXT-C.txt", "a", "f", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-CUSTOM-TXT-C.txt")));
        String contents = new String(Files.readAllBytes(Paths.get("././target/output/reports/APIDiff-CUSTOM-TXT-C.txt")));
        String refContents = new String(Files.readAllBytes(Paths.get("./sample-reports/APIDIFF-TXT.txt")));
        assertEquals(refContents.trim(), contents.trim());
    }

    @Test
    public void testCompareAPIsAndCreateCustomMDDiffCheck() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-CUSTOM-MD-C.md", "a", "f", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-CUSTOM-MD-C.md")));
        String contents = new String(Files.readAllBytes(Paths.get("././target/output/reports/APIDiff-CUSTOM-MD-C.md")));
        String refContents = new String(Files.readAllBytes(Paths.get("./sample-reports/APIDIFF-MD.md")));
        assertEquals(refContents.trim(), contents.trim());
    }

    @Test
    public void testCompareAPIsAndCreateCustomHTMLDiffCheck() throws Exception {
        String[] args = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "APIDiff-CUSTOM-HTML-C.html", "a", "f", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff-CUSTOM-HTML-C.html")));
        String contents = new String(Files.readAllBytes(Paths.get("././target/output/reports/APIDiff-CUSTOM-HTML-C.html")));
        String refContents = new String(Files.readAllBytes(Paths.get("./sample-reports/APIDIFF-HTML.html")));
        assertEquals(refContents.trim(), contents.trim());
    }

    @Test
    public void testSeedHTMLDiffCheck() throws Exception {
        String[] args = {"./sample-api/seed-rest-server-jee7-swagger.json", "./sample-api/seed-swagger.json",
            "./target/output/reports", "APISeed.html", "a", "f", "1"};
        OpenAPIDiff.main(args);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APISeed.html")));
        String contents = new String(Files.readAllBytes(Paths.get("././target/output/reports/APISeed.html")));
        String refContents = new String(Files.readAllBytes(Paths.get("./sample-reports/APISeed.html")));
        assertEquals(refContents.trim(), contents.trim());
    }

}
