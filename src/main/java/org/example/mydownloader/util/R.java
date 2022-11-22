package org.example.mydownloader.util;

import java.io.File;
import java.net.URL;

//Clase para pasarle la pantalla que solicite
public class R {
    public static URL getUI(String name) {
        return Thread.currentThread().getContextClassLoader().getResource("ui" + File.separator + name);
    }
}
