package com.kodefactory.tech.lib.security.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AssignAuthoritiesDTO {
    private Long roleId;
    private Set<Long> authorityIds;
}
