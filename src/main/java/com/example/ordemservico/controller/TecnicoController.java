package com.example.ordemservico.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ordemservico.entity.Tecnico;
import com.example.ordemservico.service.TecnicoService;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {
	private final TecnicoService tecnicoService;

	public TecnicoController(TecnicoService tecnicoService) {
		this.tecnicoService = tecnicoService;
	}

	@GetMapping
	public List<Tecnico> listar() {
		return tecnicoService.listarTodos();
	}

// GET /tecnicos/1
	@GetMapping("/{id}")
	public Tecnico buscar(@PathVariable Long id) {
		return tecnicoService.buscarPorId(id);
	}

// POST /tecnicos
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Tecnico cadastrar(@RequestBody Tecnico tecnico) {
		return tecnicoService.salvar(tecnico);
	}

//PUT /tecnicos/1
	@PutMapping("/{id}")
	public Tecnico atualizar(@PathVariable Long id,

			@RequestBody Tecnico tecnico) {

		return tecnicoService.atualizar(id, tecnico);
	}

//DELETE /tecnicos/1
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		tecnicoService.excluir(id);
	}
}