package presentacio;

import domini.controladors.CtrlDibuix;
import domini.controladors.CtrlWikipedia;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistencia.CtrlPersistencia;

import java.io.File;
import java.io.IOException;

/**
 * Grup 3: Wikipedia
 * Usuari: ricard.gascons
 * Data: 28/5/15
 */
public class FinestraPrincipal extends Application {

    private NavegacioVista navegacioVista;
    private TemesVista temesVista;
    private GenerarTemes generarTemes;
    private HistorialVista historialVista;

    private TabPane tabPane;
    private Scene scene;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Wikipedia");
        stage.setMinHeight(384);
        stage.setMinWidth(512);

        navegacioVista = new NavegacioVista();
        temesVista = new TemesVista();
        generarTemes = new GenerarTemes(this);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        /*
            Aqui afegim les TABs
         */
        tabPane.getTabs().addAll(
                navegacioVista,
                temesVista,
                generarTemes);

        /*
            Aquin afegim els menus
         */
        EventHandler<ActionEvent> action = listenerMenuItems();
        Menu menu1 = new Menu("Arxiu");
        MenuItem guardar = new MenuItem("Guardar...");
        guardar.setOnAction(action);
        MenuItem carregar = new MenuItem("Carregar...");
        carregar.setOnAction(action);
        MenuItem importar = new MenuItem("Importar...");
        importar.setOnAction(action);
        MenuItem sortir = new MenuItem("Sortir");
        sortir.setOnAction(action);
        menu1.getItems().addAll(guardar, carregar, importar, sortir);

        Menu menu2 = new Menu("Visualitzar");
        MenuItem historialCerques = new MenuItem("Historial de cerques");
        historialCerques.setOnAction(action);
        MenuItem mostrarGrafWiki = new MenuItem("Graf de la Wikipedia");
        mostrarGrafWiki.setOnAction(action);
        MenuItem mostrarGrafTemes = new MenuItem("Graf amb els temes");
        mostrarGrafTemes.setOnAction(action);
        menu2.getItems().addAll(historialCerques, mostrarGrafWiki, mostrarGrafTemes);
        Menu menu3 = new Menu("Ajuda");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1, menu2, menu3);


        //Abans era un StackPane
        VBox root = new VBox();
        root.getChildren().addAll(menuBar, tabPane);
        scene = new Scene(root,1024,768);

        stage.setScene(scene);

        stage.show();
    }

    private EventHandler<ActionEvent> listenerMenuItems() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MenuItem mItem = (MenuItem) event.getSource();
                String itemName = mItem.getText();

                if ("Guardar...".equals(itemName)) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Guardar sessió...");
                    File file = fileChooser.showSaveDialog(new Stage());
                    if (file != null) {
                        System.out.println(file);
                        try {
                            CtrlPersistencia.guardarSessio(file.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if ("Carregar...".equals(itemName)) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Carregar sessió...");
                    File file = fileChooser.showOpenDialog(new Stage());
                    if (file != null) {
                        System.out.println(file);
                        try {
                            CtrlPersistencia.carregarSessio(file.toString());
                        } catch (Exception e) {
                            dialogAltertaImportar();
                        }
                        navegacioVista.carregarCategories();
                        navegacioVista.carregarPagines();
                        //TODO: no s'actualitzen els Temes, ja que a CtrlWikipedia no shi guarden mai
                        temesVista.actualitzaTemes();
                    }
                }
                else if ("Importar...".equals(itemName)) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Importar fitxer...");
                    File file = fileChooser.showOpenDialog(new Stage());
                    if (file != null) {
                        System.out.println(file);
                        try {
                            CtrlWikipedia.getInstance().getGrafWikiFromFile(file.toString());
                        } catch (Exception e) {
                            dialogAltertaImportar();
                        }
                        navegacioVista.carregarCategories();
                        navegacioVista.carregarPagines();
                    }
                }
                else if ("Sortir".equals(itemName)) Platform.exit();
                else if ("Historial de cerques".equals(itemName)) historialVista = new HistorialVista();
                else if ("Graf de la Wikipedia".equals(itemName)) {
                    CtrlDibuix ctrlDibuix = new CtrlDibuix();
                    ctrlDibuix.DibuixarGraf();
                }
                else if ("Graf amb els temes".equals(itemName)) {
                    CtrlDibuix ctrlDibuix = new CtrlDibuix();
                    ctrlDibuix.DibuixarGrafAmbComunitats();
                }
            }
        };
    }

    private void dialogAltertaImportar() {
        final Stage dialog = new Stage();
        VBox parent = new VBox(10);
        parent.setPadding(new Insets(20));
        Label confirmation = new Label("El format especificat és incorrecte");
        Separator separator = new Separator(); separator.setVisible(false);
        HBox botons = new HBox(10);
        Button ok = new Button("D'acord");
        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                dialog.close();
            }
        });
        botons.getChildren().addAll(ok);
        botons.setAlignment(Pos.CENTER);
        parent.getChildren().addAll(confirmation, separator, botons);

        Scene dialogScene = new Scene(parent);
        dialog.setTitle("Format incorrecte!");
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void actualitzarTemes() {
        temesVista.actualitzaTemes();
    }

    public static void main(String[] args) {
        launch(args);
    }
}