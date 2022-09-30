package cn.nyaaar.partridgemngservice.service.user;

import cn.nyaaar.partridgemngservice.model.user.RegistrationReq;

public interface AppUserService {

    /**
     * 验证邮箱 token
     *
     * @param token token
     * @return 是否验证成功
     */
    boolean confirmToken(String token);

    /**
     * 注册账户
     *
     * @param request request
     * @return token
     */
    String register(RegistrationReq request);

    /**
     * 检查用户是否有空间配额可以下载
     *
     * @param userName userName
     * @return 是否有空间配额可以下载
     */
    Boolean checkUserSpaceLimit(String userName);

    /**
     * 下载或上传完成后减去用户配额
     *
     * @param userName   userName
     * @param spaceBytes bytes
     */
    void minusUserSpaceLimit(String userName, Long spaceBytes);

    /**
     * 删除完成后增加用户配额
     *
     * @param userName   userName
     * @param spaceBytes bytes
     */
    void freeUserSpaceLimit(String userName, Long spaceBytes);
}
