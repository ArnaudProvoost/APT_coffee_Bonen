package com.example.boondb;

import com.example.boondb.model.Boon;
import com.example.boondb.repository.BoonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BoonControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoonRepository boonRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getallBoons_thenReturnJsonBoons() throws Exception {

        Boon boon = new Boon("Arabica","Rusland");
        Boon boon1 = new Boon("Robusta","Noord-Korea");
        List<Boon> boonList = new ArrayList<>();
        boonList.add(boon);
        boonList.add(boon1);

        given(boonRepository.findAll()).willReturn(boonList);

        mockMvc.perform(get("/boons"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].naam",is("Arabica")))
                .andExpect(jsonPath("$[1].naam",is("Robusta")));
    }

    @Test
    public void getBoonByLand_thenReturnJsonBoons() throws Exception {

        Boon boon = new Boon("Arabica", "Rusland");
        List<Boon> boonList = new ArrayList<>();
        boonList.add(boon);

        given(boonRepository.findBoonByLandContaining("Rusland")).willReturn(boonList);

        mockMvc.perform(get("/boons/land/{land}","Rusland"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].naam", is("Arabica")));
    }

    @Test
    public void getBoonByNaam_thenReturnJsonBoons() throws Exception {

        Boon boon = new Boon("Arabica", "Rusland");
        List<Boon> boonList = new ArrayList<>();
        boonList.add(boon);

        given(boonRepository.findBoonByNaamContaining("Arabica")).willReturn(boonList);

        mockMvc.perform(get("/boons/naam/{naam}","Arabica"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].land", is("Rusland")));
    }
}
