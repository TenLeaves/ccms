package wl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import wl.service.PostInfoManageService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class PostInfoManagController {
    @Autowired
    @Qualifier("postInfoManageSrv")
    private PostInfoManageService postInfoManageSrv;

    
    
  //查询配送单
    @RequestMapping(value = "qrySendWithPostInfo")
    public String searchOrder(HttpServletRequest req,EqlPage eqlPage ,Model model) {
        Map paraMap = req.getParameterMap();
        Map inMap = Maps.newHashMap();
        Object conditionObj = paraMap.get("condition");
        List<Map> sends = Lists.newArrayList();
        inMap.put("eqlPage", eqlPage);
        if(conditionObj!=null){
            conditionObj=((String[])conditionObj)[0];
            Map conditionMap=(Map) JSONArray.parse(conditionObj.toString());
            if(notEmpty(conditionMap,"sendId")){
                inMap.put("DISTRIBUT_ID", conditionMap.get("sendId"));
            }
            if(notEmpty(conditionMap,"receivePeople")){
                inMap.put("ADDRESSEE_NAME", conditionMap.get("receivePeople"));
            }
            if(notEmpty(conditionMap,"receivePhone")){
                inMap.put("ADDRESSEE_PHONE", conditionMap.get("receivePhone"));
            }
            if(notEmpty(conditionMap,"receivePhone")){
                inMap.put("DISTRIBUT_ADDRESS", conditionMap.get("receiveAddr"));
            }
            if(notEmpty(conditionMap,"start")){
                inMap.put("START", conditionMap.get("start"));
            }
            if(notEmpty(conditionMap,"end")){
                inMap.put("END", conditionMap.get("end"));
            }
        }
        sends = postInfoManageSrv.searchSendWithPost(inMap);
        model.addAttribute("sendList",sends);
        return "pages/post/sendListFrag";
    }
    private boolean notEmpty(Map conditionObj,String key) {
        return conditionObj.get(key)!=null&&!"".equals(conditionObj.get(key));
    }
    @RequestMapping(value = "/importPostInfo", method = RequestMethod.POST)
    @ResponseBody
    public String handleUploadData(
            @RequestParam("file") CommonsMultipartFile file) {
        String str1 = "";
        Map returnMap = Maps.newHashMap();
        returnMap.put("tag", 0);
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            Map<String, String[]> titleMap = new HashMap<String, String[]>();
            String[] title = { "sendId", "receivePeople", "mobile", "address",
                    "prodDesc", "importTag", "importTime", "postId" };
            titleMap.put("title", title);
            Map map = new HashMap();
            List<Map> infos = new ArrayList();
            List<Map> errorInfos = new ArrayList();
            int successNum = 0;
            returnMap.put("tag", "0");
            try {
                infos = (List<Map>) ImportFileHelper
                        .resolveFile(titleMap, file).get("infos");
                errorInfos = importValidata(infos);
                successNum = postInfoManageSrv.bindPostInfoToSend(infos);
            } catch (Exception e) {
                e.printStackTrace();
                returnMap.put("tag", "1");
                returnMap.put("errorMsg", e.getMessage());
                Map dataMap = Maps.newHashMap();
                dataMap.put("returnMap", returnMap);
                return JSON.toJSONString(dataMap);
            }

            returnMap.put("totalNum", infos.size());
            returnMap.put("succNum", successNum);
            returnMap.put("errorNum", errorInfos.size());
            returnMap.put("errorList", errorInfos);
            Map dataMap = Maps.newHashMap();
            dataMap.put("returnMap", returnMap);
            return JSON.toJSONString(dataMap);
        }
        returnMap.put("tag", 1);
        returnMap.put("errorMsg", "请先选择文件");
        Map dataMap = Maps.newHashMap();
        dataMap.put("returnMap", returnMap);
        return JSON.toJSONString(dataMap);
    }

    /**
     * 校验批量上传客户信息
     * 
     * @throws Exception
     */
    public List importValidata(List impList) throws Exception {
        List errorList = new ArrayList();

        if (impList.size() > 500) {
            throw new Exception("文件条数不能超过500条");
        }

        for (int i = 0; i < impList.size(); i++) {
            Map impMap = (Map) impList.get(i);

            // 库存地址不能为空
            if (impMap.get("sendId") == null
                    || "".equals(impMap.get("sendAdd"))) {
                impMap.put("error", "配送序列为空");
                impMap.put("right", "填写配送序列");
                errorList.add(impMap);
                impList.remove(i);

                i--;
                continue;
            }
        }
        return errorList;
    }
    
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
