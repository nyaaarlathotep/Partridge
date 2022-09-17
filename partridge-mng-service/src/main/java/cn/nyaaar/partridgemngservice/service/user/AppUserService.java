package cn.nyaaar.partridgemngservice.service.user;

import cn.nyaaar.partridgemngservice.model.user.RegistrationRequest;

public interface AppUserService {

    boolean confirmToken(String token);

    String register(RegistrationRequest request);
}
