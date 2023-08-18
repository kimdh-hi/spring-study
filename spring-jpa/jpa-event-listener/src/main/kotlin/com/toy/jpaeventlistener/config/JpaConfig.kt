package com.toy.jpaeventlistener.config

import com.toy.jpaeventlistener.listener.BSpaceJpaListener
import jakarta.persistence.EntityManagerFactory
import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.EventType
import org.hibernate.internal.SessionFactoryImpl
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration

@Configuration
class JpaConfig(
  private val entityManagerFactory: EntityManagerFactory,
  private val bSpaceJpaListener: BSpaceJpaListener,
): InitializingBean {

  override fun afterPropertiesSet() {
    val sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl::class.java)
    val eventListenerRegistry = sessionFactory.serviceRegistry.getService(
      EventListenerRegistry::class.java
    )

    eventListenerRegistry.appendListeners(EventType.POST_DELETE, bSpaceJpaListener)
  }
}