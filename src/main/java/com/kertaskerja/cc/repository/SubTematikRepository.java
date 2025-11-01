package com.kertaskerja.cc.repository;

import com.kertaskerja.cc.entity.SubTematik;

import java.util.Optional;

// Deprecated: JPA repository replaced by JDBC service implementation
public interface SubTematikRepository {
    Optional<SubTematik> findByIdSubTematik(String idSubTematik);
}
