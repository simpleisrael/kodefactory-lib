package com.kodefactory.tech.lib.storage.repository;

import com.kodefactory.tech.lib.storage.domain.FileEO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEO, Long> {
    FileEO findByUrl(String url);
}
