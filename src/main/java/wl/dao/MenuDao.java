package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.springframework.stereotype.Repository;

import wl.entity.LogInfo;

import com.google.common.collect.Maps;

@Repository("menuRelatedDao")
public class MenuDao {
    private Eql eql = new Eql();


    public List<Map> qryMenuInfo(LogInfo logUsr,String parent_type) throws Exception{
        Map inputMap = Maps.newHashMap();
        inputMap.put("staff_id", logUsr.getStaff_id());
        inputMap.put("PARENT_TYPE", parent_type);
        return eql.select("selectMenuById").params(inputMap).execute();
    }

}
