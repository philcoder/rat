package com.philipp.manager.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public final class RandomSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	public RandomSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>(super.computeTestMethods());
		Collections.shuffle(methods);
		return methods;
	}
}
