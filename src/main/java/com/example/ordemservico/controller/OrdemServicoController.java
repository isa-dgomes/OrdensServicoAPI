package com.example.ordemservico.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ordemservico.entity.OrdemServico;
import com.example.ordemservico.enums.Prioridade;
import com.example.ordemservico.enums.StatusOS;
import com.example.ordemservico.service.OrdemServicoService;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {
	private final OrdemServicoService ordemServicoService;

	public OrdemServicoController(OrdemServicoService ordemServicoService) {
		this.ordemServicoService = ordemServicoService;
	}

//GET /ordens ou GET /ordens?status=ABERTA
	// /ordens?prioridade=ALTA
	@GetMapping
	public List<OrdemServico> listar(@RequestParam(required = false) StatusOS status,
			@RequestParam(required = false) Prioridade prioridade) {

		if (status != null) {
			return ordemServicoService.listarPorStatus(status);
		}
		
		if(prioridade != null) {
			return ordemServicoService.listarPorPrioridade(prioridade);
		}
		
		
		return ordemServicoService.listarTodas();
	}
	

//GET /ordens/1
	@GetMapping("/{id}")
	public OrdemServico buscar(@PathVariable Long id) {
		return ordemServicoService.buscarPorId(id);
	}

//GET /ordens/cliente/1
	@GetMapping("/cliente/{clienteId}")
	public List<OrdemServico> listarPorCliente(@PathVariable Long clienteId) {

		return ordemServicoService.listarPorCliente(clienteId);
	}

//POST /ordens
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServico abrir(@RequestBody OrdemServico os) {
		return ordemServicoService.abrir(os);
	}

	@PutMapping("/{id}/tecnico/{tecnicoId}")
	public OrdemServico atribuirTecnico(@PathVariable Long id, @PathVariable Long tecnicoId) {

		return ordemServicoService.atribuirTecnico(id, tecnicoId);
	}

// PUT /ordens/1/concluir?valor=150.00
	@PutMapping("/{id}/concluir")
	public OrdemServico concluir(@PathVariable Long id, @RequestParam(required = false) BigDecimal valor) {

		return ordemServicoService.concluir(id, valor);
	}

// PUT /ordens/1/cancelar
	@PutMapping("/{id}/cancelar")
	public OrdemServico cancelar(@PathVariable Long id) {
		return ordemServicoService.cancelar(id);
	}
}