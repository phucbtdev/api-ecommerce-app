package com.ecommerce_app.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreationRequest {
    private String name;
    private String description;
    @Builder.Default
    private Set<Long> permissionIds = new HashSet<>();
}
