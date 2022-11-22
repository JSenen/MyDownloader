package org.example.mydownloader.task;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.mydownloader.controller.DownloadController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;
    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    public DownloadTask(String urlText, File file) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        logger.trace("Descarga " + url.toString() + " iniciada");
        updateMessage("Conectando con el servidor . . .");

        //Acceso al fichero URL
        URLConnection urlConnection = url.openConnection();
        //Tamaño del fichero
        double fileSize = urlConnection.getContentLength();
        //Tamaño del fichero en megas
        double megaSize = fileSize / 1048576;

        BufferedInputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;

        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            downloadProgress = ((double) totalRead / fileSize);

            updateProgress(downloadProgress, 1);
            updateMessage(downloadProgress * 100 + " %");

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
