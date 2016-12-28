package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import wl.common.utils.ProDuctUtils;
import wl.dao.PostInfoManageDao;

@Service("postInfoManageSrv")
public class PostInfoManageService {
    @Autowired
    @Qualifier("postInfoManageDao")
    private PostInfoManageDao postInfoManageDao;

    public int bindPostInfoToSend(List<Map> infos) throws Exception {
        //"sendId", "receivePeople", "mobile", "address","prodDesc", "importTag", "importTime", "postId" 
        EqlTran tran = postInfoManageDao.getEql().newTran();
        int successNum=0; 
        postInfoManageDao.getEql().startBatch();
        for(Map sendMap:infos){
            try {
                if(sendMap.get("sendId") != null
                    &&! "".equals(sendMap.get("sendId"))){//有配送单号才绑定
                    postInfoManageDao.bindEachSendInfo(sendMap); 
                }
            } catch (Exception e) {
                tran.rollback();
                throw e;
            }
        }
       successNum =  postInfoManageDao.getEql().executeBatch();
        tran.commit();
        return successNum;
    }

    public List<Map> searchSendWithPost(Map inMap) {
        if(notEmpty(inMap,"START")){//2010-02
            String startYear = inMap.get("START").toString().substring(0,4);
            String startMon = inMap.get("START").toString().substring(5);
            inMap.put("START_YEAR", startYear);
            inMap.put("START_MON", startMon);
        }
        if(notEmpty(inMap,"END")){//2010-02
            String endYear = inMap.get("END").toString().substring(0,4);
            String endMon = inMap.get("END").toString().substring(5);
            inMap.put("END_YEAR", endYear);
            inMap.put("END_MON", endMon);
        }
        List<Map> resultList = postInfoManageDao.qrySendWithPostInfo(inMap);
        resultList = transProdDesc(resultList);
        return resultList;
    }
    private List<Map> transProdDesc(List<Map> resultList) {
        List<Map> list = Lists.newArrayList();
        for(Map sendMap : resultList){
            String prodCode = sendMap.get("YEAR").toString()+sendMap.get("MONTH").toString()+sendMap.get("PRODUCT_TYPE").toString();
            sendMap.put("PRODUCT_DESC", ProDuctUtils.prodToDesc(prodCode));
            list.add(sendMap);
        }
        return list;
    }

    private boolean notEmpty(Map conditionObj,String key) {
        return conditionObj.get(key)!=null&&!"".equals(conditionObj.get(key));
    }

}
