package cn.seehoo.spg.bizcom.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author adminis
 */
@Slf4j
public final class BeanCopyUtil {

    /**
     * 拷贝对象并排除指定字段（支持多层嵌套，防循环引用栈溢出）
     */
    public static <T, R> R copyBeanExcludeFields(T source, Class<R> targetClass, String... excludeFields) {
        if (source == null) {
            return null;
        }
        List<String> excludeFieldList = Arrays.asList(excludeFields);

        // 1. 初始化目标对象
        R target = ReflectUtil.newInstance(targetClass);

        // 2. 配置拷贝规则（排除指定字段）
        CopyOptions copyOptions = CopyOptions.create()
                .setPropertiesFilter((field, value) -> !excludeFieldList.contains(field.getName()))
                .setIgnoreNullValue(false)
                .setIgnoreError(true);

        // 3. 执行基础拷贝（顶层字段）
        BeanUtil.copyProperties(source, target, copyOptions);

        // 4. 递归处理嵌套对象，使用 Set 记录已处理对象，防止循环引用
        excludeNestedFields(target, excludeFieldList, new IdentityHashMap<>());

        return target;
    }

    /**
     * 递归处理嵌套对象，清空指定字段的值（带防重）
     * @param obj 待处理对象
     * @param excludeFields 需要排除的字段名
     * @param processed 已处理过的对象集合，防止循环引用导致栈溢出
     */
    private static void excludeNestedFields(Object obj, List<String> excludeFields, Map<Object, Boolean> processed) {
        if (obj == null || processed.containsKey(obj)) {
            return; // 已处理过的对象直接返回，避免循环递归
        }
        processed.put(obj, true); // 标记为已处理

        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);

                // 1. 如果是需要排除的字段，直接置空
                if (excludeFields.contains(field.getName())) {
                    field.set(obj, null);
                    continue;
                }

                // 2. 如果是嵌套对象（非基本类型/字符串），递归处理
                if (fieldValue != null
                        && !field.getType().isPrimitive()
                        && !String.class.equals(field.getType())
                        && !field.getType().getName().startsWith("java.lang.")) {
                    excludeNestedFields(fieldValue, excludeFields, processed);
                }
            } catch (IllegalAccessException e) {
                log.warn("排除嵌套字段失败，字段名：{}，原因：{}", field.getName(), e.getMessage());
            }
        }
    }
}