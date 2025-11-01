package com.wikiproject_springsuffering.control;

import com.wikiproject_springsuffering.model.Warticle;
import com.wikiproject_springsuffering.repository.CategoryRepository;
import com.wikiproject_springsuffering.repository.TagRepository;
import com.wikiproject_springsuffering.repository.WarticleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Load WarticleCUD into WebMvcTest for testing
@WebMvcTest(controllers = WarticleCUD.class)
public class WarticleCUDTest {

    // MockMvc to send HTTP requests through the controller without hitting the actual DB
    @Autowired
    private MockMvc mockMvc;

    // @WebMvcTest doesn't create repository beans, so here we have to
    @TestConfiguration
    static class TestConfig {
        @Bean
        public WarticleRepository warticleRepo() {
            // mock repository so tests can CRUD
            return Mockito.mock(WarticleRepository.class);
        }

        @Bean
        public CategoryRepository categoryRepo() {
            //We use three tables to handle articles, even though
            //I'm not actually handling category and tag info
            //we need them to establish the full warticlerepository table
            return Mockito.mock(CategoryRepository.class);
        }

        @Bean
        public TagRepository tagRepo() {
            //There's the third
            return Mockito.mock(TagRepository.class);
        }
    }

    // Establish dependancy to the mock repo to use in the test.
    // This is TestConfig.warticleRepo() and not
    // wikiproject_springsuffering.repository.WarticleRepository.
    @Autowired
    private WarticleRepository warticleRepo;

    @Test
    public void delete_withAdmin_deletesAndRedirects() throws Exception {

    //create a warticle
        Warticle article = new Warticle();
        article.setId(1);
        Mockito.when(warticleRepo.findById(1)).thenReturn(Optional.of(article));

    //delete the warticle
    mockMvc.perform(post("/wiki/articles/1/delete").sessionAttr("loggedInAdmin", "admin"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/wiki/articles"));

    // we should get an an occurence
    verify(warticleRepo, times(1)).delete(article);
    }

    @Test
    public void delete_withoutAdmin_redirectsToArticleAndDoesNotDelete() throws Exception {
        //create a warticle
        Warticle article = new Warticle();
        article.setId(2);
        Mockito.when(warticleRepo.findById(2)).thenReturn(Optional.of(article));

    //attempt deletion without admin
    mockMvc.perform(post("/wiki/articles/2/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/wiki/articles/2"));

    //number of succcessful occurences should be 0
    verify(warticleRepo, times(0)).delete(any(Warticle.class));
    }

}
