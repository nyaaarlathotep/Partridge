package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.PrivilegeEnum;
import cn.nyaaar.partridgemngservice.entity.PrUser;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
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

    private final static Long GBToBytes = 1073741824L;

    private final static String emailPattern = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";
    private final static String userNamePattern = "^[a-z0-9_-]{3,15}$";
    private final static String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$";

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
        if (!Pattern.matches(emailPattern, request.getEmail())) {
            BusinessExceptionEnum.FIELD_ERROR.assertFail("?????????????????????");
        }
        if (!Pattern.matches(userNamePattern, request.getUserName())) {
            BusinessExceptionEnum.FIELD_ERROR.assertFail("?????????????????????????????????????????????_???-????????????????????????????????????3???16????????????");
        }
        if (!Pattern.matches(passwordPattern, request.getPassword())) {
            BusinessExceptionEnum.FIELD_ERROR.assertFail("?????????????????????????????????????????????????????????????????????????????????");
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
                .setSpaceQuota(Settings.getUserSpaceQuota() * GBToBytes)
                .setLastLoginTime(DateUtil.date());
        System.out.println("??????security????????????????????????" + encoderPassword);
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
