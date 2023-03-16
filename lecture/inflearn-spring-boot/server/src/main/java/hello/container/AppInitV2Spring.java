package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/*
AnnotationConfigWebApplicationContext: 스프링 컨테이너

스프링 컨테이너에 스프링 관련 설정을 등록 (HelloConfig)

DispatcherServlet 을 추가하고 매핑정보 추가
 */

public class AppInitV2Spring implements AppInit {

  @Override
  public void onStartup(ServletContext servletContext) {
    System.out.println("AppInitV2Spring.onStartup");

    AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    applicationContext.register(HelloConfig.class);

    DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
    servletContext
      .addServlet("dispatcherV2", dispatcherServlet)
      .addMapping("/spring/*");
  }
}
