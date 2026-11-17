package com.example.ordemservico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ordemservico.entity.OrdemServico;
import com.example.ordemservico.enums.Prioridade;
import com.example.ordemservico.enums.StatusOS;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
// SELECT * FROM ordens_servico WHERE status = ?
	List<OrdemServico> findByStatus(StatusOS status);

// SELECT * FROM ordens_servico WHERE cliente_id = ?
	List<OrdemServico> findByClienteId(Long clienteId);
	
	List<OrdemServico> findByPrioridade(Prioridade prioridade);
	
	boolean existsByClienteId(Long clienteId);
}