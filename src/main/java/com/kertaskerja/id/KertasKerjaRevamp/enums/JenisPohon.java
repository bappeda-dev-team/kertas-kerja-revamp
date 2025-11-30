package com.kertaskerja.id.KertasKerjaRevamp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JenisPohon {
    TEMATIK(0),
    SUB_TEMATIK(1),
    SUB_SUB_TEMATIK(2),
    STRATEGIC(3),
    TACTICAL(4),
    OPERATIONAL(5);

    private final int level;

    public static JenisPohon fromLevel(int level) {
        for (JenisPohon j : values()) {
            if (j.level == level) {
                return j;
            }
        }
        throw new IllegalArgumentException("Level tidak valid: " + level);
    }
}