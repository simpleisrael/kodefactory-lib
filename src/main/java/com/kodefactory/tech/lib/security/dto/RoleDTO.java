package com.kodefactory.tech.lib.security.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private List<AuthorityDTO> authorities;
}