package com.seehoo.rent.app.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象复制工具：Entity/DTO/VO同名属性拷贝统一入口
 * <p>工程未引入公司common-infra-extension前，以Hutool实现等价能力，
 * 后续并入公司工程时切换为 cn.seehoo.infra.plugin.common.BeanUtils 即可</p>
 */
public final class BeanCopyUtil {

    private BeanCopyUtil() {
    }

    /** 单对象复制：将src同名属性拷贝到新dest实例 */
    public static <T> T mapper(Object src, Class<T> destClass) {
        if (src == null) {
            return null;
        }
        try {
            T dest = destClass.getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(src, dest);
            return dest;
        } catch (Exception e) {
            throw new IllegalStateException("对象复制失败：" + destClass.getSimpleName(), e);
        }
    }

    /** 列表复制 */
    public static <T> List<T> mapperCols(List<?> srcList, Class<T> destClass) {
        if (srcList == null) {
            return null;
        }
        return srcList.stream().map(src -> mapper(src, destClass)).collect(Collectors.toList());
    }

    /** Long主键转String（VO主键统一String，防前端精度丢失） */
    public static String idToStr(Long id) {
        return id == null ? null : StrUtil.toString(id);
    }
}
