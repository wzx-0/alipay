package cn.seehoo.spg.common.risk.model;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 通用提报接口请求入参
 */
@Data
@Accessors(chain = true)
public class CommonRiskSubmitReq extends BaseReq {

    /**
     * 业务场景标识，Array 类型，支持多场景并行提报
     * <p>枚举值：01-跨渠道拒绝拦截 / 02-金融专员黑名单</p>
     */
    private List<String> bizScene;

    /**
     * 业务订单类型
     * <p>枚举值：01-预审 / 02-提报</p>
     */
    private String orderType;

    /**
     * 风控异步任务ID，uuid，用于幂等/重试防重
     */
    private String taskId = UUID.fastUUID().toString();

    /**
     * 请求时间，格式 yyyy-MM-dd HH:mm:ss
     */
    private String submitTime = DateUtil.formatDateTime(new Date());

    /**
     * 所属渠道
     * <p>bizScene=01 时必传</p>
     */
    private String channelCode;

    /**
     * 项目名称，字典 D98013 项目模式
     * <p>枚举值：01-有担保业务 / 02-无担保业务</p>
     * <p>bizScene=01 时必传</p>
     */
    private String projectName = "02";

    /**
     * 业务数据，结构随 bizScene 变化
     * <p>故意声明为 {@link Object} 以保证扩展性——新增 bizScene 场景时无需修改 DTO，
     * 调用方按需传入 {@code Map<String,Object>} 或具体业务对象即可</p>
     *
     * <p>bizScene=01 示例：{@code {"tenantIdNo":"xxx","guarantorIdNo":"xxx"}}</p>
     * <p>bizScene=02 示例：{@code {"staffName":"xxx","staffIdNo":"xxx","staffIdType":"xxx"}}</p>
     */
    private Object bizData;
}
