package com.kodefactory.tech.lib.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RealTimeResponseDTO {
    private String type;
    private Object payload;
}
