package cn.seehoo.spg.bizcom.qcc.model;

import lombok.Data;

import java.sql.Date;
import java.util.List;

/**
 * 企业信息响应类
 */
@Data
public class CompanyOverViewInfoRes {


    /**
     * 验证结果 NUMBER(10)
     */
    private Integer VerifyResult;

    /**
     * 企业数据 Data
     */
    private DataRes Data;


    @Data
    public static class DataRes {
        /**
         * 企业唯一标识 VARCHAR2(50)
         */
        private String KeyNo;

        /**
         * 公司名称 VARCHAR2(200)
         */
        private String Name;

        /**
         * 统一社会信用代码 VARCHAR2(50)
         */
        private String CreditCode;

        /**
         * 法定代表人 VARCHAR2(100)
         */
        private String OperName;

        /**
         * 经营状态 VARCHAR2(50)
         */
        private String Status;

        /**
         * 成立日期 DATE
         */
        private Date StartDate;

        /**
         * 注册资本 VARCHAR2(50)
         */
        private String RegistCapi;

        /**
         * 注册资本金额 NUMBER(18,2)
         */
        private String RegisteredCapital;

        /**
         * 注册资本单位 VARCHAR2(10)
         */
        private String RegisteredCapitalUnit;

        /**
         * 注册资本币种 VARCHAR2(10)
         */
        private String RegisteredCapitalCCY;

        /**
         * 实缴资本 VARCHAR2(50)
         */
        private String RealCapi;

        /**
         * 实缴金额 NUMBER(18,2)
         */
        private String PaidUpCapital;

        /**
         * 实缴单位 VARCHAR2(10)
         */
        private String PaidUpCapitalUnit;

        /**
         * 实缴币种 VARCHAR2(10)
         */
        private String PaidUpCapitalCCY;

        /**
         * 组织机构代码 VARCHAR2(20)
         */
        private String OrgNo;

        /**
         * 工商注册号 VARCHAR2(50)
         */
        private String No;

        /**
         * 纳税人识别号 VARCHAR2(50)
         */
        private String TaxNo;

        /**
         * 经济性质 VARCHAR2(100)
         */
        private String EconKind;

        /**
         * 营业期限起 DATE
         */
        private Date TermStart;

        /**
         * 营业期限止 DATE
         */
        private Date TermEnd;

        /**
         * 纳税人类型 VARCHAR2(100)
         */
        private String TaxpayerType;

        /**
         * 人员规模 VARCHAR2(50)
         */
        private String PersonScope;

        /**
         * 参保人数 NUMBER(10)
         */
        private String InsuredCount;

        /**
         * 核准日期 DATE
         */
        private String CheckDate;

        /**
         * 地区编码 VARCHAR2(20)
         */
        private String AreaCode;

        /**
         * 所属地区 Area
         */
        private Area Area;

        /**
         * 登记机关 VARCHAR2(200)
         */
        private String BelongOrg;

        /**
         * 进出口企业代码 VARCHAR2(50)
         */
        private String ImExCode;

        /**
         * 行业分类 Industry
         */
        private Industry Industry;

        /**
         * 英文名称 VARCHAR2(200)
         */
        private String EnglishName;

        /**
         * 注册地址 VARCHAR2(300)
         */
        private String Address;

        /**
         * 邮政编码 VARCHAR2(10)
         */
        private String AddressPostalCode;

        /**
         * 年报地址 VARCHAR2(300)
         */
        private String AnnualAddress;

        /**
         * 年报邮政编码 VARCHAR2(10)
         */
        private String AnnualAddressPostalCode;

        /**
         * 经营范围 VARCHAR2(2000)
         */
        private String Scope;

        /**
         * 企业类型 VARCHAR2(10)
         */
        private String EntType;

        /**
         * 组织机构代码列表 OrgCodeList
         */
        private List<OrgCodeList> OrgCodeList;

        /**
         * 企业LOGO地址 VARCHAR2(500)
         */
        private String ImageUrl;

        /**
         * 撤销信息 RevokeInfo
         */
        private RevokeInfo RevokeInfo;

        /**
         * 曾用名 OriginalName
         */
        private List<OriginalName> OriginalName;

        /**
         * 股票信息 StockInfo
         */
        private StockInfo StockInfo;

        /**
         * 联系方式 ContactInfo
         */
        private ContactInfo ContactInfo;

