/**
 * SNAC Ingest Tool
 *
 * @author Robbie Hott
 * @license https://opensource.org/licenses/BSD-3-Clause BSD 3-Clause
 * @copyright 2019 the Rector and Visitors of the University of Virginia
 */
package org.snaccooperative.ingesttool;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class StartWindowController {

    @FXML
    private TextField dirField;

    @FXML
    private TextField keyField;

    @FXML
    private void findDirectory() throws IOException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open Directory");
        File dir = chooser.showDialog(new Stage());
        dirField.setText(dir.getAbsolutePath());
    }

    @FXML
    private void handleSubmitButtonAction() throws IOException {
        if (dirField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Directory is Required");
            alert.showAndWait();
        } else {
            App.setRoot("listwindow", dirField.getText(), keyField.getText());
        }
    }
}