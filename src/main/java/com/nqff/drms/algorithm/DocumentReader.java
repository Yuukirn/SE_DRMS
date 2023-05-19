package com.nqff.drms.algorithm;

import com.nqff.drms.pojo.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class DocumentReader {
    public static String readDocument(Document document) {
        String path = document.getFilePath();
        switch (document.getType()) {
            case 1:
                return readDOC(path);
            case 2:
                return readDOCX(path);
            case 3:
                return readPDF(path);
            case 4:
                return readTXT(path);
            default:
                return null;
        }
    }

    public static String readDOC(String path) {
        File file = new File(path);
        String text = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            String doc1 = doc.getDocumentText();
//            System.out.println(doc1);
            StringBuilder doc2 = doc.getText();
//            System.out.println(doc2);
            Range rang = doc.getRange();
            String doc3 = rang.text();
//            System.out.println(doc3);
            fis.close();
            text = doc1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String readDOCX(String path) {
        File file = new File(path);
        String text = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String doc1 = extractor.getText();
//            System.out.println(doc1);
            fis.close();
            text = doc1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String readPDF(String path) {
        File file = new File(path);
        String text = "";
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String readTXT(String path) {
        Path filePath = Paths.get(path);
        String text = "";
        try {
            text = Files.readString(filePath);
//            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

}
