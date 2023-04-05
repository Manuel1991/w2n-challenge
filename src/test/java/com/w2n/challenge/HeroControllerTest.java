package com.w2n.challenge;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void getAllHeroes() throws Exception {
        mockMvc.perform(get("/heroes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andDo(print());
    }

    @Test
    public void getHeroById() throws Exception {
        mockMvc.perform(get("/heroes/29c9d9ed-2742-47b2-b29c-6f5f5c65d5d5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").isString())
                .andDo(print());
    }

    @Test
    public void getHeroByIdAndNotFound() throws Exception {
        mockMvc.perform(get("/heroes/1ee0aa91-ee67-481c-8140-85d73df0a145").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.HERO_NOT_FOUND))
                .andDo(print());
    }
}
