package com.plastic.scraper.repository;

import com.plastic.scraper.entity.FmKoreaLastData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FmKoreaLastDataRepo extends JpaRepository<FmKoreaLastData,Long> {
}
