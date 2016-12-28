package wl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import wl.entity.CustomerInfo;
import wl.entity.LogInfo;
import wl.service.CustomerInfoService;

import com.alibaba.fastjson.JSON;

@Controller
@SessionAttributes({ "selCustPram" })
public class CustomerInfoController {

    @Resource
    CustomerInfoService customerInfoService;
    
    private Log log = LogFactory.getLog(this.getClass());
    // 添加客户信息
    @RequestMapping(value = "/addCust", method = RequestMethod.POST)
    @ResponseBody
    public Map addCust(CustomerInfo cust, HttpServletRequest request) {

        LogInfo logInfo = (LogInfo) (request.getSession()
                .getAttribute("logUser"));
        String staffCode = logInfo.getStaff_id();

        Map<String, String> map = new HashMap<String, String>();

        cust.setUpdateStaff(staffCode);
        try {
            log.info("---------------添加用户-----------------");
            log.info("参数："+cust.getComplany()+","+cust.getContactName()    +"操作人:"+staffCode);
            customerInfoService.addCustomerInfo(cust);
            log.info("---------------添加用户结束-----------------");
            map.put("status", "success");
            return map;

        } catch (Exception e) {
            log.info("---------------添加用户结束-----------------");
            log.info("失败原因:"+e.getMessage());
            
            map.put("status", "error");
            map.put("failInfo", e.getMessage());
            return map;
        }

    }
 // 删除客户信息
    @RequestMapping(value="/deleteCust",method=RequestMethod.POST)
    public String deleteCust(@ModelAttribute("selCustPram")CustomerInfo cust,String custId, HttpServletRequest request,Model model,EqlPage eqlPage){
        HttpSession session = request.getSession();
        LogInfo usr = (LogInfo) session.getAttribute("logUser");
        try{
            log.info("---------------删除用户-----------------");
            log.info("参数:"+custId+"  操作员工:"+usr.getStaff_id());
            
            customerInfoService.deleteCustomerInfo(custId);
            
            log.info("---------------删除用户结束-----------------");
        }catch(Exception e){
            log.info("---------------删除用户失败-----------------");
            log.info("失败原因:"+e.getMessage());
            e.printStackTrace();
        }
        List<CustomerInfo> custList = customerInfoService.selCust(cust, eqlPage);
        
        model.addAttribute("results", custList);
        
        return "pages/cust/custlist";
        
    }
    // 更新客户信息
    @RequestMapping(value = "/updateCust", method = RequestMethod.POST)
    @ResponseBody
    public Map updateCust(CustomerInfo cust, HttpServletRequest request) {

        LogInfo logInfo = (LogInfo) (request.getSession()
                .getAttribute("logUser"));
        String staffCode = logInfo.getStaff_id();
        cust.setUpdateStaff(staffCode);
        
        log.info("---------------更新用户信息-----------------");
        log.info("参数："+cust.getCustId()+"   操作员工："+staffCode);
        customerInfoService.updateCustomerInfo(cust);

        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "success");
        return map;
    }

    // 查询按钮 查询所有客户信息
    @RequestMapping(value = "/selAllCust", method = RequestMethod.POST)
    public String selAllCust(@ModelAttribute("customer") CustomerInfo cust,
            Model model, EqlPage eqlPage) {

        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); // 初始化 eqlPage总数为0， 这样每次查询都是查的最新的总数

        List<CustomerInfo> custList = customerInfoService
                .selCust(cust, eqlPage);

        model.addAttribute("results", custList);

        model.addAttribute("selCustPram", cust);

        return "pages/cust/custlist";
    }

    // 分页查询客户信息
    @RequestMapping(value = "/selCust", method = RequestMethod.POST)
    public String selCust(@ModelAttribute("selCustPram") CustomerInfo cust,
            Model model, EqlPage eqlPage) {

        List<CustomerInfo> custList = customerInfoService
                .selCust(cust, eqlPage);

        model.addAttribute("results", custList);

        return "pages/cust/custlist";
    }

    // 分页查询导入客户错误信息
    @RequestMapping(value = "/selCustError", method = RequestMethod.POST)
    public String selCustError(Model model, EqlPage eqlPage) {

        List<Map> errorList = customerInfoService.selCustError(eqlPage);

        model.addAttribute("errsResults", errorList);

        return "pages/cust/errorlist";
    }

    /**
     * 该方法会在该controler所有的的方法执行器执行这个函数在model中添加了 eqlPage对象
     * 
     * @param eqlPage
     * @return
     */
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(10);
        return eqlPage;
    }

    /**
     * 解析批量上传导入客户信息
     */
    @RequestMapping(value = "/importCust", method = RequestMethod.POST)
    @ResponseBody
    public String importCust(String name,
            @RequestParam("file") CommonsMultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        String resultString = "";

        // 传map 到前台转json，ie竟然出现弹出框，目前没找到解决方案，改成了 传String
        Map<String, String> map = new HashMap<String, String>();

        try {
            LogInfo logInfo = (LogInfo) (request.getSession()
                    .getAttribute("logUser"));
            String staffCode = logInfo.getStaff_id();

            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String fileType = fileName.substring(fileName.lastIndexOf("."));

                Map<String, String[]> titleMap = new HashMap<String, String[]>();
                String[] title = { "complany", "name", "custTypeDes", "phone",
                        "fixedPhone", "discount", "contactName",
                        "contactPhone", "remark" };
                titleMap.put("title", title);

                List infos;
                List errorInfos;
                try {
                    infos = (List) ImportFileHelper.resolveFile(titleMap, file)
                            .get("infos");

                    errorInfos = importValidata(infos);

                    Map dataMap = new HashMap();
                    dataMap.put("infos", infos);
                    dataMap.put("errorInfos", errorInfos);
                    dataMap.put("updateStaff", staffCode);

                    customerInfoService.patchAddCustomerInfo(dataMap);

                    int rightSize = infos.size();
                    int errorSize = errorInfos.size();

                    map.put("status", "success");
                    map.put("rightSize", rightSize + "");
                    map.put("errorSize", errorSize + "");
                    map.put("totalSize", (rightSize + errorSize) + "");

                    resultString += JSON.toJSONString(map);

                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("status", "fail");
                    map.put("error", e.toString());
                    resultString += JSON.toJSONString(map);
                }

            } else {
                map.put("status", "fail");
                map.put("error", "文件导入为空");
                resultString += JSON.toJSONString(map);

            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "fail");
            map.put("error", "文件导入失败");
            resultString += JSON.toJSONString(map);
        }

        return resultString;
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

            // 单位联系人不能为空
            if (impMap.get("complany") == null
                    || "".equals(impMap.get("complany"))) {
                impMap.put("error", "订购单位为空");
                impMap.put("right", "填写订购单位");
                errorList.add(impMap);
                impList.remove(i);

                i--;
                continue;
            }

            // 单位联系人不能为空
            if (impMap.get("name") == null || "".equals(impMap.get("name"))) {
                impMap.put("error", "单位联系人为空");
                impMap.put("right", "填写单位联系人");
                errorList.add(impMap);
                impList.remove(i);

                i--;
                continue;
            }

            // 客户类型
            if (impMap.get("custTypeDes") == null
                    || "".equals(impMap.get("custTypeDes"))) {
                impMap.put("error", "客户类型为空");
                impMap.put("right", "填写客户类型");
                errorList.add(impMap);
                impList.remove(i);
                i--;
                continue;
            } else {
                String custTypeDes = ((String) impMap.get("custTypeDes"))
                        .trim();
                if ("大客户".equals(custTypeDes)) {
                    impMap.put("custType", "1");
                } else if ("批发商".equals(custTypeDes)) {
                    impMap.put("custType", "2");
                } else {
                    impMap.put("error", "客户类型错误");
                    impMap.put("right", "正确填写客户类型");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }

            }

            // 电话号码不能为空，必须为数字

            String phone = ((String) impMap.get("phone")).trim();
            if (impMap.get("phone") != null
                    && StringUtils.isNotEmpty((String) impMap.get("phone"))) {
                if (phone.length() != 11 || !phone.matches("1[0-9]+")) {
                    impMap.put("error", "联系人手机号码不是1开头的11位数字");
                    impMap.put("right", "正确填写联系人手机号码");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }

            // 折扣率不为空的时候，校验折扣率格式
            if (impMap.get("discount") != null
                    && !"".equals(impMap.get("discount"))) {

                String discount = ((String) impMap.get("discount")).trim();

                if (!discount.matches("[1-9]\\d")) {
                    impMap.put("error", "折扣率格式不正确");
                    impMap.put("right", "正确填写折扣率");
                    errorList.add(impMap);
                    impList.remove(i);
                    i--;
                    continue;
                }
            }

            // 判断客户是否已存在
            CustomerInfo cust = new CustomerInfo();
            cust.setComplany((String) impMap.get("complany"));
            cust.setName((String) impMap.get("name"));
            if (customerInfoService.selCustExist(cust)) {
                impMap.put("error", "已存在此客户信息");
                impMap.put("right", "删除此客户信息");
                errorList.add(impMap);
                impList.remove(i);

                i--;
                continue;
            }

        }

        return errorList;
    }

    
}
