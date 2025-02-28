package presentacio;

import domini.controladors.CtrlCatPag;
import domini.controladors.CtrlComunitat;
import domini.controladors.CtrlWikipedia;
import domini.modeldades.graf.NodeCategoria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentacio.autocompletat.AutoCompleteComboBoxListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


/**
 * Grup 3: Wikipedia
 * Usuari: ricard.gascons
 * Data: 28/5/15
 */
public class TemesVista extends Tab {

    private final double SPACE = 10;
    private ListView<String> llistaT;
    private ListView<String> llistaC;


    public TemesVista() {
        setText("Navegar temes");

        llistaT = new ListView<>();
        actualitzaTemes();
        llistaT.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!llistaT.getSelectionModel().isEmpty() && !llistaT.getItems().isEmpty()) {
                    llistaC.getSelectionModel().clearSelection();
                    String tema = llistaT.getSelectionModel().getSelectedItem();
                    int id = CtrlComunitat.getInstance().getId(tema);
                    try {
                        HashSet<NodeCategoria> c =
                                CtrlComunitat.getInstance().getConjunt().getCjtComunitats().getComunitat(id).getNodes();
                        ObservableList<String> data = FXCollections.observableArrayList();
                        llistaC.setItems(data);
                        for (NodeCategoria n : c) data.add(n.getNom());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        llistaC = new ListView<>();

        HBox liniaTC = new HBox();
        liniaTC.getChildren().addAll(llistaT, llistaC);

        EventHandler<MouseEvent> action = listenerButtons();
        Button temaNouButton = new Button("Crear tema");
        temaNouButton.setMaxWidth(Double.MAX_VALUE);
        temaNouButton.setOnMouseClicked(action);
        Button modNomDescTemaButton = new Button("Modificar nom i descripció tema");
        modNomDescTemaButton.setMaxWidth(Double.MAX_VALUE);
        modNomDescTemaButton.setOnMouseClicked(action);
        Button descriccioTemaButton = new Button("Mostrar descripció tema");
        descriccioTemaButton.setMaxWidth(Double.MAX_VALUE);
        descriccioTemaButton.setOnMouseClicked(action);
        Button affegirCatButton = new Button("Afegir categoria");
        affegirCatButton.setMaxWidth(Double.MAX_VALUE);
        affegirCatButton.setOnMouseClicked(action);
        Button eliminarCatButton = new Button("Eliminar categoria");
        eliminarCatButton.setMaxWidth(Double.MAX_VALUE);
        eliminarCatButton.setOnMouseClicked(action);
        Button moureCatButton = new Button("Eliminar tema");
        moureCatButton.setMaxWidth(Double.MAX_VALUE);
        moureCatButton.setOnMouseClicked(action);
        Button opCjtsButton = new Button("Operacions entre temes");
        opCjtsButton.setMaxWidth(Double.MAX_VALUE);
        opCjtsButton.setOnMouseClicked(action);

        VBox liniaBotons = new VBox(50);
        liniaBotons.getChildren().addAll(
                temaNouButton,
                modNomDescTemaButton,
                descriccioTemaButton,
                affegirCatButton,
                eliminarCatButton,
                moureCatButton,
                opCjtsButton);
        liniaBotons.setAlignment(Pos.CENTER_LEFT);

        VBox wrapperTaules = new VBox();
        wrapperTaules.getChildren().addAll(liniaTC);
        wrapperTaules.setAlignment(Pos.CENTER_LEFT);

        HBox parentBox = new HBox(SPACE);
        parentBox.getChildren().addAll(wrapperTaules, liniaBotons);
        parentBox.setAlignment(Pos.CENTER);
        parentBox.setPadding(new Insets(20));
        setContent(parentBox);
    }

    public void actualitzaTemes() {
        Collection<String> cjtComunitats=
                CtrlWikipedia.getInstance().getConjuntsGenerats().getNoms();
        llistaT.getItems().setAll(cjtComunitats);
    }

    public void netejarLlistaCats() {
        llistaC.getItems().clear();
    }


    private EventHandler<MouseEvent> listenerButtons() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Button button = (Button) event.getSource();
                String buttonName = button.getText();
                if("Crear tema".equals(buttonName)) {
                    dialogCrearTema();
                }
                else if ("Modificar nom i descripció tema".equals(buttonName)) {
                    if (!llistaT.getSelectionModel().isEmpty()) {
                        dialogModificarNomDescTema();
                    }
                }
                else if ("Mostrar descripció tema".equals(buttonName)) {
                    if (!llistaT.getSelectionModel().isEmpty()) {
                        String nomTema = llistaT.getSelectionModel().getSelectedItem();
                        int idTema = CtrlWikipedia.getInstance().getConjuntsGenerats().getId(nomTema);
                        String descripcioTema = CtrlWikipedia.getInstance().getConjuntsGenerats().getDescripcio(idTema);
                        AlertDialog dialogDescripcio = new AlertDialog(nomTema, descripcioTema, 500, 200);
                        dialogDescripcio.show();
                    }
                }
                else if ("Afegir categoria".equals(buttonName)) {
                    if (!llistaT.getSelectionModel().isEmpty())
                        dialogAfegirCat();
                }
                else if ("Eliminar categoria".equals(buttonName)) {
                    if (!llistaT.getSelectionModel().isEmpty() && !llistaC.getSelectionModel().isEmpty()) {

                        String tema = llistaT.getSelectionModel().getSelectedItem();
                        String cat = llistaC.getSelectionModel().getSelectedItem();
                        CtrlComunitat ctrlComunitat = CtrlComunitat.getInstance();
                        int idTema = ctrlComunitat.getId(tema);
                        try {
                            ctrlComunitat.eliminarCatComunitat(idTema, cat);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        llistaC.getItems().remove(cat);
                        llistaC.getSelectionModel().clearSelection();
                    }
                }
                else if ("Eliminar tema".equals(buttonName)) {
                    if (!llistaT.getSelectionModel().isEmpty()) {
                        CtrlComunitat ctrlComunitat = CtrlComunitat.getInstance();
                        String tema = llistaT.getSelectionModel().getSelectedItem();
                        int id = ctrlComunitat.getId(tema);
                        try {
                            ctrlComunitat.eliminarComunitat(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        actualitzaTemes();
                        netejarLlistaCats();
                    }
                }
                else if ("Operacions entre temes".equals(buttonName)) {
                    Stage stage = new Stage();
                    OperacioCjtsVista operacio = new OperacioCjtsVista(TemesVista.this);
                    Scene scene = operacio.getScene();
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.setTitle("Operacions amb els temes");
                    stage.show();
                }
            }
        };
    }

    private void dialogCrearTema() {
        final Stage dialog = new Stage();
        VBox parent = new VBox(SPACE);
        parent.setPadding(new Insets(20));
        final TextField inputText = new TextField();
        Separator separator = new Separator(); separator.setVisible(false);
        HBox botons = new HBox(SPACE);
        Button ok = new Button("Crear");
        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CtrlComunitat ctrlComunitat = CtrlComunitat.getInstance();
                try {
                    if(!inputText.getText().isEmpty() && !ctrlComunitat.jaExisteixTemaNom(inputText.getText())){
                        ctrlComunitat.creaComunitat( inputText.getText());
                        actualitzaTemes();
                        dialog.close();
                    }else{
                        AlertDialog alertDialog = new AlertDialog("Error", "Ja existeix el tema o no has escrit cap nom");
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.close();
                }
            }
        });
        Button cancel = new Button("Cancel·lar");
        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dialog.close();
            }
        });
        botons.getChildren().addAll(ok, cancel);
        botons.setAlignment(Pos.CENTER);
        parent.getChildren().addAll(inputText, separator, botons);

        Scene dialogScene = new Scene(parent);
        dialog.setTitle("Crear tema");
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void dialogModificarNomDescTema() {
        final Stage dialog = new Stage();
        VBox parent = new VBox(SPACE);
        parent.setPadding(new Insets(20));
        final String nomTema = llistaT.getSelectionModel().getSelectedItem();
        final CtrlComunitat ctrlComunitat = CtrlComunitat.getInstance();
        final int id = ctrlComunitat.getId(nomTema);
        final String descTema = CtrlWikipedia.getInstance().getConjuntsGenerats().getDescripcio(id);
        final TextField inputText = new TextField(nomTema);
        final TextArea descripcio = new TextArea(descTema);
        Separator separator = new Separator(); separator.setVisible(false);
        HBox botons = new HBox(SPACE);
        Button ok = new Button("Modificar");
        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if(!inputText.getText().isEmpty() && !CtrlComunitat.getInstance().jaExisteixTemaNom(inputText.getText())){
                    ctrlComunitat.modNomComunitat(id, inputText.getText());
                    CtrlWikipedia.getInstance().getConjuntsGenerats().setDescripcio(id, descripcio.getText());
                    actualitzaTemes();
                    dialog.close();
                    } else{
                        AlertDialog alertDialog = new AlertDialog("Error", "Ja existeix el tema o no has escrit cap nom");
                        alertDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.close();
                }
            }
        });
        Button cancel = new Button("Cancel·lar");
        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dialog.close();
            }
        });
        botons.getChildren().addAll(ok, cancel);
        botons.setAlignment(Pos.CENTER);
        parent.getChildren().addAll(inputText, descripcio, separator, botons);

        Scene dialogScene = new Scene(parent);
        dialog.setTitle("Modificar nom tema");
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void dialogAfegirCat() {
        ArrayList<NodeCategoria> cats = CtrlWikipedia.getInstance().getGrafWiki().getCategories();
        final Stage dialog = new Stage();
        VBox parent = new VBox(SPACE);
        parent.setPadding(new Insets(20));
        ObservableList<String> data = FXCollections.observableArrayList();
        final ComboBox<String> inputText = new ComboBox<>();
        inputText.setPrefWidth(300);
        for (NodeCategoria c : cats) data.add(c.getNom());
        inputText.setItems(data);
        new AutoCompleteComboBoxListener(inputText);
        Separator separator = new Separator(); separator.setVisible(false);
        HBox botons = new HBox(SPACE);
        Button ok = new Button("Afegir");
        ok.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                CtrlComunitat ctrlComunitat = CtrlComunitat.getInstance();
                CtrlCatPag ctrlCatPag = CtrlCatPag.getInstance();
                if (!ctrlCatPag.existeixCategoria(inputText.getValue())) {
                    inputText.setValue("No existeix la categoria triada");
                } else {
                    try {
                        ctrlComunitat.afegirCatComunitat(
                                ctrlComunitat.getId(llistaT.getFocusModel().getFocusedItem()),
                                inputText.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String tema = llistaT.getSelectionModel().getSelectedItem();
                    int id = CtrlComunitat.getInstance().getId(tema);
                    try {
                        HashSet<NodeCategoria> c =
                                CtrlComunitat.getInstance().getConjunt().getCjtComunitats().getComunitat(id).getNodes();
                        ObservableList<String> data = FXCollections.observableArrayList();
                        llistaC.setItems(data);
                        for (NodeCategoria n : c) data.add(n.getNom());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.close();
                }
            }
        });
        Button cancel = new Button("Cancel·lar");
        cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dialog.close();
            }
        });
        botons.getChildren().addAll(ok, cancel);
        botons.setAlignment(Pos.CENTER);
        parent.getChildren().addAll(inputText, separator, botons);

        Scene dialogScene = new Scene(parent);
        dialog.setTitle("Afegir categoria");
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}