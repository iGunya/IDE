package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.di.Containers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * JavaFX App
 */
public class App extends Application {

    private static final String LOGGER_PROPERTIES =
            "handlers=java.util.logging.FileHandler\n" +
            "java.util.logging.FileHandler.pattern=D:/javaProject/haircut2/src/main/resources/logs/haircut.log\n" +
            "java.util.logging.FileHandler.level=ALL\n" +
            "java.util.logging.FileHandler.formatter=java.util.logging.SimpleFormatter\n" +
            "java.util.logging.SimpleFormatter.format=%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS,%1$tL %2$s - %5$s%6$s%n\n" +
             "java.util.logging.FileHandler.encoding=UTF-8\n" +
            "java.util.logging.FileHandler.limit=500000\n" +
            "java.util.logging.FileHandler.append=false\n";


    @Override
    public void start(Stage stage) throws IOException {
        LogManager.getLogManager().readConfiguration( new ByteArrayInputStream( LOGGER_PROPERTIES.getBytes() ) );

        Thread.setDefaultUncaughtExceptionHandler(App::showError);
        Containers.setStage( stage );
        FXMLLoader fxmlLoader = new FXMLLoader( App.class.getResource( "haircut.fxml" ) );
        Scene scene = new Scene(fxmlLoader.load(), 1150, 900);
        stage.setScene(scene);
        stage.setTitle( "Барбер (づ ◕‿◕ )づ" );
        stage.show();
    }

    private static void showError(Thread t, Throwable e) {
        Logger LOGGER = Logger.getGlobal();
        LOGGER.log( Level.SEVERE, "***Default exception handler***");
        if ( Platform.isFxApplicationThread()) {
            LOGGER.log( Level.SEVERE, "В приложении произошла ошибка: ", e );
            showErrorDialog(e);
        } else {
            LOGGER.log( Level.SEVERE, "An unexpected error occurred in " + t );
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