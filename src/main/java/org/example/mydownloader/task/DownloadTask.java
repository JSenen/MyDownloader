package org.example.mydownloader.task;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.mydownloader.controller.DownloadController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;
    private String path;
    private String filepath;
    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadTask(String urlText, File file, String path) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
        this.path = path;
        //Creamos una cadena con la ruta donde se grabara y el fichero
        filepath = path+"/"+file.toString();

    }

    @Override
    protected Integer call() throws Exception {
        logger.trace("Descarga " + url.toString() + " iniciada "+" en directorio "+path);
        updateMessage("Conectando con el servidor . . .");

        //Acceso al fichero URL
        URLConnection urlConnection = url.openConnection();
        //Tamaño del fichero
        double fileSize = urlConnection.getContentLength();
        //Tamaño del fichero en megas
        double megaSize = fileSize / 1048576;

        BufferedInputStream in = new BufferedInputStream(url.openStream());

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;

        //Calculo tiempo de la descarga
        Instant start = Instant.now();
        Instant current;
        float elapsedTime;

        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            downloadProgress = ((double) totalRead / fileSize);
            //Calculo por cada interacción
            current = Instant.now();
            elapsedTime = Duration.between(start, current).toSeconds();

            updateProgress(downloadProgress, 1);
            updateMessage(Math.round(downloadProgress * 100) + " %\t\t"+Math.round(elapsedTime)+"seg\t\t"+totalRead/1048576+"Mb de "+fileSize/1000/1048576);

            //Realiza descarga mas lenta
            Thread.sleep(1);

            fileOutputStream.write(dataBuffer, 0, bytesRead);
            totalRead += bytesRead;

            if (isCancelled()) {
                logger.trace("Descarga " + url.toString() + " cancelada");
                return null;
            }
        }
            updateProgress(1, 1);
            updateMessage("100 %");

            logger.trace("Descarga " + url.toString() + " finalizada");

            return null;
    }
}
