package pdfconverter;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

@Service
public class PdfConvertService {

    private static final String BASE_DIR = "D:/upload/";

    public JSONObject convert(MultipartFile file) throws Exception {

        String name = file.getOriginalFilename();
        String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

        File src = new File(BASE_DIR + name);
        file.transferTo(src);

        File pdf = new File(BASE_DIR + name + ".pdf");

        if ("xls".equals(ext) || "xlsx".equals(ext)) {
            ExcelToPdfConverter.convert(src, pdf);
        } else if ("doc".equals(ext) || "docx".equals(ext)) {
            WordToPdfConverter.convert(src, pdf);
        } else if ("ppt".equals(ext) || "pptx".equals(ext)) {
            PptToPdfConverter.convert(src, pdf);
        } else {
            throw new RuntimeException("지원하지 않는 형식");
        }

        JSONObject json = new JSONObject();
        json.put("originalFile", name);
        json.put("pdfFile", pdf.getName());
        json.put("size", pdf.length());
        json.put("status", "SUCCESS");

        return json;
    }
}
