package com.kertaskerja.id.KertasKerjaRevamp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Target {

    @Id
    private Long id;

    private Long indikatorId;
    private Double nilai;
    private String satuan;
    private Integer tahun;
}