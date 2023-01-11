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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.out;

public class DownloadController implements Initializable {

    public TextField tfUrl;
    public Label lbStatus;
    public ProgressBar pbProgress;
    private String urlText;
    private String path; // Ruta al directorio seleccionado
    private DownloadTask downloadTask;
    private String fileName;
    private int timeOut; // Tiempo de espera para descarga

    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadController(String urlText, String path, int timeOut) {
        logger.info("Descarga dirección " + urlText + " creada en " + path);
        this.urlText = urlText;
        this.path = path;
        this.timeOut = timeOut;
    }

    //Se ejecuta cada ve que instanciamos el controlador
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfUrl.setText(urlText);
        try {
            //Creamos directamente el fichero con el nombre del link
            fileName = this.urlText.substring(this.urlText.lastIndexOf("/") + 1);
            File file = new File(fileName);
            file.createNewFile();
            downloadTask = new DownloadTask(urlText, file, path);

            pbProgress.progressProperty().unbind();
            pbProgress.progressProperty().bind(downloadTask.progressProperty());

            downloadTask.stateProperty().addListener((observableValue, oldState,
                    newState) -> {
                out.println(observableValue.toString());
                if (newState == Worker.State.SUCCEEDED) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La descarga ha terminado");
                    alert.show();

                    //Grabar en un fichero de texto la descarga realizada para poder mostrar al usuario las que lleva realizadas
                    try(FileWriter fw = new FileWriter("DescargasRealizadas.txt", true); //Creamos fichero txt
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(bw))
                    {
                        out.println("Descarga "+fileName+" finalizada"); //Añadimos texto con el nombre del fichero

                    } catch (IOException e) {
                    }
                }
            });

            downloadTask.messageProperty()
                    .addListener((observableValue, oldValue, newValue) -> lbStatus.setText(newValue));
            //Usamos libreria java timer schedule por si se ha establecido tiempo para iniciar la descarga (timeOut)
            //Multiplicamos por 1000 timeOut porque la Libreria es en milisegundos
            new java.util.Timer().schedule(
                    new java.util.TimerTask(){
                        @Override
                        public void run(){
                            new Thread(downloadTask).start();
                        }
                    },
                    1000*this.timeOut
            );

        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            logger.error("URL mal formada", murle.fillInStackTrace());
        } catch (IOException e) {
            out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void start(ActionEvent actionEvent) {
        try{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(tfUrl.getScene().getWindow());
            if (file == null)
                return;

            downloadTask = new DownloadTask(urlText, file,path);

            pbProgress.progressProperty().unbind();
            pbProgress.progressProperty().bind(downloadTask.progressProperty());

            downloadTask.stateProperty().addListener((observableValue, oldState, newState) -> {
                out.println(observableValue.toString());
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
            out.println("An error occurred.");
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
