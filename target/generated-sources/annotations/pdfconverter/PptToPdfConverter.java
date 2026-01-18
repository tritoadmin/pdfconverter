package pdfconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hslf.HSLFSlideShow;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PptToPdfConverter {

    public static void convert(File ppt, File pdf) throws Exception {
        HSLFSlideShow pptShow = new HSLFSlideShow(new FileInputStream(ppt));
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdf));
        document.open();

        for (HSLFSlide slide : pptShow.getSlides()) {

        	pptShow.

            document.add(new Paragraph("Slide"));
            document.newPage();
        }

        document.close();
    }
}
