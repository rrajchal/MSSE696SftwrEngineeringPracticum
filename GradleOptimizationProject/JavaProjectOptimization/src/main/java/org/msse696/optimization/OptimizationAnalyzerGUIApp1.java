package org.msse696.optimization;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import org.msse696.automation.InefficiencyAnalyzerRunner;
import org.msse696.optimization.helper.JavaFileScanner;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.CombinedAnalysisReportGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GUI application for analyzing Java code for optimization inefficiencies.
 */
public class OptimizationAnalyzerGUIApp1 extends JFrame {

    private JTextField directoryPathField;
    private JCheckBox createReportCheckBox;
    private JTextField reportFileNameField;
    private JButton startAnalysisButton;
    private JTextArea outputTextArea;
    private JFileChooser directoryChooser;
    private final Font biggerFont = new Font("Arial", Font.PLAIN, 14);
    int totalNumberOfLines;
    private static final String REPORTS_DIRECTORY = "E:\\\\1RegisUniversity\\\\MSSE696_X70_SftwrEngineeringPracticumII\\\\reports";

    /**
     * Constructs the main application window.
     */
    public OptimizationAnalyzerGUIApp1() {
        setTitle("Optimization Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
    }

    /**
     * Initializes the GUI components.
     */
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel directoryLabel = new JLabel("Directory to Analyze:");
        directoryLabel.setFont(biggerFont);
        inputPanel.add(directoryLabel, gbc);

        directoryPathField = new JTextField(40);
        directoryPathField.setFont(biggerFont);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(directoryPathField, gbc);

        JButton browseButton = new JButton("Browse");
        browseButton.setFont(biggerFont);
        browseButton.addActionListener(e -> chooseDirectory());
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel reportOptionsLabel = new JLabel("Report Options:");
        reportOptionsLabel.setFont(biggerFont);
        inputPanel.add(reportOptionsLabel, gbc);

        createReportCheckBox = new JCheckBox("Create Report");
        createReportCheckBox.setFont(biggerFont);
        createReportCheckBox.setSelected(true);
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(createReportCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel fileNameLabel = new JLabel("Report File Name (.html):");
        fileNameLabel.setFont(biggerFont);
        inputPanel.add(fileNameLabel, gbc);

        reportFileNameField = new JTextField("analysis_report.html", 25);
        reportFileNameField.setFont(biggerFont);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(reportFileNameField, gbc);

        startAnalysisButton = new JButton("Start Analysis");
        startAnalysisButton.setFont(new Font("Arial", Font.BOLD, 16));
        startAnalysisButton.addActionListener(this::startAnalysis);
        startAnalysisButton.setBackground(new Color(0, 128, 0));
        startAnalysisButton.setForeground(Color.WHITE);
        startAnalysisButton.setOpaque(true);
        startAnalysisButton.setBorderPainted(false);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(startAnalysisButton, gbc);

        outputTextArea = new JTextArea(15, 60);
        outputTextArea.setFont(biggerFont);
        outputTextArea.setEditable(false);
        outputTextArea.setText("Ready to start analysis. Select a directory and click 'Start Analysis'.");
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(outputScrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        directoryChooser = new JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    /**
     * Opens a directory chooser dialog.
     */
    private void chooseDirectory() {
        int result = directoryChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = directoryChooser.getSelectedFile();
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Generates a unique report file name by appending a number if the file already exists
     * within the specified reports directory.
     *
     * @param baseFileName The base file name (e.g., "analysis_report.html").
     * @return A unique file name with the full path in the reports directory.
     */
    private String generateUniqueReportFileName(String baseFileName) {
        Path reportsPath = Paths.get(REPORTS_DIRECTORY);
        String nameWithoutExtension = baseFileName.substring(0, baseFileName.lastIndexOf('.'));
        String extension = baseFileName.substring(baseFileName.lastIndexOf('.'));
        int counter = 0;
        Path filePath;

        do {
            String currentFileName = counter == 0 ? baseFileName : nameWithoutExtension + counter + extension;
            filePath = reportsPath.resolve(currentFileName);
            counter++;
        } while (Files.exists(filePath));

        return filePath.toString();
    }

    /**
     * Starts the analysis process.
     *
     * @param e The ActionEvent triggered by the start button.
     */
    private void startAnalysis(ActionEvent e) {
        String directoryPath = directoryPathField.getText();
        boolean createReport = createReportCheckBox.isSelected();
        String reportFileName = reportFileNameField.getText();

        if (directoryPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a directory to analyze.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        startAnalysisButton.setEnabled(false);
        directoryPathField.setEnabled(false);
        createReportCheckBox.setEnabled(false);
        reportFileNameField.setEnabled(false);
        outputTextArea.setText("Analyzing...\n");

        SwingWorker<AnalysisResult, String> worker = new SwingWorker<>() {
            @Override
            protected AnalysisResult doInBackground() throws Exception {
                preSetup();

                JavaFileScanner javaFileScanner = new JavaFileScanner(directoryPath);
                totalNumberOfLines = javaFileScanner.getTotalNumberOfLines();
                List<File> javaFiles = javaFileScanner.getJavaFiles();
                List<InefficiencyAnalyzerRunner> runners = new ArrayList<>();
                int grandTotalInefficiencies = 0;
                String outputReportPath = null;

                if (!javaFiles.isEmpty()) {
                    publish("Analyzing the following Java files:\n");
                    for (File javaFile : javaFiles) {
                        publish("- " + javaFile.getName() + "\n");
                        InefficiencyAnalyzerRunner runner = new InefficiencyAnalyzerRunner();
                        runner.analyze(javaFile, false);
                        runners.add(runner);
                        String[][] recommendations = runner.getRecommendedData(); // Get recommendations

                        // Increment GUI count only if there are actual recommendations
                        if (recommendations != null && recommendations.length > 0) {
                            grandTotalInefficiencies += runner.getTotalInefficiencyCount();
                        }
                    }

                    if (createReport) {
                        Path reportsDir = Paths.get(REPORTS_DIRECTORY);
                        if (!Files.exists(reportsDir)) {
                            try {
                                Files.createDirectories(reportsDir);
                            } catch (IOException ex) {
                                return new AnalysisResult(javaFiles.size(), grandTotalInefficiencies, null, "Error creating reports directory: " + ex.getMessage());
                            }
                        }
                        String uniqueReportFileName = generateUniqueReportFileName(reportFileName);
                        outputReportPath = uniqueReportFileName;
                        CombinedAnalysisReportGenerator.generateReport(runners, outputReportPath, true);
                    } else {
                        publish("Analysis complete. Report generation skipped.\n");
                    }
                } else {
                    publish("No Java files found in the directory and its subdirectories.\n");
                    CombinedAnalysisReportGenerator.generateReport(runners, null, false);
                }

                return new AnalysisResult(javaFiles.size(), grandTotalInefficiencies, outputReportPath, null);
            }

            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    outputTextArea.append(chunk);
                }
            }

            @Override
            protected void done() {
                startAnalysisButton.setEnabled(true);
                directoryPathField.setEnabled(true);
                createReportCheckBox.setEnabled(true);
                reportFileNameField.setEnabled(true);

                try {
                    AnalysisResult result = get();
                    outputTextArea.append("Total Java Files Analyzed: " + result.totalFilesAnalyzed + "\n");
                    outputTextArea.append("Total lines of Java code:  " + totalNumberOfLines + "\n");
                    outputTextArea.append("Total # of inefficiencies: " + result.totalInefficiencies + "\n");

                    if (result.reportPath != null && createReport) {
                        outputTextArea.append("Report Link: " + result.reportPath + "\n");
                    }
                    if (result.errorMessage != null) {
                        JOptionPane.showMessageDialog(OptimizationAnalyzerGUIApp1.this, result.errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    outputTextArea.append("Error during analysis: " + e.getMessage() + "\n");
                }
            }
        };

        worker.execute();
    }

    /**
     * Configures the JavaParser.
     */
    private static void preSetup() {
        ParserConfiguration config = new ParserConfiguration();
        config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        StaticJavaParser.setConfiguration(config);
    }

    /**
     * Simple data class to hold the analysis results.
     */
    private static class AnalysisResult {
        int totalFilesAnalyzed;
        int totalInefficiencies;
        String reportPath;
        String errorMessage;

        public AnalysisResult(int totalFilesAnalyzed, int totalInefficiencies, String reportPath, String errorMessage) {
            this.totalFilesAnalyzed = totalFilesAnalyzed;
            this.totalInefficiencies = totalInefficiencies;
            this.reportPath = reportPath;
            this.errorMessage = errorMessage;
        }
    }

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            Debug.error(e.getMessage());
        }
        SwingUtilities.invokeLater(OptimizationAnalyzerGUIApp::new);
    }
}