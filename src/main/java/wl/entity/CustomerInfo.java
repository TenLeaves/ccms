package wl.entity;

// 客户信息实体类
public class CustomerInfo {
    private String custId; // 客户id
    private String complany; // 订购单位
    private String name; // 姓名
    private String phone;// 移动电话
    private String custType; // 客户类型  1：大客户  2：批发商
    private String custTypeDes; // 客户类型描述  1：大客户  2：批发商
    private String fixedPhone;// 固话 前台需要做一个拼装
    private String discount; // 折扣率
    private String contactName;// 经联人
    private String contactPhone;// 经联人电话
    private String updateTime;// 更新时间
    private String remark ; // 备注
    private String updateStaff ; // 操作员工
    
    public String getComplany() {
        return complany;
    }
    public void setComplany(String complany) {
        this.complany = complany;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCustType() {
        return custType;
    }
    public void setCustType(String custType) {
        this.custType = custType;
    }
    public String getFixedPhone() {
        return fixedPhone;
    }
    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }
    public String getDiscount() {
        return discount;
    }
    public void setDiscount(String discount) {
        this.discount = discount;
    }
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCustTypeDes() {
        return custTypeDes;
    }
    public void setCustTypeDes(String custTypeDes) {
        this.custTypeDes = custTypeDes;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getCustId() {
        return custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    public String getUpdateStaff() {
        return updateStaff;
    }
    public void setUpdateStaff(String updateStaff) {
        this.updateStaff = updateStaff;
    }
}
