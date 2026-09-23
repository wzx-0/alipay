package cn.seehoo.spg.common.opl.model.dto.xkl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 鑫快链结果同步（流水+报告） - 请求实体类
 * 接口：鑫快链结果同步（流水+报告）
 * 请求方式：HTTPS/POST
 */
@Data
public class ResultSyncRequest {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 华夏订单号
     */
    private String orderNo;

    /**
     * 证件号
     */
    private String cardNo;

    /**
     * 文件信息
     */
    private List<FileItem> fileList = new ArrayList<>();;

    /**
     * 报告数据
     */
    private ReportInfo reportInfo;

    /**
     * 流程类型：10001预审，10002终审，10003终审请款
     */
    private String processType;

    /**
     * 回传链接attach
     */
    private String urlAttach;

    /**
     * 文件信息项
     */
    @Data
    public static class FileItem {

        /**
         * 百行标准全量文件Id
         */
        private List<String> creditStandardJsonFileId;

        /**
         * 百行特征全量文件Id
         */
        private List<String> creditFeatureJsonFileId;

        /**
         * 流水源文件列表
         */
        private List<String> sourceFileId;

        /**
         * 解析后的json文件列表
         */
        private List<String> jsonFileId;

        /**
         * 文件类型：支付宝，微信，银行。。。，交管12123(多项):site：app-tmri-any
         */
        private String siteType;

        /**
         * 文件类型：支付宝，微信，银行。。。交管12123(多项):site：app-tmri-any
         */
        private String siteName;

        /**
         * 1: 个税，2: 公积金，3:支付宝四合一，5: 银行，6：微信，7：支付宝，8：驾照，9：学历，11：新公积金，12：抖音达人，13：交管12123
         */
        private String fileType;

        /**
         * app-tmri-xsz 行驶证、app-tmri-jsz 驾驶证、app-tmri-jsjl 安全行驶记录
         */
        private String subSiteType;

        /**
         * app-tmri-xsz 行驶证、app-tmri-jsz 驾驶证、app-tmri-jsjl 安全行驶记录
         */
        private String subSiteName;
    }

    /**
     * 报告数据
     */
    @Data
    public static class ReportInfo {

        /**
         * 基本信息
         */
        private BasicInformation basicInformation;

        /**
         * 客户画像
         */
        private CustomerPortrait customerPortrait;

        /**
         * 收入负债
         */
        private IncomeLiabilities incomeLiabilities;

        /**
         * 用车指数
         */
        private VehicleIndex vehicleIndex;

        /**
         * 欺诈风险
         */
        private FraudRisk fraudRisk;

        /**
         * 建议额度
         */
        private SuggestedLimit suggestedLimit;

        /**
         * 公共参数
         */
        private CommonParameters commonParameters;

        /**
         * 报告pdf地址
         */
        private String reportFileId;

        /**
         * 报告时间(取生成报告的时间戳，报告只展示年/月/日)
         */
        private String reportTime;

        /**
         * 报告编号
         */
        private String reportNumber;
    }

    /**
     * 基本信息
     */
    @Data
    public static class BasicInformation {

        /**
         * 姓名
         */
        private String name;

        /**
         * 户籍
         */
        private String hometown;

        /**
         * 性别
         */
        private String gender;

        /**
         * 年龄
         */
        private String age;
    }

    /**
     * 客户画像
     */
    @Data
    public static class CustomerPortrait {

        /**
         * 学历
         */
        private String education;

        /**
         * 婚姻属性
         */
        private String maritalStatus;

        /**
         * 家庭情况
         */
        private String familySituation;

        /**
         * 工作单位
         */
        private String workplace;

        /**
         * 常住地
         */
        private String residentialAddress;

        /**
         * 兴趣偏好
         */
        private String interestPreference;

        /**
         * 主要收入
         */
        private String mainIncome;

        /**
         * 主要支出
         */
        private String mainExpenditure;

        /**
         * 消费等级
         */
        private ConsumptionLevel consumptionLevel;

        /**
         * 理财等级
         */
        private String financeLevel;

        /**
         * 保险（理财等级）
         */
        private AnnualPremiumAmount annualPremiumAmount;

        /**
         * 理财（理财等级）
         */
        private DepositAmount depositAmount;

        /**
         * 稳定等级
         */
        private StabilityLevel stabilityLevel;
    }

    /**
     * 消费等级
     */
    @Data
    public static class ConsumptionLevel {

        /**
         * 商户消费总额支出
         */
        private String value;

        /**
         * 评级
         */
        private String rating;
    }

    /**
     * 保险（理财等级）
     */
    @Data
    public static class AnnualPremiumAmount {

        /**
         * 保险占比（年收入）
         */
        private String value;

        /**
         * 保险等级
         */
        private String rating;
    }

    /**
     * 理财（理财等级）
     */
    @Data
    public static class DepositAmount {

        /**
         * 理财金额
         */
        private String value;

        /**
         * 理财等级
         */
        private String rating;
    }

    /**
     * 稳定等级
     */
    @Data
    public static class StabilityLevel {

        /**
         * 稳定等级百分比
         */
        private String value;

        /**
         * 稳定等级
         */
        private String rating;
    }

    /**
     * 收入负债
     */
    @Data
    public static class IncomeLiabilities {

        /**
         * 收入评级
         */
        private IncomeRating incomeRating;

        /**
         * 负债评级
         */
        private DebtRating debtRating;

        /**
         * 融资趋势预测
         */
        private String financingTrendPrediction;

        /**
         * 消费趋势预测
         */
        private String consumptionTrendPrediction;

