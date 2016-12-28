package wl.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Repository;

import freemarker.template.utility.StringUtil;

@Repository("printDetailDao")
public class PrintDetailDao {
	public Map selectOrderAllInfo(String map){
		Map bookInfo = new Eql().selectFirst("selectPageDetail")
                .params(map)
                .execute();
        
        return bookInfo;
	}
	public List<Map> selectCustInfo(String map){
		List<Map> bookInfo = new Eql().select("selectCustInfo")
                .params(map)
                .execute();
        
        return bookInfo;
	}
}
