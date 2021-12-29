package com.example.boondb.repository;

import com.example.boondb.model.Boon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoonRepository extends JpaRepository<Boon,Integer> {
    List<Boon> findBoonByNaamContaining(String naam);
    List<Boon> findBoonByLandContaining(String land);
    Boon findBoonById(int id);
    Boon findBoonByUID(String UID);


}
