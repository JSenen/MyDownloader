package org.example.mydownloader.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.mydownloader.util.R;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SplashScreenController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new SplashScreen().start();
    }
        class SplashScreen extends Thread {
            @Override
            public void run() {
                //Abrimos hilo de 3 segundos para que se vea la Splash Screen
                try {
                    Thread.sleep(3000);

                    //Indicamos que pantalla abrir al acabar el hilo
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            //Cargamos controller de la pantalla principal
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(R.getUI("mainscreen.fxml"));
                            loader.setController(new AppController());
                            ScrollPane scrollPane = null;
                            try {
                                scrollPane = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            Scene scene = new Scene(scrollPane);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("MyDownloader");
                            stage.show();

                            //Ocultamos la pantalla splash
                            anchorPane.getScene().getWindow().hide();

                        }

                    });

                } catch (InterruptedException ex) {
                    Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

