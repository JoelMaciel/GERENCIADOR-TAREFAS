package com.joelmaciel.api_gerenciador.domain.services.impl;

import com.joelmaciel.api_gerenciador.api.converter.ListaConverter;
import com.joelmaciel.api_gerenciador.api.dtos.request.ItemRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.request.ListaRequestDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaDTO;
import com.joelmaciel.api_gerenciador.api.dtos.response.ListaResumoDTO;
import com.joelmaciel.api_gerenciador.domain.exceptions.ListaNaoEncontradaException;
import com.joelmaciel.api_gerenciador.domain.models.Lista;
import com.joelmaciel.api_gerenciador.domain.repositories.ListaRepository;
import com.joelmaciel.api_gerenciador.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListaServiceImplTest {

    public static final String MSG_LISTA_NAO_ENCONTRADA = "Não existe um lista de id 999 salva na base de dados";

    @InjectMocks
    private ListaServiceImpl listaService;

    @Mock
    private ListaRepository listaRepository;

    @Mock
    private ListaConverter listaConverter;

    private Lista lista;
    private ListaDTO listaDTO;
    private ListaRequestDTO listaRequestDTO;
    private ListaResumoDTO listaResumoDTO;
    private Long listaId;
    private Long listaIdInvalido;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<ItemRequestDTO> itensRequestDTO = List.of(TestUtils.getMockItemRequestDTO());
        listaRequestDTO = TestUtils.getMockListaRequestDTO(itensRequestDTO);
        listaResumoDTO = TestUtils.getMockListaResumoDTO();
        lista = TestUtils.getMockLista();
        listaDTO = TestUtils.getListaDTO();
        listaId = 1L;
        listaIdInvalido = 999L;
    }

    @Test
    @DisplayName("Quando adicionarLista é chamado com dados válidos, então deve salvar lista com sucesso.")
    void dadoUmaListaRequestDTOValido_QuandoAdicionarLista_EntaoDeveSalvarListaComSucesso() {
        when(listaConverter.toModel(listaRequestDTO)).thenReturn(lista);
        when(listaRepository.save(lista)).thenReturn(lista);
        when(listaConverter.toDTO(lista)).thenReturn(listaDTO);

        ListaDTO result = listaService.adicionarLista(listaRequestDTO);

        assertNotNull(result);

        assertEquals(listaDTO, result);
        assertEquals(listaDTO.getId(), result.getId());
        assertEquals(listaDTO.getNome(), result.getNome());
        assertEquals(listaDTO.getItens().size(), result.getItens().size());

        verify(listaRepository, times(1)).save(lista);
    }

    @Test
    @DisplayName("Quando buscarTodas é chamado com intervalo de datas, então deve retornar uma página de ListaResumoDTO dentro do intervalo.")
    void dadoUmIntervaloDeDatas_QuandoBuscarTodas_EntaoDeveRetornarListaResumoDTO() {
        LocalDate dataInicio = LocalDate.of(2024, 8, 23);
        LocalDate dataFim = LocalDate.of(2024, 8, 24);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lista> listaPage = new PageImpl<>(List.of(lista), pageable, 1);

        when(listaRepository.findByDataCriacaoBetween(dataInicio, dataFim, pageable)).thenReturn(listaPage);
        when(listaConverter.toSummaryDTO(lista)).thenReturn(listaResumoDTO);

        Page<ListaResumoDTO> result = listaService.buscarTodas(dataInicio, dataFim, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(listaRepository, times(1)).findByDataCriacaoBetween(dataInicio, dataFim, pageable);
        verify(listaRepository, never()).findAll(pageable);
    }

    @Test
    @DisplayName("Quando buscarTodas é chamado sem intervalo de datas, então deve retornar todas as listas paginadas.")
    void dadoSemIntervaloDeDatas_QuandoBuscarTodas_EntaoDeveRetornarTodasAsListasPaginadas() {
        Pageable pageable = PageRequest.of(0, 10);
        Lista lista = TestUtils.getMockLista();
        Page<Lista> listaPage = new PageImpl<>(List.of(lista), pageable, 1);

        when(listaRepository.findAll(pageable)).thenReturn(listaPage);
        when(listaConverter.toSummaryDTO(lista)).thenReturn(listaResumoDTO);

        Page<ListaResumoDTO> result = listaService.buscarTodas(null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(listaRepository, times(1)).findAll(pageable);
        verify(listaRepository, never()).findByDataCriacaoBetween(any(), any(), eq(pageable));

    }

    @Test
    @DisplayName("Quando buscarListaPorId é chamado com um ID válido, então deve retornar a lista ordenada por prioridade.")
    void dadoUmListaIdValido_QuandoBuscarListaPorId_EntaoDeveRetornarListaOrdenadaPorPrioridade() {
        when(listaRepository.findById(listaId)).thenReturn(Optional.of(lista));
        when(listaConverter.toDTO(lista)).thenReturn(listaDTO);

        ListaDTO result = listaService.buscarListaPorId(listaId);

        assertNotNull(result);
        assertEquals(1, result.getItens().size());
        assertEquals(listaDTO.getNome(), result.getNome());

        verify(listaRepository, times(1)).findById(listaId);
        verify(listaConverter, times(1)).toDTO(lista);
    }

    @Test
    @DisplayName("Quando buscarListaPorId é chamado com um ID inválido, então deve lançar ListaNaoEncontradaException.")
    void dadoUmListaIdInvalido_QuandoBuscarListaPorId_EntaoDeveLancarListaNaoEncontradaException() {
        when(listaRepository.findById(listaIdInvalido)).thenReturn(Optional.empty());

        ListaNaoEncontradaException exception = assertThrows(ListaNaoEncontradaException.class, () -> {
            listaService.buscarListaPorId(listaIdInvalido);
        });

        assertEquals(MSG_LISTA_NAO_ENCONTRADA, exception.getMessage());

        verify(listaRepository, times(1)).findById(listaIdInvalido);
        verify(listaConverter, never()).toDTO(any(Lista.class));
    }

    @Test
    @DisplayName("Quando deletarLista é chamado com um ID válido, então deve deletar a lista com sucesso.")
    void dadoUmListaIdValido_QuandoDeletarLista_EntaoDeveDeletarListaComSucesso() {
        when(listaRepository.findById(listaId)).thenReturn(Optional.of(lista));

        listaService.deletarLista(listaId);

        verify(listaRepository, times(1)).findById(listaId);
        verify(listaRepository, times(1)).delete(lista);
    }

    @Test
    @DisplayName("Quando deletarLista é chamado com um ID inválido, então deve lançar ListaNaoEncontradaException.")
    void dadoUmListaIdInvalido_QuandoDeletarLista_EntaoDeveLancarListaNaoEncontradaException() {
        when(listaRepository.findById(listaIdInvalido)).thenReturn(Optional.empty());

        ListaNaoEncontradaException exception = assertThrows(ListaNaoEncontradaException.class, () -> {
            listaService.deletarLista(listaIdInvalido);
        });

        assertEquals(MSG_LISTA_NAO_ENCONTRADA, exception.getMessage());

        verify(listaRepository, times(1)).findById(listaIdInvalido);
        verify(listaRepository, never()).delete(lista);
    }
}