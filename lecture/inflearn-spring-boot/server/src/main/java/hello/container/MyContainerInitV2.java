package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@HandlesTypes(AppInit.class)
public class MyContainerInitV2 implements ServletContainerInitializer {

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    System.out.println("MyContainerInitV2.onStartup");
    System.out.println("MyContainerInitV2 c = " + c);
    System.out.println("MyContainerInitV2 ctx = " + ctx);

    for (Class<?> aClass : c) {
      try {
        AppInit appInit = (AppInit) aClass.getDeclaredConstructor().newInstance();
        appInit.onStartup(ctx);
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
