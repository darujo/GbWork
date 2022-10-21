package com.darujo.networkstorageclient.controller;

import com.darujo.comand.Command;
import com.darujo.comand.CommandType;
import com.darujo.comand.commanddata.DirListCommandData;
import com.darujo.networkstorageclient.NetworkStorageClient;
import com.darujo.networkstorageclient.network.NetworkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

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
//        System.out.println(tt2.isSelected());
    }

    @FXML
    public void delAction(ActionEvent actionEvent) {
    }

    @FXML
    public void getAction(ActionEvent actionEvent) {

    }

    private final Image folderImage= new Image (NetworkStorageClient.class.getResourceAsStream("folder3.png"));
//    private final Node folderIcon =
//            new ImageView(folderImage);
    private ImageView getFolderIcon(){
        return new ImageView(folderImage);
    }
    private Node getFileIcon(){
        return new ImageView(fileImage);
    }
    private final Image fileImage= new Image (NetworkStorageClient.class.getResourceAsStream("folder2.png"));
//    private final Node fileIcon =
//            new ImageView(fileImage);
//    CheckBoxTreeItem<Layer> tt2;
    public CheckBoxTreeCell<Layer> getNewCheckBoxTreeCell(){
        CheckBoxTreeCell<Layer> checkBoxTreeCell =new CheckBoxTreeCell<>(){
            @Override
            public void updateItem(Layer item, boolean empty) {
                if (this.getTreeItem() != null) {
                    this.setGraphic(this.getTreeItem().getGraphic());
                }
                else{
                    empty =true;
                }
                super.updateItem(item, empty);

                if (this.getTreeItem() != null) {
// как-то нужно додменить отображение чтобы картинкине исчезали
//                    this.setGraphic(this.getTreeItem().getGraphic());
                }

            }
        };
        return  checkBoxTreeCell;
    }
    public void initializeControl() {
        treeDir.setCellFactory(param -> getNewCheckBoxTreeCell()
        );

        Layer layer = new Layer("ROOT", false);
        TreeItem<Layer> tt = new TreeItem<>(layer);
        treeDir.setRoot(tt);
        tt.setExpanded(true);
        treeDir.setShowRoot(false);
        try {
            NetworkClient.getInstance().send(Command.getCommandGetDirList(""),this::commandParse);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        Layer layer0 = new Layer("Layers", false);
//        Layer layer2 = new Layer("Layers111", false);
//
//        Layer layer3 = new Layer("Layers333", false);
//         tt2 = new CheckBoxTreeItem<>(layer2);
//         tt2.setSelected(layer2.isSelected());
//        TreeItem<Layer> tt3 = new TreeItem<>(layer3);
//
//        TreeItem<Layer> tt = new TreeItem<>(layer0,folderIcon);
//        tt.getChildren().add(tt2);
//        tt.getChildren().add(tt2);
//        treeDir.setRoot(tt);
//        treeDir.getRoot().getChildren().setAll(tt2,tt3);
//        tt.setExpanded(true);
//        treeDir.setShowRoot(false);
//        tt.getValue().isSelected();
//        tt.getChildren().setAll(tt2,tt2);

    }
    private void commandParse(Command command){
        if (command.getType() == CommandType.DIR_LIST ){
             commandListDirParse((DirListCommandData) command.getData());
        }
    }
    String [] listDir ;
    private void commandListDirParse(DirListCommandData command){
        listDir = ("root/" + command.getDirName()).split("/");
        TreeItem<Layer> zzz = treeDir.getRoot();
        workItem(1, treeDir.getRoot(), command);
//        treeDir.getRoot()
//                .getChildren()
//                .stream()
//                .filter(layerTreeItem -> {return true;})
//                .forEach(layerTreeItem -> {
//                    System.out.println("ssssss");
//
//                } );
    }

    private void workItem(int x, TreeItem<Layer> layerTreeItem, DirListCommandData command) {
        if (x == listDir.length) {
                                         addTree(layerTreeItem, command);
                                   } else  {
                                      commandListDirParse(x, layerTreeItem, command);
                                   }
    }

    private void commandListDirParse(int item,TreeItem<Layer> treeItem, DirListCommandData command){
        Object [] iy = treeItem.getChildren().toArray();

        treeItem.getChildren()
                .stream()
                .filter(layerTreeItem -> layerTreeItem.getValue().getLayerName().equals(listDir[item]))
                .forEach(layerTreeItem -> {
                    workItem(item + 1, layerTreeItem, command);
                } );
    }
    private void addTree(TreeItem<Layer> treeItem, DirListCommandData command){
        treeItem.getChildren().clear();
        for (File file: command.getFiles()) {
         Layer layer = new Layer(file.getName(),false);
         TreeItem<Layer> treeItemAdd = new TreeItem<>(layer, (file.isDirectory() ?  getFolderIcon(): getFileIcon()));

//         treeItem.
         treeItem.getChildren().add(treeItemAdd);
         if (file.isDirectory()){
                try {
                    NetworkClient.getInstance().send(Command.getCommandGetDirList(getDir(treeItemAdd)),this::commandParse);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private String getDir (TreeItem<Layer> item){
        return getParentDir(item) + item.getValue().getLayerName();
    }

    private String getParentDir (TreeItem<Layer> item){
        TreeItem<Layer> itemParent = item.getParent();
        if (itemParent != null){
            if ( itemParent.getValue().getLayerName().equals("ROOT")){
                return  "";
            }
            return getParentDir(itemParent) + itemParent.getValue().getLayerName() + "/";
        }
        return  "";
    }

}