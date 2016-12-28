package wl.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import wl.service.SupplementOrderService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Controller
public class SupplementOrderController {
    @Autowired
    @Qualifier("supplementOrderService")
    private SupplementOrderService supplementOrderService;
	
	@RequestMapping(value = "/saveOrderInfoNew", method = RequestMethod.POST)
	@ResponseBody
    public String toSaveOrderInfo(HttpServletRequest request) throws SQLException {
		Map map=request.getParameterMap();
		String str=((String[]) map.get("orderList"))[0];
		String user=((String[])map.get("user"))[0];
		Map userMap=(Map) JSON.parse(user);
		List<JSON> orderMap= (List<JSON>) JSONArray.parse(str);
		List<Map> orderList=new ArrayList();
		for(int i=0;i<orderMap.size();i++){
			String order=orderMap.get(i).toString();
			orderList.add((Map)JSON.parse(order));
		}
		HttpSession session=request.getSession();
		String errorMsg=supplementOrderService.insertStockInfo(orderList, session);
		String str1="";
		Map outMap=new HashMap();
		if(errorMsg != null){
			outMap.put("errorMsg", errorMsg);
			str1=JSON.toJSONString(outMap);
			return str1;
		}
		supplementOrderService.InsertOrderInfo(userMap,orderList,session);
		return null;
    }
    @RequestMapping(value = "/selectSeqNew", method = RequestMethod.GET)
    @ResponseBody
    public String toSelectSeq() {
        String str=supplementOrderService.CreateOrderSubId("order");
        return str;
    }
    @RequestMapping(value = "/selectSendSeqNew", method = RequestMethod.GET)
    @ResponseBody
    public String toSelectSendSeq() {
        String str=supplementOrderService.CreateOrderSubId("send");
        return "E"+str;
    }
    /**
     * 导入配送地址解析
     */
    @RequestMapping(value="/importAddressNew", method = RequestMethod.POST)
    @ResponseBody
    public String handleUploadData(String name, @RequestParam("file") CommonsMultipartFile file){
        Map map=new HashMap();
    	String str1="";
        if (!file.isEmpty()) {
               String fileName = file.getOriginalFilename();
               String fileType = fileName.substring(fileName.lastIndexOf("."));
               Map<String, String[]> titleMap = new HashMap<String, String[]>();
               String[] title = {"getCustomer","getTel","sendAdd","postcode",
            		   "sendNumber","getProWay","sendAddress","labelInfo","labelContent","textInfoSend"};
               titleMap.put("title", title);
               
               List infos = new ArrayList();
               List errorInfos= new ArrayList();
               try{
                   infos = (List)ImportFileHelper.resolveFile(titleMap, file).get("infos");
                   errorInfos = importValidata(infos);
                   for(int i=0;i<infos.size();i++){
                	   String str=supplementOrderService.CreateOrderSubId("send");
                	   ((Map) infos.get(i)).put("sendSeq","E"+str);
                	   
                	   if(((Map) infos.get(i)).get("postcode")!=null){
                	       ((Map) infos.get(i)).put("postcode",((Map) infos.get(i)).get("postcode").toString().trim());
                	   }else{
                	       ((Map) infos.get(i)).put("postcode","");
                	   }
                	   
                	   ((Map) infos.get(i)).put("sendNumber",((Map) infos.get(i)).get("sendNumber").toString().trim());
                	   if(((Map) infos.get(i)).get("labelContent")!=null){
                	       ((Map) infos.get(i)).put("labelContent",((Map) infos.get(i)).get("labelContent").toString().trim()); 
                	   }else{
                	       ((Map) infos.get(i)).put("labelContent","");
                	   }
                	   
                	   if(((Map) infos.get(i)).get("textInfoSend")!=null){
                	       ((Map) infos.get(i)).put("textInfoSend",((Map) infos.get(i)).get("textInfoSend").toString().trim()); 
                	   }else{
                	       ((Map) infos.get(i)).put("textInfoSend",""); 
                	   }
                	   
                	   String sendAdd= ((String) ((Map) infos.get(i)).get("sendAdd")).trim();
                	   if("杂志社".equals(sendAdd)){
                		   ((Map) infos.get(i)).put("sendAdd","0");
                	   }else if("邮局".equals(sendAdd)){
                		   ((Map) infos.get(i)).put("sendAdd","1");
                	   }else if("印刷厂".equals(sendAdd)){
                		   ((Map) infos.get(i)).put("sendAdd","2");
                	   }
                	   String getProWay= ((String) ((Map) infos.get(i)).get("getProWay")).trim();
                	   if("自提".equals(getProWay)){
                		   ((Map) infos.get(i)).put("getProWay","0");
                	   }else if("邮局".equals(getProWay)){
                		   ((Map) infos.get(i)).put("getProWay","1");
                	   }else if("中铁".equals(getProWay)){
                		   ((Map) infos.get(i)).put("getProWay","2");
                	   }else if("其他物流".equals(getProWay)){
                		   ((Map) infos.get(i)).put("getProWay","3");
                	   }
                	   String labelInfo= ((String) ((Map) infos.get(i)).get("labelInfo")).trim();
                	   if("是".equals(labelInfo)){
                		   ((Map) infos.get(i)).put("labelInfo","1");
                	   }else if("否".equals(labelInfo)){
                		   ((Map) infos.get(i)).put("labelInfo","0");
                	   }
                	   if(((Map) infos.get(i)).get("sendAddress")==null){
                		   ((Map) infos.get(i)).put("sendAddress","");
                	   }else{
                		   ((Map) infos.get(i)).put("sendAddress",((Map) infos.get(i)).get("sendAddress").toString().trim());
                	   }
                	   if(((Map) infos.get(i)).get("getCustomer")==null){
                		   ((Map) infos.get(i)).put("getCustomer","");
                	   }else{
                		   ((Map) infos.get(i)).put("getCustomer",((Map) infos.get(i)).get("getCustomer").toString().trim());
                	   }
                	   if(((Map) infos.get(i)).get("getTel")==null){
                		   ((Map) infos.get(i)).put("getTel","");
                	   }else{
                		   ((Map) infos.get(i)).put("getTel",((Map) infos.get(i)).get("getTel").toString().trim());
                	   }
                   }
               map.put("status", "success");
               }catch( Exception e){
                   e.printStackTrace();
                   map.put("status", "fail");
               }
               int infoLength=infos.size();
               int errorLength=errorInfos.size();
               int sum=errorLength+infoLength;
               map.put("infoLength", infoLength);
               map.put("errorLength", errorLength);
               map.put("sum", sum);
               map.put("infos", infos);
               map.put("errorInfos", errorInfos);
               str1=JSON.toJSONString(map);
        }
		return str1;
    }
    /**
     * 校验批量上传客户信息
     * @throws Exception 
     */
    public List importValidata(List impList) throws Exception{
        List errorList = new ArrayList();
        
        if(impList.size()>500){
            throw new Exception("文件条数不能超过500条");
        }
        
        for(int i=0; i<impList.size(); i++){
            Map impMap = (Map)impList.get(i);
            
            // 库存地址不能为空
            if(impMap.get("sendAdd") == null || "".equals(impMap.get("sendAdd"))){
                impMap.put("error", "库存地址为空");
                impMap.put("right", "填写库存地址");
                errorList.add(impMap);
                impList.remove(i);
                
                i--;
                continue;
            }else{
            	String sendAdd = ((String)impMap.get("sendAdd")).trim();
            	if(!"印刷厂".equals(sendAdd) && !"邮局".equals(sendAdd) && !"杂志社".equals(sendAdd)){
            		impMap.put("error", "库存地址有误");
            		impMap.put("right", "正确填写库存地址");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
            
            // 寄送方式
            if(impMap.get("getProWay") == null || "".equals(impMap.get("getProWay"))){
                impMap.put("error", "寄送方式为空");
                impMap.put("right", "填写寄送方式");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
            	String getProWay = ((String)impMap.get("getProWay")).trim();
            	if(!"其他物流".equals(getProWay) && !"自提".equals(getProWay) && !"邮局".equals(getProWay) && !"中铁".equals(getProWay)){
            		impMap.put("error", "寄送方式有误");
            		impMap.put("right", "正确填写寄送方式");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
            
         // 是否需要标签
            if(impMap.get("labelInfo") == null || "".equals(impMap.get("labelInfo"))){
                impMap.put("error", "是否需要标签为空");
                impMap.put("right", "填写是否需要标签");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
            	String labelInfo = ((String)impMap.get("labelInfo")).trim();
            	if(!"是".equals(labelInfo) && !"否".equals(labelInfo)){
            		impMap.put("error", "是否需要标签的标识有误");
            		impMap.put("right", "正确填写是否需要标签的标识");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
            }
            
            // 月配送数量
            if(impMap.get("sendNumber") == null || "".equals(impMap.get("sendNumber"))){
                impMap.put("error", "月配送数量为空");
                impMap.put("right", "填写月配送数量");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }
          // 寄送地址
            if(!"自提".equals(impMap.get("getProWay").toString().trim()) && (impMap.get("sendAddress") == null || "".equals(impMap.get("sendAddress")))){
                impMap.put("error", "寄送地址为空");
                impMap.put("right", "填写寄送地址");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }
            
         // 收件人姓名
            if(!"自提".equals(impMap.get("getProWay").toString().trim()) && (impMap.get("getCustomer") == null || "".equals(impMap.get("getCustomer")))){
                impMap.put("error", "收件人姓名为空");
                impMap.put("right", "填写收件人姓名");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }
            
         // 电话号码必须为数字
//            if(impMap.get("getTel") != null && !"".equals(impMap.get("getTel"))){
//            	String phone = ((String)impMap.get("getTel")).trim();
//            	if(phone.length() != 11 || !phone.matches("1[0-9]+")){
//            		impMap.put("error", "联系人手机号码不是1开头的11位数字");
//            		impMap.put("right", "正确填写联系人手机号码");
//            		errorList.add(impMap);
//            		impList.remove(i);
//            		i--;
//            		continue;
//            	}
//             }
            
            // 校验月配送数量格式
            if(impMap.get("sendNumber") != null && !"".equals(impMap.get("sendNumber"))){
                
            	String discount = ((String)impMap.get("sendNumber")).trim();
                
                if(!discount.matches("[1-9][0-9]*")){
                    impMap.put("error", "月配送数量格式不正确");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }
         // 校验邮编格式
            if(impMap.get("postcode") != null && !"".equals(impMap.get("postcode"))){
                
            	String discount = ((String)impMap.get("postcode")).trim();
                
                if(!discount.matches("[0-9]+")){
                    impMap.put("error", "邮编格式不正确");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }
        }
        
        return errorList;
    }
    
}
