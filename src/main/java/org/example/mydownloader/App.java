package org.example.mydownloader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.example.mydownloader.controller.AppController;
import org.example.mydownloader.util.R;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        //Cargamos el controlador de la pantalla
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("mainscreen.fxml"));
        loader.setController(new AppController());
        ScrollPane scrollPane = loader.load();

        //Dibujamos la pantalla
        Scene scene = new Scene(scrollPane);
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
