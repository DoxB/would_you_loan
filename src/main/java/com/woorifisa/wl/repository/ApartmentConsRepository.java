package com.woorifisa.wl.repository;

import com.woorifisa.wl.model.dto.ApartmentConsDto;
import com.woorifisa.wl.model.entity.ApartmentCons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApartmentConsRepository extends JpaRepository<ApartmentCons, Long> {
    List<ApartmentCons> findByKaptCode(String kaptCode);

    Optional<ApartmentCons> findByUserIdAndKaptCode(Long userId, String kaptCode);

    @Query("SELECT new com.woorifisa.wl.model.dto.ApartmentConsDto(" +
            "a.kaptCode, " +
            "COUNT(CASE WHEN a.noise = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.transport = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.parking = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.security = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.outdated = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.school = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.community = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.commercial = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.daycare = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.medical = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.restaurants = true THEN 1 ELSE NULL END), " +
            "COUNT(CASE WHEN a.parks = true THEN 1 ELSE NULL END)) " +
            "FROM ApartmentCons a " +
            "WHERE a.kaptCode = :kaptCode " +
            "GROUP BY a.kaptCode")
    ApartmentConsDto getConsCounts(@Param("kaptCode") String kaptCode);
}