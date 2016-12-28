package wl.action;

import java.util.ArrayList;
import java.util.HashMap;
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

import wl.service.OrderOutService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

@Controller
public class OrderOutController {
    @Autowired
    @Qualifier("orderOutService")
    private OrderOutService orderOutService;
    
    @RequestMapping(value = "/selOrderOutInfo", method = RequestMethod.POST)
    public String toSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(5);
        eqlPage.setTotalRows(0); 
        
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		
		List<Map> result=Lists.newArrayList();
		try{
			result=orderOutService.appCheck(userMap, eqlPage);
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("results", result);
		HttpSession session=request.getSession();
		session.setAttribute("userMap", userMap);
    	return "pages/orderOut/orderOutlist";
    }
    //分页查询配送单
    @RequestMapping(value = "/custOutPageInfo", method = RequestMethod.POST)
    public String toPageSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=Lists.newArrayList();
		try{
			result=orderOutService.appCheck(userMap, eqlPage);
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("results", result);
    	return "pages/orderOut/orderOutlist";
    }
    
  //全量导出
    @RequestMapping(value = "exportAllOrderOut",method=RequestMethod.POST)
    public void toexportAllOrderOut(HttpServletRequest request,HttpServletResponse response) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=Lists.newArrayList();
		try{
			result=orderOutService.exportCheck(userMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		ExportCustOutController export=new ExportCustOutController();
		export.exportSendList(response, request, result);
		orderOutService.updateExportInfo(result,session);
    }
    //导出
    @RequestMapping(value = "exportOrderOut",method=RequestMethod.POST)
    public void toexportOrderOut(String partExport,HttpServletRequest request,HttpServletResponse response) {
    	String[] list=partExport.split("-&-;,");
    	List<Map> result=new ArrayList<Map>();
    	for(int i=0;i<list.length;i++){
    		String[] strList=list[i].split("-&-");
    		Map map=new HashMap();
    		if(strList[0]!=null && strList[0].split("&nbsp;").length!=0){
    			map.put("ADDRESSEE_NAME", strList[0].split("&nbsp;")[0]);
    		}else{
    			map.put("ADDRESSEE_NAME","");
    		}
    		if(strList[1]!=null && strList[1].split("&nbsp;").length!=0){
    			map.put("ADDRESSEE_PHONE", strList[1].split("&nbsp;")[0]);
    		}else{
    			map.put("ADDRESSEE_PHONE","");
    		}
    		if(strList[2]!=null && strList[2].split("&nbsp;").length!=0){
    			map.put("DISTRIBUT_ADDRESS", strList[2].split("&nbsp;")[0]);
    		}else{
    			map.put("DISTRIBUT_ADDRESS","");
    		}
    		String[] str=strList[3].split("\\(");
    		String[] str2=str[1].split("\\)");
    		map.put("detail", str[0]);
    		map.put("date", str2[0]);
    		map.put("EXPORT_FLAG", strList[4]);
    		if(strList[5]!=null && strList[5].split("&nbsp;").length!=0){
    			map.put("EXPORT_TIME", strList[5].split("&nbsp;")[0]);
    		}else{
    			map.put("EXPORT_TIME","");
    		}
    		map.put("ORDER_ID", strList[6]);
    		map.put("DISTRIBUT_ID", strList[7]);
    		map.put("STOCK_ADDRESS", strList[10]);
    		if(strList[9]==null){
    			map.put("LABEL_CONTENT","");
    		}else{
    			map.put("LABEL_CONTENT", strList[9]);
    		}
    		result.add(map);
    	}
		ExportCustOutController export=new ExportCustOutController();
		export.exportSendList(response, request, result);
		HttpSession session=request.getSession();
		orderOutService.updateExportInfo(result,session);
    }
    //出库确认
    @RequestMapping(value = "sureStock",method=RequestMethod.POST)
    public String tosureStock(String partStock,HttpServletRequest request,HttpServletResponse response) {
    	String[] list=partStock.split("-&-;,");
    	List<Map> result=new ArrayList<Map>();
    	for(int i=0;i<list.length;i++){
    		String[] strList=list[i].split("-&-");
    		Map map=new HashMap();
    		if(strList[0]!=null && strList[0].split("&nbsp;").length!=0){
    			map.put("ADDRESSEE_NAME", strList[0].split("&nbsp;")[0]);
    		}else{
    			map.put("ADDRESSEE_NAME","");
    		}
    		if(strList[1]!=null && strList[1].split("&nbsp;").length!=0){
    			map.put("ADDRESSEE_PHONE", strList[1].split("&nbsp;")[0]);
    		}else{
    			map.put("ADDRESSEE_PHONE","");
    		}
    		if(strList[2]!=null && strList[2].split("&nbsp;").length!=0){
    			map.put("DISTRIBUT_ADDRESS", strList[2].split("&nbsp;")[0]);
    		}else{
    			map.put("DISTRIBUT_ADDRESS","");
    		}
    		String[] str=strList[3].split("\\(");
    		String[] str2=str[1].split("\\)");
    		map.put("detail", str[0]);
    		map.put("date", str2[0]);
    		map.put("EXPORT_FLAG", strList[4]);
    		if(strList[5]!=null && strList[5].split("&nbsp;").length!=0){
    			map.put("EXPORT_TIME", strList[5].split("&nbsp;")[0]);
    		}else{
    			map.put("EXPORT_TIME","");
    		}
    		map.put("ORDER_ID", strList[6]);
    		map.put("DISTRIBUT_ID", strList[7]);
    		map.put("STOCK_ADDRESS", strList[10]);
    		if(strList[9]==null){
    			map.put("LABEL_CONTENT","");
    		}else{
    			map.put("LABEL_CONTENT", strList[9]);
    		}
    		result.add(map);
    	}
		HttpSession session=request.getSession();
		orderOutService.updateExportInfo(result,session);
		return "pages/orderOut/orderOut";
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
