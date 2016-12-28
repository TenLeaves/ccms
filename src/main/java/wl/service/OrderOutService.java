package wl.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.OrderOutDao;
import wl.entity.LogInfo;

@Service("orderOutService")
public class OrderOutService {
    @Autowired
    @Qualifier("orderOutDao")
    private OrderOutDao orderOutDao;
    
    private Log log = LogFactory.getLog(this.getClass());
    public List<Map> appCheck(Map inMap,EqlPage eqlPage){
    	List<Map> result=new ArrayList<Map>();
    	Map map=new HashMap();
    	if(inMap.get("custName")!=null && !"".equals(inMap.get("custName"))){
    		map.put("custName", "%"+inMap.get("custName")+"%");
    	}
    	if(inMap.get("telephone")!=null && !"".equals(inMap.get("telephone"))){
    		map.put("phone", "%"+inMap.get("telephone")+"%");
    	}
    	map.put("flag", ""+(Integer.parseInt((String) inMap.get("explorWay"))+1));
    	if(inMap.get("address")!=null && !"".equals(inMap.get("address"))){
    		map.put("address", "%"+inMap.get("address")+"%");
    	}
    	Calendar c=Calendar.getInstance();
    	map.put("year", String.valueOf(c.get(Calendar.YEAR)));
    	String month_info=Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1))<10?"0"+String.valueOf(c.get(Calendar.MONTH) + 1):String.valueOf(c.get(Calendar.MONTH) + 1);
    	map.put("month", "12".equals(month_info)?"13":month_info);
    	map.put("exportTime", inMap.get("exploreTime"));
    	map.put("myselfWay", ""+(Integer.parseInt((String) inMap.get("myselfWay"))+1));
    	
    	result=orderOutDao.selectOrderAllInfo(map, eqlPage);
    	for(int i=0;i<result.size();i++){
    		if(result.get(i).get("EXPORT_TIME")!=null){
    			String time=result.get(i).get("EXPORT_TIME")+"";
    			result.get(i).put("EXPORT_TIME", time.substring(0,10));
    		}else{
    			result.get(i).put("EXPORT_TIME", "");
    		}
    		map.put("distributId", result.get(i).get("DISTRIBUT_ID"));
    		map.put("orderId", result.get(i).get("ORDER_ID"));
    		String detail="";
    		String date="";
    		detail+=("1".equals(result.get(i).get("PRODUCT_TYPE"))?"专业版":"普及版");
    		detail+=("每期"+result.get(i).get("DISTRIBUT_COUNT")+"本");
    		List<Map> outMap=orderOutDao.selStatusDetail(map);
    		for(int j=0;j<outMap.size();j++){
    			String month=(String) outMap.get(j).get("MONTH");
				if("13".equals(outMap.get(j).get("MONTH"))){
    				month="合刊";
    			}
    			if(j!=0){
    				date+=","+outMap.get(j).get("YEAR")+"-"+month;
    			}else{
    				date+=outMap.get(j).get("YEAR")+"-"+month;
    			}
    		}
    		result.get(i).put("detail", detail);
    		result.get(i).put("date", date);		
    	}
    	return result;
    }
    
    public List<Map> exportCheck(Map inMap){
    	List<Map> result=new ArrayList<Map>();
    	Map map=new HashMap();
    	map.put("custName", inMap.get("custName"));
    	map.put("phone", inMap.get("telephone"));
    	map.put("flag", ""+(Integer.parseInt((String) inMap.get("explorWay"))+1));
    	map.put("address", inMap.get("address"));
    	Calendar c=Calendar.getInstance();
    	map.put("year", String.valueOf(c.get(Calendar.YEAR)));
    	String month_info=Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1))<10?"0"+String.valueOf(c.get(Calendar.MONTH) + 1):String.valueOf(c.get(Calendar.MONTH) + 1);
    	map.put("month", "12".equals(month_info)?"13":month_info);
    	map.put("exportTime", inMap.get("exploreTime"));
    	map.put("myselfWay", ""+(Integer.parseInt((String) inMap.get("myselfWay"))+1));
    	map.put("sendWay", inMap.get("sendWay"));
    	result=orderOutDao.exportOrderAllInfo(map);
    	for(int i=0;i<result.size();i++){
    		if(result.get(i).get("EXPORT_TIME")!=null){
    			String time=result.get(i).get("EXPORT_TIME")+"";
    			result.get(i).put("EXPORT_TIME", time.substring(0,10));
    		}else{
    			result.get(i).put("EXPORT_TIME", "");
    		}
    		if("1".equals(result.get(i).get("EXPORT_FLAG"))){
    			result.get(i).put("EXPORT_FLAG","否");
    		}else{
    			result.get(i).put("EXPORT_FLAG","是");
    		}
    		if(result.get(i).get("LABEL_CONTENT")==null){
    			result.get(i).put("LABEL_CONTENT", "");
    		}
    		map.put("distributId", result.get(i).get("DISTRIBUT_ID"));
    		map.put("orderId", result.get(i).get("ORDER_ID"));
    		String detail="";
    		String date="";
    		detail+=("1".equals(result.get(i).get("PRODUCT_TYPE"))?"专业版":"普及版");
    		detail+=("每期"+result.get(i).get("DISTRIBUT_COUNT")+"本");
    		for(int j=i;j<result.size();j++){
    			if(result.get(j).get("DISTRIBUT_ID").toString().equals(result.get(i).get("DISTRIBUT_ID").toString())
    					&& result.get(j).get("ORDER_ID").toString().equals(result.get(i).get("ORDER_ID").toString())){
    				String month=(String) result.get(j).get("MONTH");
    				if("13".equals(result.get(j).get("MONTH"))){
    					month="合刊";
    				}
    				if(j!=i){
    					date+=","+result.get(j).get("YEAR")+"-"+month;
    				}else{
    					date+=result.get(j).get("YEAR")+"-"+month;
    				}
    				if(j!=i){
    					result.remove(j);
    					j--;
    				}
    			}else{
    				result.get(i).put("detail", detail);
    	    		result.get(i).put("date", date);	
    				i=(--j);
    	    		break;
    			}
    		}
    		if(i==result.size()-1){
    			result.get(i).put("detail", detail);
	    		result.get(i).put("date", date);
    		}
    		
    	}
    	return result;
    }
    public void updateExportInfo(List<Map> inMap,HttpSession session){
        
    	for(int i=0;i<inMap.size();i++){
    		Map map=new HashMap();
    		map.put("orderId", inMap.get(i).get("ORDER_ID"));
    		map.put("distributId", inMap.get(i).get("DISTRIBUT_ID"));
    		String type="专业".equals(((String)inMap.get(i).get("detail")).substring(0,2))?"1":"2";
    		map.put("count",((String)inMap.get(i).get("detail")).split("期")[1].split("本")[0]);
    		Calendar c=Calendar.getInstance();
        	map.put("year", String.valueOf(c.get(Calendar.YEAR)));
        	String month_info=Integer.parseInt(String.valueOf(c.get(Calendar.MONTH) + 1))<10?"0"+String.valueOf(c.get(Calendar.MONTH) + 1):String.valueOf(c.get(Calendar.MONTH) + 1);
        	map.put("month", "12".equals(month_info)?"13":month_info);
        	map.put("stockType", inMap.get(i).get("STOCK_ADDRESS"));
        	if("否".equals(inMap.get(i).get("EXPORT_FLAG"))){
        		inMap.get(i).put("EXPORT_FLAG","1");
        	}else if("是".equals(inMap.get(i).get("EXPORT_FLAG"))){
        		inMap.get(i).put("EXPORT_FLAG","2");
        	}
        	LogInfo logUser = (LogInfo) session.getAttribute("logUser");
        	map.put("staffId", logUser.getStaff_id());
    		orderOutDao.updateExport(map);
    		String[] date=((String)inMap.get(i).get("date")).split(",");
        	for(int j=0;j<date.length;j++){
        		String[] date1=date[j].split("-");
        		map.put("year1", date1[0]);
            	map.put("month1", date1[1]);
            	map.put("printCode", map.get("year1")+""+map.get("month1")+""+type);
            	if("1".equals(inMap.get(i).get("EXPORT_FLAG"))){
        			orderOutDao.updateStock(map);
        		}
        	}
        	Map subMap=orderOutDao.selectSub((String) inMap.get(i).get("ORDER_ID"));
        	String subId=subMap.get("SUBSCRIBE_ID").toString();
        	String custType=subMap.get("CUSTOMER_TYPE").toString();
        	if("2673".contains(custType)){
        		int index=orderOutDao.selectFinish(subId);
        		if(index==0){
        			LogInfo log=(LogInfo) session.getAttribute("logUser");
        			orderOutDao.updateSub(subId,log.getStaff_id());
        		}
        	}
    	}
    	
    }
} 