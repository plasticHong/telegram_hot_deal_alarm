package com.plastic.scraper.repository;

import com.plastic.scraper.entity.HotDealRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotDealRecordRepo extends JpaRepository<HotDealRecord,Long> {

    Optional<HotDealRecord> findByUrl(String url);

}
