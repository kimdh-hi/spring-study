package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/*
spring-web 라이브러리 META-INF/services/jakarta.servlet.ServletContainerInitializer 확인
SpringServletContainerInitializer 클래스를 지정하고 있고 @HandlesTypes 로 WebApplicationInitializer 가 지정되어 있음
따라서 WebApplicationInitializer 를 구현하는 타입의 클래스를 이용해서 애플리케이션 초기화를 수행
 */
public class AppInitV3SpringMvc implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    System.out.println("AppInitV3SpringMvc.onStartup");

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(HelloConfig.class);

    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
    servletContext
      .addServlet("dispatcherV3", dispatcherServlet)
      .addMapping("/");
  }
}
