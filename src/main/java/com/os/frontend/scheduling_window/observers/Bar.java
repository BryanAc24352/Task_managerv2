package com.os.frontend.scheduling_window.observers;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessAtTime;
import com.os.backend.main.SystemScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.chart.BarChart;

public class Bar extends AnchorPane implements Observer, Initializable {

    public BarChart<String, Integer> barChart;
    private List<ProcessAtTime> processAtTimeList;

    @FXML
    public BarChart<String, Integer> bar;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private List<Process> processList;

    private ObservableList<String> categories;

    private int previousProcessListSize;

    @Override
    public void update(SystemScheduler systemScheduler) {
        //bar.getData().clear();

        if (previousProcessListSize < processList.size()) {
            if (previousProcessListSize == 0) {

           /* System.out.println(barChart.getData().size());
            Process process = processList.get(barChart.getData().size());
            var series = new XYChart.Series();
            series.setName(String.valueOf(process.getProcessNumber()));
            series.getData().add(new XYChart.Data("", process.getRemainingTime()));
            barChart.getData().add(series);*/

                initializeChart();


                //previousProcessListSize = processList.size();
            } else {
                while (previousProcessListSize < processList.size()) {
                    updateChartWithNewProcess(processList.get(previousProcessListSize));
                    previousProcessListSize++;
                }
            }
            previousProcessListSize = processList.size();

        }


        // the next code section should update the remaining time of the current process

        Process currentRunningProcess = systemScheduler.getCurrentRunningProcess();
        //updateChartWithNewProcess(currentRunningProcess);
        int index = processList.indexOf(currentRunningProcess);

        if (index != -1) {
            XYChart.Series<String, Integer> seriesToUpdate = barChart.getData().get(index);
            XYChart.Data<String, Integer> dataToUpdate = seriesToUpdate.getData().get(seriesToUpdate.getData().size() - 1);
            // Update the y-value of the specific data point
            System.out.println(dataToUpdate.getYValue());

            //if((dataToUpdate.getYValue() >  0)) {
            if (currentRunningProcess.getRemainingTime() >= 0) {
                dataToUpdate.setYValue(currentRunningProcess.getRemainingTime());
            }

            System.out.println("Fabri");
        }
        else System.out.println("Brayan");
    }

    private void updateChartWithNewProcess(Process process) {
        // Add the category for the new process
        xAxis.getCategories().add("P" + (previousProcessListSize + 1));

        // Create a new series for the new process
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("P" + (previousProcessListSize + 1)); // Set a unique name for each series
        series.getData().add(new XYChart.Data<>(series.getName(), process.getRemainingTime()));
        //setBarColorForSeries(series, processList.indexOf(process));


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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initializeChart();
    }

    private void initializeChart() {
        xAxis.setLabel("Processes");
        yAxis.setLabel("Remaining Time");

        // Create a list to store the categories
        categories = FXCollections.observableArrayList();

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
            series.getData().add(new XYChart.Data<>(series.getName(), process.getRemainingTime()));



            barChart.getData().add(series);
        }
    }



    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }
}