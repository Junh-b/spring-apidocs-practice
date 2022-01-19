package net.junhabaek.springapidocs.restdocs;

import net.junhabaek.springapidocs.controller.StoreController;
import net.junhabaek.springapidocs.domain.aggregate.Address;
import net.junhabaek.springapidocs.domain.aggregate.Store;
import net.junhabaek.springapidocs.service.StoreService;
import net.junhabaek.springapidocs.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("restdocs")
@WebMvcTest(controllers = {StoreController.class})
@ContextConfiguration(classes = {RestDocsMockConfiguration.class})
@ComponentScan(basePackageClasses = StoreController.class)
public class StoreDocsTest extends BaseRestDocsTest {
    @Autowired
    StoreService storeService;

    FieldDescriptor[] storeDtoDescriptor = new FieldDescriptor[]{
            fieldWithPath("storeId").type(JsonFieldType.NUMBER).description("id of store"),
            fieldWithPath("storeName").type(JsonFieldType.STRING).description("name of store"),
            fieldWithPath("address.fullAddress").type(JsonFieldType.STRING).description("address of store"),
            fieldWithPath("address.postcode").type(JsonFieldType.STRING).description("postcode of store")
    };

    @Test
    void postStore() throws Exception {
        // given
        given(storeService.register(anyString(), anyString(), anyString()))
                .will(invocation -> {
                    Store store = Store.createNewStore(invocation.getArgument(0),
                            invocation.getArgument(1), invocation.getArgument(2));
                    TestUtils.setLongId(store, "id", 1L);
                    return store;
                });

        String storeName = "myStore";
        String address = "songpa-gu";
        String postcode = "12345";
        StoreController.StoreCreateDto storeCreateDto = new StoreController.StoreCreateDto(storeName, address, postcode);
        String createDtoStr = objectMapper.writeValueAsString(storeCreateDto);

        StoreController.StoreDto expectedResult =
                new StoreController.StoreDto(1L, storeName, new Address(address, postcode));

        // when
         ResultActions actions = this.mockMvc.perform(post("/store")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(createDtoStr)
                 .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expectedResult)));

        // docs
        actions.andDo(document("store/post",
                requestFields(
                        fieldWithPath("storeName").type(JsonFieldType.STRING).description("name of store"),
                        fieldWithPath("addressStr").type(JsonFieldType.STRING).description("address of store"),
                        fieldWithPath("postcode").type(JsonFieldType.STRING).description("postcode of store")
                ),
                responseFields(storeDtoDescriptor)));
    }

//    @Test
//    void getStoreWithId() {
//    }

//    @Test
//    void getAllStores() {
//    }

}
