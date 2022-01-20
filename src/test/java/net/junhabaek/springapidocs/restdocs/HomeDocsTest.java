package net.junhabaek.springapidocs.restdocs;

import net.junhabaek.springapidocs.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(controllers = {HomeController.class})
public class HomeDocsTest extends BaseRestDocsTest{

    @Test
    void home() throws Exception {
        //given
        HomeController.HomeMessage homeMessage = HomeController.HomeMessage.createHomeMessage("home");

        //when
        ResultActions actions = this.mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(homeMessage)));

        //docs
        actions.andDo(document("{method-name}",
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("welcome message")
                )));
    }
}
