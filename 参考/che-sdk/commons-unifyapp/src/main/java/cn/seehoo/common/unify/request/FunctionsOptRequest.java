package cn.seehoo.common.unify.request;

import java.util.List;

/** 功能操作请求 */
public class FunctionsOptRequest {
    /** 功能列表 */
    public List<FunctionInfo> functions;

    public FunctionsOptRequest() {
    }

    public FunctionsOptRequest(List<FunctionInfo> functions) {
        this.functions = functions;
    }

    public List<FunctionInfo> getFunctions() {
        return functions;
    }
    public void setFunctions(List<FunctionInfo> functions) {
        this.functions = functions;
    }

    /** 功能 */
    public static class FunctionInfo {
        /** ID */
        private Long id;
        /** 功能编码 */
        private String code;
        /** 功能名称 */
        private String name;
        /** 父功能ID */
        private Long pid;
        /** 路由 */
        private String route;
        /** 路径 */
        private String path;
        /** 图标路径 */
        private String icon;
        /** 功能类型 1目录、2功能、3按钮 */
        private Integer type;

        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Long getPid() {
            return pid;
        }
        public void setPid(Long pid) {
            this.pid = pid;
        }
        public String getRoute() {
            return route;
        }
        public void setRoute(String route) {
            this.route = route;
        }
        public String getPath() {
            return path;
        }
        public void setPath(String path) {
            this.path = path;
        }
        public String getIcon() {
            return icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
        public Integer getType() {
            return type;
        }
        public void setType(Integer type) {
            this.type = type;
        }
    }
}
