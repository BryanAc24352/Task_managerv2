package com.os.frontend;

import com.os.backend.main.Backend;
import com.os.frontend.scheduling_window.components.SchedulerWindow;
import com.os.frontend.start_window.StartWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private Backend backend;
    private Stage stage;
    private static String[] args;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StartWindowView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Scheduler");
        this.stage = stage;
        stage.setScene(scene);
        setIcon();
        stage.show();
        ((StartWindowController)    fxmlLoader.getController()).setMain(this);
    }

    private void setIcon() {
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/processor.png")));
        this.stage.getIcons().add(icon);
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public void moveToSchedulerView() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/os/frontend/schedulerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1200,750);

        SchedulerWindow schedulerWindowController = fxmlLoader.getController();
        //set the backend -->  attach observers automatically
        schedulerWindowController.setBackend(backend);
        schedulerWindowController.setMain(this);
        //set the stage to the new scene
        this.stage.setScene(scene);
        //start scheduling
        this.backend.startSchedule();

        /*//----------------------------------------------------------------
        // For Testing
        // TODO: add this code to the scheduler view
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ProcessesTableView.fxml"));
        // set the new scene
        Scene scene = new Scene(fxmlLoader.load());
        // attach and init the controller
        ProcessesTable processesTableController = fxmlLoader.getController();
        this.backend.attach(processesTableController);
        // make the backend notify the observers
        this.backend.startSchedule();

        this.stage.setScene(scene);
        //----------------------------------------------------------------*/

        //TODO: start scheduling
    }
    public static void main(String[] args) {
        Main.args = args;
        launch();
    }

    public Backend backend() {
        return backend;
    }

    public void restart() {
        this.stage.close();
        // Clear any existing data or objects
        this.backend = null;
        this.stage = null;
        System.gc();

        // Create a new instance of Main and start it
        try {
            Main newMain = new Main();
            newMain.start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}