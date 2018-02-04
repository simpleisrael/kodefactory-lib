package com.kodefactory.tech.lib.storage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("uploads");

    @PostConstruct
    public void initialize() {
        if(!Files.exists(Paths.get("uploads"))) {
            init();
        }
    }

    public String store(MultipartFile file) {
        String filename = generateFileName(file);
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            log.error("Unable to load file with name {}", filename);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public boolean deleteFile(String filename) {
        try {
            Files.delete(Paths.get("uploads/"+filename));
            return true;
        } catch (IOException e) {
            throw new RuntimeException(("Unable to delete " + filename + "at the moment"));
        }
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }


    public String generateFileName(MultipartFile file) {
        String prefix = new java.util.Date().getTime()+"-";
        return prefix + file.getOriginalFilename();
    }
}