        /**
         * 经纬度 LongLat
         */
        private LongLat LongLat;

        /**
         * 银行账户信息 BankInfo
         */
        private BankInfo BankInfo;

        /**
         * 是否小微企业 VARCHAR2(1)
         */
        private String IsSmall;

        /**
         * 企业规模 VARCHAR2(10)
         */
        private String Scale;

        /**
         * 企查查行业分类 QccIndustry
         */
        private QccIndustry QccIndustry;

        /**
         * 公司类型 VARCHAR2(10)
         */
        private String CompanyType;

        /**
         * 是否有英文名称 VARCHAR2(1)
         */
        private String IsOfficialEnglish;

        /**
         * 股东信息 PartnerList
         */
        private List<PartnerList> PartnerList;

        /**
         * 公开股东信息 PubPartnerList
         */
        private List<PubPartnerList> PubPartnerList;

        /**
         * 员工信息 EmployeeList
         */
        private List<EmployeeList> EmployeeList;

        /**
         * 公开员工信息 PubEmployeeList
         */
        private List<PubEmployeeList> PubEmployeeList;

        /**
         * 分公司信息 BranchList
         */
        private List<BranchList> BranchList;

        /**
         * 变更记录 ChangeList
         */
        private List<ChangeList> ChangeList;

        /**
         * 标签信息 TagList
         */
        private List<TagList> TagList;

        /**
         * 上级企业 Parent
         */
        private Parent Parent;

        /**
         * 受益人信息 Beneficiary
         */
        private Beneficiary Beneficiary;

        /**
         * 实际控制人信息 ActualControllerList
         */
        private List<ActualControllerList> ActualControllerList;

        /**
         * 新兴行业列表 EmergingIndustyList
         */
        private List<String> EmergingIndustyList;

        /**
         * 集团信息 GroupInfo
         */
        private GroupInfo GroupInfo;

        /**
         * 投资信息 InvestmentList
         */
        private List<InvestmentList> InvestmentList;

        /**
         * 产品信息 ProductList
         */
        private List<ProductList> ProductList;

        /**
         * 行政许可 AdminLicenseList
         */
        private List<AdminLicenseList> AdminLicenseList;

        /**
         * 网站备案 ApproveSiteList
         */
        private List<ApproveSiteList> ApproveSiteList;

        /**
         * 抽查检查记录 SpotCheckList
         */
        private List<SpotCheckList> SpotCheckList;

        /**
         * 税务信用记录 TaxCreditList
         */
        private List<TaxCreditList> TaxCreditList;

        /**
         * 失信信息 ShiXin
         */
        private ShiXin ShiXin;

        /**
         * 执行信息 ZhiXing
         */
        private ZhiXing ZhiXing;

        /**
         * 行政处罚 AdminPenalty
         */
        private AdminPenalty AdminPenalty;

        /**
         * 经营异常 Exception
         */
        private Exception Exception;

        /**
         * 动产抵押 ChattelMortgage
         */
        private ChattelMortgage ChattelMortgage;

        /**
         * 清算信息 Liquidation
         */
        private Liquidation Liquidation;

        /**
         * 股权质押 EquityPledge
         */
        private EquityPledge EquityPledge;

        /**
         * 严重违法 SeriousIllegal
         */
        private SeriousIllegal SeriousIllegal;

        /**
         * 股权冻结 EquityFreeze
         */
        private EquityFreeze EquityFreeze;

        /**
         * 司法拍卖 JudicialSale
         */
        private JudicialSale JudicialSale;

        /**
         * 破产信息 Bankruptcy
         */
        private Bankruptcy Bankruptcy;

        /**
         * 限制消费 Sumptuary
         */
        private Sumptuary Sumptuary;

        /**
         * 环保处罚 EnvPunishment
         */
        private EnvPunishment EnvPunishment;

        /**
         * 欠税公告 TaxOweNotice
         */
        private TaxOweNotice TaxOweNotice;

        /**
         * 税务违法 TaxIllegal
         */
        private TaxIllegal TaxIllegal;

        /**
         * 税务异常 TaxAbnormal
         */
        private TaxAbnormal TaxAbnormal;

        /**
         * 税务催报 TaxHurry
         */
        private TaxHurry TaxHurry;

