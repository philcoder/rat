package com.philipp.manager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

abstract class AbstractService<ENTITY, REPO extends CrudRepository<ENTITY, Integer>> {

	@Autowired
	protected REPO repository;

	public ENTITY save(ENTITY entity) {
		return repository.save(entity);
	}

	public Optional<ENTITY> findById(Integer id) {
		return repository.findById(id);
	}

	public boolean existsById(Integer id) {
		return repository.existsById(id);
	}

	public Iterable<ENTITY> findAll() {
		return repository.findAll();
	}

	public Iterable<ENTITY> findAllById(Iterable<Integer> ids) {
		return repository.findAllById(ids);
	}

	public long count() {
		return repository.count();
	}

	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	public void delete(ENTITY entity) {
		repository.delete(entity);
	}

	public void deleteAll() {
		repository.deleteAll();
	}
}
