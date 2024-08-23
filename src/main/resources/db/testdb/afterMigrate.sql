set foreign_key_checks = 0;

DELETE FROM item;
DELETE FROM lista;

ALTER TABLE lista AUTO_INCREMENT = 1;
ALTER TABLE item AUTO_INCREMENT = 1;

set foreign_key_checks = 1;


INSERT INTO lista (id, nome, data_criacao) VALUES
(1, 'Lista de Compras', '2024-08-10'),
(2, 'Lista de Viagem', '2024-08-15');

INSERT INTO item (id, titulo, descricao, status, is_prioritaria, data_criacao, data_conclusao, lista_id) VALUES
(1, 'Comprar Leite', 'Comprar 2 litros de leite', 'PENDENTE', true, UTC_TIMESTAMP(), NULL, 1),
(2, 'Comprar Pão', 'Comprar 1 pão integral', 'PENDENTE', false, UTC_TIMESTAMP(), NULL, 1),
(3, 'Reservar Hotel', 'Reservar hotel para a viagem de férias', 'PENDENTE', true, UTC_TIMESTAMP(), NULL, 2),
(4, 'Comprar Passagens', 'Comprar passagens aéreas para a viagem', 'PENDENTE', false,UTC_TIMESTAMP(), NULL, 2);
