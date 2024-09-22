package br.com.dsena7.pix.security;

import br.com.dsena7.pix.model.entity.UsuarioEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/*
Usamos uma secret para deixa um hash único pra aplicação como parâmetro para o algoritmo de hash HMAC256
Geralmente essa secret pode ser armazenada num cluster do K8s ou numa nuvem como Azure via variável de ambiente
 */
@Service
public class TokenSecurityService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String geraToken(UsuarioEntity usuarioEntity){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create().withIssuer("API-PIX").withSubject(usuarioEntity.getLogin()).withExpiresAt(geraExpircaoToken()).sign(algorithm);
            return token;
        }catch (JWTCreationException e){
            throw new RuntimeException("Erro na geração do token: " + e);
        }
    }

    private Instant geraExpircaoToken(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validaToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm).withIssuer("API-PIX")
                    .build().verify(token).getSubject();
        }catch (JWTVerificationException e) {
            return "";
        }
    }
}
