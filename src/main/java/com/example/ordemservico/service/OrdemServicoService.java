package com.example.ordemservico.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ordemservico.entity.Cliente;
import com.example.ordemservico.entity.OrdemServico;
import com.example.ordemservico.entity.Tecnico;
import com.example.ordemservico.enums.Prioridade;
import com.example.ordemservico.enums.StatusOS;
import com.example.ordemservico.repository.OrdemServicoRepository;
@Service
public class OrdemServicoService {
	
	private final OrdemServicoRepository ordemServicoRepository;
	private final ClienteService clienteService;
	private final TecnicoService tecnicoService;
	
	public OrdemServicoService(
			OrdemServicoRepository ordemServicoRepository,
			ClienteService clienteService,
			TecnicoService tecnicoService) {
	this.ordemServicoRepository = ordemServicoRepository;
	this.clienteService = clienteService;
	this.tecnicoService = tecnicoService;
	}
	
	// CONSULTAS (sem regra de negócio)
	public List<OrdemServico> listarTodas() {
		return ordemServicoRepository.findAll();
	}
	
	public List<OrdemServico> listarPorStatus(StatusOS status) {
		return ordemServicoRepository.findByStatus(status);
	}
	
	public List<OrdemServico> listarPorCliente(Long clienteId) {
		return ordemServicoRepository.findByClienteId(clienteId);
	}
	
	//CONSUTTA POR PRIORIDADE
	public List<OrdemServico> listarPorPrioridade(Prioridade prioridade){
		return ordemServicoRepository.findByPrioridade(prioridade);
	}
	
	public OrdemServico buscarPorId(Long id) {
		return ordemServicoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND,"Ordem de serviço não encontrada: " + id));
	}
	
	//R2
	private Cliente validarClienteCadastrado(OrdemServico os) {
		if (os.getCliente() == null || os.getCliente().getId() == null) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Informe o id do cliente");
		}
		return clienteService.buscarPorId(os.getCliente().getId());
	}
	
	//R3
	public OrdemServico abrir(OrdemServico os) {
		Cliente cliente = validarClienteCadastrado(os); // usa a Regra 2
		os.setCliente(cliente);
		os.setDataAbertura(LocalDateTime.now());
		os.setStatus(StatusOS.ABERTA);
		return ordemServicoRepository.save(os);
	}
	
	//R4
	private void validarOsNaoFinalizada(OrdemServico os) {
		if (os.getStatus() == StatusOS.CONCLUIDA
		|| os.getStatus() == StatusOS.CANCELADA) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
			"A OS " + os.getId() + " já está " + os.getStatus());

		}
	}
	
	//R5
	public OrdemServico atribuirTecnico(Long osId, Long tecnicoId) {
		OrdemServico os = buscarPorId(osId);
		validarOsNaoFinalizada(os); // usa a Regra 4
		Tecnico tecnico = tecnicoService.buscarPorId(tecnicoId);
		os.setTecnico(tecnico);
		os.setStatus(StatusOS.EM_ANDAMENTO);
		return ordemServicoRepository.save(os);
		}
	
	//R6
	private void validarTecnicoAtribuido(OrdemServico os) {
		if (os.getTecnico() == null) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,

		"Atribua um técnico antes de concluir");

		}
		}
	
	//R7
	public OrdemServico concluir(Long osId, BigDecimal valor) {
		OrdemServico os = buscarPorId(osId);
		validarOsNaoFinalizada(os); // usa a Regra 4
		validarTecnicoAtribuido(os); // usa a Regra 6
		if(valor==null ||valor.compareTo(BigDecimal.ZERO) <= 0 ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"para encerrar, o valor deve ser maior que 0");
		}
		os.setValor(valor);
		os.setDataFechamento(LocalDateTime.now());
		os.setStatus(StatusOS.CONCLUIDA);
		return ordemServicoRepository.save(os);
		}
	
	//R8
	public OrdemServico cancelar(Long osId) {
		OrdemServico os = buscarPorId(osId);
		validarOsNaoFinalizada(os); // usa a Regra 4
		os.setDataFechamento(LocalDateTime.now());
		os.setStatus(StatusOS.CANCELADA);
		return ordemServicoRepository.save(os);
		}
		 //
}
