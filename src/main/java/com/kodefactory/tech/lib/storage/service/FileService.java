package com.kodefactory.tech.lib.storage.service;

import com.kodefactory.tech.lib.storage.domain.FileEO;
import com.kodefactory.tech.lib.storage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by israel on 8/23/17.
 */
@Service
public class FileService {

    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    
    public FileEO uploadFile(String name) {
        FileEO file = new FileEO();
        file.setCreateDate(new Date());
        file.setLastModifyDate(new Date());
        file.setUrl(name);
        file.setIsTemp(false);
        String[] split = name.split("\\.");
        if(split.length>0)file.setType(split[split.length-1]);
        file = fileRepository.save(file);
        return file;
    }

    
    public Page<FileEO> listFiles(int page) {
        int start = page - 1;
        int limit = 100;
        Pageable selection = new PageRequest(start, limit, Sort.Direction.DESC, "id");
        return fileRepository.findAll(selection);
    }

    public FileEO loadFileEO(String url) {
        return fileRepository.findByUrl(url);
    }

    public boolean deleteFileEO(String url) {
        FileEO file = fileRepository.findByUrl(url);
        if(file != null) {
            fileRepository.delete(file);
            return true;
        }
        return false;
    }


}
