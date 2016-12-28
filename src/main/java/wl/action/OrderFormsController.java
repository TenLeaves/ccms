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

import wl.service.OrderFormsService;

import com.alibaba.fastjson.JSON;

@Controller
public class OrderFormsController {
    @Autowired
    @Qualifier("orderFormsService")
    private OrderFormsService orderFormsService;
    
    @RequestMapping(value = "/checkOrderFormsPage", method = RequestMethod.POST)
    public String toSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(5);
        eqlPage.setTotalRows(0); 
        
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		
		List<Map> result=orderFormsService.appCheckNew(userMap, eqlPage);
		
		model.addAttribute("results", result);
		HttpSession session=request.getSession();
		session.setAttribute("userMap", userMap);
    	return "pages/orderForm/orderFormslist";
    }
    //分页查询用户信息
    @RequestMapping(value = "/checkOrderForms", method = RequestMethod.POST)
    public String toPageSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderFormsService.appCheckNew(userMap, eqlPage);
		
		model.addAttribute("results", result);
    	return "pages/orderForm/orderFormslist";
    }
  //全量导出
    @RequestMapping(value = "exportAllOrderForms",method=RequestMethod.POST)
    public void toexportAllOrderOut(HttpServletRequest request,HttpServletResponse response) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderFormsService.appCheckNew(userMap);
		ExportOrderFormsController export=new ExportOrderFormsController();
		export.exportSendList(response, request, result);
    }
  //查询订单总数
    @RequestMapping(value = "/checkOrderFormsSum", method = RequestMethod.POST)
    @ResponseBody
    public Map toPageSubmit(HttpServletRequest request,Model model) {
    	HttpSession session=request.getSession();
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		Map result=orderFormsService.appCheck(userMap);
		
		return result;
    }
    
    @RequestMapping(value = "/checkSendFormsPage", method = RequestMethod.POST)
    public String toSendSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(5);
        eqlPage.setTotalRows(0); 
        
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo1"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		
		List<Map> result=orderFormsService.appSendCheckNew(userMap, eqlPage);
		
		model.addAttribute("results", result);
		HttpSession session=request.getSession();
		session.setAttribute("userMap", userMap);
    	return "pages/orderForm/sendFormslist";
    }
    //分页查询用户信息
    @RequestMapping(value = "/checkSendForms", method = RequestMethod.POST)
    public String toSendPageSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderFormsService.appSendCheckNew(userMap, eqlPage);
		
		model.addAttribute("results", result);
    	return "pages/orderForm/sendFormslist";
    }
  //全量导出
    @RequestMapping(value = "exportAllSendForms",method=RequestMethod.POST)
    public void toexportAllSendOut(HttpServletRequest request,HttpServletResponse response) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderFormsService.appSendCheckNew(userMap);
		ExportSendFormsController export=new ExportSendFormsController();
		export.exportSendList(response, request, result);
    }
  //查询订单总数
    @RequestMapping(value = "/checkSendFormsSum", method = RequestMethod.POST)
    @ResponseBody
    public int toSendPageSubmit(HttpServletRequest request,Model model) {
    	HttpSession session=request.getSession();
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo1"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		int result=orderFormsService.appSendCheck(userMap);
		
		return result;
    }
  
    @RequestMapping(value = "/checkDetailFormsPage", method = RequestMethod.POST)
    public String toDetailSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(5);
        eqlPage.setTotalRows(0); 
        
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo2"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		
		List<Map> result=orderFormsService.appDetailCheckNew(userMap, eqlPage);
		
		model.addAttribute("results", result);
		HttpSession session=request.getSession();
		session.setAttribute("userMap", userMap);
    	return "pages/orderForm/orderDetailFormslist";
    }
    //分页查询用户信息
    @RequestMapping(value = "/checkDetailForms", method = RequestMethod.POST)
    public String toDetailPageSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderFormsService.appDetailCheckNew(userMap, eqlPage);
		
		model.addAttribute("results", result);
    	return "pages/orderForm/orderDetailFormslist";
    }
    //全量导出
    @RequestMapping(value = "exportAllDetailForms",method=RequestMethod.POST)
    public void toexportAllDetailOut(HttpServletRequest request,HttpServletResponse response) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderFormsService.appDetailCheckNew(userMap);
		ExportDetailFormsController export=new ExportDetailFormsController();
		export.exportSendList(response, request, result);
    }
  //查询订单总数
    @RequestMapping(value = "/checkDetailFormsSum", method = RequestMethod.POST)
    @ResponseBody
    public int toDetailPageSubmit(HttpServletRequest request,Model model) {
    	HttpSession session=request.getSession();
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo2"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		int result=orderFormsService.appDetailCheck(userMap);
		
		return result;
    }
    
  

    /**
     * 该方法会在该controler所有的的方法执行器执行这个函数在model中添加了 eqlPage对象
     * @param eqlPage
     * @return
     */
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
