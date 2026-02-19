package com.smartearthen.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buildings")
@Data
@NoArgsConstructor
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String code; // ex: "BAT-A"

    @Enumerated(EnumType.STRING)
    private EarthenMaterial material; // PISE, ADOBE, BTC, COB, TORCHIS, BAUGE

    private String location;
    private Double latitude;
    private Double longitude;

    private Integer wallThicknessCm;
    private Integer constructionYear;
    private Double floorAreaM2;
    private Integer floorCount;

    private String description;

    @Column(name = "is_active")
    private boolean active = true;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Sensor> sensors = new ArrayList<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Alert> alerts = new ArrayList<>();
}
