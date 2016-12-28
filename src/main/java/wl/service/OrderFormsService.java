package wl.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.OrderFormsDao;
import wl.entity.LogInfo;

@Service("orderFormsService")
public class OrderFormsService {
    @Autowired
    @Qualifier("orderFormsDao")
    private OrderFormsDao orderFormsDao;
    public Map appCheck(Map inMap){
    	Map map=new HashMap();
    	if(inMap!=null && inMap.get("month")!=null && !"".equals(inMap.get("month").toString())){
    		map=orderFormsDao.selectOrderSumInfoNew(inMap);
    	}else{
    		map=orderFormsDao.selectOrderSumInfo(inMap);
    	}
    	return map;
    }
    
    public List<Map> appCheckNew(Map inMap,EqlPage eqlPage){
    	List<Map> result=new ArrayList<Map>();
    	
    	result=orderFormsDao.selectOrderInfo(inMap, eqlPage);
    	for(int i=0;i<result.size();i++){
    		String productType="1".equals((String)result.get(i).get("PRODUCT_TYPE"))?"专业版":"普及版";
    		if("1".equals(result.get(i).get("CONTIN_MONTH"))){
    			productType+=result.get(i).get("PRODUCT_BEGIN")+"至"+result.get(i).get("PRODUCT_END")+";";
    		}else{
    			productType+=result.get(i).get("PRODUCT_BEGIN")+";";
    		}
    		for(int j=i+1;j<result.size();j++){
    			if(((String)result.get(j).get("SUBSCRIBE_ID")).equals(((String)result.get(i).get("SUBSCRIBE_ID")))){
    				productType+="1".equals((String)result.get(j).get("PRODUCT_TYPE"))?"专业版":"普及版";
    	    		if("1".equals(result.get(j).get("CONTIN_MONTH"))){
    	    			productType+=result.get(j).get("PRODUCT_BEGIN")+"至"+result.get(j).get("PRODUCT_END")+";";
    	    		}else{
    	    			productType+=result.get(j).get("PRODUCT_BEGIN")+";";
    	    		}
    	    		result.remove(j);
    			}else{
    				break;
    			}
    		}
    		result.get(i).put("product", productType);
    	}
    	for(Map map:result){
    		if(map.get("ACCEPT_TIME")!=null && !"".equals(map.get("ACCEPT_TIME"))){
    			map.put("ACCEPT_TIME", map.get("ACCEPT_TIME").toString().substring(0, 10));
    		}
    		if(map.get("COMPANY")==null || "".equals(map.get("COMPANY"))){
    			map.put("COMPANY", "");
    		}
    		String state=map.get("SUBSCRIBE_STATE").toString();
    		if("2".equals(state)){
				map.put("SUBSCRIBE_STATE", "待配送");
			}else if("3".equals(state)){
				map.put("SUBSCRIBE_STATE", "已完成");
			}else if(("1".equals(state) && ("1".equals(map.get("FIRST_SEND").toString())|| "4".equals(map.get("CUSTOMER_TYPE").toString()) || "5".equals(map.get("CUSTOMER_TYPE").toString()))
					)||("1".equals(map.get("BEFORE_INVOICE")) && "1".equals(map.get("BEFORE_INVOICE_TAG")))){
				map.put("SUBSCRIBE_STATE", "待审批");
			}else{
				map.put("SUBSCRIBE_STATE", "待收款");
			}
    	}
    	return result;
    }
    public List<Map> appCheckNew(Map inMap){
    	List<Map> result=new ArrayList<Map>();
    	
    	result=orderFormsDao.selectOrderInfo(inMap);
    	for(int i=0;i<result.size();i++){
    		String productType="1".equals((String)result.get(i).get("PRODUCT_TYPE"))?"专业版":"普及版";
    		if("1".equals(result.get(i).get("CONTIN_MONTH"))){
    			productType+=result.get(i).get("PRODUCT_BEGIN")+"至"+result.get(i).get("PRODUCT_END")+";";
    		}else{
    			productType+=result.get(i).get("PRODUCT_BEGIN")+";";
    		}
    		for(int j=i+1;j<result.size();j++){
    			if(((String)result.get(j).get("SUBSCRIBE_ID")).equals(((String)result.get(i).get("SUBSCRIBE_ID")))){
    				productType+="1".equals((String)result.get(j).get("PRODUCT_TYPE"))?"专业版":"普及版";
    	    		if("1".equals(result.get(j).get("CONTIN_MONTH"))){
    	    			productType+=result.get(j).get("PRODUCT_BEGIN")+"至"+result.get(j).get("PRODUCT_END")+";";
    	    		}else{
    	    			productType+=result.get(j).get("PRODUCT_BEGIN")+";";
    	    		}
    	    		result.remove(j);
    			}else{
    				break;
    			}
    		}
    		result.get(i).put("product", productType);
    	}
    	for(Map map:result){
    		if(map.get("ACCEPT_TIME")!=null && !"".equals(map.get("ACCEPT_TIME"))){
    			map.put("ACCEPT_TIME", map.get("ACCEPT_TIME").toString().substring(0, 10));
    		}
    		if(map.get("COMPANY")==null || "".equals(map.get("COMPANY"))){
    			map.put("COMPANY", "");
    		}
    		if(map.get("REMARK")==null || "".equals(map.get("REMARK"))){
    			map.put("REMARK", "");
    		}
    		String state=map.get("SUBSCRIBE_STATE").toString();
    		if("2".equals(state)){
				map.put("SUBSCRIBE_STATE", "待配送");
			}else if("3".equals(state)){
				map.put("SUBSCRIBE_STATE", "已完成");
			}else if(("1".equals(state) && ("1".equals(map.get("FIRST_SEND").toString())|| "4".equals(map.get("CUSTOMER_TYPE").toString()) || "5".equals(map.get("CUSTOMER_TYPE").toString()))
					)||("1".equals(map.get("BEFORE_INVOICE")) && "1".equals(map.get("BEFORE_INVOICE_TAG")))){
				map.put("SUBSCRIBE_STATE", "待审批");
			}else{
				map.put("SUBSCRIBE_STATE", "待收款");
			}
    	}
    	return result;
    }

