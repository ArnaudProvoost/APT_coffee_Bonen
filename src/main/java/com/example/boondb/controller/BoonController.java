package com.example.boondb.controller;

import com.example.boondb.model.Boon;
import com.example.boondb.repository.BoonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class BoonController {
    @Autowired
    private BoonRepository boonRepository;

    @GetMapping("/")
    public List<Boon> redirect() {
        return getBoons();
    }

    @GetMapping("/boons")
    public List<Boon> getBoons() {
        return boonRepository.findAll();
    }

    @GetMapping("/boons/land/{land}")
    public List<Boon> getBoonsByLand(@PathVariable String land) {
        return boonRepository.findBoonByLandContaining(land);
    }

    @GetMapping("/boons/naam/{naam}")
    public List<Boon> getBoonByNaamContaining(@PathVariable String naam){
        return boonRepository.findBoonByNaamContaining(naam);
    }

    @PostMapping("/boons")
    public Boon addBoon(@RequestBody Boon boon){
        boonRepository.save(boon);
        return boon;
    }

    @PutMapping("/boons")
    public Boon updateBoon(@RequestBody Boon updatedBoon) {

        Boon retrievedBoon = boonRepository.findBoonByUID(updatedBoon.getUID());

        retrievedBoon.setLand(updatedBoon.getLand());
        retrievedBoon.setNaam(updatedBoon.getNaam());

        boonRepository.save(retrievedBoon);

        return retrievedBoon;
    }

    @DeleteMapping("/boons/{UID}")
    public ResponseEntity deleteBoon(@PathVariable String UID) {
        Boon boon = boonRepository.findBoonByUID(UID);

        if (boon != null){
            boonRepository.delete(boon);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostConstruct
    public void fillDB() {
        if(boonRepository.count() == 0){
            boonRepository.save(new Boon("Coffea Arabica","Brazilië","1"));
            boonRepository.save(new Boon("Coffea Robusta","Java","2"));
            boonRepository.save(new Boon("Coffea Liberica","Uganda","3"));
        }

        System.out.println(boonRepository.findBoonByNaamContaining("Arabica"));
    }
}
