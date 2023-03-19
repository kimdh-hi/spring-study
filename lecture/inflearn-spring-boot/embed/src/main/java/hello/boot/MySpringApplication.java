package hello.boot;

import java.util.List;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {

  public static void run(Class configClass, String[] args) {
    System.out.println("MySpringApplication.run args=" + List.of(args));

    Connector connector = new Connector();
    connector.setPort(8080);
    Tomcat tomcat = new Tomcat();
    tomcat.setConnector(connector);

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(configClass);

    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

    Context context = tomcat.addContext("", "/");
    tomcat.addServlet("", "dispatcher", dispatcherServlet);
    context.addServletMappingDecoded("/", "dispatcher");

    try {
      tomcat.start();
    } catch (LifecycleException e) {
      throw new RuntimeException(e);
    }
  }
}
