package cn.seehoo.common.unify.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.ObjectUtil;
import cn.seehoo.common.unify.config.UserSyncConfig;
import cn.seehoo.common.unify.exception.SyncUserDeptException;
import cn.seehoo.common.unify.request.SyncUserOrDeptRequest;
import cn.seehoo.common.unify.response.SyncDepartmentResponse;
import cn.seehoo.common.unify.response.SyncUserInfoResponse;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;

/**
 * @author sunyf
 * @date 2025/9/24 上午11:45
 * @since 1.0
 */
@Component
public class UserSyncClient {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserSyncClient.class);
    public static final String RETURN_CODE = "returnCode";
    public static final String RETURN_CODE_SUC = "000000";
    public static final String BEARER = "Bearer ";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String DATA = "data";
    public static final String APP_ID = "appid";
    public static final String DEPT_ID = "deptId";
    @Autowired
    private UserSyncConfig unifyUserConfig;


    /**
     * 用户信息同步
     * @param prarequest
     * @return
     */
    public List<SyncUserInfoResponse> syncUserInfo(SyncUserOrDeptRequest prarequest) {
        if (unifyUserConfig.isMock()) {
            LOGGER.info(">>>>>>[usersync]，mock开启");
            return new ArrayList<>();
        }
        // 校验参数
        prarequest.checkParams();
        // 获取token
        String header = BEARER+getToken();
        Map<String, String> headerMap = new HashMap<>(5);
        headerMap.put("Authorization", header);
        String deptId = prarequest.getDeptId();
        Map<String, Object> formMap = new HashMap<>();
        formMap.put(DEPT_ID, deptId);
        LOGGER.info(">>>>>>[usersync]，用户信息同步，参数={}", deptId);
        String response = HttpUtils.request(unifyUserConfig.getHost() + unifyUserConfig.getSyncUserInfoPath(),
                HttpUtils.METHOD_GET,headerMap , null,
                formMap, null);
        LOGGER.info(">>>>>>[usersync]，用户信息同步，结果={}", response);
        JSONObject jsonObject = JSON.parseObject(response);
        String code = jsonObject.getString(RETURN_CODE);
        if (!code.equals(RETURN_CODE_SUC)) {
            throw new BusinessException(SyncUserDeptException.USER_INVOKE_ERROR);
        }
        String data = jsonObject.getString(DATA);
        if (ObjectUtil.isEmpty(data)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(data, SyncUserInfoResponse.class);
    }


    /**
     * 部门信息同步
     * @param prarequest
     * @return
     */
    public List<SyncDepartmentResponse> syncDepartment(SyncUserOrDeptRequest prarequest) {
        if (unifyUserConfig.isMock()) {
            LOGGER.info(">>>>>>[usersync]，mock开启");
            return new ArrayList<>();
        }
        // 校验参数
        prarequest.checkParams();
        // 获取token
        String header = BEARER+getToken();
        Map<String, String> headerMap = new HashMap<>(5);
        headerMap.put("Authorization", header);
        String deptId = prarequest.getDeptId();
        Map<String, Object> formMap = new HashMap<>();
        formMap.put(DEPT_ID, deptId);
        LOGGER.info(">>>>>>[usersync]，部门信息同步，参数={}", deptId);
        String response = HttpUtils.request(unifyUserConfig.getHost() + unifyUserConfig.getSyncDepartmentPath(),
                HttpUtils.METHOD_GET,headerMap , null,
                formMap, null);
        LOGGER.info(">>>>>>[usersync]，部门信息同步，结果={}", response);
        JSONObject jsonObject = JSON.parseObject(response);
        String code = jsonObject.getString(RETURN_CODE);
        if (!code.equals(RETURN_CODE_SUC)) {
            throw new BusinessException(SyncUserDeptException.DEPT_INVOKE_ERROR);
        }
        String data = jsonObject.getString(DATA);
        if (ObjectUtil.isEmpty(data)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(data, SyncDepartmentResponse.class);
    }



    /** 获取toeken  */
    private String getToken() {
        if (unifyUserConfig.isMock()) {
            LOGGER.info(">>>>>>[usersync]，mock开启");
            return "";
        }
        LOGGER.info(">>>>>>[usersync]，获取token");
        Map<String, Object> formMap = new HashMap<>(5);
        formMap.put(USER_NAME, unifyUserConfig.getUsername());
        formMap.put(PASSWORD, unifyUserConfig.getPassword());
        formMap.put(APP_ID, unifyUserConfig.getAppid());
        String response = HttpUtils.request(unifyUserConfig.getHost() + unifyUserConfig.getGetTokenPath(),
                HttpUtils.METHOD_GET, null, null,
                formMap, null);
        LOGGER.info(">>>>>>[usersync]，获取token，结果={}", response);
        JSONObject jsonObject = JSON.parseObject(response);
        return jsonObject.getString(DATA);
    }
}
