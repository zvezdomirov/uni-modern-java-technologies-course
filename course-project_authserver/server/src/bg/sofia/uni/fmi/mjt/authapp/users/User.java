package bg.sofia.uni.fmi.mjt.authapp.users;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User implements Cloneable {
    private String username;
    private String password;
    private String passwordSalt;
    private String firstName;
    private String lastName;
    private String email;
    @NonNull
    private Role role;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s %s",
                username,
                password,
                firstName,
                lastName,
                email,
                role.toString(),
                passwordSalt);
    }
}