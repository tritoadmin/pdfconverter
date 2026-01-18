package kr.co.trito.pdf.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;

import com.aspose.cells.FileFormatType;
import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.words.Document;

public class ConverterUtil {


	public static void pptConvert(File pptFile, File pdf) {

		Presentation pres = null;

		try {

			/** PPT for Java License setting */
			com.aspose.slides.License license = new com.aspose.slides.License();
			File localFile = ResourceUtils.getFile("classpath:Aspose.Slides.lic");
			InputStream in = new FileInputStream(localFile);
			license.setLicense(in);

			pres = new Presentation(pdf.getAbsolutePath());
			pres.save(pdf.getPath(), SaveFormat.Pdf);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
		    if (pres != null) pres.dispose();
		}

	}

	public static void excelConvert(File excelFile, File pdf) {

		Workbook workbook = null;

		try {

			/** excel for Java License setting */
			com.aspose.cells.License license = new com.aspose.cells.License();
			File localFile = ResourceUtils.getFile("classpath:Aspose.Cells.lic");
			InputStream in = new FileInputStream(localFile);
			license.setLicense(in);

			workbook = new Workbook(excelFile.getAbsolutePath());
			workbook.save(pdf.getPath(), FileFormatType.PDF);

		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) workbook.dispose();
		}

	}

	public static void wordConvert(File wordFile, File pdf) {

		Document word = null;

		try {

			/** word for Java License setting */
			com.aspose.words.License license = new com.aspose.words.License();
			File localFile = ResourceUtils.getFile("classpath:Aspose.Words.lic");
			InputStream in = new FileInputStream(localFile);
			license.setLicense(in);
			word = new Document(wordFile.getPath());

			word.save(pdf.getPath(), com.aspose.words.SaveFormat.PDF);

		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			//
		}

	}


}
