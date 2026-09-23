package cn.seehoo.spg.common.mortgage.client;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.mortgage.config.ResponsibleMortgageConfig;
import cn.seehoo.spg.common.mortgage.model.req.*;
import cn.seehoo.spg.common.mortgage.model.res.*;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.page.PageDto;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxx
 * @date 2026/3/26 10:52
 */
public class ResponsibleMortgageDefaultClient implements ResponsibleMortgageClient{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponsibleMortgageDefaultClient.class);

    /** 解抵押调用根路径 */
    private static final String API_ROOT_PATH = "/baseUrl/mortgage/adminapi/mortgage/che";
    /** 合作机构配置查询 */
    private static final String API_MORTGAGE_PARTNER = "/queryMortgagePartnerConfigInfo";
    /** 创建或更新抵押任务 */
    private static final String API_MORTGAGE_CREATE = "/createOrUpdateMortgageTaskManage";
    /** 抵押信息修改 */
    private static final String API_MORTGAGE_UPDATE = "/updateMortgageTaskManageInfo";
    /** 抵押任务下发 */
    private static final String API_MORTGAGE_TASK = "/saveMortgageTaskManageInfo";
    /** 抵押信息集合查询 */
    private static final String API_MORTGAGE_SEARCH = "/queryMortgageTaskManageInfos";
    /** 获取代理人信息 */
    private static final String API_MORTGAGE_AGENT = "/getAgentInfo";
    /** 合同结清处理 */
    private static final String API_MORTGAGE_SETTLEMENT = "/settlementOrOrderCancel";
    /** 抵押签署完成通知 */
    private static final String API_MORTGAGE_SIGN = "/mortgageSignContractPush";
    /** 附件查询 */
    private static final String API_MORTGAGE_FILE = "/queryFileInfos";
    /** 查询抵押管理城市信息 */
    private static final String API_MORTGAGE_CITY = "/queryMortgageManageCityInfo";
    /** 分页查询 */
    private static final String API_MORTGAGE_PAGE = "/queryPage";
    /** 分页查询 */
    private static final String API_MORTGAGE_MANAGE_RESULT = "/mortgageTaskManageResult";

    @Autowired
    private ResponsibleMortgageConfig mortgageConfig;


    @Override
    public MortgagePartnerRes queryMortgagePartnerConfigInfo(MortgagePartnerReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 合作机构配置查询，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_PARTNER;
        LOGGER.info(">>>>>> [有担-抵解押中台] 合作机构配置查询，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 合作机构配置查询 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 合作机构配置查询，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        MortgagePartnerRes data = new MortgagePartnerRes();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toBean(retData.getString("data"), MortgagePartnerRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 合作机构配置查询错误：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public MortgageTaskCreateRes createOrUpdateMortgageTaskManage(MortgageTaskCreateReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 创建或更新抵押任务，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_CREATE;
        LOGGER.info(">>>>>> [有担-抵解押中台] 创建或更新抵押任务，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 创建或更新抵押任务 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 创建或更新抵押任务，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        MortgageTaskCreateRes data = new MortgageTaskCreateRes();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toBean(retData.getString("data"), MortgageTaskCreateRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 创建或更新抵押任务：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public MortgageTaskUpdateRes updateMortgageTaskManageInfo(MortgageTaskUpdateReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押信息修改，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_UPDATE;
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押信息修改，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押信息修改 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押信息修改，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        MortgageTaskUpdateRes data = new MortgageTaskUpdateRes();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toBean(retData.getString("data"), MortgageTaskUpdateRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押信息修改：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public MortgageTaskRes saveMortgageTaskManageInfo(MortgageTaskReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押任务下发，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_TASK;
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押任务下发，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押任务下发 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押任务下发，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        if (retData != null) {
            if ("000000".equals(retData.getString("code"))) {
                return new MortgageTaskRes();
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押任务下发：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return new MortgageTaskRes();
    }

    @Override
    public List<MortgageTaskSearchRes> queryMortgageTaskManageInfos(MortgageTaskSearchReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押信息集合查询，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_SEARCH;
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押信息集合查询，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押信息集合查询 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押信息集合查询，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        List<MortgageTaskSearchRes> data = new ArrayList<>();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toList(retData.getString("data"), MortgageTaskSearchRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押信息集合查询：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public MortgageAgentRes getAgentInfo(MortgageAgentReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 获取代理人信息，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_AGENT;
        LOGGER.info(">>>>>> [有担-抵解押中台] 获取代理人信息，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 获取代理人信息 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 获取代理人信息，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        MortgageAgentRes data = new MortgageAgentRes();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toBean(retData.getString("data"), MortgageAgentRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 获取代理人信息：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public void settlementOrOrderCancel(MortgageSettlementReq req) throws Exception {
        LOGGER.info(">>>>>> [有担-抵解押中台] 合同结清处理，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_SETTLEMENT;
        LOGGER.info(">>>>>> [有担-抵解押中台] 合同结清处理，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 合同结清处理 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 合同结清处理，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        if (retData != null) {
            if ("000000".equals(retData.getString("code"))) {
                return;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 合同结清处理：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
    }

    @Override
    public void mortgageSignContractPush(MortgageSignContractReq req) throws Exception {
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押签署完成通知，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_SIGN;
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押签署完成通知，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押签署完成通知 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押签署完成通知，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        if (retData != null) {
            if ("000000".equals(retData.getString("code"))) {
                return;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押签署完成通知：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
    }

    @Override
    public List<MortgageFileRes> queryFileInfos(MortgageFileReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 附件查询，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_FILE;
        LOGGER.info(">>>>>> [有担-抵解押中台] 附件查询，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 附件查询 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 附件查询，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        List<MortgageFileRes> data = new ArrayList<>();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toList(retData.getString("data"), MortgageFileRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 附件查询：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public MortgageManageCityInfoRes queryMortgageManageCityInfo(MortgageManageCityInfoReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 查询抵押管理城市信息，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_CITY;
        LOGGER.info(">>>>>> [有担-抵解押中台] 查询抵押管理城市信息，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 查询抵押管理城市信息 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 查询抵押管理城市信息，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        MortgageManageCityInfoRes data = new MortgageManageCityInfoRes();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toBean(retData.getString("data"), MortgageManageCityInfoRes.class);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 查询抵押管理城市信息：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public Page<MortgagePageRes> queryPageMortgage(PageDto<MortgagePageReq> pageDto) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 分页查询抵押信息，入参：{}", JSON.toJSONString(pageDto));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_PAGE;
        LOGGER.info(">>>>>> [有担-抵解押中台] 分页查询抵押信息，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(pageDto).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 分页查询抵押信息 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 分页查询抵押信息，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        Page<MortgagePageRes> data = new Page<MortgagePageRes>();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) && retData.containsKey("data")) {
                data = JSONUtil.toBean(retData.getString("data"), new TypeReference<Page<MortgagePageRes>>() {},false);
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 分页查询抵押信息：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    @Override
    public MortgageManageResultRes mortgageTaskManageResult(MortgageManageResultReq req) {
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押任务回传信息，入参：{}", JSON.toJSONString(req));
        String url = getUrl() + API_ROOT_PATH + API_MORTGAGE_MANAGE_RESULT;
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押任务回传信息，请求地址：{}",url);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String responseBody = HttpUtils.request(url, HttpUtils.METHOD_POST, headers, JSONUtil.toJsonStr(req).getBytes(StandardCharsets.UTF_8), null, "");
        if(StrUtil.isBlank(responseBody)){
            throw new BusinessException(">>>>>> [有担-抵解押中台] 分页查询抵押信息 response body 为空");
        }
        LOGGER.info(">>>>>> [有担-抵解押中台] 抵押任务回传信息，出参：{}",JSONUtil.toJsonStr(responseBody));
        JSONObject retData = JSONObject.parseObject(responseBody);
        MortgageManageResultRes data = new MortgageManageResultRes();
        if (retData != null) {
            if ("000000".equals(retData.getString("code")) ) {
                return data;
            }else {
                throw new BusinessException(">>>>>> [有担-抵解押中台] 抵押任务回传信息：{}",JSONUtil.toJsonStr(retData.getString("msg")));
            }
        }
        return data;
    }

    public String getUrl(){
        return mortgageConfig.getHost();
    }
}
