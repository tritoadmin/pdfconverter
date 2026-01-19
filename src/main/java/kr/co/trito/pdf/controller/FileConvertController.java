package kr.co.trito.pdf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.trito.pdf.service.PdfConvertService;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/convert")
public class FileConvertController {

    @Autowired
    private PdfConvertService service;

    @RequestMapping(value="/file2pdf.do", method=RequestMethod.POST)
    @ResponseBody
    public String convert(@RequestParam("file") MultipartFile file) throws Exception {
        JSONObject result = service.convertFile(file);
        return result.toString();
    }

    @RequestMapping(value="/param2pdf.do", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
    @ResponseBody
    public String convert(@RequestParam Map<String,Object> params) throws Exception {

    	JSONObject result = service.convertParam(params);

    	return result.toString();
    }
}
