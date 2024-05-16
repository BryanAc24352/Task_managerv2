package com.os.frontend;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.os.backend.Process.Process;
import com.os.frontend.Colors.Colors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Barchart implements Initializable {
    @FXML
    private BarChart<String, Integer> barChart;
    XYChart.Series<String, Integer> processSeries = new XYChart.Series<>();
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    ObservableList<Process> processList  = FXCollections.observableArrayList(
            new Process(0, 5 , 6 ),
            new Process(0, 30 , 28 ),
            new Process(0, 1 , 7 )

    );
    private void initializeChart() {
        xAxis.setLabel("Procesos");
        yAxis.setLabel("Tiempo de Rafaga");

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
            setBarColorForSeries (series,  seriesIndex);


            barChart.getData().add(series);
        }
        customizeLegend();
    }


    private void setBarColorForSeries(XYChart.Series<String, Integer> series , int seriesIndex ){

// Set the fill color of the series based on its index
        String colorStyle = Colors.getColor(seriesIndex);
        series.getData().forEach(data -> {
            data.nodeProperty().addListener((observable, oldValue, newNode) -> {
                if (newNode != null) {
                    System.out.println(colorStyle);
                    newNode.setStyle("-fx-bar-fill: "+colorStyle +";");
                    newNode.setStyle("-fx-legend-side: "+colorStyle +";");

                }
            });
        });
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChart();
        // Load the CSS file
        // Adjust the layout of the category axis to center the bars
        //xAxis.setAnimated(false); // Disable animation to ensure immediate effect
        //xAxis.setAutoRanging(false); // Disable auto-ranging to set fixed categories
        //xAxis.setTickMarkVisible(false); // Hide tick marks to prevent overlap
        //xAxis.setTickLabelGap(0); // Set gap between tick labels to 0
        //xAxis.setTickLabelRotation(0); // Set tick label rotation to 0
        xAxis.setStyle("-fx-padding: 0;"); // Remove any additional padding
        xAxis.setCenterShape(false); // Center the axis labels

    }
}
