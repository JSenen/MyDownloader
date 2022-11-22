package org.example.mydownloader.controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.mydownloader.task.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadController implements Initializable {

    public TextField tfUrl;
    public Label lbStatus;
    public ProgressBar pbProgress;
    private String urlText;
    private DownloadTask downloadTask;

    private static final Logger logger = LogManager.getLogger(DownloadController.class);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public DownloadController(String urlText) {
        logger.info("Descarga dirección " + urlText + " creada");
        this.urlText = urlText;
    }

    public void start(ActionEvent actionEvent) {
        try{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(tfUrl.getScene().getWindow());
            if (file == null)
                return;

            downloadTask = new DownloadTask(urlText, file);

            pbProgress.progressProperty().unbind();
            pbProgress.progressProperty().bind(downloadTask.progressProperty());

            downloadTask.stateProperty().addListener((observableValue, oldState, newState) -> {
                System.out.println(observableValue.toString());
                //Ventana emergente aviso descarga terminada o fallida
                if (newState == Worker.State.SUCCEEDED) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La descarga ha terminado");
                    alert.show();
                } else if (newState == Worker.State.FAILED) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Error en la descarga");
                    alert.show();
                }
            });

            downloadTask.messageProperty()
                    .addListener((observableValue, oldValue, newValue) -> lbStatus.setText(newValue));

            new Thread(downloadTask).start(); //Nuevo hilo de descarga
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            logger.error("URL mal formada", murle.fillInStackTrace());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    @FXML
    public void stop(ActionEvent event) {
        stop();
    }

    public void stop() {
        if (downloadTask != null)
            downloadTask.cancel();
    }
}
