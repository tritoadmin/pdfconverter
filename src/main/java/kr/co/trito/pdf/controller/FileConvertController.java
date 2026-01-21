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
import kr.co.trito.pdf.util.HmacTokenUtil;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/convert")
public class FileConvertController {

    @Autowired
    private PdfConvertService service;

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/file2pdf.do", method=RequestMethod.POST)
    @ResponseBody
    public String convert(@RequestParam("file") MultipartFile file) throws Exception {
        JSONObject result = service.convertFile(file);
        return result.toString();
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/param2pdf.do", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
    @ResponseBody
    public String convert2(@RequestParam Map<String,Object> params) throws Exception {
    	JSONObject result = service.convertParam(params);
    	return result.toString();
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/fileWithparam2pdf.do", method=RequestMethod.POST, produces="application/json; charset=UTF-8")
    @ResponseBody
    public String convert3(@RequestParam("file") MultipartFile file, @RequestParam("gbn") String gbn) throws Exception {
    	JSONObject result = service.convertFileWithParam(file, gbn);
    	return result.toString();
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/fileWithparam2pdf.do", method=RequestMethod.GET)
    @ResponseBody
    public String generateToken(@RequestParam("userId") String userId) throws Exception {

    	String data = userId+"&expire=1700000000";
    	String token = HmacTokenUtil.generate(data);

    	JSONObject json = new JSONObject();
    	json.put("token", token);

    	return json.toString();

    }
}
