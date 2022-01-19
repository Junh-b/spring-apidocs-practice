package net.junhabaek.springapidocs.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor
public class Address {
    private String fullAddress;
    private String postcode;
}
