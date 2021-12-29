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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        Boon boon = new Boon("Arabica","Rusland", "1");
        Boon boon1 = new Boon("Robusta","Noord-Korea","2");
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

        Boon boon = new Boon("Arabica", "Rusland", "1");
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

        Boon boon = new Boon("Arabica", "Rusland","1");
        List<Boon> boonList = new ArrayList<>();
        boonList.add(boon);

        given(boonRepository.findBoonByNaamContaining("Arabica")).willReturn(boonList);

        mockMvc.perform(get("/boons/naam/{naam}","Arabica"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].land", is("Rusland")));
    }

    @Test
    public void whenPostBoon_thenReturnJsonBoon() throws Exception {
        Boon boon1 = new Boon("Arabica","België","7");

        mockMvc.perform(post("/boons")
                .content(mapper.writeValueAsString(boon1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam",is("Arabica")))
                .andExpect(jsonPath("$.land",is("België")));
    }

    @Test
    public void givenBoon_whenPutBoon_thenReturnJsonBoon() throws Exception {
        Boon boon1 = new Boon("AR","BC","9");

        given(boonRepository.findBoonByUID("9")).willReturn(boon1);

        Boon updatedBoon = new Boon("AB","CD","9");

        mockMvc.perform(put("/boons")
                .content(mapper.writeValueAsString(updatedBoon))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam",is("AB")));
    }

    @Test
    public void givenBoon_whenDeleteBoon_thenStatusOk() throws Exception {
        Boon boonToBeDeleted = new Boon("AB","CD","10");

        given(boonRepository.findBoonByUID("10")).willReturn(boonToBeDeleted);

        mockMvc.perform(delete("/boons/{UID}","10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoBoon_whenDeleteBoon_thenStatusNotFound() throws Exception{
        given(boonRepository.findBoonByUID("10")).willReturn(null);

        mockMvc.perform(delete("/boons/{UID}","10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
