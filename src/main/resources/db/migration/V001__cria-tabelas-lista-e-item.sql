CREATE TABLE lista (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50),
    data_criacao DATETIME(6)
);


CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    status VARCHAR(25) NOT NULL,
    is_prioritaria BOOLEAN,
    data_criacao DATETIME(6),
    data_conclusao DATETIME(6),
    lista_id BIGINT,
    FOREIGN KEY (lista_id) REFERENCES lista(id) ON DELETE CASCADE
);
