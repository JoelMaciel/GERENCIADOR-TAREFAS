package com.joelmaciel.api_gerenciador.domain.exceptions;

public abstract class EntidadeNaoEncontradaException extends BusinessException {

    protected EntidadeNaoEncontradaException(String message) {
        super(message);
    }

}
