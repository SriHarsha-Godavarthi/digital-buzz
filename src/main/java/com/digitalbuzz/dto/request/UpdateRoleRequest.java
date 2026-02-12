package com.digitalbuzz.dto.request;

import com.digitalbuzz.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {
    
    @NotNull(message = "Role is required")
    private Role role;
}
