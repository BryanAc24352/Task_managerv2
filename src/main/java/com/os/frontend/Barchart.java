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

     
        ObservableList<String> categories = FXCollections.observableArrayList();

        
        for (Process process : processList) {
            categories.add("P" + (processList.indexOf(process) + 1));
        }

        
        xAxis.setCategories(categories);

        
        for (Process process : processList) {
            int seriesIndex = processList.indexOf(process);
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("P" + (seriesIndex + 1)); 
            series.getData().add(new XYChart.Data<>(series.getName(), process.getBurstTime()));

            
            setBarColorForSeries (series,  seriesIndex);


            barChart.getData().add(series);
        }
        customizeLegend();
    }


    private void setBarColorForSeries(XYChart.Series<String, Integer> series , int seriesIndex ){


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

        xAxis.setStyle("-fx-padding: 0;"); 
        xAxis.setCenterShape(false); 

    }
}
