package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class IndikatorReqDTO {

    @Data
    public static class IndikatorTematik {
        @NotBlank(message = "id tematik tidak boleh kosong!")
        private String id_tematik;

        @NotBlank(message = "Harap masukkan nama indikator")
        private String indikator;

        @NotNull(message = "Harap masukkan tahun")
        private Integer tahun;

        @Valid
        @NotNull(message = "Kolom target tidak boleh kosong!")
        private List<TargetItem> target;
    }

    @Data
    public static class IndikatorSubTematik {
        @NotBlank(message = "id sub tematik tidak boleh kosong!")
        private String id_sub_tematik;

        @NotBlank(message = "Harap masukkan nama indikator")
        private String indikator;

        @NotNull(message = "Harap masukkan tahun")
        private Integer tahun;

        @Valid
        @NotNull(message = "Kolom target tidak boleh kosong!")
        private List<TargetItem> target;
    }

    @Data
    public static class IndikatorSubSubTematik {
        @NotBlank(message = "id sub sub tematik tidak boleh kosong!")
        private String id_sub_sub_tematik;

        @NotBlank(message = "Harap masukkan nama indikator")
        private String indikator;

        @NotNull(message = "Harap masukkan tahun")
        private Integer tahun;

        @Valid
        @NotNull(message = "Kolom target tidak boleh kosong!")
        private List<TargetItem> target;
    }

    @Data
    public static class IndikatorStrategic {
        @NotBlank(message = "id strategic tidak boleh kosong!")
        private String id_strategic;

        @NotBlank(message = "Harap masukkan nama indikator")
        private String indikator;

        @NotNull(message = "Harap masukkan tahun")
        private Integer tahun;

        @Valid
        @NotNull(message = "Kolom target tidak boleh kosong!")
        private List<TargetItem> target;
    }

    @Data
    public static class IndikatorTactical {
        @NotBlank(message = "id tactical tidak boleh kosong!")
        private String id_tactical;

        @NotBlank(message = "Harap masukkan nama indikator")
        private String indikator;

        @NotNull(message = "Harap masukkan tahun")
        private Integer tahun;

        @Valid
        @NotNull(message = "Kolom target tidak boleh kosong!")
        private List<TargetItem> target;
    }

    @Data
    public static class IndikatorOperational {
        @NotBlank(message = "id operational tidak boleh kosong!")
        private String id_operational;

        @NotBlank(message = "Harap masukkan nama indikator")
        private String indikator;

        @NotNull(message = "Harap masukkan tahun")
        private Integer tahun;

        @Valid
        @NotNull(message = "Kolom target tidak boleh kosong!")
        private List<TargetItem> target;
    }

    @Data
    public static class TargetItem {
        private Integer nilai;
        private String satuan;
        private String keterangan;
    }
}
