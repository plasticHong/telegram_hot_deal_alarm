package com.plastic.scraper.repository;

import com.plastic.scraper.entity.RuliwebLastData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuliwebLastDataRepo extends JpaRepository<RuliwebLastData,Long> {
}
