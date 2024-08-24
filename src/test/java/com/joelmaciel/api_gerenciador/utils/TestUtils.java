package com.joelmaciel.api_gerenciador.utils;

import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaResumoDTO;
import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import com.joelmaciel.api_gerenciador.domain.models.Item;
import com.joelmaciel.api_gerenciador.domain.models.Lista;

import java.time.LocalDate;
import java.util.List;

public class TestUtils {

    public static ListaRequestDTO getMockListaRequestDTO(List<ItemRequestDTO> itens) {
        return ListaRequestDTO.builder()
                .nome("Lista de Reforma da Casa")
                .itens(itens)
                .build();
    }

    public static ListaRequestDTO getMockInvalidaListaRequestDTO(List<ItemRequestDTO> itens) {
        return ListaRequestDTO.builder()
                .nome("")
                .itens(null)
                .build();
    }

    public static ItemRequestDTO getMockItemRequestDTO() {
        return ItemRequestDTO.builder()
                .titulo("Depositos de Construçao")
                .descricao("Verificar quais depósitos estão com preços mais baratos.")
                .build();
    }

    public static ItemRequestDTO getMockInvalidoItemRequestDTO() {
        return ItemRequestDTO.builder()
                .titulo("")
                .descricao("")
                .build();
    }

    public static Lista getMockLista() {
        return Lista.builder()
                .id(3L)
                .nome("Lista de Reforma da Casa")
                .dataCriacao(LocalDate.now())
                .itens(List.of(getMockItemUm(), getMockItemDois()))
                .build();
    }

    public static ListaDTO getListaDTO() {
        return ListaDTO.builder()
                .id(3L)
                .nome("Lista de Reforma da Casa")
                .dataCriacao(LocalDate.now())
                .itens(List.of(getMockItemDTO()))
                .build();
    }

    public static Item getMockItemUm() {
        return Item.builder()
                .id(5L)
                .titulo("Depositos de Construçao")
                .descricao("Verificar quais depósitos estão com preços mais baratos.")
                .status(StatusItem.PENDENTE)
                .isPrioritaria(false)
                .dataCriacao(LocalDate.now())
                .dataConclusao(null)
                .build();
    }

    public static Item getMockItemDois() {
        return Item.builder()
                .id(5L)
                .titulo("Melhores Pedreiros")
                .descricao("Se informar com os vizinhos, parentes e amigos um bom pedreiro.")
                .status(StatusItem.PENDENTE)
                .isPrioritaria(true)
                .dataCriacao(LocalDate.now())
                .dataConclusao(null)
                .build();
    }

    public static ItemDTO getMockItemDTO() {
        return ItemDTO.builder()
                .id(5L)
                .titulo("Depositos de Construçao")
                .descricao("Verificar quais depósitos estão com preços mais baratos.")
                .status(StatusItem.PENDENTE)
                .isPrioritaria(false)
                .dataCriacao(LocalDate.now())
                .dataConclusao(null)
                .build();
    }

    public static ListaResumoDTO getMockListaResumoDTO() {
        return ListaResumoDTO.builder()
                .id(3L)
                .nome("Lista de Reforma da Casa")
                .dataCriacao(LocalDate.now())
                .build();
    }
}
