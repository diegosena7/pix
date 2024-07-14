package br.com.dsena7.pix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExcecoesNegocios extends Exception {
    public ExcecoesNegocios(String message) {
        super(message);
    }
}
