import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;

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

        // Print summary
        System.out.println("========================================");
        System.out.println("        JAVA PROJECT ANALYZER");
        System.out.println("========================================");

        System.out.println();
        System.out.println("PROJECT SUMMARY");
        System.out.println("----------------------------------------");

        System.out.println("Java Files       : " + files.size());
        System.out.println("Total Lines      : " + totalLines);
        System.out.println("Total Classes    : " + totalClasses);
        System.out.println("Total Methods    : " + totalMethods);

        // Print information about every Java file
        System.out.println();
        System.out.println("FILE DETAILS");
        System.out.println("----------------------------------------");

        for (JavaFileInfo info : files) {

            System.out.println(
                    info.fileName +
                            " | " +
                            info.lineCount +
                            " lines | " +
                            info.classCount +
                            " class | " +
                            info.methodCount +
                            " methods | " +
                            info.commentCount +
                            " comments | " +
                            String.format("%.2f", info.commentPercentage) +
                            "%");
        }
        writeReport(
                files,
                totalLines,
                totalClasses,
                totalMethods);

        System.out.println();
        System.out.println("========================================");
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
        int count = 0;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("class ") || line.startsWith("public class")) {
                    count++;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read: " + file.getName());
        }
        return count;
    }

    // Count Methods
    static int countMethods(File file) {
        int count = 0;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.contains("(") &&
                        line.contains(")") &&
                        (line.contains("public ") ||
                                line.contains("private ") ||
                                line.contains("protected "))) {

                    count++;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not read: " + file.getName());
        }

        return count;
    }

    static int countComments(File file) {

        int count = 0;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("//") ||
                        line.startsWith("/*") ||
                        line.startsWith("*") ||
                        line.startsWith("*/")) {

                    count++;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not read: " + file.getName());
        }

        return count;
    }

    // Writing report.txt
    static void writeReport(
            List<JavaFileInfo> files,
            int totalLines,
            int totalClasses,
            int totalMethods) {

        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"))) {

            writer.write("========================================");
            writer.newLine();
            writer.write("        JAVA PROJECT ANALYZER");
            writer.newLine();
            writer.write("========================================");
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
                        info.fileName +
                                " | " +
                                info.lineCount +
                                " lines | " +
                                info.classCount +
                                " class | " +
                                info.methodCount +
                                " methods | " +
                                info.commentCount +
                                " comments | " +
                                String.format("%.2f", info.commentPercentage) +
                                "%");

                writer.newLine();
            }

            writer.newLine();
            writer.write("========================================");

        } catch (IOException e) {

            System.out.println("Could not create report.");
        }
    }
}
