package com.luv2code.springboot.todos;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.RT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TodosApplication {

	public static void main(String[] args) {
		System.out.println("# TodosApplication.main()");

		// Devtools restart the main thread, can be seen with this:
		new RuntimeException("my stack").printStackTrace(System.err);

		IFn inc = Clojure.var("clojure.core", "inc");
		long result = (Long) inc.invoke(42);
		System.out.println("# In Java: (inc 42) ;;=> " + result);

		IFn require = Clojure.var("clojure.core", "require");
		require.invoke(Clojure.read("todos.main"));
		IFn clojureMain = Clojure.var("todos.main", "-main");
		clojureMain.applyTo(RT.seq(args));

		// App will start up from Clojure, so don't here
		// ConfigurableApplicationContext ctx = run(args);

		System.out.println("# TodosApplication.main() RETURN");
	}

	public static ConfigurableApplicationContext run(String[] args) {
		return SpringApplication.run(TodosApplication.class, args);
	}

}
