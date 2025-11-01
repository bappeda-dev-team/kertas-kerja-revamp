package cc.kertaskerja.pengajuan_kta.dto.Auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {
    @NotBlank
    private String nama;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String nomor_telepon;
    @NotBlank
    private String tipe_akun;

    // Captcha and OTP
    @NotBlank
    private String captcha_token;
    @NotBlank
    private String captcha_code;
    @NotBlank
    private String otp_code;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SendOtp {
        @NotBlank
        private String nama;
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String username;
        @NotBlank
        private String nomor_telepon;
    }
}
