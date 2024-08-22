package com.joelmaciel.api_gerenciador.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
    ERROR_SISTEMA("error-sistema", "Error Sistema"),
    DADOS_INVALIDOS("dados-inválidos", "Dados Inválidos"),
    MENSAGEM_INCOMPREENSÍVEL("mensagem-incompreensível", "Mensagem Incompreensível"),
    RECURSO_NAO_EXISTE("recurso-não-existe", "Recurso Não Existe");

    private final String title;
    private final String uri;

    ProblemType(String path, String title) {
        this.uri = "https://gerenciador_api.com.br/" + path;
        this.title = title;
    }
    }
