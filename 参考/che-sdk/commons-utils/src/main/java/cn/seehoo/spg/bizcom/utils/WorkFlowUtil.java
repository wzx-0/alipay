package cn.seehoo.spg.bizcom.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.bizcom.config.WorkAssistantConfig;
import cn.seehoo.spg.bizcom.dic.ProcState;
import cn.seehoo.spg.bizcom.dto.TaskNoticeDTO;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.hxfl.cloud.signature.constant.BaseConstant;
import com.hxfl.cloud.signature.util.CommonUtils;
import com.hxfl.cloud.signature.util.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程工具
 * */
public final class WorkFlowUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowUtil.class);
    @Autowired
    private WorkAssistantConfig wac;

    /** 流程任务通知 */
    public void taskNotice(TaskNoticeDTO tndto) throws BusinessException {
        //数据处理
        String procState = tndto.getProcState();
        tndto.setProcState(ProcState.transWdStatus(procState));
        tndto.setLabel(wac.getLabel());
        List<TaskNoticeDTO.TaskDTO> taskList = tndto.getNoticeTaskList();
        for (TaskNoticeDTO.TaskDTO task: taskList){
            String taskId = task.getTaskId();
            task.setId(taskId + wac.getLabel());
        }
        try {
            JSONObject body = JSONUtil.parseObj(tndto, false);
            String paramStr = CommonUtils.extractRequestParams(body.toString(), null, null);
            String content = CommonUtils.joinStr("#", wac.getAppKey(), paramStr);
            String sign = SignUtil.signSm2(wac.getPrivateKey(), content);
            Map<String, String> headers = new HashMap<>();
            headers.put(BaseConstant.X_REQUEST_SIGNATURE, sign);
            headers.put(BaseConstant.APP_KEY, wac.getAppKey());
            headers.put("User-Agent","Apifox/1.0.0 (https://www.apifox.cn)");
            headers.put("Content-Type","application/json");
            byte[] requestBody = body.toString().getBytes("UTF-8");
            LOGGER.info(">>>>>>[工作助手]，统一代办任务通知，参数= {}",JSON.toJSONString(tndto));
            String result = HttpUtils.request(wac.getUrl(), HttpUtils.METHOD_POST, headers, requestBody, null, null);
            LOGGER.info(">>>>>>[工作助手]，服务返回结果，result= {}",result);

        }catch (Exception e){
            LOGGER.error(">>>>>>[工作助手]，统一代办任务通知异常，e={}", e);
        }
    }

//    public static void main(String[] args) {
//        WorkFlowUtil workFlowUtil = new WorkFlowUtil();
//        WorkAssistantConfig config = new WorkAssistantConfig();
//        config.setAppKey("che");
//        config.setLabel("che");
//        config.setUrl("https://gzzs-test.hxfl.com.cn/pmobile/unity/api/task/notice");
//        config.setPrivateKey("MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgKIWil0LcIQczeqsIJWUmvbI3Qifv9yomrI8SgEkHUuCgCgYIKoEcz1UBgi2hRANCAAR7SDhPSIHLkDC0cR0AZlv0aPp8dMz2h9fZK8hA0RBgya8FEFVXVWTWFNLez4RANtvRZLLGzEmzolkYpRGb/H2m");
//        workFlowUtil.wac = config;
//        TaskNoticeDTO tndto = new TaskNoticeDTO();
//        tndto.setNoticeUsersBefores("test");
//        tndto.setProcInstId("3b365683-9aaa-11f0-bf3f-fee87c49b497");
//        tndto.setProcState("2");
//        List<TaskNoticeDTO.TaskDTO> noticeTaskList = new ArrayList<>();
//        TaskNoticeDTO.TaskDTO task = new TaskNoticeDTO.TaskDTO();
//        task.setId("8154");
//        task.setAction("id=gogzzs|name=Jump|type=pushroot|node=offlinepackage|target=00000023/unsecuredPage.html|isneedlogin=true|param:ok=1&navbar=false&readTitle=false|passData:listName=prepareList&tenantId=1&procInstId=3b365683-9aaa-11f0-bf3f-fee87c49b497&taskInstId=8154");
//        task.setTitle("hx001-提交审批");
//        task.setNoticeUsers("admin");
//        task.setBody("hx001-提交审批");
//        noticeTaskList.add(task);
//        tndto.setNoticeTaskList(noticeTaskList);
//        workFlowUtil.taskNotice(tndto);
//    }
}
