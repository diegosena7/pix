package br.com.dsena7.pix.model.dto;

import br.com.dsena7.pix.model.enums.UsurioRegrasEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {
    private String login;
    private String password;
    private UsurioRegrasEnum regra;
}
