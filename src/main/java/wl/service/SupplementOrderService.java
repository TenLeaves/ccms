package wl.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.SupplementOrderDao;
import wl.entity.LogInfo;

@Service("supplementOrderService")
public class SupplementOrderService {
    @Autowired
    @Qualifier("supplementOrderDao")
    private SupplementOrderDao supplementOrderDao;
    public String CreateOrderSubId(String str){
    	String num=supplementOrderDao.CreateOrderId(str);
    	Date now=new Date();
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    	String date=sdf.format(now);
    	String date_new=date.substring(2,date.length());
    	return date_new+num;
    }
    public String insertStockInfo(List<Map> orderList,HttpSession session) throws SQLException{
    	Eql eql=new Eql();
    	EqlTran eqlTran=eql.newTran();
    	String[] list={"杂志社","邮局","印刷厂"};
    	Date now=new Date();
    	LogInfo logUser = (LogInfo) session.getAttribute("logUser");
    	int year=now.getYear()+1900;
    	int month=now.getMonth()+1;
    	for(int i=0;i<orderList.size();i++){
    		List<Map> sendList=(List<Map>) orderList.get(i).get("sendList");
    		int type=Integer.parseInt((String) orderList.get(i).get("productType"))+1;
    		List<String> code=new ArrayList<String>();
			if("0".equals(""+orderList.get(i).get("monthChoice"))){
				String[] startT=((String) orderList.get(i).get("startTime")).split("-");
				String[] endT=((String) orderList.get(i).get("endTime")).split("-");
				if(Integer.parseInt(startT[0])==Integer.parseInt(endT[0])){
					for(int k=Integer.parseInt(startT[1]);k<=Integer.parseInt(endT[1]);k++){
						String str=startT[0];
						if(Integer.parseInt(str)<year || (Integer.parseInt(str)==year && Integer.parseInt(k+"")<=month)){
							str+=""+(k<10?"0"+k:k)+type;
							code.add(str);
						}
					}
				}else{
					for(int k=Integer.parseInt(startT[0]);k<=Integer.parseInt(endT[0]);k++){
						if(k==Integer.parseInt(startT[0])){
							for(int l=Integer.parseInt(startT[1]);l<=12;l++){
								String str=""+k;
								if(Integer.parseInt(str)<year || (Integer.parseInt(str)==year && Integer.parseInt(l+"")<=month)){
									str+=""+(l<10?"0"+l:l)+type;
									code.add(str);
								}
							}
						}else if(k!=Integer.parseInt(endT[0])){
							for(int l=1;l<=12;l++){
								String str=""+k;
								if(Integer.parseInt(str)<year || (Integer.parseInt(str)==year && Integer.parseInt(l+"")<=month)){
									str+=""+(l<10?"0"+l:l)+type;
									code.add(str);
								}
							}
						}else{
							for(int l=1;l<=Integer.parseInt(endT[1]);l++){
								String str=""+k;
								if(Integer.parseInt(str)<year || (Integer.parseInt(str)==year && Integer.parseInt(l+"")<=month)){
									str+=""+(l<10?"0"+l:l)+type;
									code.add(str);
								}
							}
						}
					}
				}
			}else{
				String[] str=((String)orderList.get(i).get("time2")).split(",");
				for(int k=0;k<str.length;k++){
					String[] dateInfo=str[k].split("-");
					String str1=dateInfo[0];
					if(Integer.parseInt(str1)<year || (Integer.parseInt(str1)==year && Integer.parseInt(dateInfo[1])<=month)){
						str1+=""+dateInfo[1]+type;
						code.add(str1);
					}
				}
			}
			for(int j=0;j<sendList.size();j++){
				int count=Integer.parseInt(""+sendList.get(j).get("sendNumber"));
				Map map=new HashMap();
				map.put("stockType", Integer.parseInt((String) sendList.get(j).get("sendAdd"))+1);
				map.put("count", count);
				map.put("staffId", logUser.getStaff_id());
				for(int k=0;k<code.size();k++){
					map.put("printCode", code.get(k));
					int num=supplementOrderDao.selectSumStock(map,eql);
					if(count<=num){
						supplementOrderDao.updateOccupyStock(map,eql);
					}else{
						eqlTran.rollback();
						return code.get(k).substring(0, 4)+"年"+code.get(k).substring(4, 6)+"月"+(code.get(k).substring(6,7)=="1"?"专业版":"普及版")+list[Integer.parseInt(""+map.get("stockType"))-1]+"缺货";
					}
				}
			}
    	}
    	eqlTran.commit();
    	return null;
    }
    public void InsertOrderInfo(Map userMap,List<Map> orderList,HttpSession session){
    	Map map=new HashMap();
    	String subscribeId=CreateOrderSubId("subscribeId");
    	map.put("subscribeId", subscribeId);
    	map.put("customerName",userMap.get("username"));
    	map.put("company", userMap.get("company"));
    	map.put("contactName", userMap.get("contactName"));
    	map.put("contactPhone", userMap.get("contactTel"));
    	map.put("customerPhone", userMap.get("telephone"));
    	map.put("fixedTel", userMap.get("fixedTel"));
    	map.put("customerType", Integer.parseInt((String) userMap.get("suppWay"))+4);
    	map.put("remark", userMap.get("textInfo"));
    	LogInfo logUser=(LogInfo) session.getAttribute("logUser");
       	session.setAttribute("subscribeId",subscribeId);
    	map.put("staffId", logUser.getStaff_id());
		int sum=0;
		for(int i=0;i<orderList.size();i++){
			int count=Integer.parseInt((String)orderList.get(i).get("orderCount"));
			int date=Integer.parseInt(""+orderList.get(i).get("sum"));
			sum+=count*date;
		}
		map.put("sum", sum);
		map.put("totalMoney","0");
		map.put("postMoney","0");
    	try{
    		supplementOrderDao.saveSubscribeInfo(map);
    		for(int i=0;i<orderList.size();i++){
    			Map inMap=new HashMap();
    			inMap.put("subscribeId",map.get("subscribeId"));
    			inMap.put("orderId", orderList.get(i).get("OrderSeq"));
    			inMap.put("productType", Integer.parseInt((String) orderList.get(i).get("productType"))+1);
    			inMap.put("productPerCount", orderList.get(i).get("orderCount"));
    			inMap.put("continMonth", Integer.parseInt(""+orderList.get(i).get("monthChoice"))+1);
    			if("0".equals(""+orderList.get(i).get("monthChoice"))){
    				inMap.put("productStart", orderList.get(i).get("startTime"));
    				inMap.put("productEnd", orderList.get(i).get("endTime"));
    			}else{
    				inMap.put("productStart", orderList.get(i).get("time2"));
    			}
    			inMap.put("staffId", logUser.getStaff_id());
    			supplementOrderDao.SaveOrderInfo(inMap);
    			//入配置表库
    			List<Map> sendList=(List<Map>) orderList.get(i).get("sendList");
    			for(int j=0;j<sendList.size();j++){
    				HashMap resultMap=new HashMap();
    				resultMap.put("orderId", inMap.get("orderId"));
    				resultMap.put("distributId", sendList.get(j).get("sendSeq"));
    				resultMap.put("address", Integer.parseInt((String) sendList.get(j).get("sendAdd"))+1);
    				resultMap.put("count", sendList.get(j).get("sendNumber"));
    				resultMap.put("distributType", Integer.parseInt((String) sendList.get(j).get("getProWay"))+1);
    				resultMap.put("distributAddress", sendList.get(j).get("sendAddress"));
    				resultMap.put("addressName", sendList.get(j).get("getCustomer"));
    				resultMap.put("addressPhone", sendList.get(j).get("getTel"));
    				resultMap.put("zipCode", sendList.get(j).get("postcode"));
    				resultMap.put("remark", sendList.get(j).get("textInfoSend"));
    				resultMap.put("labelContent", sendList.get(j).get("labelContent"));
    				resultMap.put("labelFlag", Integer.parseInt((String) sendList.get(j).get("labelInfo"))+1);
    				resultMap.put("productType", Integer.parseInt((String) orderList.get(i).get("productType"))+1);
    				resultMap.put("staffId", logUser.getStaff_id());
    				if("0".equals(""+orderList.get(i).get("monthChoice"))){
    					String[] startT=((String) orderList.get(i).get("startTime")).split("-");
    					String[] endT=((String) orderList.get(i).get("endTime")).split("-");
    					if(Integer.parseInt(startT[0])==Integer.parseInt(endT[0])){
    						resultMap.put("year", startT[0]);
    						for(int k=Integer.parseInt(startT[1]);k<=Integer.parseInt(endT[1]);k++){
    							resultMap.put("month", k<10?"0"+k:k);
    							supplementOrderDao.saveSendInfo(resultMap);
    						}
    					}else{
    						for(int k=Integer.parseInt(startT[0]);k<=Integer.parseInt(endT[0]);k++){
    							resultMap.put("year", k);
    							if(k==Integer.parseInt(startT[0])){
    								for(int l=Integer.parseInt(startT[1]);l<=12;l++){
    									resultMap.put("month", l<10?"0"+l:l);
    									supplementOrderDao.saveSendInfo(resultMap);
    								}
    							}else if(k!=Integer.parseInt(endT[0])){
    								for(int l=1;l<=12;l++){
    									resultMap.put("month", l<10?"0"+l:l);
    									supplementOrderDao.saveSendInfo(resultMap);
    								}
    							}else{
    								for(int l=1;l<=Integer.parseInt(endT[1]);l++){
    									resultMap.put("month", l<10?"0"+l:l);
    									supplementOrderDao.saveSendInfo(resultMap);
    								}
    							}
    						}
    					}
    				}else{
    					String[] str=((String)orderList.get(i).get("time2")).split(",");
    					for(int k=0;k<str.length;k++){
    						String[] dateInfo=str[k].split("-");
    						resultMap.put("month", dateInfo[1]);
    						resultMap.put("year", dateInfo[0]);
    						supplementOrderDao.saveSendInfo(resultMap);
    					}
    				}
    			}	
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
} 