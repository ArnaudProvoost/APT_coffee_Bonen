package com.example.boondb;

import com.example.boondb.model.Boon;
import com.example.boondb.repository.BoonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoonControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoonRepository boonRepository;

    private Boon boonNaam1land1 = new Boon("a","1", "1");
    private Boon boonNaam1land2 = new Boon("b","2", "2");
    private Boon boonNaam2land1 = new Boon("c","1", "3");
    private Boon boonTobeEdited = new Boon ("eee","3", "4");
    private Boon boonToBeDeleted = new Boon("ddd","1", "5");

    @BeforeEach
    public void beforeAllTests() {
        if(boonRepository.count() != 0) {
            boonRepository.deleteAll();
        }
        boonRepository.save(boonNaam1land1);
        boonRepository.save(boonNaam1land2);
        boonRepository.save(boonNaam2land1);
        boonRepository.save(boonTobeEdited);
        boonRepository.save(boonToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getallBoons_thenReturnJsonBoons() throws Exception {
        mockMvc.perform(get("/boons"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].naam",is("a")))
                .andExpect(jsonPath("$[1].naam",is("b")))
                .andExpect(jsonPath("$[2].naam",is("c")))
                .andExpect(jsonPath("$[4].naam",is("ddd")));
    }

    @Test
    public void givenBoon_whengetBoonsByLand_thenReturnJsonBoon() throws Exception {
        mockMvc.perform(get("/boons/land/{land}","1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].naam",is("a")))
                .andExpect(jsonPath("$[1].naam",is("c")))
                .andExpect(jsonPath("$[2].naam",is("ddd")));
    }

    @Test
    public void givenBoon_whengetBoonsByNaam_thenReturnJsonBoon() throws Exception {
        mockMvc.perform(get("/boons/naam/{naam}","d"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].land",is("1")));
    }

    @Test
    public void givenBoon_whenPostBoon_thenReturnJsonBoon() throws Exception {
        Boon boon = new Boon("Arabica", "België", "6");

        mockMvc.perform(post("/boons")
                .content(mapper.writeValueAsString(boon))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.land", is("België")))
                .andExpect(jsonPath("$.naam", is("Arabica")));
    }

    @Test
    public void givenBoon_whenPutBoon_thenReturnJsonBoon() throws Exception {

        Boon updatedBoon = new Boon("Arabica", "België", "4");

        mockMvc.perform(put("/boons")
                        .content(mapper.writeValueAsString(updatedBoon))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.land", is("België")))
                .andExpect(jsonPath("$.naam", is("Arabica")));
    }

    @Test
    public void givenBoon_whenDeleteBoon_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/boons/{UID}","5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
