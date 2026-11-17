package com.example.ordemservico.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ordemservico.entity.Cliente;
import com.example.ordemservico.entity.OrdemServico;
import com.example.ordemservico.repository.ClienteRepository;
import com.example.ordemservico.repository.OrdemServicoRepository;

@Service
public class ClienteService {
	private final ClienteRepository clienteRepository;
	private final OrdemServicoRepository ordemServicoRepository;
	
	public ClienteService(ClienteRepository clienteRepository,
			OrdemServicoRepository ordemServicoRepository) {
		this.clienteRepository = clienteRepository;
		this.ordemServicoRepository = ordemServicoRepository; 
	}
	

	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + id));
	}

// REGRA 1: não pode existir dois clientes com o mesmo CPF
	public Cliente salvar(Cliente cliente) {
		if (clienteRepository.existsByCpf(cliente.getCpf())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe cliente com este CPF");
		}
		return clienteRepository.save(cliente);
	}

	public Cliente atualizar(Long id, Cliente dados) {
		Cliente cliente = buscarPorId(id);
		cliente.setNome(dados.getNome());
		cliente.setTelefone(dados.getTelefone());
		cliente.setEmail(dados.getEmail());
		return clienteRepository.save(cliente);
	}

	public void excluir(Long id) {
		Cliente cliente = buscarPorId(id);
		
		if(ordemServicoRepository.existsByClienteId(id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Nao da pra excluir, pois este cliente esta vinculado a uma OS");
		}
		clienteRepository.delete(cliente);
	}
	
	
	
	
}