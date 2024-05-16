package com.os.frontend.scheduling_window.observers;

import com.os.backend.Process.ProcessAtTime;
import com.os.backend.main.SystemScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class ProcessesTable extends AnchorPane implements Observer, Initializable {


    public TableView<ProcessAtTime> table;
    public TableColumn<ProcessAtTime, Rectangle> stateColumn;
    public TableColumn<ProcessAtTime, String> processColumn;
    public TableColumn<ProcessAtTime, Integer> arrivalColumn;
    public TableColumn<ProcessAtTime, Integer> burstColumn;
    public TableColumn<ProcessAtTime, Integer> remainingColumn;
    public TableColumn<ProcessAtTime, Integer> _TAColumn;
    public TableColumn<ProcessAtTime, Integer> waitingColumn;
    public TableColumn<ProcessAtTime, Integer> priorityColumn;
    public Label avgTurnaroundLabel;
    public Label avgWaitingLabel;


    //private List<>
    private List<ProcessAtTime> processAtTimeList;
    private boolean priorityInit = false;

    @Override
    public void update(SystemScheduler systemScheduler) {

        processAtTimeList = systemScheduler.getCurrentProcessesTable();

        //Init the priority column
        if (!priorityInit) {
            priorityColumnInit();
            priorityInit = true;
        }

        // Set the items of the TableView to the existing list
        ObservableList<ProcessAtTime> observableList = FXCollections.observableArrayList(processAtTimeList);
        table.setItems(observableList);
        table.refresh();

        //update labels
        updateLabels();
    }

    private void updateLabels() {
        DecimalFormat df = new DecimalFormat("#.##"); // Format for two digits after the decimal point

        double turnaroundAverage = processAtTimeList.stream()
                .mapToInt(ProcessAtTime::getTurnaroundTime) // Map ProcessAtTime objects to turnaround times
                .average() // Calculate the average of turnaround times
                .orElse(0); // Default value if the list is empty
        this.avgTurnaroundLabel.setText(df.format(turnaroundAverage));

        double waitingAverage = processAtTimeList.stream()
                .mapToInt(ProcessAtTime::getWaitingTime) // Map ProcessAtTime objects to waiting times
                .average() // Calculate the average of waiting times
                .orElse(0); // Default value if the list is empty
        this.avgWaitingLabel.setText(df.format(waitingAverage));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ///init columns
        processColumn.setCellValueFactory(new PropertyValueFactory<>("processNumber"));
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remainingTime"));
        _TAColumn.setCellValueFactory(new PropertyValueFactory<>("turnaroundTime"));
        waitingColumn.setCellValueFactory(new PropertyValueFactory<>("waitingTime"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        table.getStyleClass().add("table");

    }

    private void priorityColumnInit() {
        //Priority column
        // Check if any priority value is -1
        boolean hasNonPriority = processAtTimeList.stream()
                .map(ProcessAtTime::getPriority)
                .noneMatch(priority -> priority == -1);

        // If any priority value is -1, remove the priorityColumn from the table
        if (hasNonPriority) {
            priorityColumn = new TableColumn<>("Priority");
            priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
            table.getColumns().add(priorityColumn);
        }
    }


    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }


}
