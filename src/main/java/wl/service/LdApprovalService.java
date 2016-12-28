package wl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.common.utils.ProDuctUtils;
import wl.dao.LdApprovalDao;
import wl.entity.LogInfo;

@Service("ldapprovalService")
public class LdApprovalService {
    @Autowired
    @Qualifier("ldapprovalDao")
    private LdApprovalDao ldapprovalDao;
    public List<Map> appCheck(EqlPage eqlPage){
    	List<Map> result=new ArrayList<Map>();
    	List<Map> inMap=ldapprovalDao.supplementInfo(eqlPage);
    	
    	if(inMap.size()==1 && inMap.get(0).get("total")==null){
    		inMap.remove(0);
    	}
    	for(int i=0;i<inMap.size();i++){
    		Map outMap=new HashMap();
    		String customerType=(String) inMap.get(i).get("CUSTOMER_TYPE");
    		String isTypeInvoiceFirst=(String) inMap.get(i).get("BEFORE_INVOICE");
    		String invoiceCheckState=(String) inMap.get(i).get("BEFORE_INVOICE_TAG");
    		if("4".equals(customerType)){
    			outMap.put("stateName", "赠刊");
    			outMap.put("detail", inMap.get(i).get("COMPANY")+""+inMap.get(i).get("CUSTOMER_NAME")+"申请赠刊");
        		outMap.put("count", "领用数量："+inMap.get(i).get("total"));
    		}else if("5".equals(customerType)){
    			outMap.put("stateName", "样刊");
    			outMap.put("detail", inMap.get(i).get("COMPANY")+""+inMap.get(i).get("CUSTOMER_NAME")+"申请样刊");
        		outMap.put("count", "领用数量："+inMap.get(i).get("total"));
    		}else if("1".equals(isTypeInvoiceFirst)&&"1".equals(invoiceCheckState)){//未付款先打印发票，且没有审核的
    			outMap.put("stateName", "末付款先打印发票");
    			outMap.put("detail", (inMap.get(i).get("COMPANY")==null?"":inMap.get(i).get("COMPANY"))+""+inMap.get(i).get("CUSTOMER_NAME")+"申请先打印发票");
    			outMap.put("count", "");
    		}else{
        		outMap.put("stateName", "未付款先发货");
    			outMap.put("detail", (inMap.get(i).get("COMPANY")==null?"":inMap.get(i).get("COMPANY"))+""+inMap.get(i).get("CUSTOMER_NAME")+"申请先发货");
    			outMap.put("count", "");
    		}
    		outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    		outMap.put("updateTime", (""+inMap.get(i).get("UPDATE_TIME")).substring(0,10));
    		outMap.put("key", inMap.get(i).get("SUBSCRIBE_ID"));
    		result.add(outMap);
    	}
    	return result;
   }
    public List<Map> appStockCheck(){
    	List<Map> result=new ArrayList<Map>();
    	List<Map> inMap=ldapprovalDao.printCheckInfo();
    	for(int i=0;i<inMap.size();i++){
    		Map outMap=new HashMap();
    		outMap.put("stateName", "印刷量审批");
    		outMap.put("detail", ProDuctUtils.prodToDesc( inMap.get(i).get("PRINT_CODE").toString()));
    		outMap.put("count", "印刷数量："+inMap.get(i).get("TOTAL_COUNT"));
    		outMap.put("staffId", inMap.get(i).get("UPDATE_STAFF"));
    		outMap.put("updateTime", (""+inMap.get(i).get("ACCEPT_TIME")).substring(0,10));
    		outMap.put("key", inMap.get(i).get("PRINT_CODE"));
    		result.add(outMap);
    	}
    	return result;
    }
    public void updateAppSucc(String key,HttpSession session){
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	ldapprovalDao.updateLdAppSuc(key,log.getStaff_id());
    }
    public void updateAppSucc1(String key,HttpSession session){
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	ldapprovalDao.updateLdAppSuc1(key,log.getStaff_id());
    }
    public void updateStockAppSucc(String key,HttpSession session){
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	ldapprovalDao.updatePageAppSuc(key, log.getStaff_id());
    }
    public void updateAppErr(String key,String boxInfo,HttpSession session){
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	ldapprovalDao.updateLdAppErr(key,boxInfo,log.getStaff_id());
    }
    public void updateAppErr1(String key,String boxInfo,HttpSession session){
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	ldapprovalDao.updateLdAppErr1(key,boxInfo,log.getStaff_id());
    }
    public void updateStockAppErr(String key,String boxInfo,HttpSession session){
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	ldapprovalDao.updatePageAppErr(key, log.getStaff_id(),boxInfo);
    }
} 