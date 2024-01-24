package org.mobios.repository;

import org.mobios.dao.NicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NicFileRepository extends JpaRepository<NicEntity,Integer> {
    List<NicEntity> findById(int id);

    List<NicEntity> findByFileName(String name);

    @Query("SELECT DISTINCT fileName FROM NicEntity")
    List<String> findDistinctFileName();


}
