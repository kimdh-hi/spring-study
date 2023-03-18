package hello;

import hello.servlet.HelloServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class EmbedTomcatServletMain {

  public static void main(String[] args) throws LifecycleException {
    System.out.println("EmbedTomcatServletMain.main");

    Connector connector = new Connector();
    connector.setPort(8080);
    Tomcat tomcat = new Tomcat();
    tomcat.setConnector(connector);

    Context context = tomcat.addContext("", "/");
    tomcat.addServlet("", "helloServlet", new HelloServlet());
    context.addServletMappingDecoded("/hello-servlet", "helloServlet");

    tomcat.start();
  }
}
