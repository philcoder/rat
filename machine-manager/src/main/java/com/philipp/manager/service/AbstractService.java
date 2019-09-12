package com.philipp.manager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

abstract class AbstractService<ENTITY, REPO extends CrudRepository<ENTITY, Integer>> {

	@Autowired
	protected REPO repository;

	public ENTITY save(ENTITY entity) {
		return repository.save(entity);
	}

	protected abstract ENTITY findById(Integer id) throws Exception;

	public boolean existsById(Integer id) {
		return repository.existsById(id);
	}

	public List<ENTITY> findAll() {
		List<ENTITY> list = new ArrayList<>();
		repository.findAll().forEach(e -> list.add(e));
		return list;
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
