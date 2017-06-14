package dk.hoejgaard.openapi.diff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import dk.hoejgaard.openapi.diff.criteria.Diff;
import dk.hoejgaard.openapi.diff.criteria.Maturity;
import dk.hoejgaard.openapi.diff.criteria.Versions;
import dk.hoejgaard.openapi.diff.output.ConsoleRender;
import dk.hoejgaard.openapi.diff.output.HtmlRender;
import dk.hoejgaard.openapi.diff.output.MarkdownRender;
import dk.hoejgaard.openapi.diff.output.XmlRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * OpenAPIDiff is the main entry point for running a compliance report for an existing API against a new API.
 * See the documentation in the readme.md file for more details on how to use it as a tool from the command line.
 */
class OpenAPIDiff {
    private static final String TARGET_RESULTS_REPORT = "./target/output/reports";
    private static final String TARGET_REPORT_FILENAME = "APIDiff.txt";
    private static Logger logger = LoggerFactory.getLogger(OpenAPIDiff.class);
    private static String existing = "./sample-api/elaborate_example_v1.json"; // alternative - try out - "./sample-api/petstore_v1.json";
    private static String future = "./sample-api/elaborate_example_v3f.json"; // alternative - try out - "./sample-api/petstore_v2.json";
    private static Versions versions = Versions.SINGLE;
    private static Maturity maturity = Maturity.HAL;
    private static Diff diffLevel = Diff.ALL;
    private static String reportFolder = TARGET_RESULTS_REPORT;
    private static String reportFileName = TARGET_REPORT_FILENAME;

    private OpenAPIDiff() {
        //intentionally empty
    }

    /**
     * The APIDiff can be used is started with "java OpenAPIDiff  existingAPI(path+file) futureAPI(path+file) resultPath resultFileName"
     * or with the shorter form "java OpenAPIDiff  existingAPI(path+file) futureAPI(path+file)" where it will be stores in the folder it
     * was executed in in a file called OpenApiDiff.txt
     *
     * @param args existingAPI (a path to the file of the existing API) futureAPI (a path to the future API) resultPath resultFileName
     * @throws Exception if the API files are not found or if trouble with creating the report or reportFolder
     **/
    public static void main(String[] args) throws Exception {
        if (args.length >= 2) {
            existing = args[0];
            future = args[1];
            checkingFilesAndDisplayStatus(args, existing, future);
            if (!Files.exists(Paths.get(existing)) || !Files.exists(Paths.get(future))) {
                displayUsageMsg();
            }
            handleUserInput(args);
        } else {
            //pretend input
            maturity = Maturity.FULL;
            diffLevel = Diff.ALL;
            reportFileName = "APIDIFF-TXT.txt";
        }
        System.out.print("The result of the comparing APIs - can be found in: " + reportFolder + "/" + reportFileName + "\n");
        System.out.print("Comparing APIs - using diff: " + diffLevel + " maturity: " + maturity + " versions: " + versions + "\n");

        String content = "Empty Report";

        APIDiff api = new APIDiff(existing, future, diffLevel, maturity, versions);
        if (reportFileName.endsWith(".txt")) {
            String consoleContent = new ConsoleRender(
                "API Comparison Results", "Delivered by Open API Diff tooling", existing, future).render(api);
            writeReport(reportFolder, "console-" + reportFileName, consoleContent);
            System.out.println(consoleContent);
            content = filtered(consoleContent);
        } else if (reportFileName.endsWith(".md")) {
            content = new MarkdownRender(
                "API Comparison Results", "Delivered by Open API Diff tooling", existing, future)
                .render(api);
        } else if (reportFileName.endsWith(".html")) {
            content = new HtmlRender(
                "API Comparison Results", "Delivered by Open API Diff tooling", existing, future)
                .render(api);
        } else if (reportFileName.endsWith(".xml")) {
            content = new XmlRender(
                "API Comparison Results", "Delivered by Open API Diff tooling", "Delivered by Open API Diff tooling", existing, future)
                .render(api);
        } else {
            logger.warn("The report format specified by the file extension did not match a supported report, the name was: {} ",
                reportFileName);
        }
        System.out.println("\n" + content);
        writeReport(reportFolder, reportFileName, content.trim() + "\n");
    }

    private static String filtered(String consoleContent) {
        return consoleContent.replaceAll("\u001B\\[[0-9]{1,2}m", "");
    }

    private static void handleUserInput(String[] args) {
        if ((args.length > 2) && (args.length < 6)) {
            if (isDiffArgument(args[2])) {
                diffLevel = getDiffArgument(args[2]);
                maturity = getMaturityArgument(args[3]);
                versions = getVersionArgument(args[4]);
            } else {
                reportFolder = args[2];
                reportFileName = args[3];
            }
        }
        if ((args.length > 5) && (args.length < 8)) {
            reportFolder = args[2];
            reportFileName = args[3];
            if (isDiffArgument(args[4])) {
                diffLevel = getDiffArgument(args[4]);
                maturity = getMaturityArgument(args[5]);
                versions = getVersionArgument(args[6]);
            }
        }
    }

