package com.joelmaciel.api_gerenciador.api.controllers;


import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import com.joelmaciel.api_gerenciador.utils.TestUtils;
import io.restassured.RestAssured;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.yaml")
class ItemControllerTest {

    public static final String MSG_VALIDACAO_DESCRICAO = "O campo descricao e obrigatorio e deve ter entre 8 e 200 caracteres.";
    public static final String MSG_VALIDACAO_TITULO = "O campo titulo e obrigatorio e deve ter entre 8 e 50 caracteres.";
    public static final String MSG_USUARIO = "Um ou mais campos estão inválidos. Preencha corretamente e tente novamente.";
    public static final String MSG_LISTA_NAO_EXISTE = "Não existe um lista de id 999 salva na base de dados";
    public static final String MSG_ITEM_NAO_PERTENCE_A_LISTA = "O item não pertence a lista informada.";
    public static final String MSG_ITEM_NAO_EXISTE = "Nao existe um item de id 999 salvo na base de dados";
    public static final String MSG_ESTIVER_NO_ESTADO_EM_PROGRESSO = "O item so pode ser concluído se estiver no estado em progresso";

    @Autowired
    private Flyway flyway;

    @LocalServerPort
    private int port;

    private Long listaId;
    private Long itemId;
    private Long itemIdNaoExistente;
    private Long itemIdNaoRelacionado;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/api/listas";

        listaId = 1L;
        itemId = 1L;
        itemIdNaoExistente = 999L;
        itemIdNaoRelacionado = 3L;

