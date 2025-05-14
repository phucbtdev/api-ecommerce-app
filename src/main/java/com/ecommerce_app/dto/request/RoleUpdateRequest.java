package com.ecommerce_app.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUpdateRequest {
    private String name;
    private String description;
    private Set<UUID> permissionIds;
}
