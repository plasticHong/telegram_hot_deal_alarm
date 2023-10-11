package com.plastic.scraper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ruliweb_last_data", schema = "sub-recipe.scrapper")
@NoArgsConstructor
public class RuliwebLastData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;


    public RuliwebLastData(String title) {
        this.title = title;
    }
}
