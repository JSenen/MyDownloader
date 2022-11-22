package org.example.mydownloader.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.mydownloader.task.DownloadTask;

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
}
