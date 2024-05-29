package com.example.pdfviewer;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Controller
public class PdfViewerController {

    @FXML
    private TreeView<File> directoryTreeView;

    @FXML
    private TableView<FileDetails> fileTableView;

    @FXML
    private TableColumn<FileDetails, String> nameColumn;

    @FXML
    private TableColumn<FileDetails, String> dateModifiedColumn;

    @FXML
    private TableColumn<FileDetails, String> sizeColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));

        directoryTreeView.setRoot(createNode(new File(System.getProperty("user.home"))));

        directoryTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            TreeItem<File> selectedItem = directoryTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                displayFiles(selectedItem.getValue());
            }
        });
    }

    private TreeItem<File> createNode(final File file) {
        return new TreeItem<File>(file) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    isLeaf = getValue().isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(TreeItem<File> treeItem) {
                File f = treeItem.getValue();
                if (f != null && f.isDirectory()) {
                    File[] files = f.listFiles();
                    if (files != null) {
                        ObservableList<TreeItem<File>> children = FXCollections.observableArrayList();
                        for (File childFile : files) {
                            children.add(createNode(childFile));
                        }
                        return children;
                    }
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

    private void displayFiles(File directory) {
        fileTableView.getItems().clear();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileTableView.getItems().add(new FileDetails(file));
            }
        }
    }
}
