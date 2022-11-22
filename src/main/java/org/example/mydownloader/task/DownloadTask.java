package org.example.mydownloader.task;

import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.mydownloader.controller.DownloadController;

import java.io.File;
import java.net.URL;

public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;
    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    @Override
    protected Integer call() throws Exception {
        return null;
    }
}
