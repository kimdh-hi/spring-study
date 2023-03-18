package hello.spring;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class EmbedTomcatSpringMain {

  public static void main(String[] args) throws LifecycleException {
    System.out.println("EmbedTomcatSpringMain.main");

    Connector connector = new Connector();
    connector.setPort(8080);
    Tomcat tomcat = new Tomcat();
    tomcat.setConnector(connector);

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(HelloConfig.class);

    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);

    Context context = tomcat.addContext("", "/");
    tomcat.addServlet("", "dispatcher", dispatcherServlet);
    context.addServletMappingDecoded("/", "dispatcher");

    tomcat.start();
  }
}
