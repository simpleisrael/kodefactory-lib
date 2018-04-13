package com.kodefactory.tech.lib.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponseDTO {
    private String filename;
    private List<String> filenames;
}
