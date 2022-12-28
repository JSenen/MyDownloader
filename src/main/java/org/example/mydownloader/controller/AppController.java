package org.example.mydownloader.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.example.mydownloader.util.R;
import java.awt.*;
import java.io.*;
import java.util.*;


public class AppController {
    public TextField tfUrl;
    public TextField tFDirectory;
    public TextField numDesc;
    public Label labelMaxDwn;
    public Button btDownload;
    public Button btFileDownload;
    public Button btDirectory;
    public Button btStopAllDownloads;
    public Button plusButton;
    public Button minusButton;
    public Button butListDownloads;
    public Button butCleanListado;
    public Button butlog;
    public String path = "/Users/JSenen/Downloads";
    public TabPane tpDownloads; //id panel pestañas
    private Map<String, DownloadController> allDownloads; //Guardamos rastro cada descarga
    public AppController() {
        allDownloads = new HashMap<>();
    } //Metodo alamcena descargas fichero


    @FXML
    public void launchDownload(ActionEvent actionEvent) {

        String urlText = tfUrl.getText();

        //Leemos numero máximo descargas establecido en la casilla de la aplicación
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
        //Selección de directorio de descarga
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(tFDirectory.getScene().getWindow());
        path = file.getPath();
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

    @FXML
    public void plusButtonClick(){

        int num = Integer.parseInt(numDesc.getText());
        numDesc.setText(String.valueOf(num+1));
    }
    @FXML
    public void minusButtonClick(){
        //Cuando vale 0 no sigue
        if (numDesc.getText().equals("")){
            numDesc.setText("0");
            return;
        }
        int num = Integer.parseInt(numDesc.getText());
        numDesc.setText(String.valueOf(num-1));
    }
    @FXML
    //Metodo al pulsar boton para ver archivo descargas terminadas
    public void openListDownloads (ActionEvent event){
        try {
            File fileDownloads = new File ("/Users/JSenen/Documents/Proyectos/PSP/MyDownloader/DescargasRealizadas.txt");
            if (!Desktop.isDesktopSupported()){
                System.out.println("no soportado");
            }
            Desktop desktop = Desktop.getDesktop();
            if (fileDownloads.exists())
                desktop.open(fileDownloads);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    //Metodo para borrar el archivo txt de descargas realizadas
    public void cleanListado (ActionEvent event) {

        File file = new File("/Users/JSenen/Documents/Proyectos/PSP/MyDownloader/DescargasRealizadas.txt"); //Buscamos fichero
        if (file.delete()){ //Si fichero existe lo elimina
            System.out.println("Eliminado");
        }
        else
        {
            System.out.println("No exite");
        }
    }
    @FXML
    //Metodo para ver el archivo LOG y comprobar descargas fallidas/Canceladas
    public void seeLog (ActionEvent event) {

        try {
            File fileDownloads = new File ("/Users/JSenen/Documents/Proyectos/PSP/MyDownloader/multidescargas.log");
            if (!Desktop.isDesktopSupported()){
                System.out.println("no soportado");
            }
            Desktop desktop = Desktop.getDesktop();
            if (fileDownloads.exists())
                desktop.open(fileDownloads);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}


