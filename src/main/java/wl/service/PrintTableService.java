package wl.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import wl.dao.PrintTableDao;
import wl.entity.LogInfo;

@Service("printTableService")
public class PrintTableService {
    @Autowired
    @Qualifier("printTableDao")
    private PrintTableDao printTableDao;
    public Map selectTableInfo(){
    	Map returnMap=new HashMap();
    	Map paraMap=new HashMap();
    	
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        String month = StringUtils.leftPad(""+c.get(Calendar.MONTH), 2, "0");
        
        paraMap.put("year",c.get(Calendar.YEAR));
        returnMap.put("year",c.get(Calendar.YEAR));
        paraMap.put("month",month);
        returnMap.put("month",month);
        
        //获取所有批发商的信息
        List<Map> result=printTableDao.selectCustomer();
    	returnMap.put("cust", result);
        for(int i=1;i<=2;i++){
        	paraMap.put("type", i);
        	Map resultMap=printTableDao.selectPage(paraMap);
        	if(resultMap == null){
        		c=Calendar.getInstance();
        		Map inMap=new HashMap();
        		inMap.put("year",c.get(Calendar.YEAR));
        		inMap.put("month",(c.get(Calendar.MONTH))<10?"0"+(c.get(Calendar.MONTH)):(c.get(Calendar.MONTH)));
        		inMap.put("type", i);
        		Map reMap=printTableDao.selectPage(inMap);
            	paraMap.put("type", i);
            	paraMap.put("year", returnMap.get("year"));
            	paraMap.put("month", returnMap.get("month"));
            	List<Map> list1=printTableDao.selectTableInfo(paraMap);
            	int sum=0;
            	for(int j=0;j<list1.size();j++){
            		int index=Integer.parseInt((String) list1.get(j).get("CUSTOMER_TYPE"));
            		if(reMap==null){
            			reMap=new HashMap();
            		}
            		if(index==4||index==5){
            			sum+=Integer.parseInt(list1.get(j).get("num").toString());
            			reMap.put("count4",sum);
            		}else{
            			reMap.put("count"+index,list1.get(j).get("num"));
            		}
            	}
            	if(list1.size()==0){
            		returnMap.put("list"+i, "");
            	}else{
            		returnMap.put("list"+i,reMap);
            	}
        	}else{
        		returnMap.put("list"+i, null);
        	}
        }
    	return returnMap;
    }

    public void updateDetailInfo(Map map,HttpSession session){
    	Eql eql=new Eql();
    	EqlTran eqlTran=eql.newTran();
    	String subId=CreateOrderSubId("printId");
    	Map inMap=new HashMap();
    	inMap.put("year", map.get("year"));
    	inMap.put("month", map.get("month"));
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	inMap.put("staffId",log.getStaff_id());
    	try{
    		inMap.put("printId",subId);
    		inMap.put("printCode", map.get("year")+""+inMap.get("month")+""+map.get("type"));
    		inMap.put("type", map.get("type"));
    		inMap.put("cusCount",  map.get("custNum"));
    		inMap.put("agentCount", map.get("wholesale"));
    		inMap.put("retailCount", map.get("sale"));
    		inMap.put("giftCount", map.get("give"));
    		inMap.put("bjPaperCount", map.get("paper"));
    		inMap.put("interBookCount", map.get("book"));
    		inMap.put("reserveCount", map.get("list"));
    		inMap.put("total", map.get("sum"));
    		printTableDao.insertOrderAllInfo(inMap, eql);
    		List<Map> list=(List<Map>) map.get("cust");
    		Map outMap=new HashMap();
    		outMap.put("printId",subId);
    		for(int j=0;j<list.size();j++){
    			outMap.put("custId", list.get(j).get("CUSTOMER_ID"));
    			if(((List)map.get("num"))!=null && !"".equals(((List)map.get("num")).get(j)+"")){
    				outMap.put("count",((List)map.get("num")).get(j));
    			}else{
    				outMap.put("count", 0);
    			}
    			printTableDao.insertCustPage(outMap, eql);
    		}
    		eqlTran.commit();
    	}catch(Exception e){
    		e.printStackTrace();
    		eqlTran.rollback();
    	}
    }
    public String CreateOrderSubId(String str){
    	String num=printTableDao.seq(str);
    	Date now=new Date();
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    	String date=sdf.format(now);
    	String date_new=date.substring(2,date.length());
    	return date_new+num;
    }

    public List<Map> selectApplyPrintTable(Map paraMap) {
        List<Map> returnList = printTableDao.qryApplyPrintTableInfo(paraMap);
        List<Map> afterTransList = Lists.newArrayList();
        //审批状态展转化描述
        Calendar calendar  = Calendar.getInstance();
        String currYear = calendar.get(Calendar.YEAR)+"";
        String currMonth = StringUtils.leftPad(calendar.get(Calendar.MONTH)+1+"", 2, "0");
        
        for(Map printMap:returnList){
          //没有确认入库，就可以修改（之前是没有审核可以修改，现在不需要审核）
            String checkState = printMap.get("APPROVE_STATUS").toString();
            String inState = printMap.get("STATUS").toString();
            String year = printMap.get("YEAR").toString();
            String month = printMap.get("MONTH").toString();
            
            printMap.put("EDIT", "--");
            if("1".equals(inState)){//当前月的未入库的 可以修改
                //printMap.put("APPROVE_STATUS", "未审核");
                if(currMonth.equals(month)&&currYear.equals(year)){
                    printMap.put("EDIT", "修改");
                }
            }
            afterTransList.add(printMap);
        }
        return afterTransList;
    }

    public Map qryPrintByCode(String printCode) {
        
        return printTableDao.selectPrintByCode(printCode);
    }

    public void updatePrintTbl(Map printTblMap) {
        
        printTableDao.updatePrintTbl(printTblMap);
    }
} 