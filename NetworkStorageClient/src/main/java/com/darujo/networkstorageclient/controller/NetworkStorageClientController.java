package com.darujo.networkstorageclient.controller;

import com.darujo.networkstorageclient.NetworkStorageClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NetworkStorageClientController {
    @FXML
    public Button addButton;
    @FXML
    public Button delButton;
    @FXML
    public Button getButton;
    @FXML
    public TreeView<Layer> treeDir;

    @FXML
    public void addAction(ActionEvent actionEvent) {
        System.out.println(tt2.isSelected());
    }

    @FXML
    public void delAction(ActionEvent actionEvent) {
    }

    @FXML
    public void getAction(ActionEvent actionEvent) {

    }

    private final Image folderImage= new Image (NetworkStorageClient.class.getResourceAsStream("folder3.png"));
    private final Node rootIcon =
            new ImageView(folderImage);
    CheckBoxTreeItem<Layer> tt2;
    public void initializeControl() {

        treeDir.setCellFactory(param -> new CheckBoxTreeCell<>());
        Layer layer0 = new Layer("Layers", false);
        Layer layer2 = new Layer("Layers111", false);

        Layer layer3 = new Layer("Layers333", false);
         tt2 = new CheckBoxTreeItem<>(layer2);
         tt2.setSelected(layer2.isSelected());
        TreeItem<Layer> tt3 = new TreeItem<>(layer3);

        TreeItem<Layer> tt = new TreeItem<>(layer0,rootIcon);
        tt.getChildren().add(tt2);
        tt.getChildren().add(tt2);
        treeDir.setRoot(tt);
        treeDir.getRoot().getChildren().setAll(tt2,tt3);
        tt.setExpanded(true);
        treeDir.setShowRoot(false);
//        tt.getValue().isSelected();
//        tt.getChildren().setAll(tt2,tt2);

    }
}