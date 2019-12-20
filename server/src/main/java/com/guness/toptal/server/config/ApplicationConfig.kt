package com.guness.toptal.server.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories("com.guness.toptal.server.repositories")
@EntityScan("com.guness.toptal.protocol.dto", "com.guness.toptal.server.model")
class ApplicationConfiguration {

    @Bean
    fun dataSource(): DataSource {
        val builder = EmbeddedDatabaseBuilder()
        return builder.setType(EmbeddedDatabaseType.HSQL).build()
    }
/*
    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource()
        em.setPackagesToScan("com.guness.toptal.protocol.dto")
        val vendorAdapter: JpaVendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        //em.setJpaProperties(additionalProperties())

        return em
    }
    */
}