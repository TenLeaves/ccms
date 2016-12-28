package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.service.LeaderFormsService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

@Controller
public class LeaderFormsController {
    @Autowired
    @Qualifier("leaderFormsService")
    private LeaderFormsService leaderFormsService;
    
    @RequestMapping(value = "/leaderFormsAction")
    public String toSubmit(HttpServletRequest request,Model model,String year) {
    	List<Map> result=Lists.newArrayList();
    	try{
    		result=leaderFormsService.appCheck(year);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	model.addAttribute("results", result);
    	return "pages/leaderForm/leader_formsList";
    }
    
  
}
