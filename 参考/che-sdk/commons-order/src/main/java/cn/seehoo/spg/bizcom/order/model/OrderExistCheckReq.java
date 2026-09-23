package cn.seehoo.spg.bizcom.order.model;

public class OrderExistCheckReq {
    /** 客户证件号 */
    private String customerIdcard;
    /** 客户名称 */
    private String customerName;
    /** 车架号 */
    private String vehicleVim;
    /** 车辆类型 */
    private String vehicleTypeCode;

    public String getCustomerIdcard() {
        return customerIdcard;
    }
    public void setCustomerIdcard(String customerIdcard) {
        this.customerIdcard = customerIdcard;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getVehicleVim() {
        return vehicleVim;
    }
    public void setVehicleVim(String vehicleVim) {
        this.vehicleVim = vehicleVim;
    }
    public String getVehicleTypeCode() {
        return vehicleTypeCode;
    }
    public void setVehicleTypeCode(String vehicleTypeCode) {
        this.vehicleTypeCode = vehicleTypeCode;
    }
}
