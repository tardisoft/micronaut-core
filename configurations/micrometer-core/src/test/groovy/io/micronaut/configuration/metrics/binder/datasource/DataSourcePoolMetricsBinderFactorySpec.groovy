package io.micronaut.configuration.metrics.binder.datasource

import io.micronaut.jdbc.metadata.DataSourcePoolMetadata
import spock.lang.Specification

import javax.sql.DataSource

class DataSourcePoolMetricsBinderFactorySpec extends Specification {

    def "test getting the beans manually"() {
        given:
        def dataSource1 = Mock(DataSourcePoolMetadata)
        def dataSource2 = Mock(DataSourcePoolMetadata)

        when:
        def binder = new DataSourcePoolMetricsBinderFactory()
        binder.dataSourceMeterBinder("default", dataSource1)
        binder.dataSourceMeterBinder("first", dataSource2)

        then:
        binder
        1 * dataSource2.getDataSource() >> Mock(DataSource)
        1 * dataSource1.getDataSource() >> Mock(DataSource)
        0 * _._

    }
}
