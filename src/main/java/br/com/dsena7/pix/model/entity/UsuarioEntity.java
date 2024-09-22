package br.com.dsena7.pix.model.entity;

import br.com.dsena7.pix.model.enums.UsurioRegrasEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "id")
public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String login;

    private String password;

    private UsurioRegrasEnum role;

    public UsuarioEntity(String login, String password, UsurioRegrasEnum regra) {
        this.login = login;
        this.password = password;
        this.role = regra;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UsurioRegrasEnum.USUARIO) {
            return List.of(new SimpleGrantedAuthority("USUARIO_ROLE"));
        }else {
            return List.of(new SimpleGrantedAuthority("DEV_ROLE"));
        }
    }

    @Override
    public String getUsername() {
        return login;
    }
}
