package net.junhabaek.springapidocs.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.junhabaek.springapidocs.controller.HomeController;
import net.junhabaek.springapidocs.controller.StoreController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;


@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = {StoreController.class, HomeController.class})
@ActiveProfiles("restdocs")
@ContextConfiguration(classes = {RestDocsMockConfiguration.class})
@ComponentScan(basePackageClasses = StoreController.class)
@Disabled
public abstract class BaseRestDocsTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        MockMvcConfigurer configurer = documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
                .and()
                .uris()
                    .withScheme("https")
                    .withHost("my-shop-domain.com")
                .and()
                .snippets()
                    .withDefaults(httpRequest(), responseBody());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(configurer) // can be replaced with @AutoConfigureRestDocs
                .build();
    }

}
