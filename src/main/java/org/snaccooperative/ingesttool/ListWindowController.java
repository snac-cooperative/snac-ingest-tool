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
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import org.snaccooperative.data.AbstractData;
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


        constellations.setRowFactory(row -> new TableRow<ConstellationWrapper>() {
            public void updateItem(ConstellationWrapper cw, boolean empty) {
                super.updateItem(cw, empty);
                if (cw != null && cw.getUploadStatus() != null) {
                    if (cw.getUploadStatus().contains("success")) {
                        setStyle("-fx-background-color: lightgreen");
                    } else if (cw.getUploadStatus().contains("failure")) {
                        setStyle("-fx-background-color: lightcoral");
                    }
                } else {
                    //setStyle("-fx-background-color: inherit");
                }

            }
        });

        resourcesView.setRowFactory(row -> new TableRow<ResourceWrapper>() {
            public void updateItem(ResourceWrapper cw, boolean empty) {
                super.updateItem(cw, empty);
                if (cw != null && cw.getUploadStatus() != null) {
                    if (cw.getUploadStatus().contains("success")) {
                        setStyle("-fx-background-color: lightgreen");
                    } else if (cw.getUploadStatus().contains("failure")) {
                        setStyle("-fx-background-color: lightcoral");
                    }
                } else {
                    //setStyle("-fx-background-color: inherit");
                }

            }
        });


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
                        if (!constellations.getItems().contains(cw)) {
                            constellations.getItems().add(cw);

                            for (ResourceRelation rr : c.getResourceRelations()) {
                                Resource r = rr.getResource();
                                if (r != null) {
                                    ResourceWrapper rw = new ResourceWrapper(r);
                                    if (!resourcesView.getItems().contains(rw)) {
                                        resourcesView.getItems().add(rw);
                                    } else {
                                        System.err.println("Did not add duplicate: " + rw.getTitle());
                                    }
                                }
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

    @FXML
    private void handleNewConstellationButtonAction() {
        SNACConnector sc = new SNACConnector(App.getData("APIKey"));
        for (ConstellationWrapper cw : constellations.getItems()) {
            if (cw.getConstellation().getID() == 0) {
                //cw.getConstellation().cleanseSubElements(AbstractData.OPERATION_INSERT);
                Constellation written = sc.writePublishConstellation(cw.getConstellation());
                if (written != null) {
                    cw.setUploadStatus("success");
                } else
                    cw.setUploadStatus("failure");
                cw.setServerResponse(sc.getLastServerMessage());
                cw.setConstellation(written);
                constellations.refresh();
            }
        }
    }
}
