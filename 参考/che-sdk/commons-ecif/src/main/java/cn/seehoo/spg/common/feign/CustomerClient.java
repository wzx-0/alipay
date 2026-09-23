package cn.seehoo.spg.common.feign;

import cn.seehoo.spg.common.feign.dto.EcifTurnoverRecordsRes;
import cn.seehoo.spg.commons.core.web.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 客户接口
 *
 * @author cj
 * @since 2023-08-28
 */
@FeignClient(name = "${customer.service.name:seehoo-customer}", contextId = "CustomerFeignClient")
public interface CustomerClient {

    /** 新增或更新对公客户 调用记录保存 */
    @PostMapping("/adminapi/customerEcifDeal/insertEcifTurnoverRecords")
    BaseResponse<Void> insertEcifTurnoverRecords(@RequestBody EcifTurnoverRecordsRes dto);
}