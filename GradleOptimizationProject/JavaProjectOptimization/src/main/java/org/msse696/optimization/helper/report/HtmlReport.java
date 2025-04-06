package org.msse696.optimization.helper.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The HtmlReport class generates a customizable HTML report with
 * actual text and recommended text in tabular format.
 */
public class HtmlReport {

    /**
     * Generates a generic HTML report with actual and recommended content in tables.
     *
     * @param title             The title of the report.
     * @param actualHeader      The header text for the "Actual" table.
     * @param actualData        The data rows for the "Actual" table, where each row is a string array.
     * @param recommendedHeader The header text for the "Recommended" table.
     * @param recommendedData   The data rows for the "Recommended" table, where each row is a string array.
     * @param outputFilePath    The file path where the generated HTML report will be saved.
     */
    public static void generateHtmlReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputFilePath) {
        File outputFile = new File(outputFilePath);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            System.err.println("Failed to create output directory: " + parentDir.getAbsolutePath());
            return;
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang='en'>");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset='UTF-8'>");
        htmlContent.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        htmlContent.append("<title>").append(title).append("</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        htmlContent.append(".container { max-width: 800px; margin: auto; }");
        htmlContent.append("h1 { text-align: center; color: blue; }"); // Title in blue
        htmlContent.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        htmlContent.append("th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }");
        htmlContent.append("th { background-color: #f4f4f4; }");
        htmlContent.append(".actual-header { color: red; }"); // Actual header in red
        htmlContent.append(".recommended-header { color: green; }"); // Recommended header in green
        htmlContent.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<div class='container'>");
        htmlContent.append("<h1>").append(title).append("</h1>");

        // Actual table
        htmlContent.append("<h3 class='actual-header'>").append(actualHeader).append("</h3>");
        htmlContent.append("<table>");
        htmlContent.append("<tr>");
        if (actualData.length > 0) {
            for (String header : actualData[0]) {
                htmlContent.append("<th>").append(header).append("</th>");
            }
        }
        htmlContent.append("</tr>");
        for (int i = 1; i < actualData.length; i++) {
            htmlContent.append("<tr>");
            for (String cell : actualData[i]) {
                htmlContent.append("<td>").append(cell).append("</td>");
            }
            htmlContent.append("</tr>");
        }
        htmlContent.append("</table>");

        // Recommended table
        htmlContent.append("<h3 class='recommended-header'>").append(recommendedHeader).append("</h3>");
        htmlContent.append("<table>");
        htmlContent.append("<tr>");
        if (recommendedData.length > 0) {
            for (String header : recommendedData[0]) {
                htmlContent.append("<th>").append(header).append("</th>");
            }
        }
        htmlContent.append("</tr>");
        for (int i = 1; i < recommendedData.length; i++) {
            htmlContent.append("<tr>");
            for (String cell : recommendedData[i]) {
                htmlContent.append("<td>").append(cell).append("</td>");
            }
            htmlContent.append("</tr>");
        }
        htmlContent.append("</table>");
        htmlContent.append("</div>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(htmlContent.toString());
            System.out.println("HTML report generated successfully: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error while writing the HTML file: " + outputFilePath);
        }
    }
}
