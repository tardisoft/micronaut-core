package io.micronaut.configuration.jdbc.hikari.metadata

import spock.lang.Shared
import spock.lang.Specification

class AbstractDataSourcePoolMetadataSpec extends Specification {

    @Shared
    def metricNames = [
            'jdbc.connections.active',
            'jdbc.connections.max',
            'jdbc.connections.min',
            'jdbc.connections.pending',
            'jdbc.connections.total',
            'jdbc.connections.usage'
    ]

}
