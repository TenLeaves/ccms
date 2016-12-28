package wl.action;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

@Controller
public class CheckCodeController {
    @Autowired  
    private Producer captchaProducer = null;  
    private Log log = LogFactory.getLog(this.getClass());
  
    @RequestMapping(value="/checkcode",method={RequestMethod.POST,RequestMethod.GET})  
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        HttpSession session = request.getSession();  
        response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");  
        String capText = captchaProducer.createText();  
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);  
        log.info("******************验证码是: " + capText);  
        BufferedImage bi = captchaProducer.createImage(capText);  
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
    } 
    //检验验证码
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public String doCheckNum(String num,HttpServletRequest request) throws Exception
    {
    	HttpSession  session = request.getSession(); 
    	if(num.equalsIgnoreCase((String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY))){
    		return "success";
    	}
        return "fail";
    }
}
