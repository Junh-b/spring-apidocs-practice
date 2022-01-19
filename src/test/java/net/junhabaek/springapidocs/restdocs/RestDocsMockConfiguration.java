package net.junhabaek.springapidocs.restdocs;

import net.junhabaek.springapidocs.service.StoreService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("restdocs")
@TestConfiguration
public class RestDocsMockConfiguration {
    @Bean
    @Primary
    public StoreService getStoreService(){
        StoreService storeService = Mockito.mock(StoreService.class);
        return storeService;
    }
}
