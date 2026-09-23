package cn.seehoo.spg.common.signpl.client;

import java.util.List;

import cn.seehoo.spg.common.signpl.model.CancelContractDTO;
import cn.seehoo.spg.common.signpl.model.DownloadContractDTO;
import cn.seehoo.spg.common.signpl.model.DownloadContractResDTO;
import cn.seehoo.spg.common.signpl.model.SignLinkDataResDTO;
import cn.seehoo.spg.common.signpl.model.SignLinkQueryDTO;
import cn.seehoo.spg.common.signpl.model.SignProcessCreateDTO;
import cn.seehoo.spg.common.signpl.model.SignProcessCreateResDTO;
import cn.seehoo.spg.common.signpl.model.SignResultQueryDTO;
import cn.seehoo.spg.common.signpl.model.SignResultResDTO;

/**
 * @desc: 签约平台Base客户端
 * @author: caofei
 * @time: 2025/9/25 17:25
 */
public interface SingPlBaseClient {
    /**
     * 创建签约流程
     * @param signProcessCreate 创建签约流程参数
     * @return 创建签约流程结果
     */
    public SignProcessCreateResDTO createSignProcess(SignProcessCreateDTO signProcessCreate);

    /**
     * 获取签约链接
     * @param signLinkQuery 订单号
     * @return 签约链接
     */
    public String getSignLink(SignLinkQueryDTO signLinkQuery);

    /**
     * 获取签约结果
     * @param signResultQuery 查询参数
     * @return 签约结果
     */
    public SignResultResDTO getSignResult(SignResultQueryDTO signResultQuery);

    /**
     * 取消合同
     * @param cancelContract 参数
     */
    public void cancelContract(CancelContractDTO cancelContract);

    /**
     * 下载合同
     * @param downloadContract 参数
     * @return 下载结果列表
     */
    public  List<DownloadContractResDTO>  downloadContracts(DownloadContractDTO downloadContract);

    /**
     * 获取签约链接
     * @param signLinkQuery 订单号
     * @return 签约链接
     */
    public SignLinkDataResDTO getSignLinkData(SignLinkQueryDTO signLinkQuery);
}
