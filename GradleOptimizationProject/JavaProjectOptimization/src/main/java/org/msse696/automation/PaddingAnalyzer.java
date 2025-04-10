package org.msse696.automation;

import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.HtmlReport;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The PaddingAnalyzer class analyzes the field arrangement in a given Java file
 * to identify and optimize memory usage by minimizing padding.
 */
public class PaddingAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/padding_report.html";
    private boolean isEfficient;

    /**
     * A simple class to represent field analysis data.
     */
    public static class FieldAnalysis {
        public String name;
        public String type;
        public int size;
        public int offset; // Offset including padding for actual order

        FieldAnalysis(String name, String type, int size) {
            this.name = name;
            this.type = type;
            this.size = size;
            this.offset = 0; // Initialized to 0, calculated later
        }
    }

    @Override
    public boolean analyze(File javaFile, boolean createReport) {
        List<FieldAnalysis> actualOrder = new ArrayList<>();
        List<FieldAnalysis> recommendedOrder = new ArrayList<>();
        isEfficient = true;

        int actualTotalBytes = 0;
        int recommendedTotalBytes = 0;

        Debug.info("Analyzing file: " + javaFile.getName());

        try (FileInputStream fileInputStream = new FileInputStream(javaFile)) {
            // Read file content
            String fileContent = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Debug.info("File content successfully read.");

            // Extract fields using regex
            List<String[]> fields = extractFields(fileContent);
            if (fields.isEmpty()) {
                Debug.info("No fields found in the file.");
                return false;
            }

            // Analyze fields and calculate sizes
            for (String[] field : fields) {
                String name = field[1];
                String type = field[0];
                int size = getFieldSize(type);

                if (size == -1) {
                    Debug.info("Unknown type: " + type);
                    continue;
                }

                actualOrder.add(new FieldAnalysis(name, type, size));
                recommendedOrder.add(new FieldAnalysis(name, type, size));
            }

            // Sorting recommended order by descending size for efficient padding
            recommendedOrder.sort(Comparator.comparingInt((FieldAnalysis fa) -> fa.size).reversed());

            // Calculate object size for actual and recommended orders
            actualTotalBytes = calculateObjectSize(actualOrder);
            recommendedTotalBytes = calculateObjectSize(recommendedOrder);

            // Check if optimization is necessary
            boolean isEfficient = isSameOrder(actualOrder, recommendedOrder);
            if (!isEfficient && createReport) {
                Debug.info("\nOptimization is required. Creating report...");
                Debug.info("Actual object size: " + actualTotalBytes + " bytes");
                Debug.info("Recommended object size: " + recommendedTotalBytes + " bytes");

                // Prepare data for the HTML report
                String[][] actualData = prepareTableData(actualOrder);
                String[][] recommendedData = getRecommendedData();

                // Generate the report
                generateReport(
                        "Padding Analysis Report for " + javaFile.getName(),
                        "Actual Order (Total Size: " + actualTotalBytes + " bytes)",
                        actualData,
                        "Recommended Order (Total Size: " + recommendedTotalBytes + " bytes)",
                        recommendedData,
                        OUTPUT_REPORT
                );
                return true;
            } else {
                Debug.info("\nNo optimization required. Report will not be generated.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + javaFile.getPath());
        }
        return false;
    }

    /**
     * Extracts fields from the content of a Java file using regex.
     *
     * @param fileContent The content of the Java file.
     * @return A list of fields, each represented as a String array [type, name].
     */
    private List<String[]> extractFields(String fileContent) {
        List<String[]> fields = new ArrayList<>();
        Pattern fieldPattern = Pattern.compile("(private|protected|public)?\\s*(\\w+)\\s+(\\w+);");
        Matcher matcher = fieldPattern.matcher(fileContent);

        while (matcher.find()) {
            String type = matcher.group(2); // Field type
            String name = matcher.group(3); // Field name
            fields.add(new String[]{type, name});
        }

        return fields;
    }

    /**
     * Calculates the total object size, including padding, for proper alignment.
     *
     * @param fieldAnalysisList List of fields to analyze.
     * @return Total object size, including padding.
     */
    private int calculateObjectSize(List<FieldAnalysis> fieldAnalysisList) {
        int totalSize = 0;
        int currentOffset = 0;

        for (FieldAnalysis fa : fieldAnalysisList) {
            int alignment = fa.size; // Assume alignment equals field size
            int padding = (currentOffset % alignment == 0) ? 0 : alignment - (currentOffset % alignment);

            fa.offset = currentOffset + padding; // Update field offset
            currentOffset = fa.offset + fa.size; // Move to next field's offset
            totalSize = currentOffset; // Update total size
        }

        return totalSize;
    }

    /**
     * Prepares field analysis data as a 2D array for HTML table generation.
     *
     * @param fieldAnalysisList List of FieldAnalysis objects to convert.
     * @return A 2D string array representing the table rows.
     */
    private String[][] prepareTableData(List<FieldAnalysis> fieldAnalysisList) {
        String[][] tableData = new String[fieldAnalysisList.size() + 1][4];
        tableData[0] = new String[]{"Field Name", "Type", "Size (bytes)", "Offset"}; // Header row
        for (int i = 0; i < fieldAnalysisList.size(); i++) {
            FieldAnalysis fa = fieldAnalysisList.get(i);
            tableData[i + 1] = new String[]{fa.name, fa.type, String.valueOf(fa.size), String.valueOf(fa.offset)};
        }
        return tableData;
    }

    /**
     * Determines the size in bytes of a given field type.
     *
     * @param type The field type as a string.
     * @return The size of the field type in bytes, or -1 if unknown.
     */
    public int getFieldSize(String type) {
        return switch (type) {
            case "boolean", "byte" -> 1;
            case "char", "short" -> 2;
            case "int", "float" -> 4;
            case "long", "double" -> 8;
            case "Boolean", "Byte", "Character", "Short", "Integer", "Float" -> 16;
            case "Long", "Double" -> 24;
            case "Object" -> 16;
            case "String" -> 40;
            default -> -1; // Unknown type
        };
    }

    /**
     * Checks if the actual and recommended field orders are identical.
     *
     * @param actualOrder      List representing the actual field order.
     * @param recommendedOrder List representing the recommended field order.
     * @return True if the orders are identical, false otherwise.
     */
    protected static boolean isSameOrder(List<FieldAnalysis> actualOrder, List<FieldAnalysis> recommendedOrder) {
        if (actualOrder.size() != recommendedOrder.size()) {
            return false;
        }
        for (int i = 0; i < actualOrder.size(); i++) {
            if (!actualOrder.get(i).name.equals(recommendedOrder.get(i).name)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader,
                               String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
    }

    @Override
    public String getReport() {
        return OUTPUT_REPORT;
    }

    @Override
    public boolean isEfficient() {
        return isEfficient;
    }

    @Override
    public String[][] getRecommendedData() {
        return new String[][]{
                {"Example", "Code"},
                {"Inefficient Code", """
            <pre><code>
            public class PaddingTestClassInefficient {
                boolean flag;
                long id;
                char grade;
                int value; // Inefficient field arrangement
            }
            </code></pre>
            """},
                {"Efficient Code", """
            <pre><code>
            public class PaddingTestClassEfficient {
                long id;
                int value;
                char grade;
                boolean flag; // Efficient field arrangement
            }
            </code></pre>
            """}
        };
    }
}
