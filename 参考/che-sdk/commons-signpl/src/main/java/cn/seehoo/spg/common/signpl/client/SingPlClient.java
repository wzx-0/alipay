package cn.seehoo.spg.common.signpl.client;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.seehoo.spg.common.signpl.model.SignLinkDataResDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.seehoo.spg.common.signpl.config.SignPlConfig;
import cn.seehoo.spg.common.signpl.constant.SignApiConstant;
import cn.seehoo.spg.common.signpl.exception.SinPlException;
import cn.seehoo.spg.common.signpl.model.CancelContractDTO;
import cn.seehoo.spg.common.signpl.model.DownloadContractDTO;
import cn.seehoo.spg.common.signpl.model.DownloadContractResDTO;
import cn.seehoo.spg.common.signpl.model.SignLinkQueryDTO;
import cn.seehoo.spg.common.signpl.model.SignPlBaseRes;
import cn.seehoo.spg.common.signpl.model.SignProcessCreateDTO;
import cn.seehoo.spg.common.signpl.model.SignProcessCreateResDTO;
import cn.seehoo.spg.common.signpl.model.SignResultQueryDTO;
import cn.seehoo.spg.common.signpl.model.SignResultResDTO;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import cn.seehoo.spg.commons.core.util.HttpUtils;

/**
 * @author caofei
 * @desc 签约平台客户端
 * @time 2025/9/25 17:05。
 */
