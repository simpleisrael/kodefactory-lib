package com.kodefactory.tech.lib.security.dto;

import lombok.Data;

@Data
public class AuthorityDTO {
    private Long id;
    private String name;
    private String description;
    private String groupName;
}
