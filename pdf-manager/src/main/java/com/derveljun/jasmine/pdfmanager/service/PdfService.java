package com.derveljun.jasmine.pdfmanager.service;

import com.derveljun.jasmine.pdfmanager.fx.PdfMainController;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Component
public class PdfService {

    int totalCnt = 0;
    public void images2Pdf(String sourceDir, String targetDir, String targetPdfFileName) throws Exception {

        log.debug("\n\n -- Create New PDF --");
        File pdfFile = createPdf(targetDir, targetPdfFileName);
        PDDocument doc = PDDocument.load(pdfFile);

        Files.walk(Paths.get(targetDir))
                .filter(f -> f.endsWith("jpg"))
                .map(f -> {
                    File curFile = f.toFile();
                    Image curImg = null;
                    try {
                        curImg = ImageIO.read(curFile);
                        float imgWidth = curImg.getWidth(null);
                        float imgHeigth = curImg.getHeight(null);

                        // Fit a PDF Page by Image Height Length
                        PDImageXObject pdImage = PDImageXObject.createFromFileByContent(curFile, doc);
                        PDRectangle newRect = new PDRectangle(0, 0, imgWidth, imgHeigth);
                        PDPage newPage = new PDPage(newRect);
                        doc.addPage(newPage);

                        // Write a PDImageXObject to PDF
                        PDPageContentStream contents = new PDPageContentStream(doc, newPage);
                        contents.drawImage(pdImage, 0, 0, imgWidth, imgHeigth);
                        contents.close();
                        log.info("Page " + (totalCnt++) + " was Drawed at PDF File.");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                });

        // Saving the document
        doc.save(pdfFile);
        // Closing the document
        doc.close();
    }

    private File createPdf(String dirPath, String fileName) throws Exception {
        // Directory Check
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!dir.isDirectory()) {
            throw new Exception("dirPath is not dir");
        }
        // File Check
        File pdfFile = new File(dirPath + File.separator + fileName + ".pdf");
        if (pdfFile.exists()) {
            pdfFile.delete();
        }
        PDDocument document = new PDDocument();
        document.save(pdfFile);
        document.close();

        log.info("PDF created: " + fileName);

        return pdfFile;
    }

}
