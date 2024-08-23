package com.joelmaciel.api_gerenciador.domain.exceptions;

public class ItemNaoEncontradoException extends EntidadeNaoEncontradaException {

    public ItemNaoEncontradoException(String message) {
        super(message);
    }

    public ItemNaoEncontradoException(Long itemId) {
        this(String.format("Nao existe um item de id %d salvo na base de dados", itemId));
    }
}
