package org.example.mydownloader.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.mydownloader.util.R;

import java.io.IOException;
import java.util.Map;

public class AppController {

    public TextField tfUrl; //id TextField url
    public TabPane tpDownloads; //id panel pestañas
    private Map<String, DownloadController> allDownloads; //Guardamos rastro cada descarga

    @FXML
    public void launchDownload (ActionEvent actionEvent){
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        launch(urlText);
    }
    public void launch(String urlText){
        //Cargamos controller de la pantalla de cada descarga
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("downloadscreen.fxml"));

            DownloadController downloadController = new DownloadController(urlText);
            loader.setController(downloadController);
            VBox vBox = loader.load();

            String filename = urlText.substring(urlText.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, vBox));

            allDownloads.put(urlText, downloadController);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }


}
