package com.kertaskerja.id.KertasKerjaRevamp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JenisPohon {

    TEMATIK(0),
    SUB_TEMATIK(1),
    SUB_SUB_TEMATIK(2),
    SUPER_SUB_TEMATIK(3),
    STRATEGIC_PEMDA(4),
    TACTICAL_PEMDA(5),
    OPERATIONAL_PEMDA(6);

    private final int level;
}
