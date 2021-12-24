package com.example.boondb.model;

import javax.persistence.*;

@Entity
public class Boon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String naam;
    private String land;


    public Boon(String naam, String land) {
        this.naam = naam;
        this.land = land;
    }

    public Boon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
}
