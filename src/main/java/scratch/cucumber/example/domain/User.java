package scratch.cucumber.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static java.lang.String.format;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private final Long id;

    @NotNull
    @Size(min = 1)
    @Column(unique = true, nullable = false)
    private final String username;

    @NotNull
    @Size(min = 1)
    @Column(unique = true, nullable = false)
    private final String password;


    User() {
        this.id = null;
        this.username = null;
        this.password = null;
    }

    public User(String username, String password) {
        this.id = null;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        final User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return format("User {\n  id=%d,\n  username='%s',\n  password='%s'\n}", id, username, password);
    }
}
