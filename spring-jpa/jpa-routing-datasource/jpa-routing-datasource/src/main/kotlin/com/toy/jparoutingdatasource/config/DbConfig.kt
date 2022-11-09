package com.toy.jparoutingdatasource.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class DbConfig(
  private val jpaProperties: JpaProperties
) {

  /**
   * master / slave DataSource 설정
   */
  @Bean(name = ["masterDataSource"])
  @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
  fun masterDataSource(): HikariDataSource = DataSourceBuilder.create()
    .type(HikariDataSource::class.java)
    .build()

  @Bean(name = ["slaveDataSource"])
  @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
  fun slaveDataSource(): HikariDataSource = DataSourceBuilder.create()
    .type(HikariDataSource::class.java)
    .build()
    .apply { isReadOnly = true }

  /**
   * routineDataSource 설정
   */
  @Bean
  @ConditionalOnBean(name = ["masterDataSource", "slaveDataSource"])
  fun routingDataSource(
    @Qualifier("masterDataSource") masterDataSource: DataSource,
    @Qualifier("slaveDataSource") slaveDataSource: DataSource
  ): DataSource {
    val routingDataSource = RoutingDataSource()
    val dataSources: Map<Any, Any> = mapOf(
      "master" to masterDataSource,
      "slave" to slaveDataSource
    )
    routingDataSource.setTargetDataSources(dataSources)
    routingDataSource.setDefaultTargetDataSource(masterDataSource)
    return routingDataSource
  }

  /**
   * 실제 쿼리 요청시까지 DataSource 에서 Connection 획득을 미루기 위한 설정
   * 실제 쿼리가 나가기 전까지 Proxy Connection 을 사용하도록?
   */
  @Primary
  @Bean(name = ["currentDataSource"])
  @ConditionalOnBean(name = ["routingDataSource"])
  fun currentDataSource(routingDataSource: DataSource) = LazyConnectionDataSourceProxy(routingDataSource)


  @Bean
  fun entityManagerFactory(currentDataSource: DataSource): LocalContainerEntityManagerFactoryBean {
    val entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties)
    return entityManagerFactoryBuilder.dataSource(currentDataSource).packages("com.toy.jparoutingdatasource.domain").build()
  }

  private fun createEntityManagerFactoryBuilder(jpaProperties: JpaProperties): EntityManagerFactoryBuilder {
    val vendorAdapter: AbstractJpaVendorAdapter = HibernateJpaVendorAdapter()
    return EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.properties, null)
  }

  @Primary
  @Bean
  fun entityManagerFactory(currentDataSource: DataSource, env: Environment): LocalContainerEntityManagerFactoryBean {
    val em = LocalContainerEntityManagerFactoryBean()
    em.jpaVendorAdapter = HibernateJpaVendorAdapter()
    em.dataSource = currentDataSource
    em.setPackagesToScan("com.toy.jparoutingdatasource.domain")
    val properties: MutableMap<String, Any> = mutableMapOf()
    properties["hibernate.physical_naming_strategy"] = SpringPhysicalNamingStrategy::class.java.name
    properties["hibernate.implicit_naming_strategy"] = SpringImplicitNamingStrategy::class.java.name
    properties["hibernate.hbm2ddl.auto"] = env.getProperty("spring.jpa.hibernate.ddl-auto")!!
    em.setJpaPropertyMap(properties)
    return em
  }

  @Primary
  @Bean
  fun transactionManager(
    entityManagerFactory: EntityManagerFactory
  ): PlatformTransactionManager? {
    val transactionManager = JpaTransactionManager()
    transactionManager.entityManagerFactory = entityManagerFactory
    return transactionManager
  }
}

class RoutingDataSource: AbstractRoutingDataSource() {
  override fun determineCurrentLookupKey() = when {
    TransactionSynchronizationManager.isCurrentTransactionReadOnly() -> "slave"
    else -> "master"
  }
}
