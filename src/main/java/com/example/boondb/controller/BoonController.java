package com.example.boondb.controller;

import com.example.boondb.model.Boon;
import com.example.boondb.repository.BoonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class BoonController {
    @Autowired
    private BoonRepository boonRepository;

    @GetMapping("/boons/land")
    public List<Boon> getBoonsByLand() {
        return boonRepository.findBoonByLandContaining("a");
    }

    @GetMapping("/boons/{naam}")
    public Boon getBoonByNaam(@PathVariable String naam){
        return boonRepository.findBoonByNaam(naam);
    }

    @PostConstruct
    public void fillDB() {
        if(boonRepository.count() == 0){
            boonRepository.save(new Boon("Arabica","Ja ma Yeet"));
            boonRepository.save(new Boon("Arabica2","Ja ma Yeet2"));
        }

        System.out.println(boonRepository.findBoonByNaam("Arabica").getNaam());
    }
}
