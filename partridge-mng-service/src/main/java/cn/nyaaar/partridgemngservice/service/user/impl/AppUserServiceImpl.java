package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.enums.PrivilegeEnum;
import cn.nyaaar.partridgemngservice.entity.PrUser;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.exception.ValidationException;
import cn.nyaaar.partridgemngservice.model.user.RegistrationReq;
import cn.nyaaar.partridgemngservice.service.PrUserService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Slf4j
public class AppUserServiceImpl implements UserDetailsService, AppUserService {

    private final PrUserService prUserService;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(PrUserService prUserService, PasswordEncoder passwordEncoder) {
        this.prUserService = prUserService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PrUser prUser = prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, username));
        if (prUser == null) {
            throw new UsernameNotFoundException(username + " not found...");
        }
        if (prUser.getValidated() != PrConstant.VALIDATED) {
            throw new UsernameNotFoundException(username + " invalided...");
        }
        // TODO user roles
        PrivilegeEnum privilegeEnum;
        if (username.equals("root")) {
            privilegeEnum = PrivilegeEnum.ROOT;
        } else {
            privilegeEnum = PrivilegeEnum.USER;
        }
        // TODO user login record
        return User.withUsername(prUser.getUserName()).password(prUser.getPassword()).roles(privilegeEnum.getCode()).build();
    }

    @Override
    public String register(RegistrationReq request) {
        String pattern = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
        boolean isValidEmail = Pattern.matches(pattern, request.getEmail());

        if (!isValidEmail) {
            throw new ValidationException(BusinessExceptionEnum.FIELD_ERROR, null, "邮箱格式异常");
        }
        BusinessExceptionEnum.USER_EXIST.assertIsNull(prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, request.getUserName())));
//        emailSender.send(
//                request.getEmail(),
//                buildEmail(request.getFirstName(), link) );
        // TODO invite code and user level
        String encoderPassword = passwordEncoder.encode(request.getPassword());
        PrUser prUser = new PrUser()
                .setUserName(request.getUserName())
                .setPassword(encoderPassword)
                .setEmail(request.getEmail())
                .setValidated(PrConstant.VALIDATED)
                .setSpaceQuota(100 * 1073741824L)
                .setLastLoginTime(DateUtil.date());
        System.out.println("使用security加密后的密码为：" + encoderPassword);
        prUserService.save(prUser);

        return "token";
    }

    // TODO emailConfirm
//    @Transactional
    @Override
    public boolean confirmToken(String token) {
//        ConfirmationToken confirmationToken = confirmationTokenService
//                .getToken(token)
//                .orElseThrow(() ->
//                        new IllegalStateException("token not found"));
//
//        if (confirmationToken.getConfirmedAt() != null) {
//            throw new IllegalStateException("email already confirmed");
//        }
//
//        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
//
//        if (expiredAt.isBefore(LocalDateTime.now())) {
//            throw new IllegalStateException("token expired");
//        }
//
//        confirmationTokenService.setConfirmedAt(token);
//        appUserService.enableAppUser(
//                confirmationToken.getAppUser().getEmail());
        return true;
    }

    @Override
    public Boolean checkUserSpaceLimit(String userName) {
        PrUser prUser = prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, userName));
        return prUser.getSpaceQuota() > 0;
    }

    @Override
    public void minusUserSpaceLimit(String userName, Long spaceBytes) {
        if (null == spaceBytes || spaceBytes <= 0) {
            return;
        }
        PrUser prUser = prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, userName));
        prUserService.update(Wrappers.lambdaUpdate(PrUser.class)
                .set(PrUser::getSpaceQuota, prUser.getSpaceQuota() - spaceBytes)
                .eq(PrUser::getUserName, userName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freeUserSpaceLimit(String userName, Long spaceBytes) {
        if (null == spaceBytes || spaceBytes <= 0) {
            return;
        }
        if (StringUtils.isBlank(userName)) {
            BusinessExceptionEnum.PERMISSION_DENY.assertFail();
        }
        PrUser prUser = prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, userName));
        prUserService.update(Wrappers.lambdaUpdate(PrUser.class)
                .set(PrUser::getSpaceQuota, prUser.getSpaceQuota() + spaceBytes)
                .eq(PrUser::getUserName, userName));
    }
}
