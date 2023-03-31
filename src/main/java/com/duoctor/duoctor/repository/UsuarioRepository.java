package com.duoctor.duoctor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.duoctor.duoctor.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public Optional<Usuario> findByUsuario(String Usuario);
	public List <Usuario> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);
}
