package com.joelmaciel.api_gerenciador.api.controllers;

import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
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
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.yaml")
class ListaControllerTest {

    public static final String MSG_DADOS_INVALIDOS = "Um ou mais campos estão inválidos. Preencha corretamente e tente novamente.";
    public static final String MSG_LISTA_NAO_PODE_ESTAR_VAZIA = "Os itens da lista nao podem ser nulos, preencha com pelo menos um item.";
    public static final String MSG_NOME_E_OBRIGATORIO = "O campo nome e obrigatorio e deve ter entre 8 e 50 caracteres.";
    public static final String MSG_LISTA_NAO_EXISTE = "Não existe um lista de id 999 salva na base de dados";

    @Autowired
    private Flyway flyway;

    @LocalServerPort
    private int port;

    private ListaRequestDTO listaRequestDTO;
    private ListaRequestDTO listaRequestDTOInvalida;
    private Long listaId;
    private Long listaIdInvalido;

    @BeforeEach
    void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/api/listas";

        List<ItemRequestDTO> itensRequestDTO = List.of(TestUtils.getMockItemRequestDTO());
        List<ItemRequestDTO> itemRequestDTOSInvalidos = List.of(TestUtils.getMockInvalidoItemRequestDTO());

        listaRequestDTO = TestUtils.getMockListaRequestDTO(itensRequestDTO);
        listaRequestDTOInvalida = TestUtils.getMockInvalidaListaRequestDTO(itemRequestDTOSInvalidos);
        listaId = 1L;
        listaIdInvalido = 999L;

        flyway.migrate();
    }

    @Test
    @DisplayName("Quando adicionarLista é chamado com dados válidos, então deve salvar lista com sucesso.")
    void dadoUmaListaRequestDTOValido_QuandoAdicionarLista_EntaoDeveSalvarListaComSucesso() {
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(listaRequestDTO)
             .when()
                .post()
             .then()
                .statusCode(201)
                .body("nome", equalTo(listaRequestDTO.getNome()))
                .body("dataCriacao", equalTo(LocalDate.now().toString()))
                .body("itens", hasSize(1))
                .body("itens[0].id", notNullValue())
                .body("itens[0].titulo", equalTo(listaRequestDTO.getItens().getFirst().getTitulo()))
                .body("itens[0].descricao", equalTo(listaRequestDTO.getItens().getFirst().getDescricao()))
                .body("itens[0].status", equalTo(StatusItem.PENDENTE.name()))
                .body("itens[0].prioritaria", equalTo(false))
                .body("itens[0].dataCriacao", equalTo(LocalDate.now().toString()));
    }

    @Test
    @DisplayName("Quando adicionarLista é chamado com dados inválidos, então deve retornar erro de validação.")
    void dadoUmaListaRequestDTOInvalido_QuandoAdicionarLista_EntaoDeveRetornarErroDeValidacao() {
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(listaRequestDTOInvalida)
             .when()
                .post()
             .then()
                .statusCode(400)
                .body("title", equalTo("Dados Inválidos"))
                .body("userMessage", equalTo(MSG_DADOS_INVALIDOS))
                .body("objects.name", hasItems("itens", "nome"))
                .body("objects.userMessage", hasItems(MSG_LISTA_NAO_PODE_ESTAR_VAZIA, MSG_NOME_E_OBRIGATORIO));
    }

    @Test
    @DisplayName("Quando buscarTodasListas é chamado, então deve retornar a lista de listas.")
    void dadoBuscarTodasListas_QuandoBuscarTodasListas_EntaoDeveRetornarListaDeListas() {
        String nomeListaEsperada1 = "Lista de Compras";
        String nomeListaEsperada2 = "Lista de Viagem";

        given()
                .accept("application/json")
             .when()
                .get()
             .then()
                .statusCode(200)
                .body("content", hasSize(greaterThan(0)))
                .body("content.nome", hasItems(nomeListaEsperada1, nomeListaEsperada2))
                .body("totalElements", equalTo(2))
                .body("totalPages", equalTo(1));
    }

    @Test
    @DisplayName("Quando buscarTodasListas é chamado com datas, então deve retornar a lista de listas filtradas.")
    void dadoBuscarTodasListasComDatas_QuandoBuscarTodasListas_EntaoDeveRetornarListaDeListasFiltradas() {
        LocalDate dataInicio = LocalDate.of(2024, 8, 1);
        LocalDate dataFim = LocalDate.of(2024, 8, 31);

        given()
                .accept("application/json")
                .queryParam("dataInicio", dataInicio.toString())
                .queryParam("dataFim", dataFim.toString())
             .when()
                .get()
             .then()
                .statusCode(200)
                .body("content", hasSize(greaterThan(0)))
                .body("content.nome", hasItems("Lista de Compras", "Lista de Viagem"))
                .body("totalElements", equalTo(2))
                .body("totalPages", equalTo(1));
    }


    @Test
    @DisplayName("Quando buscarListaPorId é chamado com ID válido, então deve retornar a lista correspondente.")
    void dadoBuscarListaPorId_QuandoBuscarListaPorId_EntaoDeveRetornarListaCorreta() {
        given()
                .accept("application/json")
             .when()
                .get("/{listaId}", listaId)
             .then()
                .statusCode(200)
                .body("id", equalTo(listaId.intValue()))
                .body("nome", equalTo("Lista de Compras"))
                .body("itens", notNullValue())
                .body("itens.size()", equalTo(2))
                .body("itens[0].id", equalTo(1))
                .body("itens[0].titulo", equalTo("Comprar Leite"))
                .body("itens[0].descricao", equalTo("Comprar 2 litros de leite"))
                .body("itens[0].status", equalTo("PENDENTE"))
                .body("itens[0].prioritaria", equalTo(true))
                .body("itens[1].id", equalTo(2))
                .body("itens[1].titulo", equalTo("Comprar Pão"))
                .body("itens[1].descricao", equalTo("Comprar 1 pão integral"))
                .body("itens[1].status", equalTo("PENDENTE"))
                .body("itens[1].dataConclusao", equalTo(null))
                .body("itens[1].prioritaria", equalTo(false));
    }

    @Test
    @DisplayName("Quando buscarListaPorId é chamado com ID inválido, então deve retornar erro 404.")
    void dadoBuscarListaPorIdComIdInvalido_QuandoBuscarListaPorId_EntaoDeveRetornarErro404() {
        given()
                .accept("application/json")
             .when()
                .get("/{listaId}", listaIdInvalido)
             .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("userMessage", equalTo(MSG_LISTA_NAO_EXISTE));
    }

    @Test
    @DisplayName("Quando deletarLista é chamado com ID válido, então a lista deve ser deletada com sucesso.")
    void dadoDeletarLista_QuandoDeletarListaComIdValido_EntaoDeveDeletarListaComSucesso() {

        given()
                .accept("application/json")
             .when()
                .delete("/{listaId}", listaId)
             .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Quando deletarLista é chamado com ID inválido, então deve retornar erro 404.")
    void dadoDeletarListaComIdInvalido_QuandoDeletarLista_EntaoDeveRetornarErro404() {

        given()
                .accept("application/json")
             .when()
                .delete("/{listaId}", listaIdInvalido)
             .then()
                .statusCode(404)
                .body("title", equalTo("Recurso Não Existe"))
                .body("userMessage", equalTo(MSG_LISTA_NAO_EXISTE));
    }

}
