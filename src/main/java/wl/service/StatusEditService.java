package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Service;

import wl.entity.CustomerInfo;

@Service
public class StatusEditService {
    
    private Eql eql = new Eql();
    
    public Eql getEql() {
		return eql;
	}

	public void setEql(Eql eql) {
		this.eql = eql;
	}

	// 查询配送单状态
    public List<Map> selStatus(Map map,EqlPage eqlPage){
        
        List<Map> statusList = eql.select("selStatus")
                .limit(eqlPage)
                .params(map)
                .execute();
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        for(Map statusMap : statusList){

            String detail="";
            String date="";
            detail+=("1".equals(statusMap.get("productType"))?"专业版":"普及版");
            String month=(String) statusMap.get("month");
            if("13".equals(month)){
                month="合刊";
            }
            date+=statusMap.get("year")+"-"+month;
            detail+=date;
            detail+=("期"+statusMap.get("count")+"本");
            statusMap.put("detail", detail);
            if("2".equals(map.get("exportInfo")+"")){
            	statusMap.put("operate", "退货");
            }
        }
        
        return statusList;
    }
    
    // 更新状态
    public int updateStatus(Map map){
        
        int ret = eql.update("updateStatus")
                .params(map)
                .execute();
        
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        return ret;
    }
    // 更新状态
    public int updateStatusNew(Map map){
        
        int ret = eql.update("updateStatusNew")
                .params(map)
                .execute();
        
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        return ret;
    }
 // 查询配送单的产品
    public Map selectStatusNew(Map map){
        
        Map ret = eql.selectFirst("selectStatusNew")
                .params(map)
                .execute();
        
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        return ret;
    }
 // 更新库存数量
    public int updatestock(Map map){
        
        int ret = eql.update("updateStock")
                .params(map)
                .execute();
        
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        return ret;
    }
}
