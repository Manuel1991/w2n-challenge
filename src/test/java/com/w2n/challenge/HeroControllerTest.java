package com.w2n.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.w2n.challenge.domain.dtos.NewHeroDTO;
import com.w2n.challenge.exceptions.ExceptionMessages;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = W2nChallengeApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.test.properties")
public class HeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String hulk_ID = "29c9d9ed-2742-47b2-b29c-6f5f5c65d5d5";

    @Test
    public void getAllHeroes() throws Exception {
        mockMvc.perform(get("/heroes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andDo(print());
    }

    @Test
    public void getHeroById() throws Exception {

        mockMvc.perform(get(String.format("/heroes/%s", hulk_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").value("Hulk"))
                .andDo(print());
    }

    @Test
    public void getHeroesByName() throws Exception {

        //enter lower case
        String filterName = "aqua";

        String stringResponse = mockMvc.perform(
                        get(String.format("/heroes?name=%s", filterName))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> namesHeroes = new ArrayList<>();

        ((ArrayNode) new ObjectMapper()
                .readTree(stringResponse)
                .get("content"))
                .forEach(jh -> namesHeroes.add(jh.get("name").asText("")));

        if (!namesHeroes
                .stream()
                .map(String::toLowerCase)
                .allMatch(n -> n.contains(filterName))) {
            throw new RuntimeException("failed getHeroesByName");
        }
    }

    @Test
    public void getHeroByIdAndNotFound() throws Exception {
        mockMvc.perform(get(String.format("/heroes/%s", UUID.randomUUID())))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_NOT_FOUND));
    }

    @Test
    public void createHeroAndUpdateAndDelete() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        NewHeroDTO heroDTO = NewHeroDTO
                .builder()
                .name(String.format("Test Hero %s", UUID.randomUUID()))
                .universe(String.format("Universe %s", UUID.randomUUID()))
                .firstApparition(1979)
                .build();

        //CREATE
        String stringCreateResponse = mockMvc.perform(post("/heroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(heroDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.name").value(heroDTO.getName()))
                .andExpect(jsonPath("$.universe").value(heroDTO.getUniverse()))
                .andExpect(jsonPath("$.firstApparition").value(heroDTO.getFirstApparition()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = mapper.readTree(stringCreateResponse).get("id").asText();

        heroDTO = NewHeroDTO
                .builder()
                .name(String.format("Test Hero %s", UUID.randomUUID()))
                .universe(String.format("Universe %s", UUID.randomUUID()))
                .firstApparition(LocalDate.now().getYear())
                .build();

        //UPDATE
        mockMvc.perform(put(String.format("/heroes/%s", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(heroDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(heroDTO.getName()))
                .andExpect(jsonPath("$.universe").value(heroDTO.getUniverse()))
                .andExpect(jsonPath("$.firstApparition").value(heroDTO.getFirstApparition()));

        //DELETE
        mockMvc.perform(delete(String.format("/heroes/%s", id)))
                .andExpect(status().isNoContent());

        //GET AND VERIFY THAT HERO DOESN'T EXIST
        mockMvc.perform(get(String.format("/heroes/%s", id)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_NOT_FOUND));
    }

    @Test
    public void updateHeroWithNameAlreadyExists() throws Exception {

        NewHeroDTO heroDTO = NewHeroDTO
                .builder()
                .name("Superman")
                .universe("DC")
                .firstApparition(1939)
                .build();

        mockMvc.perform(put(String.format("/heroes/%s", hulk_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(heroDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_ALREADY_EXISTS));

    }
}
