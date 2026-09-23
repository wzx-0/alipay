package cn.seehoo.spg.common.risk.model;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class CreditRiskReq extends BaseReq {

    /**
     * 流水号
     */
    private String businessId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 证件号码
     */
    private String idNo;

    /**
     * 流水指标数据
     */
    private List<Bill> bills;

    public CreditRiskReq convertBills(String yxJson, List<BHData> bhDatas){
        this.bills = bhDatas.stream()
                .map(JSON::toJSONString)
                .map(json -> new Bill("BH", json))
                .collect(Collectors.toList());
        this.bills.add(new Bill("YX", yxJson));
        return this;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bill {
        /**
         * 数据源类型 YX: 易鑫流水指标数据 BH: 百行信鸽流水指标数据
         */
        private String type;

        /**
         * json数据体
         */
        private String data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BHData {
        /** 标准字段 */
        private String standardField;
        /** 特征变量 */
        private String characteristicValue;
    }

}
