package cn.nyaaar.partridgemngservice.util;


/**
 * @author yuegenhua
 * @Version $Id: ThreadLocalUtil.java, v 0.1 2022-27 9:52 yuegenhua Exp $$
 */
public class ThreadLocalUtil {
    /**
     * 保存用户对象的ThreadLocal  在拦截器操作 添加、删除相关用户数据
     */
    private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

    /**
     * 添加当前登录用户方法  在拦截器方法执行前调用设置获取用户
     *
     * @param userName userName
     */
    public static void addCurrentUser(String userName) {
        userThreadLocal.set(userName);
    }

    /**
     * 获取当前登录用户方法
     */
    public static String getCurrentUser() {
        if (userThreadLocal.get() != null) {
            return userThreadLocal.get();
        }
        return "test";
    }


    /**
     * 删除当前登录用户方法  在拦截器方法执行后 移除当前用户对象
     */
    public static void remove() {
        userThreadLocal.remove();
    }
}
