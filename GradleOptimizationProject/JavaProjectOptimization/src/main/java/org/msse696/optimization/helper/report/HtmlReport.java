package org.msse696.optimization.helper.report;

import org.msse696.optimization.helper.debug.Debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The HtmlReport class generates customizable HTML reports.
 */
public class HtmlReport {

    /**
     * Generates a generic HTML report with actual and recommended content in tables.
     * This method is preserved for backward compatibility.
     *
     * @param title             The title of the report.
     * @param actualHeader      The header text for the "Actual" table.
     * @param actualData        The data rows for the "Actual" table, where each row is a string array.
     * @param recommendedHeader The header text for the "Recommended" table.
     * @param recommendedData   The data rows for the "Recommended" table, where each row is a string array.
     * @param outputFilePath    The file path where the generated HTML report will be saved.
     */
    public static void generateHtmlReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputFilePath) {
        generateHtmlTableReport(title, actualHeader, actualData, recommendedHeader, recommendedData, outputFilePath);
    }

    /**
     * Generates a generic HTML report with content in tables.
     *
     * @param title          The title of the report.
     * @param mainContent    The main content of the report as a String.
     * @param outputFilePath The file path where the generated HTML report will be saved.
     */
    public static void generateHtmlReport(String title, String mainContent, String outputFilePath) {
        writeToFile(constructHtmlDocument(title, mainContent), outputFilePath);
        Debug.info("HTML report generated successfully: " + outputFilePath);
    }

    /**
     * Generates an HTML report with two tables: "Actual" and "Recommended".
     *
     * @param title             The title of the report.
     * @param actualHeader      The header text for the "Actual" table.
     * @param actualData        The data rows for the "Actual" table.
     * @param recommendedHeader The header text for the "Recommended" table.
     * @param recommendedData   The data rows for the "Recommended" table.
     * @param outputFilePath    The file path where the generated HTML report will be saved.
     */
    public static void generateHtmlTableReport(String title, String actualHeader, String[][] actualData, String recommendedHeader, String[][] recommendedData, String outputFilePath) {
        File outputFile = new File(outputFilePath);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            Debug.error("Failed to create output directory: " + parentDir.getAbsolutePath());
            return;
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append(constructHtmlDocumentStart(title));
        htmlContent.append("<div class='container'>\n");
        htmlContent.append("<h1>").append(title).append("</h1>\n");

        // Actual table
        htmlContent.append("<h3 class='actual-header'>").append(actualHeader).append("</h3>\n");
        htmlContent.append("<table style='width:100%; border-collapse: collapse;'>\n");
        if (actualData.length > 0) {
            htmlContent.append("<tr>");
            for (String header : actualData[0]) {
                htmlContent.append("<th>").append(header).append("</th>");
            }
            htmlContent.append("</tr>\n");
            for (int i = 1; i < actualData.length; i++) {
                htmlContent.append("<tr>");
                for (String cell : actualData[i]) {
                    htmlContent.append("<td>").append(cell).append("</td>");
                }
                htmlContent.append("</tr>\n");
            }
        }
        htmlContent.append("</table>\n");

        // Recommended table
        htmlContent.append("<h3 class='recommended-header'>").append(recommendedHeader).append("</h3>\n");
        htmlContent.append("<table style='width:100%; border-collapse: collapse;'>\n");
        if (recommendedData.length > 0) {
            htmlContent.append("<tr>");
            for (String header : recommendedData[0]) {
                htmlContent.append("<th>").append(header).append("</th>");
            }
            htmlContent.append("</tr>\n");
            for (int i = 1; i < recommendedData.length; i++) {
                htmlContent.append("<tr>");
                for (String cell : recommendedData[i]) {
                    htmlContent.append(cell);
                }
                htmlContent.append("</tr>\n");
            }
        }
        htmlContent.append("</table>\n");
        htmlContent.append("</div>\n");
        htmlContent.append(constructHtmlDocumentEnd());

        writeToFile(htmlContent.toString(), outputFilePath);
        Debug.info("HTML table report generated successfully: " + outputFilePath);
    }

    /**
     * Constructs the start of the HTML document.
     *
     * @param title The title of the HTML document.
     * @return The HTML start structure as a String.
     */
    private static String constructHtmlDocumentStart(String title) {
        return "<!DOCTYPE html>\n" +
                "<html lang='en'>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
                "<title>" + title + "</title>\n" +
                "<style>\n" +
                "body { font-family: Arial, sans-serif; margin: 20px; }\n" +
                ".container { margin: auto; }\n" +
                "h1 { text-align: center; color: blue; }\n" +
                "table { width: 80%; border-collapse: collapse; margin-top: 20px; }\n" +
                "th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }\n" +
                "th { background-color: #f4f4f4; }\n" +
                ".actual-header { color: red; }\n" +
                ".recommended-header { color: green; }\n" +
                "tr:nth-child(even) { background-color: #f9f9f9; }\n" +
                "h2 { color: navy; margin-top: 30px; }\n" +
                "h3 { color: darkgreen; margin-top: 20px; }\n" +
                "p { font-weight: bold; margin-top: 15px; }\n" +
                "pre { background-color: #f4f4f4; padding: 10px; border: 1px solid #ccc; overflow-x: auto; white-space: pre-wrap; word-wrap: break-word; }\n" +
                "code { font-family: monospace; }\n" +
                "ul { list-style-type: none; padding: 0; }\n" +
                "li a { color: blue; text-decoration: none; }\n" +
                "li a:hover { text-decoration: underline; }\n" +
                "hr { border: 1px solid #ccc; margin: 20px 0; }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n";
    }

    /**
     * Constructs the end of the HTML document.
     *
     * @return The HTML end structure as a String.
     */
    private static String constructHtmlDocumentEnd() {
        return "</body>\n" +
                "</html>\n";
    }

    /**
     * Constructs a complete HTML document with the given title and main content.
     *
     * @param title     The title of the HTML document.
     * @param content The main content of the HTML document.
     * @return The complete HTML document as a String.
     */
    private static String constructHtmlDocument(String title, String content) {
        return constructHtmlDocumentStart(title) +
                "<div class='container'>\n" +
                "<h1>" + title + "</h1>\n" +
                content +
                "</div>\n" +
                constructHtmlDocumentEnd();
    }

    /**
     * Writes the HTML content to the specified file.
     *
     * @param htmlContent    The HTML content to write.
     * @param outputFilePath The path to the output HTML file.
     */
    public static void writeToFile(String htmlContent, String outputFilePath) {
        File outputFile = new File(outputFilePath);
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            Debug.error("Failed to create output directory: " + parentDir.getAbsolutePath());
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(htmlContent);
        } catch (IOException e) {
            Debug.error("Error writing to HTML file: " + outputFilePath);
        }
    }
}