package smartfoodcluster.feedme.util;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lavenger on 4/16/2016.
 */
@Entity
public class User {

    @Id
    Long id;
    String email;
    String role;
    String address;
    String name;
    Map<Long, Order> orders = new HashMap<>();

    User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
