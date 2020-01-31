package com.derveljun.jasmine.pdfmanager.fx;

import com.derveljun.jasmine.pdfmanager.service.PdfService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Data
public class PdfMainController implements Initializable {

    private ConfigurableApplicationContext context;
    private Stage stage;

    @FXML
    private Button btnSourceDirSearch,
                btnTargetDirSearch,
                btnSave;

    @FXML
    private TextField txtSourceDir, txtTargetDir, txtFileName;

    @FXML
    public TextArea txtAreaLog;

    @Autowired
    private PdfService pdfService;
    private Alert alertWarning;
    private DirectoryChooser directoryChooser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        alertWarning = new Alert(Alert.AlertType.WARNING);
        directoryChooser = new DirectoryChooser();

        btnSourceDirSearch.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);
            txtSourceDir.setText(selectedFile.getAbsolutePath());
        });

        btnTargetDirSearch.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);
            txtTargetDir.setText(selectedFile.getAbsolutePath());
        });

        btnSave.setOnAction(e -> {

            String sourceDir = txtSourceDir.getText();
            String targetDir = txtTargetDir.getText();
            String fileName = txtFileName.getText();

            if (StringUtils.isEmpty(sourceDir)
                    || StringUtils.isEmpty(targetDir)
                    || StringUtils.isEmpty(fileName)) {
                alert("Select a directory or write a File name");
                return;
            }

            try {
                pdfService.images2Pdf(sourceDir, targetDir, fileName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void alert(String content) {
        alertWarning.setContentText(content);
        alertWarning.show();
    }
}
