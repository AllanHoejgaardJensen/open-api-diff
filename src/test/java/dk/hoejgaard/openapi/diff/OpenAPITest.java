package dk.hoejgaard.openapi.diff;


import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;


import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class OpenAPITest {

    @Test
    public void testInputHandlingCorrectInput() throws Exception {
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report.txt", "all", "full", "1"};
        OpenAPIDiff.main(argsTxt);
        assertTrue(Files.exists(Paths.get("./target/output/reports/Test-Report.txt")));

        String[] mdArgs = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report.md", "all", "full", "1"};
        OpenAPIDiff.main(mdArgs);
        assertTrue(Files.exists(Paths.get("./target/output/reports/Test-Report.md")));

        String[] argsHtml = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report.html", "all", "full", "1"};
        OpenAPIDiff.main(argsHtml);
        assertTrue(Files.exists(Paths.get("./target/output/reports/Test-Report.html")));
    }

    @Test
    public void testIncorrectInput2() throws Exception {
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports"};
        OpenAPIDiff.main(argsTxt);
        assertTrue(Files.exists(Paths.get("./target/output/reports/APIDiff.txt")));
    }

    @Test
    public void testIncorrectInput3() throws Exception {
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report-3.md"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report-2.md")));
    }

    @Test
    public void testIncorrectInput4() throws Exception {
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report-3.md", "all"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report-2.md")));
    }

    @Test
    public void testIncorrectInput5() throws Exception {
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report-3.md", "all", "full"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report-2.md")));
    }

    @Test
    public void testIncorrectInputWrongDepth() throws Exception{
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report-Depth.md", "wrong", "full"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report-Depth.md")));
    }

    @Test
    public void testIncorrectInputWrongMaturity() throws Exception{
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report-Maturity.md", "all", "wrong", "1"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report-Maturity.md")));
    }

    @Test
    public void testIncorrectInputWrongVersions() throws Exception{
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report-Versions1.md", "all", "full", "100"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report-Versions1.md")));
    }

    @Test
    public void testIncorrectInputWrongFileExtension() throws Exception{
        String[] argsTxt = {"./sample-api/elaborate_example_v1.json", "./sample-api/elaborate_example_v3f.json",
            "./target/output/reports", "Test-Report.nosuchfile", "all", "full", "1"};
        OpenAPIDiff.main(argsTxt);
        assertFalse(Files.exists(Paths.get("./target/output/reports/Test-Report.nosuchfile")));
    }
}
