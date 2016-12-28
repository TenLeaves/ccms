package wl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import wl.entity.LogInfo;
import wl.entity.Payment;
import wl.service.PaymentService;


@Controller
@SessionAttributes({"selPayPram"})
public class PaymentController {
   
    @Resource
    PaymentService paymentService;
    
    private Log log = LogFactory.getLog(this.getClass());
     
    // 添加回款单信息
    @RequestMapping(value="/addPay",method=RequestMethod.POST) 
    @ResponseBody
    public void addCust(Payment pay, HttpServletRequest request){
        
        LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
        String staffCode = logInfo.getStaff_id();
        pay.setUpdateStaff(staffCode);
        log.info("-----------------添加汇款单---------------");
        log.info("参数:"+pay.toString()+"操作人:"+pay.getUpdateStaff());
        paymentService.addPayment(pay);
        log.info("-----------------添加汇款单结束---------------");
    }
    
    // 更新回款单信息
    @RequestMapping(value="/updatePay",method=RequestMethod.POST) 
    @ResponseBody
    public Map updateCust(Payment  pay, HttpServletRequest request ){
        
        LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
        String staffCode = logInfo.getStaff_id();
        pay.setUpdateStaff(staffCode);
        
        int ret = paymentService.updatePay(pay);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "success");
        return map;
    }
    // 更新回款单信息
    @RequestMapping(value="/deletePayment",method=RequestMethod.POST) 
    @ResponseBody
    public void deletePayment(HttpServletRequest req ){
        String paymentId = req.getParameter("PAYMENT_ID");
        Map paraMap = Maps.newHashMap();
        paraMap.put("PAYMENT_ID", paymentId);
        paymentService.deletePay(paraMap);
    }
    
    // 查询按钮 查询所有回款单
    @RequestMapping(value="/selAllPay",method=RequestMethod.POST) 
    public String selAllCust(Payment pay, Model model,EqlPage eqlPage){
        
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(8);
        eqlPage.setTotalRows(0); // 初始化 eqlPage总数为0， 这样每次查询都是查的最新的总数
        
        List<Payment> payList = paymentService.selPay(pay, eqlPage);
        
        model.addAttribute("results", payList);
        
        model.addAttribute("selPayPram", pay);
        
        return "pages/payment/paylist";
    }
    
    // 分页查询回款单
    @RequestMapping(value="/selPay",method=RequestMethod.POST) 
    public String selCust(@ModelAttribute("selPayPram")Payment pay, Model model,EqlPage eqlPage){
        
        List<Payment> payList = paymentService.selPay(pay, eqlPage);
        
        model.addAttribute("results", payList);
        
        return "pages/payment/paylist";
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
    /**
     * 导入回款单解析
     */
    @RequestMapping(value="/importPayment", method = RequestMethod.POST)
    @ResponseBody
    public String handleUploadData(String postCompanyInfo, @RequestParam("file") CommonsMultipartFile file,HttpServletRequest request){
        String str1="";
    	if (!file.isEmpty()) {
               String fileName = file.getOriginalFilename();
               String fileType = fileName.substring(fileName.lastIndexOf("."));
               Map<String, String[]> titleMap = new HashMap<String, String[]>();
               String[] title = { "paymentId", "paymentName", "paymentValue", "remark", "paymentFlag",
            		   "paymentTime","paymentPhone","paymentAddress","postCode","buyCount"};
               titleMap.put("title", title);
               Map map=new HashMap();
               List infos = new ArrayList();
               List errorInfos= new ArrayList();
               try{
                   infos = (List)ImportFileHelper.resolveFile(titleMap, file).get("infos");
                   errorInfos = importValidata(infos);
                   LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
                   String staffCode = logInfo.getStaff_id();
                   for(int i=0;i<infos.size();i++){
                	   Payment pay=new Payment();
                	   String paymentId= ((String) ((Map) infos.get(i)).get("paymentId")).trim();
                	   pay.setPaymentId(paymentId);
                	   String paymentTime= ((String) ((Map) infos.get(i)).get("paymentTime")).trim();
                	   pay.setPaymentTime(paymentTime);
                	   String paymentName= ((String) ((Map) infos.get(i)).get("paymentName")).trim();
                	   pay.setPaymentName(paymentName);
                	   String paymentValue= ((String) ((Map) infos.get(i)).get("paymentValue")).trim();
                	   pay.setPaymentValue(paymentValue);
                	   String remark="";
                	   if(((Map)infos.get(i)).get("remark")!=null){
                	       remark= ((String) ((Map) infos.get(i)).get("remark")).trim(); 
                	   }
                	   pay.setRemark(remark);
                	   String paymentFlag= ((String) ((Map) infos.get(i)).get("paymentFlag")).trim();
                	   if("是".equals(paymentFlag)){
                		   pay.setPaymentFlag("1");
                	   }else if("否".equals(paymentFlag)){
                		   pay.setPaymentFlag("2");   
                	   }
                	   String paymentPhone="";
                	   if(((Map) infos.get(i)).get("paymentPhone")!=null){
                	       paymentPhone= ((String) ((Map) infos.get(i)).get("paymentPhone")).trim();
                	   }
                	   pay.setPaymentPhone(paymentPhone);
                	   
                	   String paymentAddress="";
                	   if(((Map) infos.get(i)).get("paymentAddress")!=null){
                	       paymentAddress= ((String) ((Map) infos.get(i)).get("paymentAddress")).trim();
                	   }
                	   pay.setPaymentAddress(paymentAddress);
                	   
                	   String postCode="";
                	   if(((Map) infos.get(i)).get("postCode")!=null){
                	       postCode= ((String) ((Map) infos.get(i)).get("postCode")).trim();
                	   }
                	   pay.setPostCode(postCode);
                	   
                	   String buyCount="";
                	   if(((Map) infos.get(i)).get("buyCount")!=null){
                	       buyCount= ((String) ((Map) infos.get(i)).get("buyCount")).trim();
                	   }
                	   
                	   pay.setBuyCount(buyCount);
                	   pay.setPostCompany(postCompanyInfo);
                       pay.setUpdateStaff(staffCode);
                       paymentService.addPayment(pay);
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
            
            // 回款单号
            if(impMap.get("paymentId") == null || "".equals(impMap.get("paymentId"))){
                impMap.put("error", "汇款单号为空");
                impMap.put("right", "填写汇款单号");
                errorList.add(impMap);
                impList.remove(i);
                
                i--;
                continue;
            }
            
            // 付款人户名
            if(impMap.get("paymentName") == null || "".equals(impMap.get("paymentName"))){
                impMap.put("error", "付款人户名为空");
                impMap.put("right", "填写付款人户名");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }
            // 汇款时间
            if(impMap.get("paymentTime") == null || "".equals(impMap.get("paymentTime"))){
                impMap.put("error", "汇款时间为空");
                impMap.put("right", "填写汇款时间");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
                String paymentTime = ((String)impMap.get("paymentTime")).trim();
                if(!paymentTime.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
                    impMap.put("error", "汇款时间不是正确的时间格式");
                    impMap.put("right", "正确填写汇款时间");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }
         // 是否汇款金额
            if(impMap.get("paymentValue") == null || "".equals(impMap.get("paymentValue"))){
                impMap.put("error", "汇款金额为空");
                impMap.put("right", "填写汇款金额");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
                String paymentValue = ((String)impMap.get("paymentValue")).trim();
                if(!paymentValue.matches("[1-9][0-9]*.?[0-9]*")){
                    impMap.put("error", "汇款金额不是正确的金额数字");
                    impMap.put("right", "正确填写汇款金额");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }
            
            // 回款金是否收到标识
            if(impMap.get("paymentFlag") == null || "".equals(impMap.get("paymentFlag"))){
                impMap.put("error", "汇款金是否收到标识为空");
                impMap.put("right", "填写汇款金是否收到标识");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            }else{
                String paymentFlag = ((String)impMap.get("paymentFlag")).trim();
                if(!"是".equals(paymentFlag) && !"否".equals(paymentFlag)){
                    impMap.put("error", "汇款金是否收到标识不为是或否");
                    impMap.put("right", "正确填写汇款金是否收到标识为空");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }
            // 电话号码必须为数字
            if(impMap.get("paymentPhone") != null && !"".equals(impMap.get("paymentPhone"))){
            	String phone = ((String)impMap.get("paymentPhone")).trim();
            	if(phone.length() != 11 || !phone.matches("1[0-9]+")){
            		impMap.put("error", "汇款手机号码不是1开头的11位数字");
            		impMap.put("right", "正确填写汇款人手机号码");
            		errorList.add(impMap);
            		impList.remove(i);
            		i--;
            		continue;
            	}
             }
         // 邮编必须为数字
            if(impMap.get("postCode") != null && !"".equals(impMap.get("postCode"))){
            	String postCode = ((String)impMap.get("postCode")).trim();
            	if(!postCode.matches("[0-9]{6}")){
            		impMap.put("error", "邮编不是6位数字");
            		impMap.put("right", "正确填写邮编");
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
