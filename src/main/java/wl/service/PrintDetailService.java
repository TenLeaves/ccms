package wl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.PrintDetailDao;

@Service("printDetailService")
public class PrintDetailService {
    @Autowired
    @Qualifier("printDetailDao")
    private PrintDetailDao printDetailDao;
    
    public Map selectDetailInfo(String str){
    	Map result=new HashMap();
    	Map map=printDetailDao.selectOrderAllInfo(str);
    	result.put("type", map.get("YEAR")+"年中国书法"+(Integer.parseInt(""+map.get("PRODUCT_TYPE")) == 1?"专业版":"普及版")+"第"+map.get("MONTH")+"期");
    	result.put("customer", map.get("CUSTOMER_COUNT"));
    	result.put("agent", map.get("AGENT_COUNT"));
    	result.put("retail", map.get("RETAIL_COUNT"));
    	result.put("give", map.get("GIVE_COUNT"));
    	result.put("paper", map.get("BJPAPER_COUNT"));
    	result.put("list", map.get("RESERVE_COUNT"));
    	result.put("total", map.get("TOTAL_COUNT"));
    	result.put("book", map.get("INTERBOOK_COUNT"));
    	str=(String) map.get("PRINT_ID");
    	List<Map> inMap=printDetailDao.selectCustInfo(str);
    	result.put("cust", inMap);
    	return result;
    }
} 