        flyway.migrate();
    }

    @Test
    @DisplayName("Quando adicionarItem é chamado com dados válidos, então deve salvar item com sucesso.")
    void dadoUmItemRequestDTOValido_QuandoAdicionarItem_EntaoDeveSalvarItemComSucesso() {
        ItemRequestDTO itemRequestDTO = TestUtils.getMockItemRequestDTO();

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(itemRequestDTO)
             .when()
                .post("/{listaId}/itens", listaId)
             .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("titulo", equalTo(itemRequestDTO.getTitulo()))
                .body("descricao", equalTo(itemRequestDTO.getDescricao()))
                .body("status", equalTo(StatusItem.PENDENTE.name()))
                .body("prioritaria", equalTo(false))
                .body("dataCriacao", notNullValue());
    }

    @Test
    @DisplayName("Quando adicionarItem é chamado com um listaId inválido, então deve retornar erro 404")
    void dadoUmListaIdInvalido_QuandoAdicionarItem_EntaoDeveRetornarErro() {
        ItemRequestDTO itemRequestDTO = TestUtils.getMockItemRequestDTO();
        Long listaIdInvalido = 999L;

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(itemRequestDTO)
             .when()
                .post("/{listaId}/itens", listaIdInvalido)
             .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", equalTo("Recurso Não Existe"))
                .body("userMessage", equalTo(MSG_LISTA_NAO_EXISTE));
    }

    @Test
    @DisplayName("Quando adicionarItem é chamado com dados inválidos, então deve retornar status 400.")
    void dadoUmItemRequestDTOInvalido_QuandoAdicionarItem_EntaoDeveRetornarErro() {
        ItemRequestDTO itemRequestDTO = TestUtils.getMockItemRequestDTO();
        itemRequestDTO.setTitulo("");
        itemRequestDTO.setDescricao("");

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(itemRequestDTO)
             .when()
                .post("/{listaId}/itens", listaId)
             .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Dados Inválidos"))
                .body("userMessage", equalTo(MSG_USUARIO))
                .body("objects.name", hasItems("descricao", "titulo"))
                .body("objects.userMessage", hasItems(MSG_VALIDACAO_DESCRICAO, MSG_VALIDACAO_TITULO));
    }

    @Test
    @DisplayName("Quando atualizarItem é chamado com dados válidos, então deve atualizar o item com sucesso.")
    void dadoUmItemRequestDTOValido_QuandoAtualizarItem_EntaoDeveAtualizarItemComSucesso() {
        ItemRequestDTO itemRequestDTO = TestUtils.getMockItemRequestDTO();
        itemRequestDTO.setTitulo("Novo Título");

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(itemRequestDTO)
             .when()
                .patch("/{listaId}/itens/{itemId}", listaId, itemId)
             .then()
                .statusCode(HttpStatus.OK.value())
                .body("titulo", equalTo(itemRequestDTO.getTitulo()))
                .body("descricao", equalTo(itemRequestDTO.getDescricao()));
    }

    @Test
    @DisplayName("Quando atualizarItem é chamado com um itemId que não está relacionado com a lista, então deve retornar erro 404.")
    void dadoUmItemIdNaoRelacionadoComLista_QuandoAtualizarItem_EntaoDeveRetornarErro() {
        ItemRequestDTO itemRequestDTO = TestUtils.getMockItemRequestDTO();

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(itemRequestDTO)
             .when()
                .patch("/{listaId}/itens/{itemId}", listaId, itemIdNaoRelacionado)
             .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Dados Inválidos"))
                .body("userMessage", equalTo(MSG_ITEM_NAO_PERTENCE_A_LISTA));
    }

    @Test
    @DisplayName("Quando atualizarItem é chamado com dados inválidos, então deve retornar status 400.")
    void dadoUmItemRequestDTOInvalido_QuandoAtualizarItem_EntaoDeveRetornarErro() {
        ItemRequestDTO itemRequestDTO = TestUtils.getMockItemRequestDTO();
        itemRequestDTO.setTitulo("");
        itemRequestDTO.setDescricao("");

        given()
                .contentType("application/json")
                .accept("application/json")
                .body(itemRequestDTO)
             .when()
                .patch("/{listaId}/itens/{itemId}", listaId, itemId)
             .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Dados Inválidos"))
                .body("userMessage", equalTo(MSG_USUARIO))
                .body("objects.name", hasItems("descricao", "titulo"))
                .body("objects.userMessage", hasItems(MSG_VALIDACAO_DESCRICAO, MSG_VALIDACAO_TITULO));
    }

    @Test
    @DisplayName("Quando deletarItem é chamado, então deve deletar o item com sucesso.")
    void dadoUmItemExistente_QuandoDeletarItem_EntaoDeveDeletarItemComSucesso() {
        given()
                .contentType("application/json")
                .accept("application/json")
             .when()
                .delete("/{listaId}/itens/{itemId}", listaId, itemId)
             .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Quando deletarItem é chamado com um itemId que não existe, então deve retornar erro 404.")
    void dadoUmItemIdNaoExistente_QuandoDeletarItem_EntaoDeveRetornarErro() {

        given()
                .contentType("application/json")
                .accept("application/json")
             .when()
                .delete("/{listaId}/itens/{itemId}", listaId, itemIdNaoExistente)
             .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", equalTo("Recurso Não Existe"))
                .body("userMessage", equalTo(MSG_ITEM_NAO_EXISTE));
    }

    @Test
    @DisplayName("Quando deletarItem é chamado com um itemId que não pertence à lista, então deve retornar erro 400.")
    void dadoUmItemNaoPertencenteALista_QuandoDeletarItem_EntaoDeveRetornarErro() {

        given()
                .contentType("application/json")
                .accept("application/json")
             .when()
                .delete("/{listaId}/itens/{itemId}", listaId, itemIdNaoRelacionado)
             .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Dados Inválidos"))
                .body("userMessage", equalTo(MSG_ITEM_NAO_PERTENCE_A_LISTA))
                .body("timestamp", notNullValue());
    }

    @Test
    @DisplayName("Quando iniciarItem é chamado, então deve alterar o status do item para EM_PROGRESSO.")
    void dadoUmItemExistente_QuandoIniciarItem_EntaoDeveAlterarStatusParaEmProgresso() {
        given()
                .contentType("application/json")
                .accept("application/json")
             .when()
                .patch("/{listaId}/itens/{itemId}/status-progresso", listaId, itemId)
             .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @DisplayName("Quando tentar concluir um item não em progresso, então deve retornar status 400.")
    void dadoUmItemNaoEmProgresso_QuandoConcluirItem_EntaoDeveRetornarErro400() {
        given()
                .contentType("application/json")
                .accept("application/json")
             .when()
                .patch("/{listaId}/itens/{itemId}/status-confirmado", listaId, itemId)
             .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", equalTo("Dados Inválidos"))
                .body("userMessage", equalTo(MSG_ESTIVER_NO_ESTADO_EM_PROGRESSO));
    }

    @Test
    @DisplayName("Quando alterarPrioridadeItem é chamado, então deve alterar a prioridade do item.")
    void dadoUmItemExistente_QuandoAlterarPrioridadeItem_EntaoDeveAlterarPrioridade() {
        given()
                .contentType("application/json")
                .accept("application/json")
             .when()
                .patch("/{listaId}/itens/{itemId}/prioridade", listaId, itemId)
             .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }
}