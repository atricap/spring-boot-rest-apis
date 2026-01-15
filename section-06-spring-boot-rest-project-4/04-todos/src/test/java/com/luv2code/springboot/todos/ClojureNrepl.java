package com.luv2code.springboot.todos;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.PersistentArrayMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
class ClojureNrepl {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private RestTestClient restTestClient;

	private IPersistentMap getArgsMap() {
		return PersistentArrayMap.createAsIfByAssoc(new Object[] {
				Keyword.intern("context"), context,
				Keyword.intern("rest-client"), restTestClient
		});
	}

	@Test
	void run_clojure_nrepl() {
		IFn require = Clojure.var("clojure.core", "require");
		require.invoke(Clojure.read("todos.nrepl"));
		IFn main = Clojure.var("todos.nrepl", "-main");
		main.invoke(getArgsMap());

		assertTrue(true);
	}
}
