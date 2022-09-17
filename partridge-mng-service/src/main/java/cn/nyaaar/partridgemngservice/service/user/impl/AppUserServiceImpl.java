package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.entity.PrUser;
import cn.nyaaar.partridgemngservice.model.user.RegistrationRequest;
import cn.nyaaar.partridgemngservice.service.PrUserService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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
        return User.withUsername(prUser.getUserName()).password(prUser.getPassword()).build();
    }

    @Override
    public String register(RegistrationRequest request) {

//        boolean isValidEmail = emailValidator.test(request.getEmail());

//        if (isValidEmail) {
//            throw new IllegalStateException("Email is not valid!");
//        }
//        String token = appUserService.signUpUser(
//                new AppUser(
//                        request.getFirstName(),
//                        request.getLastName(),
//                        request.getEmail(),
//                        request.getPassword(),
//                        AppUserRole.USER
//                )
//        );
//        String link = "http://localhost:8080/api/v1/registration/confirm/?token=" + token;
//        emailSender.send(
//                request.getEmail(),
//                buildEmail(request.getFirstName(), link) );
        String encoderPassword = passwordEncoder.encode(request.getPassword());
        PrUser prUser = new PrUser()
                .setUserName(request.getUserName())
                .setPassword(encoderPassword)
                .setEmail(request.getEmail())
                .setValidated(PrConstant.VALIDATED)
                .setLastLoginTime(DateUtil.date());
        System.out.println("使用security加密后的密码为：" + encoderPassword);
        prUserService.save(prUser);

        return "token";
    }

    // TODO
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

}
