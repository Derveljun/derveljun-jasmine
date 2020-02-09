package com.derveljun.jasmine.pdfmanager.client;

import com.derveljun.jasmine.pdfmanager.service.PdfService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Data;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Data
@Service
public class PdfMainController implements Initializable {

    private ConfigurableApplicationContext context;
    private Stage stage;

    @FXML
    private ListView lvTargetDirs;

    @FXML
    private Button btnTargetDirSearch, btnSave, btnDirsChoose;

    @FXML
    private TextField txtTargetDir;

    private PdfService pdfService;
    private Alert alertWarning;
    private DirectoryChooser directoryChooser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pdfService = new PdfService();
        alertWarning = new Alert(Alert.AlertType.INFORMATION);
        directoryChooser = new DirectoryChooser();

        btnTargetDirSearch.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);

            if (selectedFile != null)
                txtTargetDir.setText(selectedFile.toString());
        });

        btnDirsChoose.setOnAction(e -> {
            File selectedFile = directoryChooser.showDialog(stage);
            try {
                List<String> dirs = Files.walk(Paths.get(selectedFile.getAbsolutePath()))
                        .filter(Files::isDirectory)
                        .map(f -> f.toString())
                        .collect(Collectors.toList());
                if (dirs == null || dirs.isEmpty() || dirs.size() == 1) {
                    alert("선택 된 폴더가 없습니다.");
                    return;
                }

                dirs.remove(0);
                lvTargetDirs.getItems().removeAll();
                lvTargetDirs.getItems().addAll(dirs);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnSave.setOnAction(e -> {

            String targetDir = txtTargetDir.getText();
            List<String> dirs = lvTargetDirs.getItems();

            try {
                for (String sourceDir : dirs) {
                    String[] splt = sourceDir.split("\\\\");
                    String pdfFileName = splt[splt.length - 1];

                    pdfService.images2Pdf(sourceDir, targetDir, pdfFileName);
                }

                // TODO rxJava로 바꾸기
                // TODO 진행 표시 : Listview 옆에 진행률 표시
                // TODO 완료 표시처리
                // TODO 폴더 리스트로 변경
                alert("변환 완료");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void alert(String content) {
        alertWarning.setContentText(content);
        alertWarning.show();
    }

    public static void main(String[] args) {
        System.out.println("D:\\test\\모던 자바 인 액션 [라울-게이브리얼 우르마, 마리오 푸스코, 앨런 마이크로프트, (우정은)][한빛미디어]");

        String[] split = "D:\\test\\모던 자바 인 액션 [라울-게이브리얼 우르마, 마리오 푸스코, 앨런 마이크로프트, (우정은)][한빛미디어]".split("\\\\");
    }
}
