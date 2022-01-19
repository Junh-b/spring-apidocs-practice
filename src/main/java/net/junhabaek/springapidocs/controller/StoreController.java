package net.junhabaek.springapidocs.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.junhabaek.springapidocs.domain.aggregate.Address;
import net.junhabaek.springapidocs.domain.aggregate.Store;
import net.junhabaek.springapidocs.service.StoreService;
import org.mapstruct.Mapping;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreDtoMapper storeDtoMapper;

    @GetMapping("/store")
    public ResponseEntity<List<StoreDto>> getAllStoreInfos(){
        List<Store> stores = storeService.getAllStores();
        List<StoreDto> storeDtos = stores.stream().map(storeDtoMapper::entityToDto).collect(Collectors.toList());
        return ResponseEntity.ok(storeDtos);
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<StoreDto> getStoreInfo(@PathVariable Long id){
        Store store = storeService.findStore(id);
        StoreDto storeDto = storeDtoMapper.entityToDto(store);
        return ResponseEntity.ok(storeDto);
    }

    @PostMapping("/store")
    public ResponseEntity<StoreDto> registerNewStore(@RequestBody @Valid StoreCreateDto storeCreateDto){
        Store store = storeService.register(storeCreateDto.storeName, storeCreateDto.addressStr, storeCreateDto.postcode);
        StoreDto storeDto = storeDtoMapper.entityToDto(store);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeDto);
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class StoreCreateDto{
        @NotBlank @Size(min = 3, max = 20)
        private String storeName;
        @NotBlank @Size(min = 6)
        private String addressStr;
        @NotBlank @Size(min = 5)
        private String postcode;
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class StoreDto{
        private Long storeId;
        private String storeName;
        private Address address;
    }

    @Mapper(
            componentModel = "spring",
            injectionStrategy = InjectionStrategy.CONSTRUCTOR,
            unmappedTargetPolicy = ReportingPolicy.ERROR
    )
    public interface StoreDtoMapper{
        @Mappings({
                @Mapping(target = "storeId", source = "store.id"),
                @Mapping(target = "storeName", source = "store.name"),
                @Mapping(target = "address", source = "store.address")
        })
        StoreDto entityToDto(Store store);
    }
}
