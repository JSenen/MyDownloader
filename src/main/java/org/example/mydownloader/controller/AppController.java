package org.example.mydownloader.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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
    public TextField numDesc;
    public Label labelMaxDwn;
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
        //Leemos numero máximo descargas establecido
        int numDwn = Integer.parseInt(numDesc.getText());
        if (numDwn <= 0 ){
            //Si supera máxinmo salta aviso
            labelMaxDwn.setText("NUMERO MAXIMO ALCANZADO");
            return;
        }else{
            labelMaxDwn.setText("");
        }
        tfUrl.clear();
        tfUrl.requestFocus();
        path = tFDirectory.getText();
        launch(urlText,path,numDwn);
    }

    @FXML
    public void selectDirectory(Event event){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(tFDirectory.getScene().getWindow());
        path = file.getParent();
        tFDirectory.setText(path);

    }

    public void launch(String urlText, String path, int numDwn) {

        //Vamos descontando descargas de las establecidas
        numDesc.setText(String.valueOf(numDwn-1));

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

        int numDwn = Integer.parseInt(numDesc.getText());
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
                numDwn = numDwn -1;
                System.out.println(data);
                // Lanzar controlador y descarga
                //Va descontando descargas y lanza aviso si se supera numero establecido
                if (numDwn >= 0 ){
                    launch(data,path,numDwn);
                    labelMaxDwn.setText("");
                }else{
                    labelMaxDwn.setText("NUMERO MAXIMO ALCANZADO");
                    return;
                }
                numDesc.setText(String.valueOf(numDwn));


            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Se ha producido un error");
            e.printStackTrace();
        }
    }
}


