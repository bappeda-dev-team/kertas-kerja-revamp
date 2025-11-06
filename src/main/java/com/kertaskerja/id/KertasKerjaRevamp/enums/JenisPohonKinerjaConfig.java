package com.kertaskerja.id.KertasKerjaRevamp.enums;

import com.kertaskerja.id.KertasKerjaRevamp.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum JenisPohonKinerjaConfig {
    TEMATIK("TMK", 1, "sub-tematik"),
    SUB_TEMATIK("STMK", 2, "sub-sub-tematik"),
    SUB_SUB_TEMATIK("SSTMK", 3, "strategic"),
    STRATEGIC("STR", 4, "tactical"),
    TACTICAL("TAC", 5, "operational"),
    OPERATIONAL("OPS", 6, "");

    private final String prefix;
    private final Integer levelPokin;
    private final String jenisChild;

    public static JenisPohonKinerjaConfig from(String jenis) {
        return Arrays.stream(values())
              .filter(j -> j.name().equalsIgnoreCase(jenis.replace("-", "_")))
              .findFirst()
              .orElseThrow(() -> new BadRequestException("Jenis Pokin tidak valid: " + jenis));
    }
}
