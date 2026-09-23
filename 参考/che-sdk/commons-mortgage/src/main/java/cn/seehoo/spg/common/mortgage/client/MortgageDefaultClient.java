package cn.seehoo.spg.common.mortgage.client;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.mortgage.config.MortgageConfig;
import cn.seehoo.spg.common.mortgage.model.*;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.core.util.RequestUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 抵押中台台客户端
 */
public class MortgageDefaultClient implements MortgageClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MortgageDefaultClient.class);

    @Autowired
    private MortgageConfig mortgageConfig;

    private static final String API_settlementOrOrderCancel = "/baseUrl/mortgage/adminapi/mortgage/che/settlementOrOrderCancel";
    private static final String API_getAgentInfo = "/baseUrl/mortgage/adminapi/mortgage/che/getAgentInfo";
    private static final String API_createOrUpdateMortgageTaskManage = "/baseUrl/mortgage/adminapi/mortgage/che/createOrUpdateMortgageTaskManage";
    private static final String API_saveMortgageTaskManageInfo = "/baseUrl/mortgage/adminapi/mortgage/che/saveMortgageTaskManageInfo";
    private static final String API_mortgageSignContractPush = "/baseUrl/mortgage/adminapi/mortgage/che/mortgageSignContractPush";
    private static final String API_queryFileInfos = "/baseUrl/mortgage/adminapi/mortgage/che/queryFileInfos";
    private static final String API_queryMortgageTaskManageInfos = "/baseUrl/mortgage/adminapi/mortgage/che/queryMortgageTaskManageInfos";
    private static final String API_queryMortgageType = "/baseUrl/mortgage/adminapi/mortgage/che/queryMortgageType";

    public static void main(String[] args) {

        MortgageConfig config = new MortgageConfig();
        config.setMock(true);
        config.setHost("https://retail-dev.hxfl.com.cn");

        MortgageDefaultClient client = new MortgageDefaultClient();
        client.mortgageConfig = config;

        // 第一步: 抵押方式为2线上时, 获取代理人相关字段和抵押方式入库, 塞值生成合同
        stepOne(client);

        // 第二步：订单放款起租时, 创建抵押任务
        stepTwo(client);

        // 第三步：签完合同后, 抵押签署完成通知
        stepThree(client);

        // 第四步：合同结清, 发起工单取消
        stepFour(client);

        // 第五步：抵押信息集合查询
        QueryMortgageTaskManageInfoReq taskQueryReq = new QueryMortgageTaskManageInfoReq();
        taskQueryReq.setOrderNo("251023007");
        client.queryMortgageTaskManageInfos(taskQueryReq);

        // 第六步：附件查询
        QueryFileInfoReq fileReq = new QueryFileInfoReq();
        fileReq.setBussinessNoList(Arrays.asList("251023007"));
        client.queryFileInfos(fileReq);

    }

    private static void stepOne(MortgageDefaultClient client) {
        // 抵押方式查询
        QueryMortgageTypeReq queryTypeReq = new QueryMortgageTypeReq();
        queryTypeReq.setMortgageSystemType("02");
        queryTypeReq.setOrderNo("251016008");
        String mortgageType = client.queryMortgageType(queryTypeReq);
        // TODO 代理人相关字段和抵押方式入库, 塞值生成合同
        if("2".equals(mortgageType)){// 线上抵押
            // 获取代理人信息
            GetAgentInfoReq agentInfoReq = new GetAgentInfoReq();
            agentInfoReq.setBizNo("251016008");
            agentInfoReq.setBizType("PD_BANYA");
            // 订单上渠道编号
            agentInfoReq.setChannelId("hx_002");
            // 上牌省市区
            agentInfoReq.setProvinceCode("110000");
            agentInfoReq.setProvinceName("北京市");
            agentInfoReq.setCityCode("110100");
            agentInfoReq.setCityName("北京市");
            agentInfoReq.setDistrictCode("");
            agentInfoReq.setDistrictName("");
            GetAgentInfoRes agentInfo = client.getAgentInfo(agentInfoReq);
            System.out.println(agentInfo);
        }
    }

    private static void stepTwo(MortgageDefaultClient client) {
//        String orderNo = "251023006";// 线上
        String orderNo = "251023007";// 线下
        MortgageTaskManageReq mortgageTaskReq = new MortgageTaskManageReq();
        // 办理类型 1线下，2线上
        mortgageTaskReq.setType("1");
        mortgageTaskReq.setOrderNo(orderNo);
        mortgageTaskReq.setCustomerNo("430626199508085566");
        mortgageTaskReq.setCertification("430626199508085566");
        mortgageTaskReq.setBusinessModel("1");
        mortgageTaskReq.setIsAttachment("2");
        mortgageTaskReq.setCarVin("SALWA2FV9HA178767");
        mortgageTaskReq.setCustomName("呵呵");
        // 查询订单订单车辆上牌方表信息
        mortgageTaskReq.setLicenseCityCode("410000");
        mortgageTaskReq.setLicenseCityName("410000");
        mortgageTaskReq.setLicenseProvinceCode("410000");
        mortgageTaskReq.setLicenseProvinceName("410000");
        mortgageTaskReq.setLicensingDistrictCode("410000");
        mortgageTaskReq.setLicensingDistrictName("410000");
        mortgageTaskReq.setOrderStatus("1026");//订单状态
        mortgageTaskReq.setCustomMobile("17788998899");//客户手机号
        mortgageTaskReq.setCarNum("湘A-xxxx");//车牌号
        mortgageTaskReq.setContractStatus("1");
        mortgageTaskReq.setChannelId("CXR");
        mortgageTaskReq.setChannelName("极致优车");
        //经销商名称
        mortgageTaskReq.setAgentName("车商名称");
        //融资租赁合同号
        mortgageTaskReq.setContractNo("金租100x-01");
        //抵押合同编号
        mortgageTaskReq.setContractMortgageNo("DY金租-xx");
        // 车辆信息
        mortgageTaskReq.setVehicleType("2");
        mortgageTaskReq.setVehicleEngineNo("123");
        // 代理人信息
        mortgageTaskReq.setWorkerName("侯燕玲");
        mortgageTaskReq.setWorkerMobile("17600518661");
        mortgageTaskReq.setWorkerIdType("A");
        mortgageTaskReq.setWorkerIdNumber("654001199908030026");
        mortgageTaskReq.setWorkerPostalCode("02020");
        mortgageTaskReq.setWorkerMailAddress("北京市北京市东湖国际中心");
        mortgageTaskReq.setWorkerHandleChannel("1");

        //抵押合同到期日：合同签署日期+合同期限+3年
        Date contractSignDate = new Date();
        int productTerm = 24;
        LocalDateTime now = DateUtil.parseLocalDateTime(
                DateUtil.format(contractSignDate, "yyyy-MM-dd 00:00:00"));
        LocalDateTime dead = now.plusMonths(productTerm + 36);
        mortgageTaskReq.setDeadLineYmd(dead.toLocalDate());
        mortgageTaskReq.setProductTerm(String.valueOf(productTerm));
        //合同结束日期
        LocalDateTime deadLine = now.plusMonths(productTerm);
        mortgageTaskReq.setContractDate(deadLine.toLocalDate());

        JSONObject retJson = client.createOrUpdateMortgageTaskManage(mortgageTaskReq);
        if("000000".equals(retJson.getString("code"))){
            // 创建抵押任务后后, 抵押任务下发
            MortgageTaskManageInfoOpenApiReq taskPushReq = new MortgageTaskManageInfoOpenApiReq();
            taskPushReq.setOrderNo(orderNo);
            taskPushReq.setMortgageProvinceCode("110000");
            taskPushReq.setMortgageProvinceName("北京市");
            taskPushReq.setMortgageCityCode("110100");
            taskPushReq.setMortgageCityName("北京市");
            taskPushReq.setBusinessPeople("侯燕玲");
            taskPushReq.setBusinessPhone("17600518661");
            client.saveMortgageTaskManageInfo(taskPushReq);
        }
    }

    private static void stepThree(MortgageDefaultClient client) {
        PushOrderFileReq req = new PushOrderFileReq();
        req.setBizNo("251023006");
        req.setBizType("CZR");
        req.setMortgageContractNo("xxx");
        List<PushOrderFileReq.FileInfoDto> files = new ArrayList<>();
        req.setFiles(files);
        PushOrderFileReq.FileInfoDto file1 = new PushOrderFileReq.FileInfoDto();
        files.add(file1);
        file1.setFileName("xxx01");
        file1.setFileType("xxx02");
        file1.setFileUrl("xxx03");
        client.mortgageSignContractPush(req);
    }

    private static void stepFour(MortgageDefaultClient client) {
        SettlementInfoReq req = new SettlementInfoReq();
        req.setOrderNo("251023003");
        req.setSettlementType("1");
        req.setContractEndDate(LocalDateTime.now());
        String result = client.settlementOrOrderCancel(req);
        System.out.println(result);
    }

    /**
     * 抵押方式查询: 抵押类型 1线下 2线上 获取代理人前，获取抵押方式, 线上才需要获取代理人, 签署抵押相关合同
     *
     * @param req
     * @return
     */
    @Override
    public String queryMortgageType(QueryMortgageTypeReq req) {
        LOGGER.info(">>>>>> [抵押中台] 抵押方式查询，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_queryMortgageType,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 抵押方式查询 response body 为空");
        }
        JSONObject retData = JSONObject.parseObject(httpResponse);
        if(retData!=null){
            if("000000".equals(retData.getString("code")) && retData.containsKey("data")){
                return retData.getString("data");
            }
            if(mortgageConfig.getMock()){
                LOGGER.error(">>>>>> [抵押中台] 抵押方式查询 响应异常 {}:{} Mock返回2-线上抵押",
                        retData.getString("code"), retData.getString("msg"));
                return "2";// 线上
            }
        }
        return null;
    }

    /**
     * 获取代理人信息
     *
     * @param req
     * @return
     */
    @Override
    public GetAgentInfoRes getAgentInfo(GetAgentInfoReq req) {
        LOGGER.info(">>>>>> [抵押中台] 获取代理人信息，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_getAgentInfo,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 获取代理人信息 response body 为空");
        }
        JSONObject retData = JSONObject.parseObject(httpResponse);
        if(retData!=null && "000000".equals(retData.getString("code"))){
            GetAgentInfoRes res = JSONUtil.toBean(retData.getString("data"), GetAgentInfoRes.class);
            return res;
        }
        return null;
    }

    /**
     * 创建抵押任务
     *
     * @param req
     * @return
     */
    @Override
    public JSONObject createOrUpdateMortgageTaskManage(MortgageTaskManageReq req) {
        LOGGER.info(">>>>>> [抵押中台] 创建抵押任务，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_createOrUpdateMortgageTaskManage,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 创建抵押任务 response body 为空");
        }
        return JSONObject.parseObject(httpResponse);
    }

    /**
     * 发起工单取消
     *
     * @param req
     * @return
     */
    @Override
    public String settlementOrOrderCancel(SettlementInfoReq req) {
        LOGGER.info(">>>>>> [抵押中台] 发起工单取消，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_settlementOrOrderCancel,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 发起工单取消 response body 为空");
        }
        return httpResponse;
    }

    /**
     * 抵押任务下发
     *
     * @param req
     * @return
     */
    @Override
    public String saveMortgageTaskManageInfo(MortgageTaskManageInfoOpenApiReq req) {
        LOGGER.info(">>>>>> [抵押中台] 抵押任务下发，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_saveMortgageTaskManageInfo,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 抵押任务下发 response body 为空");
        }
        return httpResponse;
    }

    /**
     * 抵押签署完成通知
     *
     * @param req
     * @return
     */
    @Override
    public String mortgageSignContractPush(PushOrderFileReq req) {
        LOGGER.info(">>>>>> [抵押中台] 抵押签署完成通知，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_mortgageSignContractPush,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 抵押签署完成通知 response body 为空");
        }
        return httpResponse;
    }

    /**
     * 附件查询
     *
     * @param req
     * @return
     */
    @Override
    public List<FileAllInfoRes> queryFileInfos(QueryFileInfoReq req) {
        LOGGER.info(">>>>>> [抵押中台] 附件查询，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_queryFileInfos,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 附件查询 response body 为空");
        }
        JSONObject retJson = JSONObject.parseObject(httpResponse);
        List<FileAllInfoRes> ret = null;
        if(retJson!=null && retJson.containsKey("data")){
            ret = JSONUtil.toBean(retJson.getString("data"), new TypeReference<List<FileAllInfoRes>>() {}, true);
        }
        return ret;
    }

    /**
     * 抵押信息集合查询
     *
     * @param req
     * @return
     */
    @Override
    public List<MortgageTaskManageInfoRes> queryMortgageTaskManageInfos(QueryMortgageTaskManageInfoReq req) {
        LOGGER.info(">>>>>> [抵押中台] 抵押信息集合查询，入参：{}", JSON.toJSONString(req));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(mortgageConfig.getHost() + API_queryMortgageTaskManageInfos,
                HttpUtils.METHOD_POST,headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [抵押中台] 抵押信息集合查询 response body 为空");
        }
        JSONObject retJson = JSONObject.parseObject(httpResponse);
        List<MortgageTaskManageInfoRes> ret = null;
        if(retJson!=null && retJson.containsKey("data")){
            ret = JSONUtil.toBean(retJson.getString("data"), new TypeReference<List<MortgageTaskManageInfoRes>>() {}, true);
        }
        return ret;
    }

    /**
     * 接口转发处理
     */

    public final static String REPLACE_STR = "v1/mortgageplus/";

    /**
     * 请求抵押中台-接口授权
     *
     * @return
     */
    @Override
    public String createToken() {
        String url = mortgageConfig.getHost() + "/baseUrl/admin/oauth/token";
        HttpRequest httpRequest = HttpUtil.createPost(url);
        httpRequest.contentType("application/x-www-form-urlencoded");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("client_id", mortgageConfig.getAppKey());
        paramMap.put("client_secret", mortgageConfig.getAppSecret());
        paramMap.put("grant_type", "client_credentials");
        httpRequest.form(paramMap);
        String result = httpRequest.execute().body();
        LOGGER.info(">>>>>> [抵押中台] 转发获取token：{}", result);
        return JSONUtil.parseObj(result).getStr("access_token");
    }

    /**
     * 抵押中台-请求转发
     *
     * @param request
     * @return
     */
    @Override
    public Object doRequestMortgage(HttpServletRequest request, String body) {
        LOGGER.info(">>>>>> [抵押中台] 转发前端请求，入参：{}", body);
        String requestURI = request.getRequestURI();
        // v1/mortgageplus/ mortgage/adminapi/xxx
        //      -> /baseUrl/mortgage/ adminapi/xxx
        // v1/mortgageplus/ mortgage/adminapi/xxx
        //      -> /baseUrl/ mortgage/adminapi/xxx
        requestURI = requestURI.replace(REPLACE_STR, "");
        if ("/".equals(String.valueOf(requestURI.charAt(0)))) {
            requestURI = requestURI.substring(1, requestURI.length());
        }
        String url = mortgageConfig.getHost() + "/baseUrl/" + requestURI;
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (ObjectUtil.isNotEmpty(parameterMap)) {
            Set<String> keySet = parameterMap.keySet();
            int pack = 1;
            for (String key : keySet) {
                if (pack == 1) {
                    url = url + "?" + key + "=" + parameterMap.get(key)[0];
                } else {
                    url = url + "&" + key + "=" + parameterMap.get(key)[0];
                }
                pack = pack + 1;
            }
        }
        Method method = Method.GET;
        if (request.getMethod().toLowerCase().equals("post")) {
            method = Method.POST;
        }
        HttpRequest httpRequest = HttpUtil.createRequest(method, url);
        httpRequest.body(body);
        setHeader(httpRequest, createToken(), null);
        String result = httpRequest.execute().body();
        LOGGER.info(">>>>>> [抵押中台] 转发前端请求，响应：{}", result);
        cn.hutool.json.JSONObject obj = JSONUtil.parseObj(result);
        if(obj!=null && obj.containsKey("data")){
            return obj.get("data");
        }else {
            return obj;
        }
    }

    /**
     * 处理新风控请求头
     *
     * @param httpRequest
     * @param token
     * @param userAccount
     */
    @SneakyThrows
    public void setHeader(HttpRequest httpRequest, String token, String userAccount) {
        httpRequest.header("Content-Type", "application/json");
        httpRequest.header("Authorization", "Bearer " + token);
        httpRequest.header("mortgageSystemType", "02");
        httpRequest.header("Appid", mortgageConfig.getAppid());
        httpRequest.header("userAccount", RequestUtils.getHeaderValue(RequestUtils.USER_ACCOUNT));
        httpRequest.header("userNo", RequestUtils.getHeaderValue(RequestUtils.USER_ID));
        String userName = RequestUtils.getHeaderValue(RequestUtils.USER_NAME);
        // 有担要求脱敏处理
        if(StrUtil.isNotEmpty(userName)){
            httpRequest.header("nickName", Base64.encode(userName));
        }
    }

}
