package cn.nyaaar.partridgemngservice.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "RegisterDto")
public class RegistrationReq {

    @Schema(title = "邮箱")
    @NotNull(message = "请指定邮箱")
    private String email;

    @Schema(title = "用户名")
    @NotNull(message = "请指定用户名")
    private String userName;

    @Schema(title = "密码")
    @NotNull(message = "请指定密码")
    private String password;
}
