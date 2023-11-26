package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader( App.class.getResource( "haircut.fxml" ) );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle( "Барбер (づ ◕‿◕ )づ" );
        stage.setMaximized( true );
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}