        /**
         * 税务提醒 TaxReminder
         */
        private TaxReminder TaxReminder;

    }


    @Data
    public static class Area {
        /**
         * 省份 VARCHAR2(50)
         */
        private String Province;

        /**
         * 城市 VARCHAR2(50)
         */
        private String City;

        /**
         * 区县 VARCHAR2(50)
         */
        private String County;
    }

    @Data
    public static class Industry {
        /**
         * 行业代码 VARCHAR2(10)
         */
        private String IndustryCode;

        /**
         * 行业名称 VARCHAR2(100)
         */
        private String Industry;

        /**
         * 子行业代码 VARCHAR2(10)
         */
        private String SubIndustryCode;

        /**
         * 子行业名称 VARCHAR2(100)
         */
        private String SubIndustry;

        /**
         * 中类代码 VARCHAR2(10)
         */
        private String MiddleCategoryCode;

        /**
         * 中类名称 VARCHAR2(100)
         */
        private String MiddleCategory;

        /**
         * 小类代码 VARCHAR2(10)
         */
        private String SmallCategoryCode;

        /**
         * 小类名称 VARCHAR2(100)
         */
        private String SmallCategory;
    }

    @Data
    public static class OrgCodeList {
        /**
         * 主代码 VARCHAR2(20)
         */
        private String PrimaryCode;

        /**
         * 子代码 VARCHAR2(20)
         */
        private String SecondaryCode;
    }

    @Data
    public static class RevokeInfo {
        /**
         * 注销日期 DATE
         */
        private String CancelDate;

        /**
         * 注销原因 VARCHAR2(200)
         */
        private String CancelReason;

        /**
         * 撤销日期 DATE
         */
        private String RevokeDate;

        /**
         * 撤销原因 VARCHAR2(200)
         */
        private String RevokeReason;
    }

    @Data
    public static class OriginalName {
        /**
         * 曾用名 VARCHAR2(200)
         */
        private String Name;

        /**
         * 变更日期 DATE
         */
        private String ChangeDate;
    }

    @Data
    public static class StockInfo {
        /**
         * 股票代码 VARCHAR2(50)
         */
        private String StockNumber;

        /**
         * 股票类型 VARCHAR2(50)
         */
        private String StockType;
    }

    @Data
    public static class ContactInfo {
        /**
         * 网站列表 LIST<STRING>
         */
        private List<String> WebSiteList;

        /**
         * 邮箱 VARCHAR2(100)
         */
        private String Email;

        /**
         * 更多邮箱 MoreEmailList
         */
        private List<MoreEmailList> MoreEmailList;

        /**
         * 联系电话 VARCHAR2(20)
         */
        private String Tel;

        /**
         * 更多电话 MoreTelList
         */
        private List<MoreTelList> MoreTelList;
    }

    @Data
    public static class MoreEmailList {
        /**
         * 邮箱 VARCHAR2(100)
         */
        private String Email;

        /**
         * 来源 VARCHAR2(100)
         */
        private String Source;
    }

    @Data
    public static class MoreTelList {
        /**
         * 电话 VARCHAR2(20)
         */
        private String Tel;

        /**
         * 来源 VARCHAR2(100)
         */
        private String Source;
    }

    @Data
    public static class LongLat {
        /**
         * 经度 VARCHAR2(20)
         */
        private String Longitude;

        /**
         * 纬度 VARCHAR2(20)
         */
        private String Latitude;
    }

    @Data
    public static class BankInfo {
        /**
         * 银行名称 VARCHAR2(200)
         */
        private String Bank;

        /**
         * 银行账号 VARCHAR2(50)
         */
        private String BankAccount;

        /**
         * 公司名称 VARCHAR2(200)
         */
        private String Name;

        /**
         * 统一信用代码 VARCHAR2(50)
         */
        private String CreditCode;

        /**
         * 地址 VARCHAR2(300)
         */
        private String Address;

        /**
         * 电话 VARCHAR2(20)
         */
        private String Tel;
    }

    @Data
    public static class QccIndustry {
        /**
         * 一级分类名称
         */
        private String AName;
        /**
         * 二级分类名称
         */
        private String BName;
        /**
         * 三级分类名称
         */
        private String CName;
        /**
         * 四级分类名称
         */
        private String DName;
    }

    @Data
    public static class PartnerList {
        /**
         * 股东唯一标识
         */
        private String KeyNo;