public class SingPlClient implements SingPlBaseClient{
    private static final Logger LOGGER = LoggerFactory.getLogger(SingPlClient.class);
    @Autowired
    private SignPlConfig signPlConfig;
    @Override
    public SignProcessCreateResDTO createSignProcess(SignProcessCreateDTO signProcessCreate) {
        LOGGER.info(">>>>>> [签约平台] 创建签约流程，签约订单：{}，签约场景：{}", signProcessCreate.getBusinessNo(), signProcessCreate.getBusinessScene());
        signProcessCreate.signDictMapping();
        String configAuthType = signPlConfig.getAuthType();
        LOGGER.info(">>>>>> [签约平台] 创建签约流程，实名方式:{}",configAuthType);
        if (StrUtil.isNotBlank(configAuthType) && CollectionUtil.isNotEmpty(signProcessCreate.getSigners())) {
            for (SignProcessCreateDTO.Signer signer : signProcessCreate.getSigners()) {
                signer.setAuthType(configAuthType);
            }
        }
        signProcessCreate.setPushUrl(signPlConfig.getPushUrl());
        LOGGER.info(">>>>>> [签约平台] 创建签约流程，签约:{}",JSONUtil.toJsonStr(signProcessCreate));
        byte[] reqBodyBytes = JSONUtil.toJsonStr(signProcessCreate).getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(signPlConfig.getHost() + SignApiConstant.CREATE_SIGN_PROCESS,HttpUtils.METHOD_POST,headers, reqBodyBytes, null,"");
        LOGGER.info(">>>>>> [签约平台] 创建签约流程，出参：{}", httpResponse);
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [签约平台] 签约平台 response body 为空");
            throw new BusinessException(SinPlException.SIGN_PLAT_RES_BODY_NULL);
        }
        SignPlBaseRes signProcessRes = JSONUtil.toBean(httpResponse, SignPlBaseRes.class);
        if(!signProcessRes.bizSuccess()|| StrUtil.isBlank(signProcessRes.getData())){
            LOGGER.error(">>>>>> [签约平台] 创建签约流程失败：{}", signProcessRes.getMsg());
            throw new BusinessException(signProcessRes.getCode(), signProcessRes.getMsg());
        }
        return JSONUtil.toBean(signProcessRes.getData(),SignProcessCreateResDTO.class);
    }
    @Override
    public String getSignLink(SignLinkQueryDTO signLinkQuery) {
        LOGGER.info(">>>>>> [签约平台] 获取签约链接，签约订单：{}", signLinkQuery.getOrderNo());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        byte[] reqBodyBytes = JSONUtil.toJsonStr(signLinkQuery).getBytes(StandardCharsets.UTF_8);
        String httpResponse =  HttpUtils.request(signPlConfig.getHost() + SignApiConstant.GET_SIGN_LINK,HttpUtils.METHOD_POST,headers, reqBodyBytes, null,"");
        LOGGER.info(">>>>>> [签约平台] 获取签约链接，出参：{}", httpResponse);
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [签约平台] 签约平台 response body 为空");
            throw new BusinessException(SinPlException.SIGN_PLAT_RES_BODY_NULL);
        }
        SignPlBaseRes res = JSONUtil.toBean(httpResponse, SignPlBaseRes.class);
        if(!res.bizSuccess()|| StrUtil.isBlank(res.getData())){
            LOGGER.error(">>>>>> [签约平台] 获取签约链接失败：{}", res.getMsg());
            throw new BusinessException(res.getCode(), res.getMsg());
        }
        return res.getData();
    }
    @Override
    public SignResultResDTO getSignResult(SignResultQueryDTO signResultQuery) {
        LOGGER.info(">>>>>> [签约平台] 获取签约结果，签约订单：{}", signResultQuery.getOrderNo());
        byte[] reqBodyBytes = JSONUtil.toJsonStr(signResultQuery).getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(signPlConfig.getHost() + SignApiConstant.GET_SIGN_RESULT,HttpUtils.METHOD_POST,headers, reqBodyBytes, null,"");
        LOGGER.info(">>>>>> [签约平台] 获取签约结果，出参：{}", httpResponse);
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [签约平台] 签约平台 response body 为空");
            throw new BusinessException(SinPlException.SIGN_PLAT_RES_BODY_NULL);
        }
        SignPlBaseRes res = JSONUtil.toBean(httpResponse, SignPlBaseRes.class);
        if(!res.bizSuccess() || StrUtil.isBlank(res.getData())){
            LOGGER.error(">>>>>> [签约平台] 获取签约结果失败：{}", res.getMsg());
            throw new BusinessException(res.getCode(), res.getMsg());
        }
        SignResultResDTO signResultResDTO = JSONUtil.toBean(res.getData(), SignResultResDTO.class);
        return signResultResDTO;
    }
    @Override
    public void cancelContract(CancelContractDTO cancelContract) {
        LOGGER.info(">>>>>> [签约平台] 取消合同，签约订单：{}", cancelContract.getOrderNo());
        byte[] reqBodyBytes = JSONUtil.toJsonStr(cancelContract).getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(signPlConfig.getHost() + SignApiConstant.CANCEL_CONTRACT,HttpUtils.METHOD_POST,headers, reqBodyBytes, null,"");
        LOGGER.info(">>>>>> [签约平台] 取消合同，出参：{}", httpResponse);
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [签约平台] 签约平台 response body 为空");
            throw new BusinessException(SinPlException.SIGN_PLAT_RES_BODY_NULL);
        }
        SignPlBaseRes res = JSONUtil.toBean(httpResponse, SignPlBaseRes.class);
        if(!res.bizSuccess()){
            LOGGER.error(">>>>>> [签约平台] 取消合同失败：{}", res.getMsg());
            throw new BusinessException(res.getCode(), res.getMsg());
        }
    }

	@Override
    public  List<DownloadContractResDTO> downloadContracts(DownloadContractDTO downloadContract) {
        LOGGER.info(">>>>>> [签约平台] 下载附件:{}",JSONUtil.parse(downloadContract));
        byte[] reqBodyBytes = JSONUtil.toJsonStr(downloadContract).getBytes(StandardCharsets.UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        String httpResponse =  HttpUtils.request(signPlConfig.getHost() + SignApiConstant.CONTRACT_DOWNLOAD,HttpUtils.METHOD_POST,headers, reqBodyBytes, null,"");
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [签约平台] 签约平台 response body 为空");
            throw new BusinessException(SinPlException.SIGN_PLAT_RES_BODY_NULL);
        }
        SignPlBaseRes res = JSONUtil.toBean(httpResponse, SignPlBaseRes.class);
        if(!res.bizSuccess()|| StrUtil.isBlank(res.getData())){
            LOGGER.error(">>>>>> [签约平台] 下载附件失败：{}", res.getMsg());
            throw new BusinessException(res.getCode(), res.getMsg());
        }
        return JSONUtil.toList(res.getData(),DownloadContractResDTO.class);
    }

    @Override
    public SignLinkDataResDTO getSignLinkData(SignLinkQueryDTO signLinkQuery) {
        LOGGER.info(">>>>>> [签约平台] 获取签署方信息，签约订单：{}", signLinkQuery.getOrderNo());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        byte[] reqBodyBytes = JSONUtil.toJsonStr(signLinkQuery).getBytes(StandardCharsets.UTF_8);
        String httpResponse =  HttpUtils.request(signPlConfig.getHost() + SignApiConstant.GET_SIGN_LINK_DATA,HttpUtils.METHOD_POST,headers, reqBodyBytes, null,"");
        LOGGER.info(">>>>>> [签约平台] 获取签署方信息，出参：{}", httpResponse);
        if(StrUtil.isBlank(httpResponse)){
            LOGGER.error(">>>>>> [签约平台] 签约平台 response body 为空");
            throw new BusinessException(SinPlException.SIGN_PLAT_RES_BODY_NULL);
        }
        SignPlBaseRes res = JSONUtil.toBean(httpResponse, SignPlBaseRes.class);
        if(!res.bizSuccess()){
            LOGGER.error(">>>>>> [签约平台] 获取签署方信息失败：{}", res.getMsg());
            throw new BusinessException(res.getCode(), res.getMsg());
        }
        if(ObjUtil.isNull(res.getData()) || ObjUtil.isEmpty(res.getData())){
            LOGGER.info(">>>>>> [签约平台] 返回无下一签署方信息：{}", JSONUtil.parse(res));
            return null;
        }
        return JSONUtil.toBean(res.getData(), SignLinkDataResDTO.class);
    }
}
