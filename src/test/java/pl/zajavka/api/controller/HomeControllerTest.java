package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.zajavka.api.controller.HomeController.HOME;

@ActiveProfiles("test")
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class HomeControllerTest {

    private MockMvc mockMvc;

    @Test
    void homeWorksCorrectly() throws Exception {
//        given/when/then
        mockMvc.perform(MockMvcRequestBuilders.get(HOME))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));

    }
}