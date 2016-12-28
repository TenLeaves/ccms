package wl.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.Eql;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.common.utils.LogUtils;
import wl.common.utils.ProDuctUtils;
import wl.dao.OrderInfoDaoImpl;
import wl.dao.SaleOrderDao;
import wl.entity.CustomerInfo;
import wl.entity.LogInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("orderInfoSrv")
public class OrderInfoService {
    private String[] stockDesc = { "", "杂志社", "邮局", "印刷厂" };
    @Autowired
    @Qualifier("orderInfoDao")
    private OrderInfoDaoImpl orderInfoDaoImpl;

    @Autowired
    @Qualifier("saleOrderDao")
    private SaleOrderDao saleOrderDao;
    
    private Log log = LogFactory.getLog(this.getClass());

    public List<Map> qureyCertainCust(Map paraMap) {
        return orderInfoDaoImpl.qryExisitCust(paraMap);
    }

    public List<CustomerInfo> queryExistCust(Map paraMap) {
        List<Map> custMapList = orderInfoDaoImpl.qryExisitCust(paraMap);
        List<CustomerInfo> customerList = toCustomerList(custMapList);
        return customerList;
    }

    private List<CustomerInfo> toCustomerList(List<Map> custMapList) {
        List<CustomerInfo> customerInfoList = Lists.newArrayList();
        for (Map custMap : custMapList) {
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setCustId(custMap.get("CUSTOMER_ID").toString());
            customerInfo.setName(custMap.get("CUSTOMER_NAME").toString());
            String companly = custMap.get("COMPANY").toString();
            customerInfo.setComplany(companly);
            
            Object phone=custMap.get("CUSTOMER_PHONE");
            if(phone!=null){
                customerInfo.setPhone(custMap.get("CUSTOMER_PHONE").toString());
            }else{
                customerInfo.setPhone("");
            }
            
            customerInfo.setCustType(custMap.get("CUSTOMER_TYPE").toString());
            
            Object discount=custMap.get("DISCOUNT_RATE");
            if(discount!=null){
                customerInfo.setDiscount(custMap.get("DISCOUNT_RATE").toString());
            }else{
                customerInfo.setDiscount("");
            }
            
            Object concatName=custMap.get("CONTACT_NAME");
            if(concatName!=null){
                customerInfo.setContactName(custMap.get("CONTACT_NAME").toString());
            }else{
                customerInfo.setContactName("");
            }
            
            Object concatPhone=custMap.get("CONTACT_PHONE");
            if(concatPhone!=null){
                customerInfo.setContactPhone(custMap.get("CONTACT_PHONE")
                        .toString());
            }else{
                customerInfo.setContactPhone("");
            }
            
            Object remark=custMap.get("REMARK");
            if(remark!=null){
                customerInfo.setRemark(custMap.get("REMARK").toString());
            }else{
                customerInfo.setRemark("");
            }
            
            Object fixtel=custMap.get("FIXED_TELEPHONE");
            if(fixtel!=null){
                customerInfo.setFixedPhone(custMap.get("FIXED_TELEPHONE")
                        .toString());  
            }else{
                customerInfo.setFixedPhone("");
            }
            
            customerInfoList.add(customerInfo);
        }
        return customerInfoList;
    }

    // 查询单个指定的客户信息
    public Map queryCertainCust(Map custMap) {
        Map resultMap = orderInfoDaoImpl.qryCertainCust(custMap);
        return resultMap;
    }

