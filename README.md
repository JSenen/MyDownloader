# ACTIVIDAD APRENDIZAJE
## 1EVA - 2DAM - PROGRAMACIÓN SERVICIOS Y PROCESOS

![Github](https://img.shields.io/badge/github-black?style=for-the-badge&logo=github&logoColor=white)
![JavaFx](https://img.shields.io/badge/JavaFx-orange?style=for-the-badge&logo=javafx&logoColor=white)

Requisitos (1 pto cada uno, obligatorios)

●	Posibilidad de **descargar múltiples ficheros** al mismo tiempo  ✅

La clase  DownloadTask extends Task<Integer> nos permite realizar un multihilo de descargas. Creando un hilo concurrente por cada una de ellas.

●	Por cada descarga se irá indicando el **progreso de descarga tanto en tamaño como en porcentaje total descargado** ✅

Dentro de la clase DownloadTask, en el metodo de llamada call(). Hemos implementado el siguiente código
```
    updateProgress(downloadProgress, 1);
    updateMessage(Math.round(downloadProgress * 100) + " %\t\t"+Math.round(elapsedTime)+"seg\t\t"+totalRead/1048576+"Mb de "+Math.round(fileSize/1048576)+" Totales");
```
El cual nos ofrece en la barra de progreso tanto el porcentaje, como el tiempo de descarga y los megas descargados del total.

●	Todas las descargas deben poderse **cancelar y eliminar** de la ventana de la aplicación ✅

Por medio de SceneBuilder, hemos implementado un boton en cada descarga y un boton general. Ambos con los métodos.
stopAllDownloads() y stop(). Los cuales se implementan en el AppControler para eliminar las descargas de forma general o individual
``` 
    @FXML
    public void stopAllDownloads() {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }
```
Y en el DownloadController

```
    @FXML
    public void stop(ActionEvent event) {
        stop();
    }

    public void stop() {
        if (downloadTask != null)
            downloadTask.cancel();
    }
```
Tambien es posible **cerrar cada pestaña** de descarga al haber utilizado en la tabPolicy de el fichero xml lo siguiente
   ``` 
   <TabPane fx:id="tpDownloads" prefHeight="287.0" prefWidth="760.0" tabClosingPolicy="SELECTED_TAB" /> 
   ```
Permitiendo que cada panel ofrezca un <kbd>x</kbd> para cerrar individualmente

●	La **ruta** donde se descargan los ficheros, que será fija, **se podrá configurar desde la aplicación** ✅

Se ha creado un nuevo camp de texto, asociado a una variable "path", en la que figura la ruta donde se guardará por defecto.
También se ha añadido un nuevo boton, para que el usuario pueda seleccionar la ruta de destino si así lo desea.
En la clase DownloadTask se ha pasado un nuevo dato, path, con la ruta. Y entre ese dato y el fichero se le pasa al FileOutputStream 
para que lo guarde en la ruta seleccionada.

```
        public DownloadTask(String urlText, File file, String path) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
        this.path = path;
        //Creamos una cadena con la ruta donde se grabara y el fichero
        filepath = path+"/"+file.toString();

    } 
         FileOutputStream fileOutputStream = new FileOutputStream(filepath); 
```
●	Se mantendrá un historial de todos los ficheros descargados por la aplicación y todas las descargas fallidas/canceladas. Este fichero se almacenará 
    como fichero de registro y podrá consultarse desde el interfaz de usuario ✅
    
Se crea una dependencia en el pom

```
    <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.19.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.19.0</version>
        </dependency> 
```
La cual nos permite utilizar la **librería log4j** para ir guardando un registro de cada descarga y del funcionamiento de la aplicación
Le indicamos la clase DonwloaderController como la que supervisionamos
```
    private static final Logger logger = LogManager.getLogger(DownloadController.class);
```
Y en el constructor de la clase vamos guardando los registros de modo "info".
```
    public DownloadController(String urlText) {
        logger.info("Descarga dirección " + urlText + " creada");
        this.urlText = urlText;
    }
```

Se debe crear un fichero xml en el directorio resources, llamado log42j con la configuración, igualmente que se le indica en dicho fichero
un fichero "multidescargas.log" en el que se irá guardando los regitros log en de la ejecución del programa
También se ha creado un fichero Descargas.txt que se encarga de registrar las descargas terminadas y al cual se puede acceder desde un boton en la interfaz de usuario, así como borrarlo.
```
//Grabar en un fichero de texto la descarga realizada para poder mostrar al usuario las que lleva realizadas
                    try(FileWriter fw = new FileWriter("DescargasRealizadas.txt", true); //Creamos fichero txt
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(bw))
                    {
                        out.println("Descarga "+fileName+" finalizada"); //Añadimos texto con el nombre del fichero

                    } catch (IOException e) {
                    }
```

***

Otras funcionalidades (1 pto cada una)

●	Programar el comienzo de una descarga para un momento determinado ✅

Se ha añadido un campo a la ventana en el que el usuario puede introducir el numero de segundos para que se inicie la descarga.
Para ello, el hilo (Threath) que inicia la descarga se ha introducido dentro del Timer.schedule de la libreria java y se ha usado 
la libreria por defecto de java Time.Task para crear una tarea Asíncrona.

```
 //Usamos libreria java timer schedule por si se ha establecido tiempo para iniciar la descarga (timeOut)
            //Multiplicamos por 1000 timeOut porque la Libreria es en milisegundos
            new java.util.Timer().schedule(
                    new java.util.TimerTask(){
                        @Override
                        public void run(){
                            new Thread(downloadTask).start();
                        }
                    },
                    1000*this.timeOut
            );
```

●	La aplicación podrá leer listas de enlaces de un fichero de texto y encolará las descargas ✅

Para ello se ha creado en la clase AppController el método readDLC(), el cual se encarga de leer linea a linea el fichero seleccionado por 
el usuario y ir mandando cada descarga al DownloadControler. En el mismo se añade un contador para no superar el límite máximo de descargas
indicadas en la interfaz de usuario
```
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
```
●	Posibilidad de configurar número máximo de descargas simultáneas ✅

Se ha creado en la interfaz de usuario un campo para indicar el **número de descargas permitidas**. También se le ha añadido dos botones para poder
**aumentar o disminuir** el contador; o puede introducirlo directamente en el TextEdit.
Se crea una variable "numDwn" de número entero que recoge su valor del anterior campo, y va disminuyendo por cada descarga hasta llegar a 0, con lo
que activara un campo "Label" que nos indicará que hemos alcanzado el límite.
**El contador se utiliza tanto para descargas individuales como leidas de un fichero DLC**
```
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
```
●	Al cancelar la descarga, opcionalmente para el usuario, se podrá eliminar el fichero que se estaba descargando o se había descargado ❌

●	Al iniciar la aplicación se mostrará un SplashScreen ✅

Se crea un SplashScreen que arranca al inicio de la aplicación.

```
 @Override
    public void start(Stage stage) throws Exception {

        //Cargamos el controlador de la pantalla incial de Splash
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("splashscreen.fxml"));
        loader.setController(new SplashScreenController());
        AnchorPane anchorPane = loader.load();

        //Dibujamos la pantalla
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setTitle("MyDownloader");
        stage.show();
    }
```
También se crea un Controller, el cual lanza un hilo de 4 segundos hasta que carga el siguiente controller de la ventana principal de la aplicación.
```
public class SplashScreenController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new SplashScreen().start();
    }
        class SplashScreen extends Thread {
            @Override
            public void run() {
                //Abrimos hilo de 3 segundos para que se vea la Splash Screen
                try {
                    Thread.sleep(3000);

                    //Indicamos que pantalla abrir al acabar el hilo
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            //Cargamos controller de la pantalla principal
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(R.getUI("mainscreen.fxml"));
                            loader.setController(new AppController());
                            ScrollPane scrollPane = null;
                            try {
                                scrollPane = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            Scene scene = new Scene(scrollPane);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setTitle("MyDownloader");
                            stage.show();

                            //Ocultamos la pantalla splash
                            anchorPane.getScene().getWindow().hide();

                        }

                    });

                } catch (InterruptedException ex) {
                    Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
```

●	Posibilidad de reanudar descargas canceladas previamente ❌

●	Si el usuario asi lo selecciona, que elija la ubicación de la descarga en el mismo momento en que ésta inicia, saltándose entonces la que haya configurada en la aplicación ❌ 

●	En el caso de que haya límite de descargas y éste haya sido superado, que el usuario pueda lanzar más descargas y éstas queden encoladas esperando el momento en que puedan ser lanzadas ✅

Se ha credo un campo en la interfaz para que el usuario pueda indicar en cualquier momento el numero de descargas.

●	Mostrar la velocidad de descarga (MB/s) en todo momento  ✅

Durante la descarga, se puede observar en una etiqueta "label" el **progreso en Mb de la misma en relación al total así como tiempo trascurrido**
```
updateProgress(downloadProgress, 1);
updateMessage(Math.round(downloadProgress * 100) + " %\t\t"+Math.round(elapsedTime)+"seg\t\t"+totalRead/1048576+"Mb de "+Math.round(fileSize/1048576)+" Totales");
```
●	Realizar el seguimiento del proyecto utilizando la plataforma GitHub para almacenar el código y gestionando las issues (bug, mejoras, . . .) a medida que se vaya trabajando en él ✅

Este proyecto se ha **gestionado mediante GitHub.**

