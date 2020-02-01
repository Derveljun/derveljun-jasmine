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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Data
@Service
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

    private PdfService pdfService;
    private Alert alertWarning;
    private DirectoryChooser directoryChooser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pdfService = new PdfService();
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

                // TODO rxJava로 바꾸기
                // TODO 완료 표시처리
                // TODO 진행 표시
                // TODO 폴더 리스트로 변경

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
