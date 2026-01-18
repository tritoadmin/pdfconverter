package pdfconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class ExcelToPdfConverter {

    public static void convert(File excel, File pdf) throws Exception {

        Workbook wb = WorkbookFactory.create(new FileInputStream(excel));
        Sheet sheet = wb.getSheetAt(0);

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(pdf));
        doc.open();

        for (Row row : sheet) {
            StringBuffer sb = new StringBuffer();
            for (Cell cell : row) {
                sb.append(cell.toString()).append(" | ");
            }
            doc.add(new Paragraph(sb.toString()));
        }

        doc.close();
//        wb.close();
    }
}
