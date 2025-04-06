package org.msse696.automation;

import org.msse696.optimization.helper.report.HtmlReport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The PaddingAnalyzer class analyzes the field arrangement in a given class
 * to identify and optimize memory usage by minimizing padding.
 */
public class PaddingAnalyzer implements Analyzer {

    private static final String OUTPUT_REPORT = "target/results/reports/padding_report.html";

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

    /**
     * Analyzes a class's field arrangement, checks if optimization is needed,
     * and generates a report if necessary.
     *
     * @param clazz The class to analyze.
     * @return True if optimization is required, false otherwise.
     */
    @Override
    public boolean analyze(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<FieldAnalysis> actualOrder = new ArrayList<>();
        List<FieldAnalysis> recommendedOrder = new ArrayList<>();

        int actualTotalBytes = 0;
        int recommendedTotalBytes = 0;

        System.out.println("Analyzing class: " + clazz.getName());
        for (Field field : fields) {
            String type = field.getType().getSimpleName();
            int size = getFieldSize(type);
            if (size == -1) {
                System.out.println("Unknown type: " + type);
                continue;
            }

            actualOrder.add(new FieldAnalysis(field.getName(), type, size));
            recommendedOrder.add(new FieldAnalysis(field.getName(), type, size));
        }

        // Sorting recommended order in descending size for efficient padding
        recommendedOrder.sort(Comparator.comparingInt((FieldAnalysis fa) -> fa.size).reversed());

        // Calculate object size for actual and recommended orders
        actualTotalBytes = calculateObjectSize(actualOrder);
        recommendedTotalBytes = calculateObjectSize(recommendedOrder);

        // Check if optimization is necessary
        boolean optimizationNeeded = !isSameOrder(actualOrder, recommendedOrder);
        if (optimizationNeeded) {
            System.out.println("\nOptimization is required. Creating report...");
            System.out.println("Actual object size: " + actualTotalBytes + " bytes");
            System.out.println("Recommended object size: " + recommendedTotalBytes + " bytes");

            // Prepare data for the HTML report
            String[][] actualData = prepareTableData(actualOrder);
            String[][] recommendedData = prepareTableData(recommendedOrder);

            // Generate the report using the dedicated method
            generateReport(
                    "Padding Analysis Report for " + clazz.getName(),
                    "Actual Order (Total Size: " + actualTotalBytes + " bytes)",
                    actualData,
                    "Recommended Order (Total Size: " + recommendedTotalBytes + " bytes)",
                    recommendedData,
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo optimization required. Report will not be generated.");
        }

        return optimizationNeeded;
    }

    /**
     * Generates an HTML report using the provided data.
     *
     * @param title            The title of the report.
     * @param actualHeader     The header for actual analysis data.
     * @param actualData       The actual analysis data.
     * @param recommendedHeader The header for recommended improvements.
     * @param recommendedData  The recommended improvement data.
     * @param outputPath       The file path where the report will be saved.
     */
    @Override
    public void generateReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputPath) {
        HtmlReport.generateHtmlReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputPath);
    }

    /**
     * Calculates the total object size including padding for proper alignment.
     *
     * @param fieldAnalysisList List of fields to analyze.
     * @return Total object size including padding.
     */
    private int calculateObjectSize(List<FieldAnalysis> fieldAnalysisList) {
        int totalSize = 0;
        int currentOffset = 0;

        for (FieldAnalysis fa : fieldAnalysisList) {
            int alignment = fa.size; // Assume alignment equals field size for simplicity
            int padding = (currentOffset % alignment == 0) ? 0 : alignment - (currentOffset % alignment);

            fa.offset = currentOffset + padding; // Update field offset considering padding
            currentOffset = fa.offset + fa.size; // Move to next field's offset
            totalSize = currentOffset; // Update total size
        }

        return totalSize;
    }

    /**
     * Converts field analysis data into a 2D array suitable for HTML table generation.
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
     * Checks if the actual and recommended field orders are identical.
     *
     * @param actualOrder List representing the actual field order.
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

            // Boxed types
            case "Boolean", "Byte", "Character", "Short", "Integer", "Float" -> 16;
            case "Long", "Double" -> 24;

            // Other common types
            case "Object" -> 16;
            case "String" -> 40; // Metadata and content size (approximation)

            default -> -1; // Unknown type
        };
    }

    /**
     * Returns the report file path.
     *
     * @return The report file path.
     */
    public String getReportName() {
        return OUTPUT_REPORT;
    }
}
