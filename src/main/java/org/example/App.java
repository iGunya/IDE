package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.di.Containers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(App::showError);
        Containers.setStage( stage );
        FXMLLoader fxmlLoader = new FXMLLoader( App.class.getResource( "haircut.fxml" ) );
        Scene scene = new Scene(fxmlLoader.load(), 1150, 900);
        stage.setScene(scene);
        stage.setTitle( "Барбер (づ ◕‿◕ )づ" );
        stage.show();
    }

    private static void showError(Thread t, Throwable e) {
        System.err.println("***Default exception handler***");
        if ( Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            System.err.println("An unexpected error occurred in "+t);
        }
    }

    private static void showErrorDialog(Throwable e) {
        e.printStackTrace();
        StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));
        Alert alert = new Alert( Alert.AlertType.ERROR);
        alert.setContentText( "ERROR" );
        alert.setContentText( e.getMessage() );
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

}