package presentacio.classesVista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Grup 3: Wikipedia
 * Usuari: ricard
 * Data: 5/14/15
 */
public class MenuPrincipal implements Initializable{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void carregarDades(ActionEvent event) {
        NavegadorVistes.loadVista(NomsVistes.AfegirDades);
    }

    @FXML
    public void navegarWikipedia(ActionEvent event) {
        NavegadorVistes.loadVista(NomsVistes.NavegacioWikipedia);
    }
}
