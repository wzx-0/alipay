package cn.seehoo.common.unify.client;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.seehoo.common.unify.config.UnifyAppConfig;
import cn.seehoo.common.unify.exception.UnifyAppException;
import cn.seehoo.common.unify.request.*;
import cn.seehoo.common.unify.response.AppUserResponse;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import cn.seehoo.spg.commons.core.util.RequestUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunyf
 * @date 2025/9/24 上午11:45
 * @since 1.0
 */
@Component
public class UnifyAppClient {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UnifyAppClient.class);
    public static final String RETURN_CODE_SUC = "000000";
    public static final String RETURN_CODE = "returnCode";
    public static final String MESSAGE = "MESSAGE";
    public static final String BEARER = "Bearer ";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_SECRENT = "client_secret";
    public static final String GRANT_TYPE = "grant_type";
    public static final String DATA = "data";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String Content_Type = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String authorization = "Authorization";
    public static final String APP_ID = "appId";
    @Autowired
    private UnifyAppConfig unitAppConfig;

    /**
     * 功能新增同步
     * @param foptr
     * @return
     */
    public boolean addPermResource(FunctionsOptRequest foptr) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        // 获取token
        String token = BEARER + getToken();
        List<Long> funcIds = unitAppConfig.getFunctionIds();
        for (FunctionsOptRequest.FunctionInfo function : foptr.getFunctions()) {
            try {
                Long funcId = function.getId();
                if (!funcIds.contains(funcId)) {
                    // 不在配置中的菜單跳過
                    continue;
                }
                // 功能新增调用统一APP
                PermResourceAddRequest prarequest = new PermResourceAddRequest(function.getName(), String.valueOf(function.getType()),
                        function.getPath(), function.getIcon(), function.getId(), unitAppConfig);
                Map<String, String> headerMap = new HashMap<>(5);
                headerMap.put(authorization, token);
                headerMap.put(APP_ID, unitAppConfig.getAppid());
                headerMap.put(Content_Type, APPLICATION_JSON);
                String jsonString = JSON.toJSONString(prarequest);
                LOGGER.info(">>>>>>[UnifyApp]，功能新增同步，参数={}", jsonString);
                String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getResourceAddPath(),
                        HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8), null, null);
                LOGGER.info(">>>>>>[UnifyApp]，功能新增同步，结果={}", response);
            } catch (Exception e) {
                LOGGER.error(">>>>>>[UnifyApp]，功能新增同步，发生异常", e);
            }
        }
        return true;
    }

    /**
     * 功能修改
     * @param foptr
     * @return
     */
    public boolean updatePermResource(FunctionsOptRequest foptr) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        // 获取token
        String token = BEARER + getToken();
        List<Long> funcIds = unitAppConfig.getFunctionIds();
        for (FunctionsOptRequest.FunctionInfo function : foptr.getFunctions()) {
            try {
                Long funcId = function.getId();
                if (!funcIds.contains(funcId)) {
                    // 不在配置中的菜單跳過
                    continue;
                }
                // 功能修改调用统一APP
                PermResourceUpdateRequest request = new PermResourceUpdateRequest(function.getName(), function.getId(), function.getPath(),
                        function.getIcon(), unitAppConfig);
                Map<String, String> headerMap = new HashMap<>(5);
                headerMap.put(authorization, token);
                headerMap.put(APP_ID, unitAppConfig.getAppid());
                headerMap.put(Content_Type, APPLICATION_JSON);
                String jsonString = JSON.toJSONString(request);
                LOGGER.info(">>>>>>[UnifyApp]，功能修改同步，参数={}", jsonString);
                String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getResourceUpdatePath(),
                        HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8), null, null);
                LOGGER.info(">>>>>>[UnifyApp]，功能修改同步，结果={}", response);
            } catch (Exception e) {
                LOGGER.error(">>>>>>[UnifyApp]，功能修改同步，发生异常", e);
            }
        }
        return true;
    }



    /**
     * 角色基本信息创建同步
     * @param prarequest
     * @return
     */
    public boolean addUserRole(UserRoleAddRequest prarequest) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        // 校验参数
        prarequest.checkParams();
        try {
            prarequest.setAppCode(unitAppConfig.getAppCode());
            prarequest.setAppName(unitAppConfig.getAppName());
            prarequest.setAppBizCode(unitAppConfig.getAppBizCode());
            prarequest.setAppBizName(unitAppConfig.getAppBizName());
            // 获取token
            String token = BEARER+getToken();
            Map<String, String> headerMap = new HashMap<>(5);
            headerMap.put(authorization, token);
            headerMap.put(APP_ID, unitAppConfig.getAppid());
            headerMap.put(Content_Type, APPLICATION_JSON);
            String jsonString = JSON.toJSONString(prarequest);
            LOGGER.info(">>>>>>[UnifyApp]，角色基本信息创建同步，参数={}", jsonString);
            String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getUserRoleAddPath(),
                    HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8), null, null);
            LOGGER.info(">>>>>>[UnifyApp]，角色基本信息创建同步，结果={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            String code = jsonObject.getString(RETURN_CODE);
            if (!RETURN_CODE_SUC.equals(code)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，角色基本信息创建异常", e);
            return false;
        }
    }

    /**
     * 角色基本信息编辑同步
     * @param prarequest
     * @return
     */
    public boolean updateUserRole(UserRoleUpdateRequest prarequest) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        try {
            prarequest.setAppCode(unitAppConfig.getAppCode());
            // 获取token
            String token = BEARER+getToken();
            Map<String, String> headerMap = new HashMap<>(5);
            headerMap.put(authorization, token);
            headerMap.put(APP_ID, unitAppConfig.getAppid());
            headerMap.put(Content_Type, APPLICATION_JSON);

            String jsonString = JSON.toJSONString(prarequest);
            LOGGER.info(">>>>>>[UnifyApp]，角色基本信息编辑同步，参数={}", jsonString);
            String response  = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getUserRoleUpdatePath(),
                        HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8), null, null);
            LOGGER.info(">>>>>>[UnifyApp]，角色基本信息编辑同步，结果={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            String code = jsonObject.getString(RETURN_CODE);
            if (!RETURN_CODE_SUC.equals(code)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，角色基本信息编辑异常", e);
            return false;
        }
    }

    /**
     * 角色关联权限同步
     * @param rfcr
     * @return
     */
    public boolean grantRoleResource(RoleFunctionConfigRequest rfcr) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        try {
            // 获取token
            String token = BEARER+getToken();
            RoleGrantPermissionRequest rgpr = new RoleGrantPermissionRequest(rfcr.getRoleId(), rfcr.getFunctionIds(), unitAppConfig);
            if (rgpr.checkPermissionItemEmpty()) {
                LOGGER.info(">>>>>>[UnifyApp]，无需同步菜单权限");
                return true;
            }
            Map<String, String> headerMap = new HashMap<>(5);
            headerMap.put(authorization, token);
            headerMap.put(APP_ID, unitAppConfig.getAppid());
            headerMap.put(Content_Type, APPLICATION_JSON);
            String jsonString = JSON.toJSONString(rgpr);
            LOGGER.info(">>>>>>[UnifyApp]，角色关联权限同步，参数={}", jsonString);
            String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getUserRolePermissionGrantPath(),
                    HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8),
                    null, null);
            LOGGER.info(">>>>>>[UnifyApp]，角色关联权限同步，结果={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            String code = jsonObject.getString(RETURN_CODE);
            if (!RETURN_CODE_SUC.equals(code)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，角色关联权限同步异常", e);
            return false;
        }
    }

    /**
     * 用户角色同步
     */
    public boolean configUserRoles(UserRoleOptRequest request) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        LOGGER.info(">>>>>>[UnifyApp]，用户配置角色，参数={}", JSON.toJSONString(request));
        try {
            UserRoleBindOptRequest bindOptRequest = new UserRoleBindOptRequest(ListUtil.of(request.getUserId()), unitAppConfig.getAppCode(),
                    unitAppConfig.getAppBizCode());
            List<Long> newRoleIds = request.getNewRoleIds();
            List<Long> oldRoleIds = request.getOldRoleIds();
            // 本次新绑定的角色Ids
            List<Long> addRoleIds = CollectionUtil.subtractToList(newRoleIds, oldRoleIds);
            // 本次解绑定的角色Ids
            List<Long> deleteRoleIds = CollectionUtil.subtractToList(oldRoleIds, newRoleIds);
            // 获取token
            String token = BEARER+getToken();
            Map<String, String> headerMap = new HashMap<>(5);
            headerMap.put(authorization, token);
            headerMap.put(APP_ID, unitAppConfig.getAppid());
            headerMap.put(Content_Type, APPLICATION_JSON);
            for (Long deleteRoleId : deleteRoleIds) {
                bindOptRequest.setRoleId(deleteRoleId);
                String jsonString = JSON.toJSONString(bindOptRequest);
                LOGGER.info(">>>>>>[UnifyApp]，用户解绑角色同步，参数={}", jsonString);
                String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getUserRoleUnbindPath(),
                        HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8),
                        null, null);
                LOGGER.info(">>>>>>[UnifyApp]，用户解绑角色同步，结果={}", response);
            }
            for (Long addRoleId : addRoleIds) {
                bindOptRequest.setRoleId(addRoleId);
                String jsonString = JSON.toJSONString(bindOptRequest);
                LOGGER.info(">>>>>>[UnifyApp]，用户绑定角色同步，参数={}", jsonString);
                String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getUserRoleBindPath(),
                        HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8),
                        null, null);
                LOGGER.info(">>>>>>[UnifyApp]，用户绑定角色同步，结果={}", response);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，用户绑定角色错误，e=", e);
            return false;
        }
    }

    /**
     * 车辆用户信息同步
     * @param
     * @return
     */
    public boolean syncUser(UserSyncRequest usr) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        try {
            OutsideUserSyncRequest syncRequest = new OutsideUserSyncRequest();
            syncRequest.setSourceId(usr.getId());
            syncRequest.setUserName(usr.getName());
            syncRequest.setPhoneNo(usr.getPhone());
            syncRequest.setIdCardNo(usr.getIdNo());
            syncRequest.setCompanyCode(usr.getCompanyCode());
            syncRequest.setCompanyName(usr.getCompanyName());
            syncRequest.setIsEnable(usr.checkStopUse() ? OutsideUserSyncRequest.STOP_USE : OutsideUserSyncRequest.ENABLE_USE);
            syncRequest.setOperatorType(usr.getOperatorType());
            syncRequest.setAppBizCodes(unitAppConfig.getAppBizCode());
            syncRequest.setAppCode(unitAppConfig.getAppCode());
            // 获取token
            String token = BEARER + getToken();
            Map<String, String> headerMap = new HashMap<>(5);
            headerMap.put(authorization, token);
            headerMap.put(APP_ID, unitAppConfig.getAppid());
            headerMap.put(Content_Type, APPLICATION_JSON);
            String jsonString = JSON.toJSONString(syncRequest);
            LOGGER.info(">>>>>>[UnifyApp]，车辆用户信息同步，参数={}, ", jsonString);
            String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getCarUserInfoSyncPath(),
                    HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8),
                    null, null);
            LOGGER.info(">>>>>>[UnifyApp]，车辆用户信息同步，结果={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            String code = jsonObject.getString(RETURN_CODE);
            if (!RETURN_CODE_SUC.equals(code)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，车辆用户信息同步异常", e);
            return false;
        }
    }

    /** 通过token获取当前用户 */
    public AppUserResponse getAppUser() {
        String authorization = RequestUtils.getHeaderValue("app-authorization");
        String deviceId = RequestUtils.getHeaderValue("X-Device-Id");
        if (ObjectUtil.isEmpty(authorization)) {
            throw new BusinessException(UnifyAppException.APP_TOKEN_NULL);
        }
        String response = null;
        try {
            Map<String, String> headerMap = MapUtil.of("Authorization", authorization);
            headerMap.put("X-Device-Id", deviceId);
            LOGGER.info(">>>>>>[UnifyApp]，通过token获取用户信息，参数={}", authorization);
            response = HttpUtils.request(unitAppConfig.getAuthHost() + unitAppConfig.getGetCurUserPath(),
                    HttpUtils.METHOD_POST, headerMap, null, null, null);
            LOGGER.info(">>>>>>[UnifyApp]，通过token获取用户信息，结果={}", response);
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，通过token获取用户信息发生异常", e);
            throw new BusinessException(UnifyAppException.GET_APP_USER_ERROR);
        }
        JSONObject jsonObject = JSON.parseObject(response);
        String code = jsonObject.getString(RETURN_CODE);
        String data = jsonObject.getString(DATA);
        if (!RETURN_CODE_SUC.equals(code) || ObjectUtil.isEmpty(data)) {
            throw new BusinessException(UnifyAppException.GET_APP_USER_ERROR);
        }
        return JSON.parseObject(data, AppUserResponse.class);
    }

    /**
     * 立即发送消息
     */
    public boolean immediatelySend(ImmediatelySendRequest sendRequest){
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return true;
        }
        try {
            sendRequest.setSeqNo(IdUtil.simpleUUID());
            sendRequest.setAppCode(unitAppConfig.getAppCode());
            sendRequest.getMessageCMDList().forEach(cmd -> cmd.setAppCode(unitAppConfig.getAppCode()));
            // 获取token
            String token = BEARER + getToken();
            Map<String, String> headerMap = new HashMap<>(5);
            headerMap.put(authorization, token);
            headerMap.put(APP_ID, unitAppConfig.getAppid());
            headerMap.put(Content_Type, APPLICATION_JSON);
            String jsonString = JSON.toJSONString(sendRequest);
            LOGGER.info(">>>>>>[UnifyApp]，车辆立即发送消息，参数={}, ", jsonString);
            String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getImmediatelySendPath(),
                    HttpUtils.METHOD_POST, headerMap, jsonString.getBytes(StandardCharsets.UTF_8),
                    null, null);
            LOGGER.info(">>>>>>[UnifyApp]，车辆立即发送消息，结果={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            String code = jsonObject.getString(RETURN_CODE);
            if (!RETURN_CODE_SUC.equals(code)) {
                return false;
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>[UnifyApp]，车辆立即发送消息", e);
            return false;
        }
        return true;
    }

    /**
     * 校验设备数量
     */
    public void checkDeviceNum(String phoneNo) {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return;
        }
        String authorization = RequestUtils.getHeaderValue("app-authorization");
        String deviceId = RequestUtils.getHeaderValue("X-Device-Id");
        if (ObjectUtil.isEmpty(authorization)) {
            throw new BusinessException(UnifyAppException.APP_TOKEN_NULL);
        }
        String response = null;
        try {
            Map<String, String> headerMap = MapUtil.of("Authorization", authorization);
            headerMap.put("X-Device-Id", deviceId);
            Map<String, String> body = MapUtil.of("phoneNo", phoneNo);
            LOGGER.info(">>>>>>[UnifyApp]，查询设备数量，参数={} 手机号={}", authorization, phoneNo);
            response = HttpUtils.request(unitAppConfig.getAuthHost() + unitAppConfig.getQueryDevicesPath(),
                    HttpUtils.METHOD_POST, headerMap, JSON.toJSONString(body).getBytes(StandardCharsets.UTF_8), null, null);
            LOGGER.info(">>>>>>[UnifyApp]，查询设备数量，结果={}", response);
            } catch (Exception e) {
                LOGGER.error(">>>>>>[UnifyApp]，校验统一APP设备数量发生异常", e);
                throw new BusinessException(UnifyAppException.QUERY_DEVICE_ERROR);
            }
            JSONObject jsonObject = JSON.parseObject(response);
            String code = jsonObject.getString(RETURN_CODE);
            JSONArray arrays = jsonObject.getJSONArray(DATA);
            if (!RETURN_CODE_SUC.equals(code) ) {
                throw new BusinessException(UnifyAppException.QUERY_DEVICE_ERROR);
            }
            if (arrays == null ||  arrays.size() <=  unitAppConfig.getMaxDeviceNum()) {
                return;
            }
        throw new BusinessException(UnifyAppException.DEVICE_NUM_LIMIT);
    }

    /** 获取toeken  */
    private String getToken() {
        if (unitAppConfig.checkMock()) {
            LOGGER.info(">>>>>>[UnifyApp]，mock开启");
            return "";
        }
        try {
            LOGGER.info(">>>>>>[UnifyApp]，获取token");
            Map<String, Object> formMap = new HashMap<>(5);
            formMap.put(CLIENT_ID, unitAppConfig.getClientId());
            formMap.put(CLIENT_SECRENT, unitAppConfig.getClientSecrent());
            formMap.put(GRANT_TYPE, unitAppConfig.getGrantType());
            String response = HttpUtils.request(unitAppConfig.getHost() + unitAppConfig.getGetTokenPath(),
                    HttpUtils.METHOD_GET, null, null,
                    formMap, null);
            LOGGER.info(">>>>>>[UnifyApp]，获取token，结果={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            return jsonObject.getString(ACCESS_TOKEN);
        } catch (Exception e) {
            LOGGER.error(">>>>>>[UnifyApp]，发生异常", e);
            return null;
        }
    }
}