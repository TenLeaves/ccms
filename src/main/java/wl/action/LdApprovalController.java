package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import wl.service.LdApprovalService;

@Controller
public class LdApprovalController {
    @Autowired
    @Qualifier("ldapprovalService")
    private LdApprovalService ldapprovalService;
    
    /**
     * 订单审批
     * @param request
     * @param eqlPage
     * @param model
     * @return
     */
    @RequestMapping(value = "/ldapprovalCheckInfo", method = RequestMethod.POST)
    public String toSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); 
        
		List<Map> custList = ldapprovalService.appCheck(eqlPage);
		model.addAttribute("results", custList);
		return "pages/ldapproval/ldapprovallist";
    }
    /**
     * 印刷但申请 审批
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/ldapprovalStockCheckInfo", method = RequestMethod.POST)
    public String toStockSubmit(HttpServletRequest request,Model model) {
        
		List<Map> custList = ldapprovalService.appStockCheck();
		model.addAttribute("results", custList);
		return "pages/ldapproval/ldapprovalStocklist";
    }
    
    @RequestMapping(value = "/ldapprovalInfo", method = RequestMethod.POST)
    public String toApproval(HttpServletRequest request, EqlPage eqlPage,Model model) {

		List<Map> custList = ldapprovalService.appCheck(eqlPage);
		
		model.addAttribute("results", custList);
    	
		return "pages/ldapproval/ldapprovallist";
    }
    
    @RequestMapping(value = "/updateLdAppInfo", method = RequestMethod.POST)
    public String toUpdateLdAppInfo(HttpServletRequest request,EqlPage eqlPage,Model model) throws Exception {
		HttpSession session=request.getSession();
		Map map=request.getParameterMap();
		String user=((String[])map.get("user"))[0];
		Map userMap=(Map) JSON.parse(user);
		if("末付款先打印发票".equals(userMap.get("name"))){
			ldapprovalService.updateAppSucc1((String) userMap.get("key"), session);
		}else{
			ldapprovalService.updateAppSucc((String) userMap.get("key"),session);
		}
		List<Map> custList = ldapprovalService.appCheck(eqlPage);
		model.addAttribute("results", custList);
    	
		return "pages/ldapproval/ldapprovallist";
    }
    
    @RequestMapping(value = "/updateStockLdAppInfo", method = RequestMethod.POST)
    public String toUpdateStockLdAppInfo(HttpServletRequest request,Model model) {
    	HttpSession session=request.getSession();
		Map map=request.getParameterMap();
		String user=((String[])map.get("user"))[0];
		Map userMap=(Map) JSON.parse(user);
		ldapprovalService.updateStockAppSucc((String) userMap.get("key"), session);
		List<Map> custList = ldapprovalService.appStockCheck();
		model.addAttribute("results", custList);
    	
		return "pages/ldapproval/ldapprovalStocklist";
    }
    
    @RequestMapping(value = "/updateErrAppInfo", method = RequestMethod.POST)
    public String toUpdateErrAppInfo(HttpServletRequest request,EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map map=request.getParameterMap();
		String user=((String[])map.get("user"))[0];
		Map userMap=(Map) JSON.parse(user);
		if("末付款先打印发票".equals(userMap.get("name"))){
			ldapprovalService.updateAppErr1((String)userMap.get("key"),(String)userMap.get("boxInfo"), session);
		}else{
			ldapprovalService.updateAppErr((String)userMap.get("key"),(String)userMap.get("boxInfo"), session);
		}
		List<Map> custList = ldapprovalService.appCheck(eqlPage);
		model.addAttribute("results", custList);
    	
		return "pages/ldapproval/ldapprovallist";
    }
    @RequestMapping(value = "/updateStockErrAppInfo", method = RequestMethod.POST)
    public String toUpdateStockErrAppInfo(HttpServletRequest request,Model model) {
    	HttpSession session=request.getSession();
		Map map=request.getParameterMap();
		String user=((String[])map.get("user"))[0];
		Map userMap=(Map) JSON.parse(user);
		ldapprovalService.updateStockAppErr((String)userMap.get("key"),(String)userMap.get("boxInfo"), session);
		List<Map> custList = ldapprovalService.appStockCheck();
		model.addAttribute("results", custList);
    	
		return "pages/ldapproval/ldapprovalStocklist";
    }
    
    /**
     * 该方法会在该controler所有的的方法执行器执行这个函数在model中添加了 eqlPage对象
     * @param eqlPage
     * @return
     */
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(10);
        return eqlPage;
    }
}