    public int appSendCheck(Map inMap){
    	int map=orderFormsDao.selectSendSumInfo(inMap);
    	return map;
    }
    
    public List<Map> appSendCheckNew(Map inMap,EqlPage eqlPage){
    	List<Map> result=new ArrayList<Map>();
    	
    	result=orderFormsDao.selectSendInfo(inMap, eqlPage);
    	for(Map map:result){
    		if(map.get("EXPORT_FLAG")!=null && !"".equals(map.get("EXPORT_FLAG"))){
    			if("1".equals(map.get("EXPORT_FLAG").toString())){
    				map.put("EXPORT_FLAG", "未配送");
    			}else{
    				map.put("EXPORT_FLAG", "已配送");
    			}
    		}
    		if(map.get("EXPORT_TIME")==null || "".equals(map.get("EXPORT_TIME"))){
    			map.put("EXPORT_TIME", "");
    		}
    		String product=("1".equals(map.get("PRODUCT_TYPE").toString())?"中国书法专业版":"中国书法普及版");
    		product+=map.get("YEAR").toString()+"年第";
    		product+=("13".equals(map.get("MONTH").toString())?"合刊":map.get("MONTH").toString()+"期");
    		map.put("product", product);
    	}
    	return result;
    }
    public List<Map> appSendCheckNew(Map inMap){
    	List<Map> result=new ArrayList<Map>();
    	
    	result=orderFormsDao.selectSendInfo(inMap);
    	for(Map map:result){
    		if(map.get("EXPORT_FLAG")!=null && !"".equals(map.get("EXPORT_FLAG"))){
    			if("1".equals(map.get("EXPORT_FLAG").toString())){
    				map.put("EXPORT_FLAG", "未配送");
    			}else{
    				map.put("EXPORT_FLAG", "已配送");
    			}
    		}
    		if(map.get("EXPORT_TIME")==null || "".equals(map.get("EXPORT_TIME"))){
    			map.put("EXPORT_TIME", "");
    		}
    		String product=("1".equals(map.get("PRODUCT_TYPE").toString())?"中国书法专业版":"中国书法普及版");
    		product+=map.get("YEAR").toString()+"年第";
    		product+=("13".equals(map.get("MONTH").toString())?"合刊":map.get("MONTH").toString()+"期");
    		map.put("product", product);
    	}
    	return result;
    }
    public int appDetailCheck(Map inMap){
    	int map=orderFormsDao.selectDetailOrderSumInfo(inMap);
    	return map;
    }
    
