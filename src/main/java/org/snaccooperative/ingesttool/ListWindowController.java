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

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.snaccooperative.data.Constellation;

import org.snaccooperative.data.Resource;
import org.snaccooperative.data.ResourceRelation;

public class ListWindowController {

    @FXML private TableView<ConstellationWrapper> constellations;
    @FXML private TableColumn<ConstellationWrapper, String> name;
    @FXML private TableColumn<ConstellationWrapper, String> status;

    @FXML private TableView<ResourceWrapper> resourcesView;
    @FXML private TableColumn<ResourceWrapper, String> rtitle;
    @FXML private TableColumn<ResourceWrapper, String> rstatus;

    @FXML private ProgressBar progressBar;
    protected Task tableLoader;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void initialize() throws IOException {
        name.setCellValueFactory(new PropertyValueFactory<ConstellationWrapper, String>("nameEntry"));
        status.setCellValueFactory(new PropertyValueFactory<ConstellationWrapper, String>("status"));
        rtitle.setCellValueFactory(new PropertyValueFactory<ResourceWrapper, String>("title"));
        rstatus.setCellValueFactory(new PropertyValueFactory<ResourceWrapper, String>("status"));

        tableLoader = loadTables();
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(tableLoader.progressProperty());

        new Thread(tableLoader).start();

    }

    private Task loadTables() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                String dir = null;
                if (App.hasData("Directory")) {
                    dir = App.getData("Directory");
                }

                File dirFile = new File(dir);
                int numFiles = 0;
                for (File file : dirFile.listFiles()) {
                    if (file.isFile() && file.getName().contains("json")) {
                        numFiles++;
                    }
                }

                int i = 0;
                for (File file : dirFile.listFiles()) {
                    if (file.isFile() && file.getName().contains("json")) {
                        System.err.println("Loading: "+file.getName());
                        Constellation c = Constellation.readFromFile(file);
                        System.err.println("ID " + c.getID());
                        ConstellationWrapper cw = new ConstellationWrapper(c);
                        constellations.getItems().add(cw);

                        for (ResourceRelation rr : c.getResourceRelations()) {
                            Resource r = rr.getResource();
                            if (r != null) {
                                ResourceWrapper rw = new ResourceWrapper(r);
                                resourcesView.getItems().add(rw);
                            }
                        }
                        updateMessage(file.getName());
                        updateProgress(++i, numFiles);
                    }
                }
                System.err.println("Done loading");
                return null;
            }
        };
    }

    @FXML
    private void handleNewResourcesButtonAction() {

    }
}
