package cn.nyaaar.partridgemngservice.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "RegisterDto")
public class RegistrationRequest {

    private String email;

    private String userName;

    private String password;
}
