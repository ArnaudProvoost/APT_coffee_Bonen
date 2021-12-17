package com.example.boondb;

import com.example.boondb.model.Boon;
import com.example.boondb.repository.BoonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoonControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoonRepository boonRepository;

    private Boon boonNaam1land1 = new Boon("1","1");
    private Boon boonNaam1land2 = new Boon("1","2");
    private Boon boonNaam2land1 = new Boon("2","1");
    private Boon boonToBeDeleted = new Boon("999","1");

    @BeforeEach
    public void beforeAllTests() {
        if(boonRepository.count() != 0) {
            boonRepository.deleteAll();
        }
        boonRepository.save(boonNaam1land1);
        boonRepository.save(boonNaam1land2);
        boonRepository.save(boonNaam2land1);
        boonRepository.save(boonToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {
        boonRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getallBoons_thenReturnJsonBoons() throws Exception {
        mockMvc.perform(get("/boons"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].naam",is("1")))
                .andExpect(jsonPath("$[1].naam",is("1")))
                .andExpect(jsonPath("$[2].naam",is("2")))
                .andExpect(jsonPath("$[3].naam",is("999")));
    }

    @Test
    public void givenBoon_whengetBoonsByLand_thenReturnJsonBoon() throws Exception {
        mockMvc.perform(get("/boons/land/{land}","1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].naam",is("1")))
                .andExpect(jsonPath("$[1].naam",is("2")))
                .andExpect(jsonPath("$[2].naam",is("999")));
    }
}
