package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Service;

import wl.entity.CustomerInfo;

@Service
public class CustomerInfoService {
    
    // 添加客户信息
    public void addCustomerInfo(CustomerInfo cust) throws Exception{
        
        CustomerInfo custmomer = new Eql().selectFirst("selCustExist")
                .returnType(CustomerInfo.class)
                .params(cust)
                .execute();
        
        if(custmomer != null){
            throw new Exception("此客户已存在，请重新添加");
        }
        
        new Eql().insert("addCustomerInfo")
                .params(cust)
                .execute();
        
    }
    
    // 删除客户信息
    public void deleteCustomerInfo(String custId) throws Exception{
        
       int index=new Eql().delete("deleteCust")
                .params(custId)
                .execute();
        
        if(index != 1){
            throw new Exception("删除失败，请重试");
        }
        
    }
    // 查询客户是否已存在信息
    public boolean selCustExist(CustomerInfo cust){
        
        CustomerInfo custmomer = new Eql().selectFirst("selCustExist")
                .returnType(CustomerInfo.class)
                .params(cust)
                .execute();
        
        if(custmomer != null){
            return true;
        }
        return false; 
    }
    
    // 批量添加客户信息
    public void patchAddCustomerInfo(Map praMap){
        
        Eql esql = new Eql();
        
        List infos = (List)praMap.get("infos");
        List errorInfos = (List)praMap.get("errorInfos");
        String updateStaff = (String)praMap.get("updateStaff");
        
        // 插入客户信息
        esql.startBatch(500);
        for (int i = 0; i < infos.size(); ++i) {
            Map custMap = (Map)infos.get(i);
            if(custMap.get("phone")!=null){
                custMap.put("phone", ((String)custMap.get("phone")).trim());
            }else{
                custMap.put("phone", "");
            }
            
            custMap.put("complany", ((String)custMap.get("complany")).trim());
            custMap.put("name", ((String)custMap.get("name")).trim());
            if(custMap.get("fixedPhone")!=null){
                custMap.put("fixedPhone", ((String)custMap.get("fixedPhone")).trim());
            }else{
                custMap.put("fixedPhone", "--");
            }
            
            if(custMap.get("contactName")!=null){
                custMap.put("contactName", ((String)custMap.get("contactName")).trim());
            }else{
                custMap.put("contactName", "");
            }
            
            
            if(custMap.get("discount")!=null){
                custMap.put("discount", ((String)custMap.get("discount")).trim());
            }else{
                custMap.put("discount", "");
            }
            
            if(custMap.get("contactPhone")!=null){
                custMap.put("contactPhone", ((String)custMap.get("contactPhone")).trim());
            }else{
                custMap.put("contactPhone", "");
            }
            if(custMap.get("remark")!=null){
                custMap.put("remark", ((String)custMap.get("remark")).trim());
            }else{
                custMap.put("remark", "");
            }
            
            custMap.put("updateStaff", updateStaff);
            int ret = esql.insert("addCustomerInfo")
                   .params(custMap)
                   .execute();
        }

        esql.executeBatch();
        
        // 先删除错误临时表
        esql.delete("delCustError").execute();
        
        // 插入错误临时表
        esql.startBatch(500);
        for (int i = 0; i < errorInfos.size(); ++i) {
            Map errorMap = (Map)errorInfos.get(i);
            int ret = esql.insert("addErrorInfo")
                   .params(errorMap)
                   .execute();
        }

        esql.executeBatch();
       
    }
    
    // 更新客户信息
    public void updateCustomerInfo(CustomerInfo cust){
        
        new Eql().update("updateCustomerInfo")
                .params(cust)
                .execute();
    }
    
    // 查询客户信息
    public List<CustomerInfo> selCust(CustomerInfo cust,EqlPage eqlPage){
        
        List<CustomerInfo> customerList = new Eql().select("selCust")
                .limit(eqlPage)
                .returnType(CustomerInfo.class)
                .params(cust)
                .execute();
        
        return customerList;
    }
    
    // 查询导入客户错误信息
    public List<Map> selCustError(EqlPage eqlPage){
        
        List<Map> custErrorList = new Eql().select("selCustError")
                .limit(eqlPage)
                .execute();
        
        return custErrorList;
    }
    
    // 导出客户错误信息
    public List<Map> selAllCustError(){
        
        List<Map> custErrorList = new Eql().select("selCustError").execute();
        
        return custErrorList;
    }
}
