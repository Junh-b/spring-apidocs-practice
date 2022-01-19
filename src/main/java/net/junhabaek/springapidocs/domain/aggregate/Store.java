package net.junhabaek.springapidocs.domain.aggregate;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter(value = AccessLevel.PROTECTED) @AllArgsConstructor
public class Store {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fullAddress",column = @Column(name="full_address")),
            @AttributeOverride(name = "postcode", column = @Column(name = "postcode"))
    })
    private Address address;

    public static Store createNewStore(String name, String addressStr, String postcode){
        Store store = new Store();
        store.name=name;
        store.address = new Address(addressStr, postcode);
        return store;
    }
}
