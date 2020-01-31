package com.derveljun.jasmine.pdfmanager;

import com.derveljun.jasmine.pdfmanager.fx.PdfMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PdfManagerApplication extends Application {

    private String[] args;
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
//        args = args;
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        context = SpringApplication.run(PdfManagerApplication.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/PdfMain.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 275));
        primaryStage.show();

        PdfMainController controller = new PdfMainController();
        controller.setStage(primaryStage);
        controller.setContext(context);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
    }
}
