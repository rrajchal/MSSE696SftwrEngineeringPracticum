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
public class PaddingAnalyzer {
    private static final String OUTPUT_REPORT = "target/results/reports/padding_report.html";

    /**
     * A simple class to represent field analysis data.
     */
    public static class FieldAnalysis {
        public String name;
        public String type;
        public int size;

        FieldAnalysis(String name, String type, int size) {
            this.name = name;
            this.type = type;
            this.size = size;
        }
    }

    /**
     * Analyzes a class's field arrangement, checks if optimization is needed,
     * and generates a report if necessary.
     *
     * @param clazz The class to analyze.
     * @return True if optimization is required, false otherwise.
     */
    public static boolean analyze(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<FieldAnalysis> actualOrder = new ArrayList<>();
        List<FieldAnalysis> recommendedOrder = new ArrayList<>();

        int totalBytes = 0;

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
            totalBytes += size;
            System.out.println(field.getName() + " (" + type + "): " + size + " bytes");
        }

        // Sorting recommended order in descending size for efficient padding
        recommendedOrder.sort(Comparator.comparingInt((FieldAnalysis fa) -> fa.size).reversed());

        // Check if optimization is necessary
        boolean optimizationNeeded = !isSameOrder(actualOrder, recommendedOrder);
        if (optimizationNeeded) {
            System.out.println("\nOptimization is required. Creating report...");

            // Prepare data for the HTML report
            String[][] actualData = prepareTableData(actualOrder);
            String[][] recommendedData = prepareTableData(recommendedOrder);

            // Generate the report using HtmlReport
            HtmlReport.generateHtmlReport(
                    "Padding Analysis Report for " + clazz.getName(),
                    "Actual Order",
                    actualData,
                    "Recommended Order",
                    recommendedData,
                    OUTPUT_REPORT
            );
        } else {
            System.out.println("\nNo optimization required. Report will not be generated.");
        }

        return optimizationNeeded;
    }

    /**
     * Converts field analysis data into a 2D array suitable for HTML table generation.
     *
     * @param fieldAnalysisList List of FieldAnalysis objects to convert.
     * @return A 2D string array representing the table rows.
     */
    private static String[][] prepareTableData(List<FieldAnalysis> fieldAnalysisList) {
        String[][] tableData = new String[fieldAnalysisList.size() + 1][3];
        tableData[0] = new String[]{"Field Name", "Type", "Size (bytes)"}; // Header row
        for (int i = 0; i < fieldAnalysisList.size(); i++) {
            FieldAnalysis fa = fieldAnalysisList.get(i);
            tableData[i + 1] = new String[]{fa.name, fa.type, String.valueOf(fa.size)};
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
    public static int getFieldSize(String type) {
        return switch (type) {
            case "boolean" -> 1;
            case "byte" -> 1;
            case "char" -> 2;
            case "short" -> 2;
            case "int" -> 4;
            case "float" -> 4;
            case "long" -> 8;
            case "double" -> 8;

            // Boxed types
            case "Boolean" -> 16;
            case "Byte" -> 16;
            case "Character" -> 16;
            case "Short" -> 16;
            case "Integer" -> 16;
            case "Float" -> 16;
            case "Long" -> 24;
            case "Double" -> 24;

            // Other common types
            case "Object" -> 16;
            case "String" -> 40; // Metadata and content size (approximation)

            default -> -1; // Unknown type
        };
    }

    public static String getReportName() {
        return OUTPUT_REPORT;
    }
}
