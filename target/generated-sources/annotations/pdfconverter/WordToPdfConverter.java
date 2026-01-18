package pdfconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class WordToPdfConverter {

    public static void convert(File word, File pdf) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdf));
        document.open();

        if (word.getName().endsWith(".doc")) {
            HWPFDocument doc = new HWPFDocument(new FileInputStream(word));
            Range range = doc.getRange();
            document.add(new Paragraph(range.text()));
        } else {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(word));
            for (XWPFParagraph p : doc.getParagraphs()) {
                document.add(new Paragraph(p.getText()));
            }
        }

        document.close();
    }
}
