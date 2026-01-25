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
    STRATEGIC_OPD(4),
    TACTICAL_PEMDA(5),
    TACTICAL_OPD(5),
    OPERATIONAL_PEMDA(6),
    OPERATIONAL_OPD(6),
    OPERATIONAL_N(7);

    private final int level;
}
