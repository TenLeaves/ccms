package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Repository;

@Repository("approvalCheckDao")
public class ApprovalCheckDao {
	public List SendFeeInfo(Map map,EqlPage eqlPage){
		List<Map> sendFeeInfo = new Eql().select("sendFeeInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return sendFeeInfo;
	}
	public List sendInvoiceInfo(Map map,EqlPage eqlPage){
		List<Map> sendFeeInfo = new Eql().select("sendInvoiceInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return sendFeeInfo;
	}
	public List printCheckInfo(Map map,EqlPage eqlPage){
		List<Map> printCheckInfo = new Eql().select("printCheckInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return printCheckInfo;
	}
	public List printAllCheckInfo(Map map,EqlPage eqlPage){
		List<Map> printCheckInfo = new Eql().select("printAllCheckInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return printCheckInfo;
	}
	public List supplementInfo(Map map,EqlPage eqlPage){
		List<Map> supplementInfo = new Eql().select("supplementInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return supplementInfo;
	}
	public List bookInfo(Map map,EqlPage eqlPage){
		List<Map> bookInfo = new Eql().select("bookInfo")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return bookInfo;
	}
}
