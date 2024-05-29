package com.example.pdfviewer;

import com.itextpdf.kernel.pdf.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;

@Controller
public class PdfViewerController {

    @FXML
    private ListView<File> pdfListView;

    @FXML
    private Button convertButton;

    @FXML
    public void initialize() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select PDF Directory");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            File[] pdfFiles = selectedDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
            if (pdfFiles != null) {
                pdfListView.getItems().addAll(pdfFiles);
            }
        }
    }

    @FXML
    public void convertSelectedPdf() {
        File selectedFile = pdfListView.getSelectionModel().getSelectedItem();
        if (selectedFile != null) {
            try {
                String password = "yourpassword"; // Change this to dynamically set the password if needed
                convertToPasswordProtectedPdf(selectedFile, password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertToPasswordProtectedPdf(File file, String password) throws IOException {
        String dest = file.getParent() + File.separator + "protected_" + file.getName();
        PdfReader reader = new PdfReader(file.getAbsolutePath());
        WriterProperties props = new WriterProperties().setStandardEncryption(
                password.getBytes(), password.getBytes(),
                EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ENCRYPTION_AES_128);

        PdfWriter writer = new PdfWriter(dest, props);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        pdfDoc.close();
    }
}
