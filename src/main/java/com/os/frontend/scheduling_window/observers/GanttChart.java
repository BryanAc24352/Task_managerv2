package com.os.frontend.scheduling_window.observers;

import com.os.backend.Process.Process;
import com.os.backend.main.SystemScheduler;
import com.os.frontend.Colors.Colors;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class GanttChart extends ScrollPane implements Observer, Initializable {

    public HBox ganttBox;
    public ScrollPane scrollPane;
    private int time = 0;

    @Override
    public void update(SystemScheduler system) {
        Process currentProcess = system.getCurrentRunningProcess();

        VBox vbox;

        if (currentProcess == null) {
            vbox = addIdlebox();
        } else {
            int index = currentProcess.getProcessNumber() - 1;
            vbox = addProcessBox(index);
        }

        ganttBox.getChildren().add(vbox);


    }

    private void enlargeGanntBox() {
        scrolViewChange();


    }


    private void scrolViewChange() {
        if (time > 19) {
            //ScrollPane sp = this;
            this.scrollPane.setHvalue(1);

            ganttBox.setPrefWidth(ganttBox.getPrefWidth() + 10);
        }else{
            this.scrollPane.setHvalue(0);
        }
        this.scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);

    }

    public VBox addProcessBox(int index) {
        VBox vbox = new VBox();
        vbox.setPrefHeight(75);
        vbox.setPrefWidth(50);

        StackPane box = new StackPane();
        box.setPrefHeight(55);
        box.setPrefWidth(50);
        String colorStyle = Colors.getColor(index);
        box.setStyle("-fx-background-color:" + colorStyle);

        Label label = new Label("P" + (index + 1));               // index +1 عشان هو هنا P1
        label.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        box.getChildren().add(label);


        Label timestamp = new Label("" + this.time++);
        timestamp.setStyle("-fx-font-size: 10; ");
        timestamp.setStyle("-fx-background-color:" + colorStyle);
        timestamp.setPadding(new Insets(0, 2, 0, 2));
        vbox.getChildren().addAll(box, timestamp);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setStyle("-fx-padding: 0 2 0 2 ; -fx-background-color:" + colorStyle);


        enlargeGanntBox();


        return vbox;

    }

    public VBox addIdlebox() {
        VBox vbox = new VBox();
        vbox.setPrefHeight(75);
        vbox.setPrefWidth(50);

        StackPane box = new StackPane();
        box.setPrefHeight(55);
        box.setPrefWidth(50);
        box.setStyle("-fx-background-color:  #ccefbf");
        Label label = new Label("Idle");
        label.setStyle("-fx-font-size: 12; -fx-font-weight: bold");
        box.getChildren().add(label);


        Label timestamp = new Label("" + this.time++);
        timestamp.setStyle("-fx-font-size: 10; ");
        timestamp.setPadding(new Insets(0, 2, 0, 2));

        vbox.getChildren().addAll(box, timestamp);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setStyle("-fx-padding: 0 2 0 2;-fx-background-color:  #ccefbf");


        enlargeGanntBox();


        return vbox;

    }

    public HBox initializeGanttBox() {
        ganttBox = new HBox();
        ganttBox.setAlignment(Pos.CENTER_LEFT);
        ganttBox.setMaxWidth(Double.MAX_VALUE);
        ganttBox.setPrefHeight(75.0);
        ganttBox.setPrefWidth(1800.0);
        ganttBox.setStyle("-fx-background-color: #30305f; -fx-border-color: #30305f;");
        return ganttBox;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*ganttBox = initializeGanttBox();*/
        this.setStyle("-fx-background-color: #561154");
/*
        VBox sp1 = addProcessBox(1);
        VBox sp2 = addProcessBox(2);
        VBox sp3 = addProcessBox(3);
        VBox sp4 = addIdlebox();
        VBox sp5 = addIdlebox();
        VBox sp6 = addIdlebox();

        ganttBox.getChildren().addAll(sp1, sp2,sp3 ,sp4);
*/
        // Start a separate thread to continuously add VBoxes
        /*Thread addVBoxesThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(200); // Sleep for 200 milliseconds
                    Platform.runLater(() -> ganttBox.getChildren().add(addProcessBox(time + 1))); // Add a new process box
                    System.out.print(time+ "  ");
                    System.out.println(this.getHvalue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        /*addVBoxesThread.setDaemon(true);
        addVBoxesThread.start();*/


    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
