package com.joelmaciel.api_gerenciador.domain.services.impl;

import com.joelmaciel.api_gerenciador.api.converter.ItemConverter;
import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ItemDTO;
import com.joelmaciel.api_gerenciador.domain.enums.StatusItem;
import com.joelmaciel.api_gerenciador.domain.exceptions.BusinessException;
import com.joelmaciel.api_gerenciador.domain.models.Item;
import com.joelmaciel.api_gerenciador.domain.models.Lista;
import com.joelmaciel.api_gerenciador.domain.repositories.ItemRepository;
import com.joelmaciel.api_gerenciador.domain.services.ListaService;
import com.joelmaciel.api_gerenciador.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ItemServiceImplTest {

    private static final String MSG_NO_ESTADO_PENDENTE = "O item so pode se iniciado se estiver no estado pendente";
    private static final String MSG_NO_ESTADO_EM_PROGRESSO = "O item so pode ser concluído se estiver no estado em progresso";
    public static final String MSG_ITEM_NAO_PERTENCE_A_LISTA = "O item não pertence a lista informada.";

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ListaService listaService;

    @Mock
    private ItemConverter itemConverter;

    private ItemRequestDTO itemRequestDTO;
    private ItemRequestDTO itemRequestDTOInvalido;
    private ItemDTO itemDTO;
    private Item item;
    private Lista lista;
    private Long listaId;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        listaId = 1L;
        itemRequestDTO = TestUtils.getMockItemRequestDTO();
        itemRequestDTOInvalido = TestUtils.getMockInvalidoItemRequestDTO();
        itemDTO = TestUtils.getMockItemDTO();
        item = TestUtils.getMockItemUm();
        lista = TestUtils.getMockLista();
    }

    @Test
    @DisplayName("Quando adicionarItem é chamado com dados válidos, então deve salvar item com sucesso.")
    void dadoUmItemRequestDTOValido_QuandoAdicionarItem_EntaoDeveSalvarItemComSucesso() {

        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);
        when(itemConverter.toModel(itemRequestDTO)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemConverter.toDTO(item)).thenReturn(itemDTO);

        ItemDTO result = itemService.adicionarItem(listaId, itemRequestDTO);

        assertNotNull(result);
        assertEquals(itemDTO, result);

        assertTrue(lista.getItens().contains(item), "O item deveria estar presente na lista.");

        verify(listaService, times(1)).buscarOptionalLista(listaId);
        verify(itemConverter, times(1)).toModel(itemRequestDTO);
        verify(itemRepository, times(1)).save(item);
        verify(itemConverter, times(1)).toDTO(item);
    }

    @Test
    @DisplayName("Quando adicionarItem é chamado com ItemRequestDTO inválido, então deve lançar IllegalArgumentException.")
    void dadoUmItemRequestDTOInvalido_QuandoAdicionarItem_EntaoDeveLancarIllegalArgumentException() {
        when(itemConverter.toModel(itemRequestDTOInvalido)).thenThrow(new IllegalArgumentException("ItemRequestDTO inválido"));

        assertThrows(IllegalArgumentException.class, () -> itemService.adicionarItem(listaId, itemRequestDTOInvalido));

        verify(itemConverter, times(1)).toModel(itemRequestDTOInvalido);
    }

    @Test
    @DisplayName("Quando atualizarItem é chamado com dados válidos, então deve atualizar item com sucesso.")
    void dadoUmItemRequestDTOValido_QuandoAtualizarItem_EntaoDeveAtualizarItemComSucesso() {
        Long listaId = 3L;
        Long itemId = 2L;

        Item mockItem = TestUtils.getMockItemComLista();
        Item itemAtualizado = TestUtils.getMockUpdateItemUm();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            item.setId(itemId);
            return item;
        });
        when(itemConverter.toDTO(any(Item.class))).thenReturn(TestUtils.getMockItemDTOFromItem(itemAtualizado));

        ItemRequestDTO updateItemRequestDTO = TestUtils.getMockUpdateItemRequestDTO();

        ItemDTO result = itemService.atualizarItem(listaId, itemId, updateItemRequestDTO);

        assertNotNull(result);
        assertEquals(updateItemRequestDTO.getTitulo(), result.getTitulo());
        assertEquals(updateItemRequestDTO.getDescricao(), result.getDescricao());

        verify(itemRepository, times(1)).save(any(Item.class));
    }


    @Test
    @DisplayName("Quando deleteItem é chamado com dados válidos, então deve deletar item com sucesso.")
    void dadoUmItemIdValido_QuandoDeleteItem_EntaoDeveDeletarItemComSucesso() {
        Long listaId = 3L;
        Long itemId = 5L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);

        itemService.deleteItem(listaId, itemId);

        verify(itemRepository, times(1)).delete(item);
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

    @Test
    @DisplayName("Quando deleteItem é chamado com item que não pertence à lista, então deve lançar uma BusinessException.")
    void dadoUmItemQueNaoPertenceALista_QuandoDeleteItem_EntaoDeveLancarBusinessException() {
        Long listaId = 3L;
        Long itemId = 5L;

        Lista outraLista = TestUtils.getMockLista();
        outraLista.setId(99L);
        Item itemComOutraLista = TestUtils.getMockItemUm();
        itemComOutraLista.setLista(outraLista);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemComOutraLista));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(TestUtils.getMockLista());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> itemService.deleteItem(listaId, itemId));

        assertEquals(MSG_ITEM_NAO_PERTENCE_A_LISTA, exception.getMessage());
        verify(itemRepository, never()).delete(any(Item.class));
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

    @Test
    @DisplayName("Quando iniciarItem é chamado com um item no estado PENDENTE, então deve alterar o status para EM_PROGRESSO.")
    void dadoUmItemNoEstadoPendente_QuandoIniciarItem_EntaoDeveAlterarStatusParaEmProgresso() {
        Long listaId = 3L;
        Long itemId = 5L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);

        item.setStatus(StatusItem.PENDENTE);

        itemService.iniciarItem(listaId, itemId);

        assertEquals(StatusItem.EM_PROGRESSO, item.getStatus());

        verify(itemRepository, times(1)).findById(itemId);
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

    @Test
    @DisplayName("Quando iniciarItem é chamado com um item no estado EM_PROGRESSO, então deve lançar BusinessException.")
    void dadoUmItemNoEstadoEmProgresso_QuandoIniciarItem_EntaoDeveLancarBusinessException() {
        Long listaId = 3L;
        Long itemId = 5L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);

        item.setStatus(StatusItem.EM_PROGRESSO);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            itemService.iniciarItem(listaId, itemId);
        });

        assertEquals(MSG_NO_ESTADO_PENDENTE, exception.getMessage());

        verify(itemRepository, times(1)).findById(itemId);
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

    @Test
    @DisplayName("Quando concluirItem é chamado com um item no estado EM_PROGRESSO, então deve alterar o status para CONCLUIDA.")
    void dadoUmItemNoEstadoEmProgresso_QuandoConcluirItem_EntaoDeveAlterarStatusParaConcluida() {
        Long listaId = 3L;
        Long itemId = 5L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);

        item.setStatus(StatusItem.EM_PROGRESSO);

        itemService.concluirItem(listaId, itemId);

        assertEquals(StatusItem.CONCLUIDA, item.getStatus());
        assertNotNull(item.getDataConclusao());

        verify(itemRepository, times(1)).findById(itemId);
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

    @Test
    @DisplayName("Quando concluirItem é chamado com um item no estado PENDENTE, então deve lançar BusinessException.")
    void dadoUmItemNoEstadoPendente_QuandoConcluirItem_EntaoDeveLancarBusinessException() {
        Long listaId = 3L;
        Long itemId = 5L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);

        item.setStatus(StatusItem.PENDENTE);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            itemService.concluirItem(listaId, itemId);
        });

        assertEquals(MSG_NO_ESTADO_EM_PROGRESSO, exception.getMessage());

        verify(itemRepository, times(1)).findById(itemId);
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

    @Test
    @DisplayName("Quando alterarPrioridadeItem é chamado, então deve definir a prioridade como verdadeira.")
    void dadoUmItem_QuandoAlterarPrioridadeItem_EntaoDeveDefinirPrioridadeComoVerdadeira() {
        Long listaId = 3L;
        Long itemId = 5L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(listaService.buscarOptionalLista(listaId)).thenReturn(lista);

        itemService.alterarPrioridadeItem(listaId, itemId);

        assertTrue(item.isPrioritaria());

        verify(itemRepository, times(1)).findById(itemId);
        verify(listaService, times(1)).buscarOptionalLista(listaId);
    }

}