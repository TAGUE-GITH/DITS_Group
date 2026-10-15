package com.dits.dits_group.common.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoredFileRepository extends JpaRepository<StoredFile, String> {

    @Query("select f.path from StoredFile f")
    List<String> findAllPaths();
}
