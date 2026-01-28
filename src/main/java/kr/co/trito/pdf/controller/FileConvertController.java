package kr.co.trito.pdf.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.trito.pdf.service.PdfConvertService;
import kr.co.trito.pdf.util.EncryptTokenGenerator;
import kr.co.trito.pdf.util.EncryptTokenParser;
import kr.co.trito.pdf.util.EncryptTokenValidator;
import kr.co.trito.pdf.util.HmacUtil;
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
    public String convert3(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String,Object> params) throws Exception {

//    	String token = "";
//    	String auth = request.getHeader("Authorization");
//
//    	if (auth != null && auth.startsWith("Bearer ")) {
//    	    token = auth.substring(7);
//    	}
//
//
//    	boolean isValidToken = true;
//    	// 요청 시 검증
//    	if (!EncryptTokenValidator.validate(token)) {
//    		isValidToken = false;
//    		response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 금지");
//    	}


//    	String userId = EncryptTokenParser.getUserId(token);

    	String gbn = (String)params.get("gbn");

    	JSONObject result = service.convertFileWithParam(file, gbn);
//    	result.put("token", token);
//    	result.put("userId", userId);
//    	result.put("isValidToken", isValidToken);

    	return result.toString();
    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/token.do", method=RequestMethod.GET)
    @ResponseBody
    public String generateToken(@RequestParam("userId") String userId) throws Exception {

    	String token = EncryptTokenGenerator.generate(userId, 3600);

    	JSONObject json = new JSONObject();
    	json.put("token", token);

    	return json.toString();

    }

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/authKey.do", method=RequestMethod.GET)
    @ResponseBody
    public String generateAuthKey(@RequestParam("accessKey") String accessKey, @RequestParam("secretKey") String secretKey, @RequestParam("apiUrl") String apiUrl) throws Exception {

		String timestamp = String.valueOf(System.currentTimeMillis());

		String data = "GET\n" + apiUrl + "\n" + timestamp;
		String signature = HmacUtil.generate(data, secretKey);

    	JSONObject json = new JSONObject();
    	json.put("accessKey", accessKey);
    	json.put("secretKey", secretKey);
    	json.put("timestamp", timestamp);
    	json.put("signature", signature);

    	return json.toString();

    }



    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Java 1.6 HMAC 인증 성공";
    }

}
