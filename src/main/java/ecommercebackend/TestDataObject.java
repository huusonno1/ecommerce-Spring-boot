package ecommercebackend;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Collate;

@Entity
public class TestDataObject {
    @Id
    private long id;
    @Column
    private String username;

}
