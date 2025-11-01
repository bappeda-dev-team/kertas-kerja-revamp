package cc.kertaskerja.pengajuan_kta.dto.Auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountResponse {
    private Long id;
    private String nama;
    private String email;
    private String username;
    private String nomor_telepon;
    private String tipe_akun;
    private String status;
    private String role;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SendOtp {
        private String nama;
        private String email;
        private String nomor_telepon;
        private CaptchaResponse captcha;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class CaptchaResponse {
            private String key;
            private String image;
        }
    }
}
