package org.example.kurki.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.validation.constraints.Size;


@Entity
@Data
@Table(name = "chicken_race")
public class ChickenRace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "race")
    private String race;

    @Size(max = 255)
    @Column(name = "description")
    private String description;
}