    public List<Map> appDetailCheckNew(Map inMap,EqlPage eqlPage){
    	List<Map> result=new ArrayList<Map>();
    	
    	result=orderFormsDao.selectDetailOrderInfo(inMap, eqlPage);
    	for(Map map:result){
    		String state=map.get("SUBSCRIBE_STATE").toString();
    		if("2".equals(state)){
				map.put("SUBSCRIBE_STATE", "待配送");
			}else if("3".equals(state)){
				map.put("SUBSCRIBE_STATE", "已完成");
			}else if(("1".equals(state) && ("1".equals(map.get("FIRST_SEND").toString())|| "4".equals(map.get("CUSTOMER_TYPE").toString()) || "5".equals(map.get("CUSTOMER_TYPE").toString()))
					)||("1".equals(map.get("BEFORE_INVOICE")) && "1".equals(map.get("BEFORE_INVOICE_TAG")))){
				map.put("SUBSCRIBE_STATE", "待审批");
			}else{
				map.put("SUBSCRIBE_STATE", "待收款");
			}
    		String product=map.get("year").toString()+"年";
    		product+=("1".equals(map.get("PRODUCT_TYPE").toString())?"中国书法专业版":"中国书法普及版");
    		map.put("product", product);
    		int money=("1".equals(map.get("PRODUCT_TYPE").toString())?50:38);
    		money=Integer.parseInt(map.get("PRODUCT_PER_COUNT").toString())*12*money;
    		map.put("money", money);
    		String custType=map.get("CUSTOMER_TYPE").toString();
    		if("1".equals(custType)){
				map.put("custType", "大客户");
			}else if("2".equals(custType)){
				map.put("custType", "批发商");
			}else if("3".equals(custType)){
				map.put("custType", "零售商");
			}else if("4".equals(custType)){
				map.put("custType", "赠刊");
			}else if("5".equals(custType)){
				map.put("custType", "样刊");
			}else if("6".equals(custType)){
				map.put("custType", "报刊发行局");
			}else if("7".equals(custType)){
				map.put("custType", "发行站");
			}
    		if(map.get("COMPANY")==null || "".equals(map.get("COMPANY"))){
    			map.put("COMPANY", "");
    		}
    	}
    	return result;
    }
    public List<Map> appDetailCheckNew(Map inMap){
    	List<Map> result=new ArrayList<Map>();
    	
    	result=orderFormsDao.selectDetailOrderInfo(inMap);
    	for(Map map:result){
    		String state=map.get("SUBSCRIBE_STATE").toString();
    		if("2".equals(state)){
				map.put("SUBSCRIBE_STATE", "待配送");
			}else if("3".equals(state)){
				map.put("SUBSCRIBE_STATE", "已完成");
			}else if(("1".equals(state) && ("1".equals(map.get("FIRST_SEND").toString())|| "4".equals(map.get("CUSTOMER_TYPE").toString()) || "5".equals(map.get("CUSTOMER_TYPE").toString()))
					)||("1".equals(map.get("BEFORE_INVOICE")) && "1".equals(map.get("BEFORE_INVOICE_TAG")))){
				map.put("SUBSCRIBE_STATE", "待审批");
			}else{
				map.put("SUBSCRIBE_STATE", "待收款");
			}
    		String product=map.get("year").toString()+"年";
    		product+=("1".equals(map.get("PRODUCT_TYPE").toString())?"中国书法专业版":"中国书法普及版");
    		map.put("product", product);
    		int money=("1".equals(map.get("PRODUCT_TYPE").toString())?50:38);
    		money=Integer.parseInt(map.get("PRODUCT_PER_COUNT").toString())*12*money;
    		map.put("money", money);
    		String custType=map.get("CUSTOMER_TYPE").toString();
    		if("1".equals(custType)){
				map.put("custType", "大客户");
			}else if("2".equals(custType)){
				map.put("custType", "批发商");
			}else if("3".equals(custType)){
				map.put("custType", "零售商");
			}else if("4".equals(custType)){
				map.put("custType", "赠刊");
			}else if("5".equals(custType)){
				map.put("custType", "样刊");
			}else if("6".equals(custType)){
				map.put("custType", "报刊发行局");
			}else if("7".equals(custType)){
				map.put("custType", "发行站");
			}
    		if(map.get("COMPANY")==null || "".equals(map.get("COMPANY"))){
    			map.put("COMPANY", "");
    		}
    	}
    	return result;
    }
} 