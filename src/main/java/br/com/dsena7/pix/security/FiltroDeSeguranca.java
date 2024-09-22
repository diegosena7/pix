package br.com.dsena7.pix.security;

import br.com.dsena7.pix.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroDeSeguranca extends OncePerRequestFilter {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TokenSecurityService tokenSecurityService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recuperaTokenRemoveBearer(request);

        if (token != null){
            var login = tokenSecurityService.validaToken(token);
            UserDetails userDetails = usuarioRepository.findByLogin(login);
            var usuarioAutenticado = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usuarioAutenticado);
        }else{
            filterChain.doFilter(request, response);
        }
    }

    private String recuperaTokenRemoveBearer(HttpServletRequest request){
        var header = request.getHeader("Authorization");
        if (header== null){
            return null;
        }else {
            return header.replace("Bearer ", "");
        }
    }
}
