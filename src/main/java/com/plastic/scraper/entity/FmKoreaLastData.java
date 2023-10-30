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
@Table(name = "fm_korea_last_data", schema = "sub-recipe.scrapper")
@NoArgsConstructor
public class FmKoreaLastData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "article_id")
    private String articleId;
    @Column(name = "title")
    private String title;
}
