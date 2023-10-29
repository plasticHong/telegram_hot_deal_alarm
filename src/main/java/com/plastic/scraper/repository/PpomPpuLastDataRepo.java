package com.plastic.scraper.repository;

import com.plastic.scraper.entity.PpomPpuLastData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PpomPpuLastDataRepo extends JpaRepository<PpomPpuLastData,Long> {
}
