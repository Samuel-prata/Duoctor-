package com.duoctor.duoctor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.duoctor.duoctor.model.Produtos;
import com.duoctor.duoctor.repository.CategoriaRepository;
import com.duoctor.duoctor.repository.ProdutosRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos") // produto = postagem do blogpessoal
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutosController {

	@Autowired
	private ProdutosRepository produtosRepository;

	// Injeção de Dependência do Recurso Categoria na Classe ProdutosController
	@Autowired
	private CategoriaRepository categoriaRepository;

	// Listar todos os produtos
	@GetMapping
	public ResponseEntity<List<Produtos>> getAll() {
		return ResponseEntity.ok(produtosRepository.findAll());
	}

	// Lista Produtos por ID
	@GetMapping("/{id}")
	public ResponseEntity<Produtos> getById(@PathVariable Long id) {
		return produtosRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// Lista Produtos por Nomes
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produtos>> getByTitle(@PathVariable String nome) {
		return ResponseEntity.ok(produtosRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	// É uma anotação para postar
	@PostMapping
	public ResponseEntity<Produtos> post(@Valid @RequestBody Produtos produtos) {
		if (categoriaRepository.existsById(produtos.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtosRepository.save(produtos));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

	}

	// É uma anotação para atualizar
	@PutMapping
	public ResponseEntity<Produtos> put(@Valid @RequestBody Produtos produtos) {
		if (produtosRepository.existsById(produtos.getId())) {
			if (categoriaRepository.existsById(produtos.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(produtosRepository.save(produtos));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

	}

	// Anotação de Status http
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produtos> produtos = produtosRepository.findById(id);

		if (produtos.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		produtosRepository.deleteById(id);
	}

}
