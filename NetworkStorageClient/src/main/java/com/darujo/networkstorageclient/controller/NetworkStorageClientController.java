package com.darujo.networkstorageclient.controller;

import com.darujo.command.commanddata.*;
import com.darujo.command.object.PathFile;
import com.darujo.networkstorageclient.dialogs.Dialogs;
import com.darujo.command.Command;
import com.darujo.command.CommandType;
import com.darujo.networkstorageclient.NetworkStorageClient;
import com.darujo.networkstorageclient.fileworker.FileWorkClient;
import com.darujo.networkstorageclient.network.NetworkClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NetworkStorageClientController {
    @FXML
    public Button addButton;
    @FXML
    public Button delButton;
    @FXML
    public Button getButton;
    @FXML
    public TreeView<Layer> treeDir;
    final DirectoryChooser directoryChooser = new DirectoryChooser();
    final FileChooser fileChooser = new FileChooser();
    @FXML
    public MenuItem addShareMenu;
    @FXML
    public MenuItem getShareMenu;
    @FXML
    public Button addDirButton;
    @FXML
    public MenuItem viewGuidMenu;
    private File dirSave;

    @FXML
    public void addAction(ActionEvent actionEvent) {
        AddFiles();
    }


    @FXML
    public void addShare(ActionEvent actionEvent) {
        sendGetGuid();
    }

    @FXML
    public void getShare(ActionEvent actionEvent) {
        getShareSend();
    }


    @FXML
    public void addDirAction(ActionEvent actionEvent) {

        addDir();

    }

    @FXML
    public void delAction(ActionEvent actionEvent) {
        acceptCheckFile(treeDir.getRoot(), this::sendDeleteFile);
    }

    public void sendDeleteFile(TreeItem<Layer> item) {
        try {
            send(Command.getDelFileCommand(getPathDir(item)));
            getFolderList(getPathDir(item.getParent()));
        } catch (IOException e) {
            Platform.runLater(() -> showErrorMes(e.getMessage()));
        }
    }

    @FXML
    public void getAction(ActionEvent actionEvent) {
        dirSave = directoryChooser.showDialog(NetworkStorageClient.getInstance().getStorageStage());
        if (dirSave != null) {
            acceptCheckFile(treeDir.getRoot(), this::sendGetFile);
        } else {
            System.out.println("нет файла");
        }
    }

    private final PathFile emptyPathFile = new PathFile("");
    private final Image folderImage = new Image(NetworkStorageClient.class.getResourceAsStream("folder3.png"));

    private ImageView getFolderIcon() {
        return new ImageView(folderImage);
    }

    private Node getFileIcon() {
        ImageView imageView = new ImageView(fileImage);
        imageView.setFitHeight(16);
        imageView.setFitWidth(16);
        return imageView;
    }

    private final Image fileImage = new Image(NetworkStorageClient.class.getResourceAsStream("folder2.png"));

    //    public CheckBoxTreeCell<Layer> getNewCheckBoxTreeCell(){
//        return new CheckBoxTreeCell<>(){
//            @Override
//            public void updateItem(Layer item, boolean empty) {
//                if (this.getTreeItem() != null) {
//                    this.setGraphic(this.getTreeItem().getGraphic());
//                }
//                else{
//                    empty =true;
//                }
//                super.updateItem(item, empty);
//
//                if (this.getTreeItem() != null) {
//// как-то нужно подменить отображение чтобы картинки не исчезали
//                    this.setGraphic(this.getTreeItem().getGraphic());
//                }
//
//            }
//        };
//
//    }
    public void initializeControl() {
//        treeDir.setCellFactory(param -> getNewCheckBoxTreeCell()
//        );
//        treeDir.setCellFactory(new Callback<TreeView<Layer>, TreeCell<Layer>>() {
//
//            @Override
//            public TreeCell<Layer> call(TreeView<Layer> param) {
//
//                return new CheckBoxTreeCell<Layer>();
//            }
//        });
//        directoryChooser.setTitle("");
        treeDir.setCellFactory(CheckBoxTreeCell.<Layer>forTreeView());
        Layer layer = new Layer("ROOT", false, "");
        TreeItem<Layer> tt = new TreeItem<>(layer);
        treeDir.setRoot(tt);
        tt.setExpanded(true);
        treeDir.setShowRoot(false);

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

    private void acceptCheckFile(TreeItem<Layer> treeItem, Consumer<TreeItem<Layer>> callBack) {
        for (TreeItem<Layer> item : treeItem.getChildren()) {
            if (item.getValue().isFile()) {
                if (item instanceof CheckBoxTreeItem checkBoxTreeItem
                        && checkBoxTreeItem.selectedProperty().getValue()) {
                    callBack.accept(item);
//                    getFile.add(getDir(item));
                }
            } else {
                acceptCheckFile(item, callBack);
            }
        }
    }

    private void sendGetFile(TreeItem<Layer> item) {
        send(Command.getGetSendFileCommand(getPathDir(item)));
    }

    public void getFolderList() {
        getFolderList(emptyPathFile);
    }

    public void getFolderList(PathFile dirName) {
        send(Command.getGetDirListCommand(dirName));
    }

    public void sendFile(PathFile path, File file) {
        try {
            send(Command.getSendFileCommand(path, file));
        } catch (IOException e) {
            Platform.runLater(() -> showErrorMes(e.getMessage()));
        }
    }

    private void commandParse(Command command) {
        if (command.getType() == CommandType.DIR_LIST) {
            commandListDirParse((DirListCommandData) command.getData(), this::addTree);
        } else if (command.getType() == CommandType.FAILED_TOKEN) {
            Platform.runLater(() -> NetworkStorageClient.getInstance().authShow());
            showErrorMes(((MessageCommandData) command.getData()).getText());
        } else if (command.getType() == CommandType.SEND_FILE) {
            FileWorkClient.saveFile(dirSave.toPath(), (SendFileCommandData) command.getData(), this::showErrorMes);
        } else if (command.getType() == CommandType.ERROR_MESSAGE) {
            showErrorMes(((MessageCommandData) command.getData()).getText());
        } else if (command.getType() == CommandType.FILE_GUID) {
            commandListDirParse((FileNameCommandData) command.getData(), this::fileAddGuid);
        }
    }

    private String[] listDir;

    private void commandListDirParse(FileNameCommandData command, BiConsumer<TreeItem<Layer>, FileNameCommandData> callBack) {
        if (command.getFilePath().getGuid() == null) {
            listDir = ("root/" + command.getFilePath().getPath()).split("/");
            workItem(1, treeDir.getRoot(), command, callBack);
        } else {

            listDir = command.getFilePath().getPath().split("/");
            workItem(command.getFilePath().getPath().equals("") ? 1 : 0, searchItemForGuid(command.getFilePath().getGuid()), command, callBack);
        }
    }

    private TreeItem<Layer> searchItemForGuid(String guid) {
        return searchItemForGuid(treeDir.getRoot(), guid);
    }

    private TreeItem<Layer> searchItemForGuid(TreeItem<Layer> treeItem, String guid) {
        for (Object itemArr : treeItem.getChildren().toArray()) {
            TreeItem<Layer> itemArrOne = (TreeItem<Layer>) itemArr;
            if (itemArrOne == null) {
                return null;
            }
            if (itemArrOne.getValue().getGuid() == null || !itemArrOne.getValue().getGuid().equals(guid)) {
                itemArrOne = searchItemForGuid(itemArrOne, guid);
                if (itemArrOne != null) {
                    return itemArrOne;
                }
            } else {
                return itemArrOne;
            }
        }
        return null;
    }

    private void workItem(int level, TreeItem<Layer> layerTreeItem, FileNameCommandData command, BiConsumer<TreeItem<Layer>, FileNameCommandData> callback) {
        if (level == listDir.length) {
            callback.accept(layerTreeItem, command);
        } else {
            commandListDirParse(level, layerTreeItem, command, callback);

        }
    }

    private void commandListDirParse(int item, TreeItem<Layer> treeItem, FileNameCommandData command, BiConsumer<TreeItem<Layer>, FileNameCommandData> callback) {
//        Не понял из-за чег возникает ошибка конкуретного доступа
//        treeItem.getChildren()
//                .stream()
//                .filter(layerTreeItem -> layerTreeItem.getValue().getLayerName().equals(listDir[item]))
//                .forEach(layerTreeItem -> workItem(item + 1, layerTreeItem, command,callback));
        for (Object itemArr : treeItem.getChildren().toArray()) {
            TreeItem<Layer> itemArrOne = (TreeItem<Layer>) itemArr;
            if (itemArrOne.getValue().getLayerName().equals(listDir[item])) {
                workItem(item + 1, itemArrOne, command, callback);
            }

        }
    }

    private void fileAddGuid(TreeItem<Layer> treeItem, FileNameCommandData command) {
        if (command instanceof FileGuidCommandData) {
            fileAddGuid(treeItem, (FileGuidCommandData) command);
        }
    }

    private void fileAddGuid(TreeItem<Layer> treeItem, FileGuidCommandData command) {
        TreeItem<Layer> parentItem = treeItem.getParent();
        Layer layer = new Layer(treeItem.getValue().getLayerName(), treeItem.getValue().isFile(), command.getGuid());
        CheckBoxTreeItem<Layer> treeItemAdd = new CheckBoxTreeItem<>(layer, (layer.isFile() ? getFileIcon() : getFolderIcon()));
        parentItem.getChildren().remove(treeItem);
        parentItem.getChildren().add(treeItemAdd);
        getFolderList(getPathDir(treeItemAdd));


    }

    private void addTree(TreeItem<Layer> treeItem, FileNameCommandData command) {
        if (command instanceof DirListCommandData) {
            addTree(treeItem, (DirListCommandData) command);
        }
    }

    private void addTree(TreeItem<Layer> treeItem, DirListCommandData command) {
        treeItem.getChildren().clear();
        command.getFiles().forEach(entry -> {
                    Layer layer = new Layer(entry.getFileName(), entry.isFile(), entry.getGuid());
                    boolean flagCreate = true;
                    if (entry.getGuid() != null) {
                        if (treeItem == treeDir.getRoot()) {
                            flagCreate = (searchItemForGuid(entry.getGuid())==null);
                        } else {
                            delGuidForRoot(entry.getGuid());
                        }
                    }
                    if(flagCreate) {
                        CheckBoxTreeItem<Layer> treeItemAdd = new CheckBoxTreeItem<>(layer, (!entry.isFile() ? getFolderIcon() : getFileIcon()));
                        treeItem.getChildren().add(treeItemAdd);
                        if (!entry.isFile()) {
                            getFolderList(getPathDir(treeItemAdd));
                        }
                    }
                }
        );

    }

    private void delGuidForRoot(String guid) {
        treeDir.getRoot().getChildren().removeIf(item -> item.getValue().getGuid() != null && item.getValue().getGuid().equals(guid));
    }

    private PathFile getPathDir(TreeItem<Layer> item) {
        if (item.getValue().getGuid() == null) {
            return getParentPathDir(item, new PathFile(item.getValue().getLayerName()));

        } else {
            return new PathFile(item.getValue().getGuid(), "");
        }
    }

    private PathFile getParentPathDir(TreeItem<Layer> item, PathFile pathFile) {
        TreeItem<Layer> itemParent = item.getParent();
        if (itemParent != null) {
            if (itemParent == treeDir.getRoot()) {
                return pathFile;
            }
            if (itemParent.getValue().getGuid() == null) {
                pathFile.addPath(itemParent.getValue().getLayerName());
                pathFile = getParentPathDir(itemParent, pathFile);
            } else {
                pathFile.setGuid(itemParent.getValue().getGuid());
            }
            return pathFile;
        }
        return null;
    }

    private void send(Command command) {
        try {
            NetworkClient.getInstance().send(command, this::commandParse);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void showErrorMes(String text) {
        Platform.runLater(() -> Dialogs.showDialog(Alert.AlertType.ERROR, "Ошибка", "Ошибка", text));
    }

    private void setClipboard(String str) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(str);
        clipboard.setContent(content);
    }

    private String editText(String title, String text, String label, String initValue) {
        TextInputDialog dialog = new TextInputDialog(initValue);
        dialog.setTitle(title);
        dialog.setHeaderText(text);
        dialog.setContentText(label);


        Optional<String> result = dialog.showAndWait();
        String s = result.orElse(null);

        System.out.println(s);
        return s;
    }

    private void addDir() {
        TreeItem<Layer> dirItem = null;
        for (TreeItem<Layer> item : treeDir.getSelectionModel().getSelectedItems()) {
            dirItem = item.getValue().isFile() ? item.getParent() : item;
        }
        if (dirItem != null) {
            String newDir = editText("Создане директории", "Создать директорию в " + getPathDir(dirItem), "Новая директория", "");
            if (!newDir.isEmpty() && !newDir.isBlank()) {
                PathFile dirPath = getPathDir(dirItem);
                dirPath.addPath(newDir);
                sendFile(dirPath, null);
                getFolderList(dirPath);
            }
        } else {
            dirItem = treeDir.getRoot();
            String newDir = editText("Создание директории", "Создать директорию в " + getPathDir(dirItem), "Новая директория", "");
            if (!newDir.isEmpty() && !newDir.isBlank()) {
                sendFile(new PathFile(newDir), null);
                getFolderList(emptyPathFile);
            }
        }


    }

    private void AddFiles() {
        List<File> files = fileChooser.showOpenMultipleDialog(NetworkStorageClient.getInstance().getStorageStage());
        if (treeDir.getSelectionModel().getSelectedItems().size() == 0) {
            files.forEach(file -> {
                sendFile(emptyPathFile, file);
                getFolderList(emptyPathFile);
            });
        } else {
            for (TreeItem<Layer> item : treeDir.getSelectionModel().getSelectedItems()) {
                PathFile dir = item.getValue().isFile() ? getPathDir(item.getParent()) : getPathDir(item);
                files.forEach(file -> {
                    sendFile(dir, file);
                    getFolderList(dir);
                });
            }
        }
    }

    private void sendGetGuid() {
        if (treeDir.getSelectionModel().getSelectedItems().size() == 0) {
            showErrorMes("Не выбрана дириктория или файл");
        } else {
            for (TreeItem<Layer> item : treeDir.getSelectionModel().getSelectedItems()) {
                send(Command.getGetNewGuidCommand(getPathDir(item)));
            }
        }
    }

    private void viewGuid() {

        Platform.runLater(() -> {
            for (TreeItem<Layer> item : treeDir.getSelectionModel().getSelectedItems()) {
                String guid = editText("Просмотр GUID", "Скопировать GUID для директории " + getPathDir(item) + " в буфер обмена?", "GUID:", item.getValue().getGuid());
                if (!guid.isEmpty() && !guid.isBlank()) {
                    setClipboard(guid);
                }
            }
        });
    }

    @FXML
    public void viewGuidAction(ActionEvent actionEvent) {
        viewGuid();
    }

    private void getShareSend() {
        Platform.runLater(() -> {
            String guid = editText("Получение доступа", "Введите GUID доступа", "GUID:", "");
            if (!guid.isEmpty() && !guid.isBlank()) {
                send(Command.getAddGuidCommand(guid));
                send(Command.getGetDirListCommand(emptyPathFile));
            }

        });
    }
}