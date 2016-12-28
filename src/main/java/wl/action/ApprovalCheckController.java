package wl.action;

import java.util.HashMap;
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

import wl.service.ApprovalCheckService;

@Controller
public class ApprovalCheckController {
    @Autowired
    @Qualifier("approvalCheckService")
    private ApprovalCheckService approvalCheckService;
	
    @RequestMapping(value = "/approvalCheckInfo", method = RequestMethod.POST)
    public String toSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); 
        
    	Map map=request.getParameterMap();
		String approvalState=((String[]) map.get("approvalState"))[0];
		String approvalType=((String[]) map.get("approvalType"))[0];
		Map appMap=new HashMap();
		appMap.put("approvalState", approvalState);
		appMap.put("approvalType", approvalType);
	    HttpSession session=request.getSession();
	    session.setAttribute("userMap", appMap);
		List<Map> custList = approvalCheckService.appCheck(approvalState,approvalType,session,eqlPage);
		
		model.addAttribute("results", custList);
    	
		return "pages/approval/approvallist";
    }
    
    @RequestMapping(value = "/approvalInfo", method = RequestMethod.POST)
    public String toApproval(HttpServletRequest request, EqlPage eqlPage,Model model) {
		Map appMap=new HashMap();
	
	    HttpSession session=request.getSession();
	    appMap=(Map) session.getAttribute("userMap");
		String approvalState=(String) appMap.get("approvalState");
		String approvalType=(String) appMap.get("approvalType");

		List<Map> custList = approvalCheckService.appCheck(approvalState,approvalType,session,eqlPage);
		
		model.addAttribute("results", custList);
    	
		return "pages/approval/approvallist";
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
