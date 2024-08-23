package com.joelmaciel.api_gerenciador.domain.exceptions;

public class ListaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public ListaNaoEncontradaException(String message) {
        super(message);
    }
    public ListaNaoEncontradaException(Long listaId) {
        this(String.format("Não existe um lista de id %d salva na base de dados", listaId));
    }
}