        /**
         * 收入趋势预测
         */
        private String incomeTrendPrediction;
    }

    /**
     * 收入评级
     */
    @Data
    public static class IncomeRating {

        /**
         * 收入金额
         */
        private String value;

        /**
         * 收入评级
         */
        private String rating;
    }

    /**
     * 负债评级
     */
    @Data
    public static class DebtRating {

        /**
         * 负债金额
         */
        private String value;

        /**
         * 负债评级
         */
        private String rating;
    }

    /**
     * 用车指数
     */
    @Data
    public static class VehicleIndex {

        /**
         * 驾驶等级
         */
        private DrivingLevel drivingLevel;

        /**
         * 准驾车型
         */
        private String qualifiedVehicleType;

        /**
         * 驾驶证状态
         */
        private String drivingLicenseStatus;

        /**
         * 累计积分
         */
        private String accumulatedScore;

        /**
         * 高速缴费次数
         */
        private String highSpeedPaymentCount;

        /**
         * 高速缴费总金额
         */
        private String highSpeedPaymentAmount;

        /**
         * 加油次数
         */
        private String fuelingCount;

        /**
         * 加油总金额
         */
        private String fuelingAmount;

        /**
         * 停车场缴费次数
         */
        private String parkingCount;

        /**
         * 停车场缴费总金额
         */
        private String parkingAmount;

        /**
         * 交通罚款
         */
        private String trafficFine;

        /**
         * 车辆改装
         */
        private String vehicleModification;

        /**
         * 车辆维修
         */
        private String vehicleRepair;

        /**
         * 车辆保养
         */
        private String vehicleMaintenance;

        /**
         * 车险金额
         */
        private String carInsuranceAmount;
    }

    /**
     * 驾驶等级
     */
    @Data
    public static class DrivingLevel {

        /**
         * 驾驶等级次数
         */
        private String value;

        /**
         * 驾驶等级
         */
        private String rating;
    }

    /**
     * 欺诈风险
     */
    @Data
    public static class FraudRisk {

        /**
         * 车价偏离
         */
        private String priceDeviation;

        /**
         * 人车匹配度
         */
        private String personVehicleMatch;

        /**
         * 高危指数
         */
        private HighRiskIndex highRiskIndex;

        /**
         * 高危占比（新增）
         */
        private HighRiskInfo highRiskInfo;
    }

    /**
     * 高危指数
     */
    @Data
    public static class HighRiskIndex {

        /**
         * 高危指数
         */
        private String rating;

        /**
         * 高危职业(高危指数：0.1)
         */
        private String hazardousOccupation;

        /**
         * 夜间交易频率(高危指数：0.2)
         */
        private String nightTransactionFrequency;

        /**
         * 刷单(高危指数：0.2)
         */
        private String isShard;

        /**
         * 娱乐会所(高危指数：0.2)
         */
        private String isEntertainmentClub;

        /**
         * 医院(高危指数：0.1)
         */
        private String isHospital;

        /**
         * 车险(高危指数：0.1)
         */
        private String isCarInsurance;

        /**
         * 大额交易(高危指数：0.1)
         */
        private String isLargeTransaction;
    }

    /**
     * 高危占比（新增）
     */
    @Data
    public static class HighRiskInfo {

        /**
         * 高危职业 是否 （新增）
         */
        private String isHazardousOccupation;

        /**
         * 凌晨2-4点交易天数占比（新增）
         */
        private Double nightTransactionFrequencyProportion;

        /**
         * 刷单占比（新增）
         */
        private Double isShardProportion;

        /**
         * 娱乐会所占比（新增）
         */
        private Double isEntertainmentClubProportion;

        /**
         * 医院占比（新增）
         */
        private Double isHospitalProportion;

        /**
         * 车险占比（新增）
         */
        private Double isCarInsuranceProportion;

        /**
         * 是否大额交易：（新增）
         */
        private String isLargeTransactionFlag;
    }

    /**
     * 建议额度
     */
    @Data
    public static class SuggestedLimit {

        /**
         * 建议额度
         */
        private String recommendedCreditLimit;
    }

    /**
     * 公共参数
     */
    @Data
    public static class CommonParameters {

        /**
         * 易鑫车价
         */
        private String yiXinCarPrice;

        /**
         * 流水授信
         */
        private String flowCredit;

        /**
         * 个税授信
         */
        private String individualIncomeTaxCredit;

        /**
         * 公积金授信
         */
        private String providentFundCredit;

        /**
         * 刷单金额
         */
        private String brushOrderAmount;

        /**
         * 月工资
         */
        private String monthlySalary;

        /**
         * 月负债
         */
        private String monthlyLiabilities;

        /**
         * 月支出
         */
        private String monthlyExpenses;

        /**
         * 月收入
         */
        private String monthlyIncome;

        /**
         * 年收入
         */
        private String annualIncome;

        /**
         * 近三个月的交易金额是否大于70000（新增）
         */
        private String singleLargeAbTradeIn3Month;

        /**
         * 近三个月的车辆大额交易 >50000 切包含车（新增）
         */
        private String carLargeAbTrade3Month;

        /**
         * 进三个月交易金额大于9000的次数超过5次（新增）
         */
        private String multiLargeAbTradeIn3Month;

        /**
         * 近六个月月均收入（新增）
         */
        private String sixMonthAvgIncome;

        /**
         * 近三个月月均收入（新增）
         */
        private String threeMonthAvgIncome;

        /**
         * 出现的省份（新增）
         */
        private List<String> provinceList;

        /**
         * 出现的省份（新增）
         */
        private List<String> cityList;
    }
}
