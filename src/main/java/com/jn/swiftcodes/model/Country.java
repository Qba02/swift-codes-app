package com.jn.swiftcodes.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, length = 2, unique = true)
    private String iso2;

    @Column(nullable = false, unique = true)
    private String name;

    @PrePersist
    @PreUpdate
    private void convertFieldsToUpperCase() {
        this.name = this.name != null ? this.name.toUpperCase() : null;
        this.iso2 = this.iso2 != null ? this.iso2.toUpperCase() : null;
    }

}