        /**
         * 股东名称
         */
        private String StockName;

        /**
         * 股东类型
         */
        private String StockType;

        /**
         * 持股比例
         */
        private String StockPercent;

        /**
         * 认缴出资额
         */
        private String ShouldCapi;

        /**
         * 认缴金额
         */
        private String SubscribedCapital;

        /**
         * 认缴单位
         */
        private String SubscribedCapitalUnit;

        /**
         * 认缴币种
         */
        private String SubscribedCapitalCCY;

        /**
         * 认缴日期
         */
        private String ShoudDate;

        /**
         * 实缴日期
         */
        private String StakeDate;

        /**
         * 统一社会信用代码
         */
        private String CreditCode;

        /**
         * 所属地区
         */
        private String Area;
    }

    @Data
    public static class PubPartnerList {
        /**
         * 股东名称
         */
        private String StockName;

        /**
         * 持股比例
         */
        private String StockPercent;

        /**
         * 持股类型
         */
        private String HoldType;

        /**
         * 持股金额
         */
        private String Amount;

        /**
         * 认缴金额
         */
        private String SubscribedCapital;

        /**
         * 认缴单位
         */
        private String SubscribedCapitalUnit;

        /**
         * 认缴币种
         */
        private String SubscribedCapitalCCY;

        /**
         * 统一信用代码
         */
        private String CreditCode;

        /**
         * 所属地区
         */
        private String Area;
    }

    @Data
    public static class EmployeeList {
        /**
         * 员工唯一标识
         */
        private String KeyNo;

        /**
         * 员工姓名
         */
        private String Name;

        /**
         * 职位
         */
        private String Job;
    }

    @Data
    public static class PubEmployeeList {
        /**
         * 员工姓名
         */
        private String Name;

        /**
         * 职位
         */
        private String Job;
    }

    @Data
    public static class BranchList {
        /**
         * 分公司唯一标识
         */
        private String KeyNo;

        /**
         * 分公司名称
         */
        private String Name;

        /**
         * 分公司负责人
         */
        private String OperName;

        /**
         * 成立日期
         */
        private String StartDate;

        /**
         * 经营状态
         */
        private String Status;

        /**
         * 所属地区
         */
        private Area Area;
    }

    @Data
    public static class ChangeList {
        /**
         * 变更项目名称
         */
        private String ProjectName;

        /**
         * 变更日期
         */
        private String ChangeDate;

        /**
         * 变更前内容列表
         */
        private List<String> BeforeList;

        /**
         * 变更后内容列表
         */
        private List<String> AfterList;
    }

    @Data
    public static class TagList {
        /**
         * 标签类型
         */
        private String Type;

        /**
         * 标签名称
         */
        private String Name;
    }

    @Data
    public static class Parent {
        /**
         * 上级企业唯一标识
         */
        private String KeyNo;

        /**
         * 上级企业名称
         */
        private String Name;

        /**
         * 法定代表人
         */
        private String OperName;

        /**
         * 成立日期
         */
        private String StartDate;

        /**
         * 经营状态
         */
        private String Status;

        /**
         * 注册资本
         */
        private String RegistCapi;
    }

    @Data
    public static class Beneficiary {
        /**
         * 受益人唯一标识
         */
        private String KeyNo;

        /**
         * 受益人姓名
         */
        private String Name;

        /**
         * 最终受益比例
         */
        private String FinalBenefitPercent;

        /**
         * 识别原因
         */
        private String Reason;
    }

    @Data
    public static class ActualControllerList {
        /**
         * 控制人唯一标识
         */
        private String KeyNo;

        /**
         * 控制人姓名
         */
        private String Name;

        /**
         * 最终受益比例
         */
        private String FinalBenefitPercent;

        /**
         * 控制比例
         */
        private String ControlPercent;

        /**
         * 是否实际控制人
         */
        private String IsActual;
    }

    @Data
    public static class GroupInfo {
        /**
         * 集团ID
         */
        private String GroupId;

        /**
         * 集团名称
         */
        private String Name;

        /**
         * 集团LOGO
         */
        private String Logo;
    }

    @Data
    public static class InvestmentList {
        /**
         * 投资企业唯一标识
         */
        private String KeyNo;

        /**
         * 投资企业名称
         */
        private String Name;

        /**
         * 成立日期
         */
        private String StartDate;

