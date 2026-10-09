import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Analyzer {

    public static void main(String[] args) {

        String path = args[0];

        File project = new File(path);

        // Analyze the entire project
        List<JavaFileInfo> files = analyzeDirectory(project);

        // Total Lines
        int totalLines = 0;
        for (JavaFileInfo info : files) {
            totalLines += info.lineCount;
        }

        // Total classes
        int totalClasses = 0;
        for (JavaFileInfo info : files) {
            totalClasses += info.classCount;
        }

        // Total methods
        int totalMethods = 0;

        for (JavaFileInfo info : files) {
            totalMethods += info.methodCount;
        }

        // Largest file
        JavaFileInfo largestFile = null;
        for (JavaFileInfo info : files) {
            if (largestFile == null || info.lineCount > largestFile.lineCount) {
                largestFile = info;
            }
        }

        writeReport(
                files,
                totalLines,
                totalClasses,
                totalMethods);

    }

    // Recursively find all Java files
    static List<JavaFileInfo> analyzeDirectory(File directory) {

        List<JavaFileInfo> files = new ArrayList<>();

        File[] subfiles = directory.listFiles();

        for (File file : subfiles) {

            if (file.isDirectory()) {

                // Ignore Git's internal directory
                if (!file.getName().equals(".git")) {

                    // Get Java files from this directory
                    // and add them to our list
                    files.addAll(analyzeDirectory(file));
                }

            } else if (file.getName().toLowerCase().endsWith(".java")) {

                // Create information about this Java file
                JavaFileInfo info = new JavaFileInfo();

                info.fileName = file.getName();
                info.lineCount = countLines(file);
                info.classCount = countClasses(file);
                info.methodCount = countMethods(file);
                info.commentCount = countComments(file);
                info.commentPercentage = (double) info.commentCount / info.lineCount * 100;

                // Store the object
                files.add(info);
            }
        }

        return files;
    }

    // Count the number of lines in a file
    static int countLines(File file) {

        int count = 0;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                count++;
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not read: " + file.getName());
        }

        return count;
    }

    // CountClasses
    static int countClasses(File file) {
        try {
            JavaSourceScanner.ScanResult result = JavaSourceScanner.scan(file.toPath());

            Matcher matcher = Pattern.compile(
                    "\\bclass\\s+[A-Za-z_$][\\w$]*").matcher(result.source);

            int count = 0;

            while (matcher.find()) {
                count++;
            }

            return count;

        } catch (IOException e) {
            System.out.println("Could not read: " + file.getName());
            return 0;
        }
    }

    // Count Methods
    static int countMethods(File file) {
        try {
            JavaSourceScanner.ScanResult result = JavaSourceScanner.scan(file.toPath());

            String source = result.source;

            Pattern pattern = Pattern.compile(
                    "(?m)^\\s*(?:(?:public|protected|private|static|final|abstract|synchronized|native|default|strictfp)\\s+)*"
                            +
                            "(?:[\\w$<>\\[\\],.?]+\\s+)+" +
                            "[A-Za-z_$][\\w$]*\\s*\\([^;{}]*\\)\\s*(?:throws\\s+[\\w$.,\\s]+)?\\s*\\{");

            Matcher matcher = pattern.matcher(source);
            int count = 0;

            while (matcher.find()) {
                count++;
            }

            return count;

        } catch (IOException e) {
            System.out.println("Could not read: " + file.getName());
            return 0;
        }
    }

    // read comments
    static int countComments(File file) {
        try {
            return JavaSourceScanner.scan(file.toPath()).commentLines;
        } catch (IOException e) {
            System.out.println("Could not read: " + file.getName());
            return 0;
        }
    }

    // Writing report.txt

    static void writeReport(
            List<JavaFileInfo> files,
            int totalLines,
            int totalClasses,
            int totalMethods) {

        String reportPath = "../report/report.txt";

        try (
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(reportPath))) {

            String timestamp = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date());

            writer.write("========================================");
            writer.newLine();
            writer.write("          JAVA PROJECT ANALYZER");
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

            writer.write("Generated At : " + timestamp);
            writer.newLine();
            writer.newLine();

            writer.write("PROJECT SUMMARY");
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            writer.write("Java Files       : " + files.size());
            writer.newLine();
            writer.write("Total Lines      : " + totalLines);
            writer.newLine();
            writer.write("Total Classes    : " + totalClasses);
            writer.newLine();
            writer.write("Total Methods    : " + totalMethods);
            writer.newLine();
            writer.newLine();

            writer.write("FILE DETAILS");
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            for (JavaFileInfo info : files) {
                writer.write(
                        info.fileName
                                + " | " + info.lineCount + " lines"
                                + " | " + info.classCount + " classes"
                                + " | " + info.methodCount + " methods"
                                + " | " + info.commentCount + " comment lines"
                                + " | " + String.format(
                                        java.util.Locale.US,
                                        "%.2f",
                                        info.commentPercentage)
                                + "%");

                writer.newLine();
            }

            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.write("             END OF REPORT");
            writer.newLine();
            writer.write("========================================");
            writer.newLine();

        } catch (IOException e) {
            System.err.println(
                    "Could not create report at " + reportPath
                            + ": " + e.getMessage());
        }
    }

}
