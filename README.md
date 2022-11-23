# ACTIVIDAD APRENDIZAJE
## 1EVA - 2DAM - PROGRAMACIÓN SERVICIOS Y PROCESOS

![Github](https://img.shields.io/badge/github-black?style=for-the-badge&logo=github&logoColor=white)
![JavaFx](https://img.shields.io/badge/JavaFx-orange?style=for-the-badge&logo=javafx&logoColor=white)

Requisitos (1 pto cada uno, obligatorios)

●	Posibilidad de descargar múltiples ficheros al mismo tiempo  ✅
    
    La clase ` DownloadTask extends Task<Integer>`nos permite realizar un multihilo de descargas. Creando un hilo concurrente por cada
    una de ellas.

●	Por cada descarga se irá indicando el progreso de descarga tanto en tamaño como en porcentaje total descargado ✅

    Dentro de la clase DownloadTask, en el metodo de llamada call(). Hemos implementado el siguiente código
    ` updateProgress(downloadProgress, 1);
    updateMessage(Math.round(downloadProgress * 100) + " %\t\t"+Math.round(elapsedTime)+"seg\t\t"+totalRead/1048576+"Mb de "+fileSize/1000/1048576);´
    El cual nos ofrece en la barra de progreso tanto el porcentaje, como el tiempo de descarga y los megas descargados del total.

●	Todas las descargas deben poderse cancelar y eliminar de la ventana de la aplicación ✅

    Por medio de SceneBuilser, hemos implementado un boton en cada descarga y un boton general. Ambos con los métodos.
    stopAllDownloads() y stop(). Los cuales se implementan en el AppControler
    ` @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }`
    Y en el DownloadController
    `@FXML
    public void stop(ActionEvent event) {
        stop();
    }

    public void stop() {
        if (downloadTask != null)
            downloadTask.cancel();
    }`
●	La ruta donde se descargan los ficheros, que será fija, se podrá configurar desde la aplicación -[ ] Pendiente ❌
●	Se mantendrá un historial de todos los ficheros descargados por la aplicación y todas las descargas fallidas/canceladas. Este fichero se almacenará como fichero de registro y podrá consultarse desde el interfaz de usuario
    
    Se crea una dependencia en el pom
    `<dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.19.0</version>
        </dependency>`
    La cual nos permite utilizar la librería log4j para ir guardando un registro de cada descarga
    Creamos un atributo en el DownloadController para su gestión:
    `private static final Logger logger = LogManager.getLogger(DownloadController.class);`
    Y en el constructor de la clase vamos guardando los registros
    ` public DownloadController(String urlText) {
        logger.info("Descarga dirección " + urlText + " creada");
        this.urlText = urlText;
    }`

    Se debe crear un fichero xml en el directorio resources, llamado log42j con la configuración
    

Otras funcionalidades (1 pto cada una)

●	Programar el comienzo de una descarga para un momento determinado ❌
●	La aplicación podrá leer listas de enlaces de un fichero de texto y encolará las descargas ✅
●	Posibilidad de configurar número máximo de descargas simultáneas ❌
●	Al cancelar la descarga, opcionalmente para el usuario, se podrá eliminar el fichero que se estaba descargando o se había descargado ❌
●	Al iniciar la aplicación se mostrará un SplashScreen ❌
●	Posibilidad de reanudar descargas canceladas previamente
●	Si el usuario asi lo selecciona, que elija la ubicación de la descarga en el mismo momento en que ésta inicia, saltándose entonces la que haya configurada en la aplicación ❌
●	En el caso de que haya límite de descargas y éste haya sido superado, que el usuario pueda lanzar más descargas y éstas queden encoladas esperando el momento en que puedan ser lanzadas ❌
●	Mostrar la velocidad de descarga (MB/s) en todo momento  ✅
●	Realizar el seguimiento del proyecto utilizando la plataforma GitHub para almacenar el código y gestionando las issues (bug, mejoras, . . .) a medida que se vaya trabajando en él ✅

