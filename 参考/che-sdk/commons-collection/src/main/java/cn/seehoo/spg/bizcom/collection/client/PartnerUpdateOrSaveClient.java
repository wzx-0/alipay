package cn.seehoo.spg.bizcom.collection.client;

import cn.seehoo.spg.bizcom.collection.dto.CollectionSyncDTO;
import cn.seehoo.spg.commons.core.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * 其他合作方
 * @author zhangxx
 * @date 2026/1/8 9:21
 */
@Component
public interface PartnerUpdateOrSaveClient {
    /** 同步其他合作方信息 */
    public boolean syncPartnerInfo(CollectionSyncDTO csdto) throws BusinessException;
}
