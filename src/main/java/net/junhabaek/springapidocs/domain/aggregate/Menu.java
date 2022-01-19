package net.junhabaek.springapidocs.domain.aggregate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter(value = AccessLevel.PROTECTED)
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "store_id")
    private Long storeId;

    public static Menu createMenu(String menuName, Long storeId){
        Menu menu = new Menu();
        menu.name = menuName;
        menu.storeId = storeId;
        return menu;
    }

}
