package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class IndikatorResDTO {
    private String indikator;
    private Integer tahun;
    private List<TargetItem> target;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TargetItem {
        private Integer nilai;
        private String satuan;
        private String keterangan;
    }
}