        /**
         * 经营状态
         */
        private String Status;

        /**
         * 投资比例
         */
        private String FundedRatio;

        /**
         * 投资金额
         */
        private String ShouldCapi;

        /**
         * 实缴金额
         */
        private String SubscribedCapital;

        /**
         * 实缴单位
         */
        private String SubscribedCapitalUnit;

        /**
         * 实缴币种
         */
        private String SubscribedCapitalCCY;

        /**
         * 行业分类
         */
        private Industry Industry;

        /**
         * 所属地区
         */
        private Area Area;
    }

    @Data
    public static class ProductList {
        /**
         * 产品名称
         */
        private String Name;

        /**
         * 成立日期
         */
        private String StartDate;

        /**
         * 轮次描述
         */
        private String RoundDesc;

        /**
         * 所在地
         */
        private String Location;

        /**
         * 产品描述
         */
        private String Description;
    }

    @Data
    public static class AdminLicenseList {
        /**
         * 许可文号
         */
        private String LicensDocNo;

        /**
         * 许可名称
         */
        private String LicensDocName;

        /**
         * 有效期起
         */
        private String ValidityFrom;

        /**
         * 有效期止
         */
        private String ValidityTo;

        /**
         * 发证机关
         */
        private String LicensOffice;

        /**
         * 许可内容
         */
        private String LicensContent;

        /**
         * 来源
         */
        private String Source;
    }

    @Data
    public static class ApproveSiteList {
        /**
         * 企业名称
         */
        private String Name;

        /**
         * 网站地址
         */
        private String WebAddress;

        /**
         * 域名
         */
        private String DomainName;

        /**
         * 备案号
         */
        private String LesenceNo;

        /**
         * 审核日期
         */
        private String AuditDate;
    }

    @Data
    public static class SpotCheckList {
        /**
         * 检查机关
         */
        private String ExecutiveOrg;

        /**
         * 检查类型
         */
        private String Type;

        /**
         * 检查日期
         */
        private String Date;

        /**
         * 检查结果
         */
        private String Consequence;
    }

    @Data
    public static class TaxCreditList {
        /**
         * 纳税人识别号
         */
        private String TaxNo;

        /**
         * 年度
         */
        private String Year;

        /**
         * 信用等级
         */
        private String Level;

        /**
         * 税务机关
         */
        private String Org;
    }

    @Data
    public static class ShiXin {
        /**
         * 失信总金额
         */
        private String TotalAmount;

        /**
         * 失信案件总数
         */
        private String TotalCount;

        /**
         * 失信案件详情列表
         */
        private List<ShiXinData> DataList;

        @Data
        public static class ShiXinData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 案件编号
             */
            private String CaseNo;

            /**
             * 执行法院
             */
            private String ExecuteCourt;

            /**
             * 执行文号
             */
            private String ExecuteNo;

            /**
             * 案件金额
             */
            private String Amount;

            /**
             * 执行状态
             */
            private String ExecuteStatus;

            /**
             * 行为描述
             */
            private String ActionRemark;

            /**
             * 登记日期
             */
            private String RegisterDate;

