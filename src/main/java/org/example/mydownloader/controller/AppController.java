package org.example.mydownloader.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.mydownloader.util.R;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppController {

    public TextField tfUrl;
    public TextField tFDirectory;
    public Button btDownload;
    public Button btFileDownload;
    public Button btDirectory;
    public String path = "/Users/JSenen/Downloads";

    public TabPane tpDownloads; //id panel pestañas
    private Map<String, DownloadController> allDownloads; //Guardamos rastro cada descarga
    public AppController() {
        allDownloads = new HashMap<>();

    } //Metodo alamcena descargas fichero


    @FXML
    public void launchDownload(ActionEvent actionEvent) {

        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        path = tFDirectory.getText();
        launch(urlText,path);
    }

    @FXML
    public void selectDirectory(Event event){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(tFDirectory.getScene().getWindow());
        path = file.getParent();
        tFDirectory.setText(path);

    }

    public void launch(String urlText, String path) {
        //Cargamos controller de la pantalla de cada descarga
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("downloadscreen.fxml"));

            tFDirectory.setText(path);

            DownloadController downloadController = new DownloadController(urlText,path);
            loader.setController(downloadController);
            VBox vBox = loader.load();

            String filename = urlText.substring(urlText.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, vBox));

            allDownloads.put(urlText, downloadController);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }

    @FXML
    public void launchFileDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        readDLC();
    }

    @FXML
    public void readDLC() {
        //Descarga desde fichero
         try {
            // Usuario selecciona fichero
            FileChooser fileChooser = new FileChooser();
            File dlcFile = fileChooser.showOpenDialog(tfUrl.getScene().getWindow());
            
            if (dlcFile == null)
                return;

            // Leer fichero
            Scanner reader = new Scanner(dlcFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
                // Lanzar controlador y descarga
                launch(data,path);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Se ha producido un error");
            e.printStackTrace();
        }
    }
}


