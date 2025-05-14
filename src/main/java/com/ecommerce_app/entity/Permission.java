package com.ecommerce_app.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "permissions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {

    @Column(nullable = false, unique = true)
    String name;

    String description;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles = new HashSet<>();

}
