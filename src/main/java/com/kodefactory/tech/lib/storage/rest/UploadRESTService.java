package com.kodefactory.tech.lib.storage.rest;

import com.kodefactory.tech.lib.core.rest.BaseREST;
import com.kodefactory.tech.lib.core.service.BaseServiceImpl;
import com.kodefactory.tech.lib.exception.RestException;
import com.kodefactory.tech.lib.storage.domain.FileEO;
import com.kodefactory.tech.lib.storage.dto.DeleteFileRequest;
import com.kodefactory.tech.lib.storage.dto.UploadResponseDTO;
import com.kodefactory.tech.lib.storage.service.FileService;
import com.kodefactory.tech.lib.storage.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("upload-service")
public class UploadRESTService extends BaseServiceImpl {

    private Logger logger = LoggerFactory.getLogger(UploadRESTService.class);

    private StorageService storageService;
    private FileService fileService;

    public UploadRESTService(StorageService storageService, FileService fileService){
        this.storageService = storageService;
        this.fileService = fileService;
    }


    @PostMapping("/upload")
    public ResponseEntity<Object> handleFileUpload(@RequestParam("image") MultipartFile file) throws RestException {
        String message = "";
        try {
            String filename = storageService.store(file);
            fileService.uploadFile(filename);
            return ResponseEntity.status(HttpStatus.OK).body(new UploadResponseDTO(filename, null));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throwException(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }


    @PostMapping("/upload-multiple")
    public ResponseEntity<Object> handleMultipleFileUpload(@RequestParam("image") MultipartFile[] files) throws RestException {
        System.out.println("File Size: "+files.length);
        String message = "";
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                String filename = storageService.store(file);
                fileService.uploadFile(filename);
                fileNames.add(filename);
            });
            return ResponseEntity.status(HttpStatus.OK).body(new UploadResponseDTO(null, fileNames));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throwException(e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/list-files/{page}")
    public ResponseEntity<List<String>> getListFiles(@PathVariable int page) {
        if(page == 0) page = 1;
        Page<FileEO> files = fileService.listFiles(page);
        List<String> fileNames = files.getContent().stream().map(FileEO::getUrl)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(fileNames);
    }

    @PostMapping("/delete-file")
    public ResponseEntity<Boolean> getListFiles(@RequestBody DeleteFileRequest request) {
        boolean deleted = fileService.deleteFileEO(request.getUrl());
        if(deleted) {
            deleted = storageService.deleteFile(request.getUrl());
        }

        return ResponseEntity.ok().body(deleted);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Object> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .body(file);
    }

    @GetMapping("/download-files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

}