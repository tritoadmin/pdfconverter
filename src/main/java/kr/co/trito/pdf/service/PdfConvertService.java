package kr.co.trito.pdf.service;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.trito.pdf.converter.ConverterUtil;
import kr.co.trito.pdf.util.FileCommonUtil;
import net.sf.json.JSONObject;

@Service
public class PdfConvertService {

	@Value("${upload.dir}")
    private String BASE_DIR;

    public JSONObject convertFile(MultipartFile file) throws Exception {

        String name 	= file.getOriginalFilename();
        String fileName = FileCommonUtil.getFileName(name);
        String ext 		= FileCommonUtil.getExtension(name);
        String saveName = FileCommonUtil.generateUniqueFileName(name);

        File src = new File(BASE_DIR + saveName);
        file.transferTo(src);

        File pdf = new File(BASE_DIR + fileName + ".pdf");

        if ("xls".equals(ext) || "xlsx".equals(ext)) {
            ConverterUtil.excelConvert(src, pdf);
        } else if ("doc".equals(ext) || "docx".equals(ext)) {
        	ConverterUtil.wordConvert(src, pdf);
        } else if ("ppt".equals(ext) || "pptx".equals(ext)) {
        	ConverterUtil.pptConvert(src, pdf);
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

    public JSONObject convertParam(Map<String,Object> params) throws Exception {

    	String gbn 		= (String) params.get("gbn");
    	String name 	= (String) params.get("originalFileName");
    	String filePath = (String) params.get("filePath");

    	String fileName = name.substring(0, name.lastIndexOf("."));
    	String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

    	File src = new File(filePath);

    	File dir = new File(BASE_DIR + "\\" + gbn );

    	if( !dir.exists()) {
    		dir.mkdir();
    	}

    	File pdf = new File(BASE_DIR + "\\" + gbn + "\\" + fileName + ".pdf");

    	if ("xls".equals(ext) || "xlsx".equals(ext)) {
    		ConverterUtil.excelConvert(src, pdf);
    	} else if ("doc".equals(ext) || "docx".equals(ext)) {
    		ConverterUtil.wordConvert(src, pdf);
    	} else if ("ppt".equals(ext) || "pptx".equals(ext)) {
    		ConverterUtil.pptConvert(src, pdf);
    	} else {
    		throw new RuntimeException("지원하지 않는 형식");
    	}

    	JSONObject json = new JSONObject();
    	json.put("gbn"			, gbn);
    	json.put("filePath"		, filePath);
    	json.put("originalFile"	, name);
    	json.put("pdfFile"		, pdf.getPath());
    	json.put("size"			, pdf.length());
    	json.put("status"		, "SUCCESS");

    	return json;
    }

    public JSONObject convertFileWithParam(MultipartFile file, String gbn) throws Exception {

    	String name 	= file.getOriginalFilename();
        String fileName = FileCommonUtil.getFileName(name);
        String ext 		= FileCommonUtil.getExtension(name);
        String saveName = FileCommonUtil.generateUniqueFileName(fileName);
        String saveFileName = FileCommonUtil.generateUniqueFileName(saveName);

    	File dir = new File(BASE_DIR + "\\" + gbn );

    	if( !dir.exists()) {
    		dir.mkdir();
    	}

    	File src = new File(BASE_DIR + "\\" + gbn + "\\" + saveName);
    	file.transferTo(src);

//    	File pdf = new File(BASE_DIR + "\\" + gbn + "\\" + saveFileName + ".pdf");
    	File pdf = new File(BASE_DIR + "\\" + gbn + "\\" + saveFileName );

    	if ("xls".equals(ext) || "xlsx".equals(ext)) {
    		ConverterUtil.excelConvert(src, pdf);
    	} else if ("doc".equals(ext) || "docx".equals(ext)) {
    		ConverterUtil.wordConvert(src, pdf);
    	} else if ("ppt".equals(ext) || "pptx".equals(ext)) {
    		ConverterUtil.pptConvert(src, pdf);
    	} else {
    		throw new RuntimeException("지원하지 않는 형식");
    	}

    	JSONObject json = new JSONObject();
    	json.put("gbn"			, gbn);
    	json.put("filePath"		, dir.getAbsolutePath());
    	json.put("originalFile"	, name);
    	json.put("originalExt"	, ext);
    	json.put("saveFile"	    , saveName);
    	json.put("pdfFile"		, pdf.getPath());
    	json.put("size"			, pdf.length());
    	json.put("status"		, "SUCCESS");

    	return json;
    }
}
