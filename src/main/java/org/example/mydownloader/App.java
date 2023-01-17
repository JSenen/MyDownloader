package org.example.mydownloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.mydownloader.controller.SplashScreenController;
import org.example.mydownloader.util.R;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        //Cargamos el controlador de la pantalla incial de Splash
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("splashscreen.fxml"));
        loader.setController(new SplashScreenController());
        AnchorPane anchorPane = loader.load();

        //Dibujamos la pantalla
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setTitle("MyDownloader");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
