package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

@Repository("postInfoManageDao")
public class PostInfoManageDao {
    private Eql eql = new Eql();

    public Eql getEql() {
        return eql;
    }

    public void setEql(Eql eql) {
        this.eql = eql;
    }

    public void bindEachSendInfo(Map sendMap) throws Exception {
        eql.update("bindPostInfoToSend").params(sendMap).execute();
    }

    public List<Map> qrySendWithPostInfo(Map inMap) {
        EqlPage page = (EqlPage) inMap.get("eqlPage");
        List<Map> resultMap = eql.select("selectSendWithPostInfo").params(inMap).limit(page).execute();
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean());
//        System.out.println(o.get(0).getRunSql());
        return resultMap;
    }

}
