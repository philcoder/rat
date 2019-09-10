package com.philipp.manager.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

abstract class AbstractRepository {
	@Autowired
	private TestEntityManager entityManager;

	public <E> E persist(E entity) {
		entity = entityManager.merge(entity);
		entityManager.flush();
		return entity;
	}
}
