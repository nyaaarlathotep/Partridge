package cn.nyaaar.partridgemngservice.service.user;

import cn.nyaaar.partridgemngservice.model.user.RegistrationReq;

public interface AppUserService {

    boolean confirmToken(String token);

    String register(RegistrationReq request);
}
