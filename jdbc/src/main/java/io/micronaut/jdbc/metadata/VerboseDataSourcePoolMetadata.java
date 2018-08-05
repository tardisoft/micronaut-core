/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.jdbc.metadata;

/**
 * Provides access meta-data that is commonly available from most pooled
 * {@link javax.sql.DataSource} implementations.
 *
 * @param <T> datasource {@link javax.sql.DataSource}
 * @author Christian Oestreich
 * @since 2.0.0
 */
public interface VerboseDataSourcePoolMetadata<T extends javax.sql.DataSource> extends DataSourcePoolMetadata<T> {

    /**
     * Provide method to get the configured pool name.
     *
     * @return Name of pool
     */
    String getPoolName();

    /**
     * Get the number of pending connections
     *
     * @return Value of pending
     */
    Integer getPending();

    /**
     * Get the number of total connections
     *
     * @return Value of total
     */
    Integer getTotal();

}
