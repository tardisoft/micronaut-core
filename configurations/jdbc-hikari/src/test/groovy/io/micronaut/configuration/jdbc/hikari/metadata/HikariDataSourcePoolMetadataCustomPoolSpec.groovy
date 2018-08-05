package io.micronaut.configuration.jdbc.hikari.metadata


import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.MapPropertySource
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static io.micronaut.configuration.metrics.micrometer.MeterRegistryFactory.MICRONAUT_METRICS_BINDERS
import static io.micronaut.configuration.metrics.micrometer.MeterRegistryFactory.MICRONAUT_METRICS_ENABLED

class HikariDataSourcePoolMetadataCustomPoolSpec extends AbstractDataSourcePoolMetadataSpec {

    @Shared
    @AutoCleanup
    ApplicationContext context = ApplicationContext.run(MapPropertySource.of(
            'HikariDataSourcePoolMetadataCustomPoolSpec',
            ['datasources.default.poolName'               : 'DefaultPool',
             'datasources.foo.poolName'                   : 'FooPool',
             'endpoints.metrics.sensitive'                : false,
             (MICRONAUT_METRICS_ENABLED)                  : true,
             (MICRONAUT_METRICS_BINDERS + ".jdbc.enabled"): true]
    ), "HikariDataSourcePoolMetadataCustomPoolSpec")

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = context.getBean(EmbeddedServer).start()

    @Shared
    @AutoCleanup
    RxHttpClient httpClient = context.createBean(RxHttpClient, embeddedServer.getURL())

    @Unroll
    def "check metrics endpoint for datasource metrics for #metric"() {
        when:
        def response = httpClient.exchange("/metrics", Map).blockingFirst()
        Map result = response.body()

        then:
        response.code() == HttpStatus.OK.code
        result.names.contains(metric)

        where:
        metric << metricNames
    }

    @Unroll
    def "check metrics endpoint for datasource metrics #metric"() {
        when:
        def response = httpClient.exchange("/metrics/$metric", Map).blockingFirst()
        Map result = (Map) response.body()

        then:
        response.code() == HttpStatus.OK.code
        result.name == metric

        when:
        def tags = result.availableTags.findAll {
            it.tag == 'pool'
        }

        then:
        tags

        and:
        tags.each { Map tag ->
            assert tag.values.contains('DefaultPool')
            assert tag.values.contains('FooPool')
        }

        where:
        metric << metricNames
    }


}