    private static void writeReport(String reportFolder, String reportFileName, String content) {
        try {
            File dir = new File(reportFolder);
            if (dir.mkdirs()) {
                System.out.println("Creating dir = " + dir);
                logger.info("Creating dir = {}", dir);
            }
            try (FileWriter fw = new FileWriter(reportFolder + "/" + reportFileName)) {
                fw.write(content);
                fw.close();
            }
        } catch (IOException e) {
            System.out.println("A problem occurred while attempting to write file: " + reportFolder + "/" + reportFileName);
            logger.error("A problem occurred while attempting to write file: {}/{} ", reportFolder, reportFileName);
        }
    }

    private static Versions getVersionArgument(String arg) {
        if ("1".equals(arg)) return Versions.SINGLE;
        if ("2".equals(arg)) return Versions.DOUBLE;
        if ("3".equals(arg)) return Versions.TRIPLE;
        return versions;
    }

    private static Maturity getMaturityArgument(String arg) {
        if ("full".equals(arg) || "f".equals(arg)) return Maturity.FULL;
        if ("hal".equals(arg) || "h".equals(arg)) return Maturity.HAL;
        if ("low".equals(arg) || "l".equals(arg)) return Maturity.LOW;
        if ("non".equals(arg) || "n".equals(arg)) return Maturity.NONE;
        return maturity;
    }

    private static Diff getDiffArgument(String arg) {
        if (isDiffAll(arg)) return Diff.ALL;
        if (isDiffBreaking(arg)) return Diff.BREAKING;
        if (isDiffPotentiallyBreaking(arg)) return Diff.POTENTIALLY_BREAKING;
        if (isDiffLaissezFaire(arg)) return Diff.LAISSEZ_FAIRE;
        return diffLevel;
    }

    private static boolean isDiffArgument(String arg) {
        return isDiffAll(arg) || isDiffBreaking(arg) ||
            isDiffPotentiallyBreaking(arg) || isDiffLaissezFaire(arg);
    }

    private static boolean isDiffLaissezFaire(String arg) {
        return "laissez-faire".equals(arg) || "l".equals(arg);
    }

    private static boolean isDiffPotentiallyBreaking(String arg) {
        return "potentiallybreaking".equals(arg) || "pb".equals(arg);
    }

    private static boolean isDiffBreaking(String arg) {
        return "breaking".equals(arg) || "b".equals(arg);
    }

    private static boolean isDiffAll(String arg) {
        return "all".equals(arg) || "a".equals(arg);
    }

    private static void checkingFilesAndDisplayStatus(String[] args, String existing, String future) {
        String existStr = "Reading existing API from " + args[0];
        logger.info("Reading existing API from {} ", args[0]);
        String resultOld = Files.exists(Paths.get(existing)) ? "ok\n" : "did not exist\n";
        logger.info("Existing API -> file found {} ", resultOld);
        System.out.print(existStr + " -> file found " + resultOld);
        String newStr = "Reading new API from " + args[1];
        String resultNew = Files.exists(Paths.get(future)) ? "ok\n" : "did not exist\n";
        logger.info("New API -> file found {}", resultNew);
        System.out.println(newStr + " -> file found " + resultNew);
    }

    private static void displayUsageMsg() {
        System.out.println("THE USAGE IS: java OpenAPIDiff  existingAPI(path+file) futureAPI(path+file) " +
            "[existingAPI(path+file) futureAPI(path+file)]\n");
        System.out.println("- which means at least an existing API and the new one that the existing is compared to is necessary");
        System.out.println("- if the resulting file needs to go to a specific folder with a specific name - the two latter arguments" +
            "needs to be included");

        System.out.println("- if you run the file from a folder where the api's are in a subfolder the syntax would be:\n");
        System.out.println("  java OpenAPIDiff ./apis/existing-api.json ./apis/new-api.json\n\n");

        System.out.println("- if you run the file from a folder where the api's are in a subfolder and wants the result in a report " +
            "sub-folder having the name api-diff.txt the syntax would be:\n");
        System.out.println("  java OpenAPIDiff ./apis/existing-api.json ./apis/new-api.json report api-diff.txt \n\n");

        System.out.println("- if you do not want the full check and the full maturity and more than single overlapping producer versions" +
            " then you can add arguments and the syntax would then be:\n");
        System.out.println("  java OpenAPIDiff ./apis/existing-api.json ./apis/new-api.json report api-diff.txt" +
            " [diff-level] [maturity] [versions]\n");
        System.out.println("  -  [diff-level can be: all/a, breaking/b, potentiallybreaking/pb, laissez-faire/l]");
        System.out.println("  -  [maturity can be: full/f, hal/h(default), low/l, non/n] - full includes all the opinionated parts");
        System.out.println("  -  [version can be: 1(default)/2/3 - which works with hal maturity to check for the correct version overlap");
    }

}
