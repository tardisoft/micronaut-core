package io.micronaut.configuration.jdbc.hikari


import spock.lang.Specification

class DatasourceFactorySpec extends Specification {

    def "wire class with constructor"() {
        expect:
        new DatasourceFactory()
    }
}
