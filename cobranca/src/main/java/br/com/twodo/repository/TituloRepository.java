package br.com.twodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.twodo.model.Titulo;

import java.util.List;

public interface TituloRepository extends JpaRepository<Titulo, Long>{

	List<Titulo> findByDescricaoContaining(String descricao);
	
}
