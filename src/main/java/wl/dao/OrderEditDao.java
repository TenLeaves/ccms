package wl.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Repository;

@Repository("orderEditDao")
public class OrderEditDao {
	public List<Map> selectOrderEditInfo(Map map,EqlPage eqlPage){
		List<Map> bookInfo = new Eql().select("selectSubInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return bookInfo;
	}

    public void deleteSubScrible(Map inMap) throws Exception{
        // TODO Auto-generated method stub
        new Eql().delete("deleteSubscrible").params(inMap).execute();
    }

    public void deleteChildOrder(Map inMap) {
        new Eql().delete("deleteChildOrder").params(inMap).execute();
        
    }

    public void deleteDistribute(Map inMap) {
       new Eql().delete("deleteDistribute").params(inMap).execute();
    }

}