            /**
             * 公示日期
             */
            private String PublicDate;
        }
    }

    @Data
    public static class ZhiXing {
        /**
         * 执行案件总金额
         */
        private String TotalAmount;

        /**
         * 执行案件总数
         */
        private String TotalCount;

        /**
         * 执行案件列表
         */
        private List<ZhiXinData> DataList;

        @Data
        public static class ZhiXinData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 案件编号
             */
            private String CaseNo;

            /**
             * 招标金额
             */
            private String BiaoDi;

            /**
             * 执行法院
             */
            private String ExecuteCourt;

            /**
             * 登记日期
             */
            private String RegisterDate;
        }
    }

    @Data
    public static class AdminPenalty {
        /**
         * 处罚总金额
         */
        private String TotalAmount;

        /**
         * 处罚总数
         */
        private String TotalCount;

        /**
         * 处罚详情列表
         */
        private List<AdminPenaltyData> DataList;

        @Data
        public static class AdminPenaltyData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 处罚文号
             */
            private String DocNo;

            /**
             * 处罚原因
             */
            private String PunishReason;

            /**
             * 处罚结果
             */
            private String PunishResult;

            /**
             * 处罚金额
             */
            private String PunishAmt;

            /**
             * 处罚机关
             */
            private String PunishOffice;

            /**
             * 处罚日期
             */
            private String PunishDate;
        }
    }

    @Data
    public static class Exception {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 经营异常记录列表
         */
        private List<ExceptionData> DataList;

        @Data
        public static class ExceptionData {
            /**
             * 添加日期
             */
            private String AddDate;

            /**
             * 添加机关
             */
            private String AddOffice;

            /**
             * 添加原因
             */
            private String AddReason;
        }
    }

    @Data
    public static class ChattelMortgage {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 动产抵押列表
         */
        private List<ChattelMortgageData> DataList;

        @Data
        public static class ChattelMortgageData {
            /**
             * 登记编号
             */
            private String RegisterNo;

            /**
             * 状态
             */
            private String Status;

            /**
             * 登记日期
             */
            private String RegisterDate;

            /**
             * 担保债权金额
             */
            private String SecureClaimsAmount;

            /**
             * 抵押人
             */
            private List<String> Pledger;

            /**
             * 抵押权人
             */
            private List<String> Pledgee;

            /**
             * 债务期限
             */
            private String DebtTerm;
        }
    }

    @Data
    public static class Liquidation {
        /**
         * 清算负责人
         */
        private String Leader;

        /**
         * 清算成员
         */
        private String Member;
    }

    @Data
    public static class EquityPledge {
        /**
         * 质押总数
         */
        private String TotalCount;

        /**
         * 股权质押列表
         */
        private List<EquityPledgeData> DataList;

        @Data
        public static class EquityPledgeData {
            /**
             * 质押ID
             */
            private String Id;

            /**
             * 质押登记编号
             */
            private String RegisterNo;

            /**
             * 出质人列表
             */
            private List<String> PledgorList;

            /**
             * 质权人列表
             */
            private List<String> PledgeeList;

            /**
             * 关联公司
             */
            private String RelatedCompany;

            /**
             * 质押金额
             */
            private String PledgedAmount;

            /**
             * 登记日期
             */
            private String RegisterDate;

            /**
             * 状态
             */
            private String Status;
        }
    }

    @Data
    public static class SeriousIllegal {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 严重违法记录列表
         */
        private List<SeriousIllegalData> DataList;

        @Data
        public static class SeriousIllegalData {
            /**
             * 添加日期
             */
            private String AddDate;

            /**
             * 添加机关
             */
            private String AddOffice;

            /**
             * 添加原因
             */
            private String AddReason;
        }
    }

    @Data
    public static class EquityFreeze {
        /**
         * 冻结总数
         */
        private String TotalCount;

        /**
         * 股权冻结列表
         */
        private List<EquityFreezeData> DataList;

        @Data
        public static class EquityFreezeData {
            /**
             * 冻结ID
             */
            private String Id;

            /**
             * 冻结文号
             */
            private String DocNo;

            /**
             * 被执行人
             */
            private String BeExecuted;

            /**
             * 冻结公司
             */
            private String FreezeCompany;

            /**
             * 股权金额
             */
            private String EquityAmount;

            /**
             * 执行法院
             */
            private String ExecuteCourt;

            /**
             * 状态
             */
            private String Status;

            /**
             * 冻结开始日期
             */
            private String FreezeStartDate;

            /**
             * 冻结结束日期
             */
            private String FreezeEndDate;
        }
    }

    @Data
    public static class JudicialSale {
        /**
         * 拍卖总数
         */
        private String TotalCount;

        /**
         * 司法拍卖列表
         */
        private List<JudicialSaleData> DataList;

        @Data
        public static class JudicialSaleData {
            /**
             * 拍卖ID
             */
            private String Id;

            /**
             * 拍卖名称
             */
            private String Name;

            /**
             * 案件编号
             */
            private String CaseNo;

            /**
             * 拍卖时间
             */
            private String AuctionTime;

            /**
             * 执行法院
             */
            private String ExecuteGov;
        }
    }

    @Data
    public static class Bankruptcy {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 破产案件列表
         */
        private List<BankruptcyData> DataList;

        @Data
        public static class BankruptcyData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 案件编号
             */
            private String CaseNo;

            /**
             * 公布日期
             */
            private String PublicDate;

            /**
             * 申请人列表
             */
            private List<String> ApplicantList;

            /**
             * 被申请人列表
             */
            private List<String> RespondentList;
        }
    }

    @Data
    public static class Sumptuary {
        /**
         * 总金额
         */
        private String TotalAmount;

        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 限制高消费记录列表
         */
        private List<SumptuaryData> DataList;

        @Data
        public static class SumptuaryData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 案件编号
             */
            private String CaseNo;

            /**
             * 公司名称
             */
            private String CompanyName;

            /**
             * 关联人姓名
             */
            private String RelatedName;

            /**
             * 申请人
             */
            private String Applicant;

            /**
             * 案件金额
             */
            private String Amount;

            /**
             * 登记日期
             */
            private String RegisterDate;

            /**
             * 公示日期
             */
            private String PublicDate;

            /**
             * 执行法院
             */
            private String ExecuteCourt;
        }
    }

    @Data
    public static class EnvPunishment {
        /**
         * 处罚总金额
         */
        private String TotalAmount;

        /**
         * 处罚总数
         */
        private String TotalCount;

        /**
         * 环境处罚列表
         */
        private List<EnvPunishmentData> DataList;

        @Data
        public static class EnvPunishmentData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 处罚文号
             */
            private String DocNo;

            /**
             * 处罚原因
             */
            private String PunishReason;

            /**
             * 处罚结果
             */
            private String PunishResult;

            /**
             * 处罚金额
             */
            private String PunishAmt;

            /**
             * 处罚机关
             */
            private String PunishOffice;

            /**
             * 处罚日期
             */
            private String PunishDate;
        }
    }

    @Data
    public static class TaxOweNotice {
        /**
         * 总金额
         */
        private String TotalAmount;

        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 欠税公告列表
         */
        private List<TaxOweNoticeData> DataList;

        @Data
        public static class TaxOweNoticeData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 税种
             */
            private String Title;

            /**
             * 欠税金额
             */
            private String Amount;

            /**
             * 新欠税金额
             */
            private String NewAmount;

            /**
             * 公布日期
             */
            private String PublishDate;

            /**
             * 发布机关
             */
            private String PublishOffice;
        }
    }

    @Data
    public static class TaxIllegal {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 税务违法记录列表
         */
        private List<TaxIllegalData> DataList;

        @Data
        public static class TaxIllegalData {
            /**
             * 案件ID
             */
            private String Id;

            /**
             * 公布日期
             */
            private String PublishDate;

            /**
             * 案件性质（如虚开发票等）
             */
            private String CaseNature;

            /**
             * 税务机关
             */
            private String TaxGov;

            /**
             * 违法内容
             */
            private String IllegalContent;

            /**
             * 处罚内容
             */
            private String PunishContent;
        }
    }

    @Data
    public static class TaxAbnormal {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 税务异常记录列表
         */
        private List<TaxAbnormalData> DataList;

        @Data
        public static class TaxAbnormalData {
            /**
             * 纳税人识别号
             */
            private String TaxNo;

            /**
             * 添加机关
             */
            private String AddOffice;

            /**
             * 添加日期
             */
            private String AddDate;
        }
    }

    @Data
    public static class TaxHurry {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 税务催报记录列表
         */
        private List<TaxHurryData> DataList;

        @Data
        public static class TaxHurryData {
            /**
             * 税种
             */
            private String TaxCategory;

            /**
             * 欠税金额
             */
            private String TaxOwedAmt;

            /**
             * 所属期起
             */
            private String PeriodStartDate;

            /**
             * 所属期止
             */
            private String PeriodEndDate;

            /**
             * 截止日期
             */
            private String DeadlineDate;

            /**
             * 税务机关
             */
            private String TaxAuthority;

            /**
             * 公布日期
             */
            private String PublishDate;
        }
    }

    @Data
    public static class TaxReminder {
        /**
         * 数据总数
         */
        private String TotalCount;

        /**
         * 税务提醒记录列表
         */
        private List<TaxReminderData> DataList;

        @Data
        public static class TaxReminderData {
            /**
             * 税种
             */
            private String TaxCategory;

            /**
             * 所属期起
             */
            private String PeriodStartDate;

            /**
             * 所属期止
             */
            private String PeriodEndDate;

            /**
             * 税务机关
             */
            private String TaxAuthority;

            /**
             * 公布日期
             */
            private String PublishDate;
        }
    }
}
