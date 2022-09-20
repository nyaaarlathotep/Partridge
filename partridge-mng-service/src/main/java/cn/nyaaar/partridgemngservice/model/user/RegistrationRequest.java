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

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "用户名")
    private String userName;

    @Schema(name = "密码")
    private String password;
}
