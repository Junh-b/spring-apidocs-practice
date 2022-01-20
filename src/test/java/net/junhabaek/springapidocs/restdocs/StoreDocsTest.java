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
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        ConstraintDescriptions createStoreConstraintDescriptions =
                new ConstraintDescriptions(StoreController.StoreCreateDto.class);

        actions.andDo(document("store/createNewStore",
                requestFields(
                        fieldWithPath("storeName").type(JsonFieldType.STRING).description("name of store")
                            .attributes(key("constraints")
                                .value(createStoreConstraintDescriptions.descriptionsForProperty("storeName"))),
                        fieldWithPath("addressStr").type(JsonFieldType.STRING).description("address of store")
                            .attributes(key("constraints")
                                .value(createStoreConstraintDescriptions.descriptionsForProperty("addressStr"))),
                        fieldWithPath("postcode").type(JsonFieldType.STRING).description("postcode of store")
                            .attributes(key("constraints")
                                .value(createStoreConstraintDescriptions.descriptionsForProperty("postcode")))
                ),
                responseFields(storeDtoDescriptor)));
    }

    @Test
    void getStoreWithId() throws Exception {
        // given

        String storeName = "myStore";
        String address = "songpa-gu";
        String postcode = "12345";
        Long storeId = 1L;

        given(storeService.findStore(any(Long.class)))
                .will(invocation -> {
                    Store store = Store.createNewStore(storeName, address, postcode);
                    TestUtils.setLongId(store, "id", invocation.getArgument(0));
                    return store;
                });

        StoreController.StoreDto expectedResult =
                new StoreController.StoreDto(storeId, storeName, new Address(address, postcode));

        // when
        ResultActions actions = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/store/{id}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expectedResult)));

        // docs
        actions.andDo(document("store/getStoreById",
                pathParameters(
                        parameterWithName("id").description("id of store")
                ),
                responseFields(storeDtoDescriptor)));
    }

    @Test
    void getAllStores() throws Exception {

        String firstStoreName = "myStore1";
        String firstAddress = "songpa-gu";
        String firstPostcode = "12345";
        Long firstStoreId = 1L;

        String secondStoreName = "myStore2";
        String secondAddress = "gangnam-gu";
        String secondPostcode = "67890";
        Long secondStoreId = 2L;

        given(storeService.getAllStores())
                .will(invocation -> {
                    Store firstStore = Store.createNewStore(firstStoreName, firstAddress, firstPostcode);
                    TestUtils.setLongId(firstStore, "id", firstStoreId);

                    Store secondStore = Store.createNewStore(secondStoreName, secondAddress, secondPostcode);
                    TestUtils.setLongId(secondStore, "id", secondStoreId);
                    return List.of(firstStore, secondStore);
                });

        StoreController.StoreDto firstStoreDto =
                new StoreController.StoreDto(firstStoreId, firstStoreName, new Address(firstAddress, firstPostcode));

        StoreController.StoreDto secondStoreDto =
                new StoreController.StoreDto(secondStoreId, secondStoreName, new Address(secondAddress, secondPostcode));

        List<StoreController.StoreDto> expectedResult = List.of(firstStoreDto, secondStoreDto);

        // when
        ResultActions actions = this.mockMvc.perform(get("/store")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(expectedResult)));

        // docs
        actions.andDo(document("store/getAllStores",
                responseFields(fieldWithPath("[]").description("An array of Store Information"))
                                .andWithPrefix("[].", storeDtoDescriptor)));
    }
}
