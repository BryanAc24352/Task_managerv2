package com.os.frontend.start_window;

import com.os.backend.Process.PriorityProcess;
import com.os.backend.Process.Process;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessBlockController extends AnchorPane implements Initializable {

    // JavaFX components
    public Label nameLabel;
    public Spinner<Integer> arrivalSpinner;
    public Spinner<Integer> burstSpinner;
    public VBox priorityBlock;
    public Spinner<Integer> prioritySpinner;
    public HBox hBox;

    // Process information
    private static int counter = 1;
    private int number;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private boolean isPriorityActive;

    public ProcessBlockController() {
        number = counter++;
        isPriorityActive = false;
        arrivalTime = 0;
        burstTime = 1;
        priority = 0;
    }

    public void setNumber(int number) {
        this.number = number;
        nameLabel.setText(String.valueOf(number));
    }

    public Process createProcess() {
        Process p = isPriorityActive ? new PriorityProcess(priority) : new Process();
        p.setArrivalTime(arrivalTime);
        p.setBurstTime(burstTime);
        p.setProcessNumber(number);

        return p;
    }

    // Initialization method
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        arrivalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, arrivalTime));
        burstSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, burstTime));
        prioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, priority));
        nameLabel.setText(String.valueOf(number));
    }

    // Event handler method
    public void updateFields(MouseEvent ignoredE) {
        arrivalTime = arrivalSpinner.getValue();
        burstTime = burstSpinner.getValue();
        priority = prioritySpinner.getValue();
    }

    // Helper methods
    public void hidePriority() {
        isPriorityActive = false;
        hBox.getChildren().remove(priorityBlock);
    }

    public void showPriority() {
        isPriorityActive = true;
        if (!hBox.getChildren().contains(priorityBlock)) {
            hBox.getChildren().add(priorityBlock);
        }
    }

    // Static method to remove process block
    public static void removeProcessBlock() {
        counter--;
    }

    public static void removeProcessBlock(int size) {
        counter -= size;
    }

    @Override
    public String toString() {
        return "ProcessBlockController{" +
                "number=" + number +
                ", priority=" + priority +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", isPriorityActive=" + isPriorityActive +
                '}';
    }

    public void reset() {
        this.priority = 0;
        this.burstTime = 1;
        this.arrivalTime = 0;

        this.arrivalSpinner.getValueFactory().setValue(0);
        this.burstSpinner.getValueFactory().setValue(1);
        this.prioritySpinner.getValueFactory().setValue(0);

        // to ensure that min values are assigned correctly to the controller
        this.updateFields(null);
    }

    public void incrementProcessNumber() {
        this.number++;
        this.nameLabel.setText(Integer.toString(this.number));
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        ((SpinnerValueFactory.IntegerSpinnerValueFactory) this.arrivalSpinner.getValueFactory()).setMin(arrivalTime);
    }

    public static void resetProcessCounter(){
        counter = 1;
    }
}
