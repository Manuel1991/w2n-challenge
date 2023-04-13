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

    @Test
    public void getAllHeroes() throws Exception {
        mockMvc.perform(get("/heroes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void getHeroByIdAndNotFound() throws Exception {
        _getHeroByIdAndNotFound(UUID.randomUUID().toString());
    }

    @Test
    public void createAndGetById() throws Exception {

        NewHeroDTO heroDTO = getRandomNewHero();

        String id = _create(heroDTO);

        _getHeroesById(id, heroDTO);
        _delete(id);
    }

    @Test
    public void createAndGetByName() throws Exception {

        NewHeroDTO heroDTO = getRandomNewHero();

        String id = _create(heroDTO);

        _getHeroesByName(heroDTO.getName());
        _delete(id);
    }

    @Test
    public void createAndUpdateAndDelete() throws Exception {

        //CREATE
        NewHeroDTO heroDTO = getRandomNewHero();

        String id = _create(heroDTO);

        //UPDATE
        heroDTO = getRandomNewHero();

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
        _deleteAndVerifyNotFound(id);
    }

    @Test
    public void createWithNameAlreadyExists() throws Exception {

        NewHeroDTO heroA = getRandomNewHero();

        NewHeroDTO heroB = NewHeroDTO.builder()
                .name(heroA.getName())
                .universe(heroA.getUniverse())
                .build();

        //CREATE heroA and get id
        String id_heroA = _create(heroA);

        //TRY TO CREATE heroB
        mockMvc.perform(post("/heroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(heroB)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_ALREADY_EXISTS));

        //DELETE heroA
        _deleteAndVerifyNotFound(id_heroA);
    }

    @Test
    public void updateWithNameAlreadyExists() throws Exception {

        NewHeroDTO heroA = getRandomNewHero(),
                heroB = getRandomNewHero();

        String id_heroA = _create(heroA);
        String id_heroB = _create(heroB);

        heroB.setName(heroA.getName());

        mockMvc.perform(put(String.format("/heroes/%s", id_heroB))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(heroB)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_ALREADY_EXISTS));

        //DELETE heroA and heroB
        _deleteAndVerifyNotFound(id_heroA);
        _deleteAndVerifyNotFound(id_heroB);
    }

    private NewHeroDTO getRandomNewHero() {
        return NewHeroDTO
                .builder()
                .name(String.format("Test Hero %s", UUID.randomUUID()))
                .universe(String.format("Universe %s", UUID.randomUUID()))
                .firstApparition(LocalDate.now().getYear())
                .build();
    }

    private void _getHeroesById(String id, NewHeroDTO heroDTO) throws Exception {
        mockMvc.perform(get(String.format("/heroes/%s", id)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value(heroDTO.getName()))
                .andExpect(jsonPath("$.universe").value(heroDTO.getUniverse()))
                .andExpect(jsonPath("$.firstApparition").value(heroDTO.getFirstApparition()));
    }

    private void _getHeroByIdAndNotFound(String id) throws Exception {
        mockMvc.perform(get(String.format("/heroes/%s", id)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_NOT_FOUND));
    }

    private void _getHeroesByName(String filterName) throws Exception {

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
                .allMatch(n -> n.contains(filterName.toLowerCase()))) {
            throw new RuntimeException("failed get_heroes_by_name");
        }
    }

    private String _create(NewHeroDTO heroDTO) throws Exception {

        String stringResponse = mockMvc.perform(post("/heroes")
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

        return mapper.readTree(stringResponse).get("id").asText();
    }

    private void _delete(String id) throws Exception {
        mockMvc.perform(delete(String.format("/heroes/%s", id))).andExpect(status().isNoContent());
    }

    private void _deleteAndVerifyNotFound(String id) throws Exception {
        //DELETE
        _delete(id);
        //GET AND VERIFY THAT HERO DOESN'T EXIST
        _getHeroByIdAndNotFound(id);
    }
}
