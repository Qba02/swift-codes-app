package com.jn.swiftcodes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String swiftCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false)
    private boolean isHeadquarter;

    @ManyToOne
    @JoinColumn(name = "headquarters_id")
    private Bank headquarters;
}
