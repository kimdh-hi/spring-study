package com.toy.hibernatelisteners.config

import com.toy.hibernatelisteners.domain.listeners.EntityChangeLoggingListener
import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.EventType
import org.hibernate.internal.SessionFactoryImpl
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class JpaConfig(
  private val entityManagerFactory: EntityManagerFactory,
  private val entityChangeLoggingListener: EntityChangeLoggingListener
): InitializingBean {

  override fun afterPropertiesSet() {
    val sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl::class.java)
    val entityListenerRegistry = sessionFactory.serviceRegistry.getService(EventListenerRegistry::class.java)

    entityListenerRegistry.appendListeners(EventType.POST_INSERT,
      entityChangeLoggingListener
    )

    entityListenerRegistry.appendListeners(EventType.POST_UPDATE,
      entityChangeLoggingListener
    )

    entityListenerRegistry.appendListeners(EventType.POST_DELETE,
      entityChangeLoggingListener
    )
  }
}