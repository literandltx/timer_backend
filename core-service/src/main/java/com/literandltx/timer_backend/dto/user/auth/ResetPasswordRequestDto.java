package com.literandltx.timer_backend.dto.user.auth;

import com.literandltx.timer_backend.validation.FieldMatch;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "Password and repeat password shouldn't be empty and should be equal"
)
public class ResetPasswordRequestDto {
    @Size(min = 8)
    private String password;

    @Size(min = 8)
    private String repeatPassword;
}
