package br.com.dsena7.pix.controller;

import br.com.dsena7.pix.model.dto.AutenticacaoDto;
import br.com.dsena7.pix.model.dto.LoginResponseDTO;
import br.com.dsena7.pix.model.dto.RegistroUsuarioDTO;
import br.com.dsena7.pix.model.entity.UsuarioEntity;
import br.com.dsena7.pix.repository.UsuarioRepository;
import br.com.dsena7.pix.security.FiltroDeSeguranca;
import br.com.dsena7.pix.security.TokenSecurityService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired private TokenSecurityService tokenSecurityService;
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AutenticacaoDto dtoLogin) {
        var senhaUsuario = new UsernamePasswordAuthenticationToken(dtoLogin.getLogin(), dtoLogin.getPassword());
        var autenticacaoSenha = this.authenticationManager.authenticate(senhaUsuario);

        var token = tokenSecurityService.geraToken((UsuarioEntity) autenticacaoSenha.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("registro")
    public ResponseEntity registro(@RequestBody @Valid RegistroUsuarioDTO registroUsuarioDTO){
        if (usuarioRepository.findByLogin(registroUsuarioDTO.getLogin()) != null){
            return ResponseEntity.badRequest().build();
        }else {
            String senha = new BCryptPasswordEncoder().encode(registroUsuarioDTO.getPassword());
            UsuarioEntity usuarioEntity = new UsuarioEntity(registroUsuarioDTO.getLogin(),
                    senha, registroUsuarioDTO.getRegra());

            this.usuarioRepository.save(usuarioEntity);
            return ResponseEntity.ok().build();
        }
    }
}
