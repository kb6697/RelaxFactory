package rxf;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface AppFactory extends RequestFactory {
	GreetingContext greeting();
}