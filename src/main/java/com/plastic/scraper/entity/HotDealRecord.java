package com.plastic.scraper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hot_deal_record", schema = "sub-recipe.scrapper")
@NoArgsConstructor
public class HotDealRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    public HotDealRecord(String url) {
        this.url = url;
    }
}
