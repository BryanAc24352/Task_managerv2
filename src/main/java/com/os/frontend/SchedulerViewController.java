package com.os.frontend;

import com.os.backend.Process.PriorityProcess;
import com.os.backend.Process.Process;
import com.os.frontend.Colors.Colors;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class SchedulerViewController implements Initializable {


    public ScrollPane scrollPane;
    @FXML
    private TableView<Process> processTable;
    @FXML
    private TableColumn<Process, Integer> pidColumn;
    @FXML
    private TableColumn<Process, Integer> arrivalColumn;
    @FXML
    private TableColumn<Process, Integer> burstColumn;
    @FXML
    private TableColumn<Process, Integer> priorityColumn;
    @FXML
    private TableColumn<Process, Integer> remainingColumn;

    @FXML
    private Label CPUScheduler;
    @FXML
    private BarChart<String, Integer> barChart;
    XYChart.Series<String, Integer> processSeries = new XYChart.Series<>();
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private int scrolViewChange;


    public HBox ganttBox;
    public Button addButton;
    public TextField arrivalField;
    public TextField priorityField;
    public TextField burstField;
    public Button stopButton;
    public Button startButton;


    ObservableList<Process> processList = FXCollections.observableArrayList(
            new Process(0, 5, 6),
            new Process(0, 30, 28),
            new Process(0, 1, 7)

    );


    private Map<Integer, String> seriesColors = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTable();
        initializeChart();
        scrollPane.setHvalue(0);


    }
   /* private void updateChart(Process process){
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Process " + processList.indexOf(process)); // Set a unique name for each series

        series.getData().add(new XYChart.Data<>(String.valueOf(process.getArrivalTime()), process.getBurstTime()));
        barChart.getData().add(series);
    }*/

   /* private void initializeChart() {
        xAxis.setLabel("Arrival Time");
        yAxis.setLabel("Process");

        for (Process process : processList) {
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Process " + processList.indexOf(process)); // Set a unique name for each series

            series.getData().add(new XYChart.Data<>(String.valueOf(process.getArrivalTime()), process.getBurstTime()));
            barChart.getData().add(series);
        }
    }*/


    public void initializeTable() {
        schedularViewAnimation();
        //TODO: pidColumn.setCellFactory(new PropertyValueFactory<Process,Integer>()); Handle pid
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("arrivalTime"));
        burstColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("burstTime"));
        //TODO:remainingColumn.setCellValueFactory(new PropertyValueFactory<Process,Integer>("arrivalTime"));

        processTable.setItems(processList);
    }

    ///////////////////////////////////////Adding a category instead of a series
    private void initializeChart() {
        xAxis.setLabel("Procesos");
        yAxis.setLabel("Tiempo de rafaga");
        String labelStyle = "-fx-text-fill: #76ABAE;";
        xAxis.setStyle(labelStyle);
        yAxis.setStyle(labelStyle);
        // Create a list to store the categories
        ObservableList<String> categories = FXCollections.observableArrayList();

        // Add category names for each process in the list
        for (Process process : processList) {
            categories.add("P" + (processList.indexOf(process) + 1));
        }

        // Set the categories on the X-axis
        xAxis.setCategories(categories);

        // Create a series for each process and add it to the chart
        for (Process process : processList) {
            int seriesIndex = processList.indexOf(process);
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("P" + (seriesIndex + 1)); // Set a unique name for each series
            series.getData().add(new XYChart.Data<>(series.getName(), process.getBurstTime()));

            //set Fill Color for series
            setBarColorForSeries(series, seriesIndex);


            barChart.getData().add(series);
        }
    }


    private void setBarColorForSeries(XYChart.Series<String, Integer> series, int seriesIndex) {
/*
        // Set the fill color of the series based on its index
        String colorStyle = Colors.getColor(seriesIndex);
        series.getData().forEach(data -> {
            data.nodeProperty().addListener((observable, oldValue, newNode) -> {
                if (newNode != null) {
                    SystemScheduler.out.println(colorStyle);
                    newNode.setStyle("-fx-bar-fill: "+colorStyle +";");

                }
            });
        });*/
    }


    private void customizeLegend() {
        AnchorPane legend = (AnchorPane) barChart.lookup(".chart-legend");
        ObservableList<javafx.scene.Node> legendItems = legend.getChildren();

        for (javafx.scene.Node legendItem : legendItems) {
            if (legendItem instanceof Label) {
                Label label = (Label) legendItem;
                int seriesIndex = legendItems.indexOf(legendItem);
                System.out.println(Colors.getColor(seriesIndex));
                Rectangle rectangle = new Rectangle(10, 10, Color.web(Colors.getColor(seriesIndex)));
                label.setGraphic(rectangle);
            }
        }
    }


    public void addNewProcess(ActionEvent actionEvent) {
        PriorityProcess process = new PriorityProcess(0, parseInt(arrivalField.getText()),
                parseInt(burstField.getText()),
                priorityField.getText() == null ? parseInt(priorityField.getText()) : 0);

        processList.add(process);
        arrivalField.setText("");
        burstField.setText("");
        priorityField.setText("");

        // Update the chart with the new process
        updateChartWithNewProcess(process);
    }

    private void updateChartWithNewProcess(Process process) {
        // Add the category for the new process
        xAxis.getCategories().add("P" + (processList.size()));

        // Create a new series for the new process
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("P" + (processList.size())); // Set a unique name for each series
        series.getData().add(new XYChart.Data<>(series.getName(), process.getBurstTime()));
        setBarColorForSeries(series, processList.indexOf(process));


        // Add the series to the chart
        barChart.getData().add(series);

        //TODO: testing of setting a specific color to the new process in chart
        // Set the color of the series after the node is created
        /*series.nodeProperty().addListener((observable, oldNode, newNode) -> {
            if (newNode != null) {
                newNode.setStyle("-fx-background-color: RED");
            }
        });*/
    }


    public void executeTest(ActionEvent actionEvent) {
        // Get the index of the selected process in the table
        int selectedIndex = processTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex != -1) { // Check if a row is selected
            // Execute the process at the selected index
            executeProcess(selectedIndex);
        }
    }

    private void executeProcess(int index) {
        Process process = processList.get(index);
        if (process.getBurstTime() > 0) {
            process.setBurstTime(process.getBurstTime() - 1);
            System.out.println(process.getBurstTime());
            updateChart(index, process);
            updateTable(index, process);
            ganttChartUpdate(process);

        }
    }

    private void updateChart(int index, Process process) {
        XYChart.Series<String, Integer> series = barChart.getData().get(index);

        // Get the last data point and update its value
        XYChart.Data<String, Integer> lastDataPoint = series.getData().get(series.getData().size() - 1);
        lastDataPoint.setYValue(process.getBurstTime());

        // Set the fill color for the series
        setBarColorForSeries(series, processList.indexOf(process));


        //     barChart.getData().add(series);
    }

    private void updateTable(int index, Process process) {
        // Get the process at the specified index in the table
        Process updatedProcess = processTable.getItems().get(index);

        // Update the burst time of the process
        updatedProcess.setBurstTime(process.getBurstTime());

        // Refresh the table view to reflect the changes
        processTable.refresh();

        // Select the row corresponding to the updated process
        processTable.getSelectionModel().select(updatedProcess);

    }

    private void ganttChartUpdate(Process process) {
        //TODO:Get the color or the bar

        // Get the default color of the series
        //   String seriesColor = getDefaultSeriesColor(process);


        // Create and style the StackPane with the same color as the series
        StackPane box = new StackPane();
        box.setPrefHeight(75);
        box.setPrefWidth(50);
        String colorStyle = Colors.getColor(processList.indexOf(process));
        box.setStyle("-fx-background-color:" + colorStyle); // Set the background color
        //SystemScheduler.out.println("Default color for process " + (processList.indexOf(process) + 1) + ": " + seriesColor);
        Label label = new Label("P" + (processList.indexOf(process) + 1));
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold");
        box.getChildren().add(label);

        // Add the StackPane to the ganttBox
        ganttBox.getChildren().add(box);

        
        ganttBox.setPrefWidth(ganttBox.getPrefWidth() + 9);
        double viewValue = (double) (ganttBox.getChildren().size() * 75 + 250) / 1060;


        
        scrolViewChange();

    }

    private void scrolViewChange() {
        scrolViewChange++;
        if (scrolViewChange > 19) {
            System.out.println(scrollPane.getHvalue());
            scrollPane.setHvalue(1);
        }

    }

    private String getDefaultSeriesColor(Process process) {
        // Get the index of the series associated with the process
        int seriesIndex = processList.indexOf(process);

        // Check if the series index is valid
        if (seriesIndex >= 0 && seriesIndex < barChart.getData().size()) {
            // Construct the style class for the series
            String seriesStyleClass = "default-color" + seriesIndex;

            // Loop through the style classes of the bar chart
            for (String styleClass : barChart.getStyleClass()) {
                // Check if the style class matches the series style class
                if (styleClass.contains(seriesStyleClass)) {
                    // Extract the color from the style class
                    int colorIndex = styleClass.lastIndexOf("-");
                    return styleClass.substring(colorIndex + 1);
                }
            }
        }

        return null; // Default color not found
    }

    private void schedularViewAnimation() {
        TranslateTransition moveInTransition = new TranslateTransition(Duration.seconds(1), CPUScheduler);
        moveInTransition.setFromY(0); // Starting position
        moveInTransition.setToY(-20); // Move in by 20 pixels

        // Create translate transition for moving out
        TranslateTransition moveOutTransition = new TranslateTransition(Duration.seconds(1), CPUScheduler);
        moveOutTransition.setFromY(-20); // Starting position
        moveOutTransition.setToY(0); // Move out to original position
/*
        // Chain the two transitions
        moveInTransition.setOnFinished(event -> moveOutTransition.play());
        moveOutTransition.setOnFinished(event -> moveInTransition.play());
*/
        // Start the initial animation
        moveInTransition.play();
    }

}
