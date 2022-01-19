package net.junhabaek.springapidocs.service;

import lombok.RequiredArgsConstructor;
import net.junhabaek.springapidocs.domain.aggregate.Store;
import net.junhabaek.springapidocs.infra.StoreRepository;
import net.junhabaek.springapidocs.util.StreamUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public Store register(String storeName, String addressStr, String postcode){
        Store store = Store.createNewStore(storeName, addressStr, postcode);
        storeRepository.save(store);
        return store;
    }

    @Transactional(readOnly = true)
    public Store findStore(Long storeId){
        return storeRepository.findById(storeId).get();
    }

    @Transactional(readOnly = true)
    public List<Store> getAllStores(){
        return StreamUtil.convertToList(storeRepository.findAll());
    }
}
