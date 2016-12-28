package wl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.dao.OrderEditDao;
import wl.entity.LogInfo;

@Service("orderEditService")
public class OrderEditService {
    @Autowired
    @Qualifier("orderEditDao")
    private OrderEditDao orderEditDao;

	public List<Map> appCheck(Map userMap, EqlPage eqlPage,HttpSession session) {
		Map map=new HashMap();
		if(userMap.get("orderSeq")!=null && !"".equals(userMap.get("orderSeq"))){
			map.put("orderSeq", "%"+userMap.get("orderSeq")+"%");
		}
		map.put("startTime", userMap.get("startTime"));
		map.put("endTime", userMap.get("endTime"));
		if(userMap.get("custName")!=null && !"".equals(userMap.get("custName"))){
			map.put("custName", "%"+userMap.get("custName")+"%");
		}
		if(userMap.get("address")!=null && !"".equals(userMap.get("address"))){
			map.put("address", "%"+userMap.get("address")+"%");
		}
		map.put("subType", userMap.get("subType"));
    	LogInfo log=(LogInfo) session.getAttribute("logUser");
    	String right=log.getRoleName();
    	int role=right.indexOf("领导");
    	if(role>=0){
			map.put("staffRight", 1+"");
		}else{
			map.put("staffRight", 0+"");
			map.put("staff_id",log.getStaff_id());
		}
		List<Map> result=orderEditDao.selectOrderEditInfo(map, eqlPage);
		if(result.size()==1 && result.get(0).get("num")==null){
			result.remove(0);
		}
		
		for(int i=0;i<result.size();i++){
			String time=(""+result.get(i).get("ACCEPT_TIME")).substring(0,10);
			result.get(i).put("ACCEPT_TIME",time);
			if(result.get(i).get("COMPANY")==null){
				result.get(i).put("COMPANY","");
			}
			if(result.get(i).get("TOTAL_MONEY")==null){
				result.get(i).put("TOTAL_MONEY","0");
			}
			String state=(String) result.get(i).get("SUBSCRIBE_STATE");
			String sendFirst=(String) result.get(i).get("FIRST_SEND");
			if("2".equals(state)){
				result.get(i).put("state", "待配送");
				result.get(i).put("classType", "icon-one");
			}else if("3".equals(state)){
				result.get(i).put("state", "已完成");
				result.get(i).put("classType", "icon-four");
			}else if(("1".equals(state) && "1".equals(sendFirst))|| "4".equals(result.get(i).get("CUSTOMER_TYPE")) || "5".equals(result.get(i).get("CUSTOMER_TYPE"))){
				result.get(i).put("state", "待审批");
				result.get(i).put("classType", "icon-three");
			}else{
				result.get(i).put("state", "待收款");
				result.get(i).put("classType", "icon-two");
			}
		}
		return result;
	}
	
	//根据总订单删除订单
    public void deleteOrder(Map inMap) throws Exception{
        EqlTran tran = new Eql().newTran();
        try {
            //删除配送列表
            orderEditDao.deleteDistribute(inMap);
          //删除子订单
            orderEditDao.deleteChildOrder(inMap);
            //删除总订单
            orderEditDao.deleteSubScrible(inMap);
            
            
            
            tran.commit();
        } catch (Exception e) {
            tran.rollback();
            throw e;
        }
    }
   
} 