    // 保存订单
    public String insertOrderInfo(Map userMap, List<Map> orderList,
            HttpSession session) throws Exception {
        log.info("-------------添加订单------------");
        Map custMap = new HashMap();
        String subscribeId = saleOrderDao.CreateOrderId("subscribeId");
        SimpleDateFormat sdfm = new SimpleDateFormat("yyMMdd");
        String preStr = sdfm.format(new Date());
        subscribeId = preStr + subscribeId;
        custMap.put("subscribeId", subscribeId);
        custMap.put("username", userMap.get("CUSTOMER_NAME"));
        custMap.put("telephone", userMap.get("CUSTOMER_PHONE"));
        custMap.put("fixedTel", userMap.get("FIXED_TELEPHONE"));
        custMap.put("fixedTel", userMap.get("FIXED_TELEPHONE"));
        custMap.put("textInfo", userMap.get("REMARK"));
        // 大客户 批发商  报刊发行局  发行站
        custMap.put("discount", userMap.get("DISCOUNT_RATE"));
        String custType=userMap.get("CUSTOMER_TYPE").toString();
        custMap.put("custType", custType);
        if("267".contains(custType)){
            custMap.put("SUBSCRIBE_STATE", "2");
        }else{
            custMap.put("SUBSCRIBE_STATE", "1");
        }
        
        
        custMap.put("company", userMap.get("COMPANY"));
        custMap.put("contactName", userMap.get("CONTACT_NAME"));
        custMap.put("contactPhone", userMap.get("CONTACT_PHONE"));
        LogInfo logUser=null;
        try {
           logUser = (LogInfo) session.getAttribute("logUser");
        } catch (Exception e) {
            throw new Exception("由于您长时间没有操作，请退出后重新登录");
        }
        
        custMap.put("staffId", logUser.getStaff_id());
        custMap.put("sendFirst", userMap.get("sendFirst"));
        custMap.put("isNeedTicket", userMap.get("ticket"));
        custMap.put("isTypeFirst", userMap.get("typeFirst"));

        int sum = 0;
        for (int i = 0; i < orderList.size(); i++) {
            int count = Integer.parseInt((String) orderList.get(i)
                    .get("amount"));
            int date = getDateNum(orderList.get(i));
            sum += count * date;
        }
        custMap.put("sum", sum);
        custMap.put("totalMoney", countNum(orderList, 1));
        EqlTran tran = new Eql().newTran();
        try {
            
            log.info("生成总订单:"+custMap.toString()+"\n");
            
            orderInfoDaoImpl.saveSubscribeInfo(custMap);
            for (int i = 0; i < orderList.size(); i++) {
                Map inMap = new HashMap();
                inMap.put("subscribeId", custMap.get("subscribeId"));
                inMap.put("orderId", orderList.get(i).get("orderId"));
                inMap.put("subscribeType",
                        (String) orderList.get(i).get("orderWay"));
                // inMap.put("needInvoice", (String)
                // orderList.get(i).get("fapiao"));
                // inMap.put("firstSend",(String)
                // orderList.get(i).get("sendAny"));
                inMap.put("productType",
                        (String) orderList.get(i).get("prodmode"));
                inMap.put("productPerCount", orderList.get(i).get("amount"));
                inMap.put("continMonth", orderList.get(i).get("consistant"));
                inMap.put("productStart", orderList.get(i).get("start"));
                inMap.put("productEnd", orderList.get(i).get("end"));
                inMap.put("staffId", logUser.getStaff_id());
                
                log.info("生成子订单:"+inMap.toString()+"\n");
                saleOrderDao.SaveOrderInfo(inMap);

                List<Map> sendList = (List<Map>) orderList.get(i).get("sendList");
                log.info("生成配送信息:"+sendList.toString()+"\n");
                for (int j = 0; j < sendList.size(); j++) {
                    HashMap resultMap = new HashMap();
                    resultMap.put("orderId", inMap.get("orderId"));
                    resultMap
                            .put("distributId", sendList.get(j).get("orderId"));
                    resultMap.put("address", sendList.get(j).get("profrom"));
                    resultMap.put("count", sendList.get(j).get("amout"));
                    resultMap.put("distributType",
                            sendList.get(j).get("sendway"));
                    resultMap.put("distributAddress",
                            sendList.get(j).get("addr"));
                    resultMap.put("addressName", sendList.get(j).get("name"));
                    resultMap.put("addressPhone", sendList.get(j).get("phone"));
                    resultMap.put("zipCode", sendList.get(j).get("postcode"));
                    resultMap.put("labelFlag", sendList.get(j).get("bookmark"));
                    resultMap.put("labelContent", sendList.get(j)
                            .get("markVal"));
                    resultMap.put("remark", sendList.get(j)
                            .get("remark"));
                    

                    resultMap.put("productType",
                            orderList.get(i).get("prodmode"));
                    if ("1".equals(orderList.get(i).get("consistant"))) {
                        String[] startT = ((String) orderList.get(i).get(
                                "start")).split("-");
                        String[] endT = ((String) orderList.get(i).get("end"))
                                .split("-");
                        if (Integer.parseInt(startT[0]) == Integer
                                .parseInt(endT[0])) {
                            resultMap.put("year", startT[0]);
                            for (int k = Integer.parseInt(startT[1]); k <= Integer
                                    .parseInt(endT[1]); k++) {
                                resultMap.put("month", k < 10 ? "0" + k : k);
                                saleOrderDao.saveSendInfo(resultMap);
                            }
                        } else {
                            for (int k = Integer.parseInt(startT[0]); k <= Integer
                                    .parseInt(endT[0]); k++) {
                                resultMap.put("year", k);
                                if (k == Integer.parseInt(startT[0])) {
                                    for (int l = Integer.parseInt(startT[1]); l <= 12; l++) {
                                        resultMap.put("month", l < 10 ? "0" + l
                                                : l);
                                        saleOrderDao.saveSendInfo(resultMap);
                                    }
                                } else if (k != Integer.parseInt(endT[0])) {
                                    for (int l = 1; l <= 12; l++) {
                                        resultMap.put("month", l < 10 ? "0" + l
                                                : l);
                                        saleOrderDao.saveSendInfo(resultMap);
                                    }
                                } else {
                                    for (int l = 1; l <= Integer
                                            .parseInt(endT[1]); l++) {
                                        resultMap.put("month", l < 10 ? "0" + l
                                                : l);
                                        saleOrderDao.saveSendInfo(resultMap);
                                    }
                                }
                            }
                        }
                    } else {
                        String[] str = ((String) orderList.get(i).get("start"))
                                .split(",");
                        for (int k = 0; k < str.length; k++) {
                            String[] dateInfo = str[k].split("-");
                            resultMap.put("month", dateInfo[1]);
                            resultMap.put("year", dateInfo[0]);
                            saleOrderDao.saveSendInfo(resultMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tran.rollback();
        }
        tran.commit();
        log.info("----------------订单生成完毕---------------");
        
        return subscribeId;
    }

    private int getDateNum(Map map) {
        int totalNum = 0;
        String isConsistant = map.get("consistant").toString();
        String startTime = map.get("start").toString();
        String endTime = map.get("end").toString();
        if ("1".equals(isConsistant)) {// 连续
            String start[] = startTime.split("-");
            String end[] = endTime.split("-");
            totalNum = (parseInt(end[0]) - parseInt(start[0])) * 12
                    - parseInt(start[1]) + parseInt(end[1]) + 1;
        } else {// 不连续
            totalNum = startTime.split(",").length;
        }
        return totalNum;
    }

    // 查询回款单信息
    public List<Map> queryPayMent(Map paraMap) {

        return orderInfoDaoImpl.selectPayment(paraMap);
    }

    public List<Map> queryOrder(Map paraMap) {

        return orderInfoDaoImpl.selectOrder(paraMap);
    }

    // 帮顶回款单
    public void bindPaymentToOrder(List<Map> paymentList, Map orderMap) {

        orderInfoDaoImpl.bindpayMent(paymentList, orderMap);
    }

    // 校验所选的产品中以往产品时候有库存
    public void verifyStockHasEnfProd(List<Map> orders, HttpServletRequest req)
            throws Exception {
        LogInfo logUser= null;
        try {
            logUser = (LogInfo) req.getSession().getAttribute("logUser");   
        } catch (Exception e) {
            throw new Exception("由于您长时间没有操作，请退出后重新登录");
        }
        
        EqlTran tran = new Eql().newTran();
        for (Map eachOrder : orders) {// 遍历订单
            String start = (String) eachOrder.get("start"), end = (String) eachOrder
                    .get("end"), amount = (String) eachOrder.get("amount"), mode = (String) eachOrder
                    .get("prodmode");
            try {
                if ("1".equals(eachOrder.get("consistant").toString())) {// 连续
                    validateWhenOrderIsLianxu(eachOrder, start, mode);
                } else {// 不连续
                    eachOrder.put("staffId", logUser.getStaff_id());
                    validateWhenOrderIsNotLianxu(eachOrder, start, mode);
                }
            } catch (Exception e) {
                // 校验库存失败回滚
                tran.rollback();
                throw e;
            }
        }
        tran.commit();
    }

    /**
     * 当产品为不连续月
     * 
     * @param eachOrder
     * @param start
     * @param mode
     * @throws Exception
     */
    private void validateWhenOrderIsNotLianxu(Map eachOrder, String start,
            String mode) throws Exception {
        String prods[] = start.split(",");
        for (String prod : prods) {
            int currYear = parseInt(getCurrentYear().toString());
            int currMonth = parseInt(getCurrentMonth().toString());
            Map tempMap = Maps.newHashMap();
            if (parseInt(prod.substring(0, 4)) <= currYear) {// 只校验以往的产品库存是否足够
                if (parseInt(prod.substring(5)) <= currMonth) {
                    String printCode = prod.substring(0, 4) + prod.substring(5)
                            + mode;
                    tempMap.put("PRINT_CODE", printCode);
                    validateEachSendAmount(eachOrder, mode, tempMap);
                }
            }

        }
    }

    /**
     * 当订单中的产品为连续月
     * 
     * @param eachOrder
     * @param start
     * @param mode
     * @throws Exception
     */
    private void validateWhenOrderIsLianxu(Map eachOrder, String start,
            String mode) throws Exception {
        int startYear = parseInt(start.substring(0, 4));
        int endYear = parseInt(getCurrentYear().toString());
        int startMonth = parseInt(start.substring(5));
        int endMonth = parseInt(getCurrentMonth().toString());
        int totalYear = endYear - startYear;
        if (totalYear > 0) {// 存在跨年
            for (int i = 0; i <= totalYear; i++) {
                if (i == 0) {// 第一年
                    for (int j = startMonth; j <= 12; j++) {
                        Map tempMap = Maps.newHashMap();
                        tempMap.put("PRINT_CODE",
                                startYear + StringUtils.leftPad("" + j, 2, "0")
                                        + mode);

                        validateEachSendAmount(eachOrder, mode, tempMap);

                    }
                } else if (i > 0 && i == totalYear - 1) {
                    for (int k = 1; k <= 12; k++) {
                        Map tempMap = Maps.newHashMap();
                        tempMap.put(
                                "PRINT_CODE",
                                i + startYear
                                        + StringUtils.leftPad("" + k, 2, "0")
                                        + mode);
                        validateEachSendAmount(eachOrder, mode, tempMap);
                    }
                } else {// 最后一年
                    for (int m = 1; m <= endMonth; m++) {
                        Map tempMap = Maps.newHashMap();
                        tempMap.put("PRINT_CODE", i + startYear + "-"
                                + StringUtils.leftPad("" + m, 2, "0") + mode);
                        validateEachSendAmount(eachOrder, mode, tempMap);
                    }
                }
            }
        } else {// 不跨年
            for (int n = startMonth; n <= endMonth; n++) {
                Map tempMap = Maps.newHashMap();
                tempMap.put("PRINT_CODE",
                        startYear + StringUtils.leftPad("" + n, 2, "0") + mode);
                validateEachSendAmount(eachOrder, mode, tempMap);
            }
        }
    }

    /**
     * 遍历并校验配送当中的数量，校验通过预占产品
     * 
     * @param eachOrder
     * @param mode
     * @param tempMap
     * @throws Exception
     */
    private void validateEachSendAmount(Map eachOrder, String mode, Map tempMap)
            throws Exception {
        List<Map> sendList = (List<Map>) eachOrder.get("sendList");
        for (Map eachSend : sendList) {
            String stockType = eachSend.get("profrom").toString();
            tempMap.put("STOCK_TYPE", stockType);
            Map resultMap = orderInfoDaoImpl
                    .queryStockAmountByPrintCode(tempMap);
            if (resultMap != null) {
                int wantAmount = parseInt(eachSend.get("amout").toString());
                int reaminCount = parseInt(resultMap.get("REMAIN_COUNT")
                        .toString());
                int hasOccupyAmt = parseInt(resultMap.get("OCCUPY_AMOUNT")
                        .toString());
                //int leftAmount = reaminCount - hasOccupyAmt;
                int leftAmount = reaminCount ;
                if (wantAmount > leftAmount) {
                    String prodDescString = ProDuctUtils.prodToDesc(tempMap
                            .get("PRINT_CODE").toString());
                    throw new Exception("您提交的订单中产品[" + prodDescString + "] 在 ["
                            + stockDesc[parseInt(stockType)] + "]没有足够的库存");
                }

                // 校验通过预占产品
                tempMap.put("OCCUPY_AMOUNT", hasOccupyAmt + wantAmount);
                orderInfoDaoImpl.occupyBookAftCheck(tempMap);
            } else {
                String prodDescString = ProDuctUtils.prodToDesc(tempMap.get(
                        "PRINT_CODE").toString());
                throw new Exception("您提交的订单中产品[" + prodDescString + "] 在 ["
                        + stockDesc[parseInt(stockType)] + "]没有足够的库存");
            }

        }
    }

    // 计算总钱数
    private int countNum(List<Map> orderList, int custType) {
        int sum = 0;
        // 总钱数
        int total = 0;
        for (int i = 0; i < orderList.size(); i++) {
            sum = 0;
            // 每月订阅的数量
            int count = Integer.parseInt((String) orderList.get(i)
                    .get("amount"));
            // 订阅的月数
            int date = getTotalMonth(orderList.get(i));
            // 不连续月存在合刊，判断是否为不连续月
            if ("1".equals("" + orderList.get(i).get("consistant"))) {
                String[] str = ((String) orderList.get(i).get("start"))
                        .split(",");
                for (int k = 0; k < str.length; k++) {
                    String[] dateInfo = str[k].split("-");
                    // 如果是合刊，就把订阅月数加11
                    if ("13".equals(dateInfo[1])) {
                        date += 11;
                    }
                }
            }
            List<Map> sendList = (List<Map>) orderList.get(i).get("sendList");
            // 订购的产品类型
            int productType = Integer.parseInt((String) orderList.get(i).get(
                    "prodmode"));
            for (int j = 0; j < sendList.size(); j++) {
                // 月配送数量
                int sendNumber = Integer.parseInt(sendList.get(j).get(
                        "amout")
                        + "");
                // 配送方式
                int distributType = Integer.parseInt((String) sendList.get(j)
                        .get("sendway"));
                // 需要邮递的数量（除去自提）
                if (distributType != 1) {
                    sum += sendNumber;
                }
            }
            // 产品相应的钱数
            int money = (productType == 1) ? 50 : 38;
            // 不需要邮费的钱数
            total += (count - sum) * date * money;
            int postNum = 0;
            // 需要邮递的总钱数
            postNum = sum * money * date;
            if (custType == 2) {
                // 计算邮费
                if (sum >= 12) {
                    postNum += 0;
                } else if (sum >= 2) {
                    postNum += postNum * 0.15;
                } else {
                    postNum += 8;
                }
            }
            total += postNum;
        }
        return total;
    }

    private int getTotalMonth(Map map) {
        String consis = map.get("consistant").toString();
        String start = (String) map.get("start");
        String end = map.get("end").toString();
        if ("1".equals(consis)) {// 连续2014-05
            return getMonthSpace(start, end);
        }
        return start.split(",").length;

    }

    public int getMonthSpace(String date1, String date2) {

        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(sdf.parse(date1));
            c2.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return result == 0 ? 1 : (Math.abs(result)+1);

    }

    private Object getCurrentYear() {
        String year = Calendar.getInstance().get(Calendar.YEAR) + "";
        return year;
    }

    private Object getCurrentMonth() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return StringUtils.leftPad("" + month, 2, "0");
    }

    private int parseInt(String substring) {
        return Integer.parseInt(substring);
    }

    public Map searchTotalMoneyHasBind(Map orderMap) {

        return orderInfoDaoImpl.searchTotalMoneyHasBind(orderMap);
    }

    public void updateSubscribleState(Map orderMap) {
        orderInfoDaoImpl.updateSubscribleState(orderMap);

    }

    public void addCustomerInfo(Map inMap) {
        orderInfoDaoImpl.addNewCustomer(inMap);
        
    }
}
