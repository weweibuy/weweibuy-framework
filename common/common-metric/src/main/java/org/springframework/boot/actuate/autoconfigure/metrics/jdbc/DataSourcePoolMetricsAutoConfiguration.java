/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.metrics.jdbc;

import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.log.LogMessage;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for metrics on all available
 * {@link DataSource datasources}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({ MetricsAutoConfiguration.class, DataSourceAutoConfiguration.class,
		SimpleMetricsExportAutoConfiguration.class })
@ConditionalOnClass({ DataSource.class, MeterRegistry.class })
@ConditionalOnBean({ DataSource.class, MeterRegistry.class })
public class DataSourcePoolMetricsAutoConfiguration {


	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(HikariDataSource.class)
	static class HikariDataSourceMetricsConfiguration {

		private static final Log logger = LogFactory.getLog(HikariDataSourceMetricsConfiguration.class);

		private final MeterRegistry registry;

		HikariDataSourceMetricsConfiguration(MeterRegistry registry) {
			this.registry = registry;
		}

		@Autowired
		void bindMetricsRegistryToHikariDataSources(Collection<DataSource> dataSources) {
			for (DataSource dataSource : dataSources) {
				HikariDataSource hikariDataSource = DataSourceUnwrapper.unwrap(dataSource, HikariConfigMXBean.class,
						HikariDataSource.class);
				if (hikariDataSource != null) {
					bindMetricsRegistryToHikariDataSource(hikariDataSource);
				}
			}
		}

		private void bindMetricsRegistryToHikariDataSource(HikariDataSource hikari) {
			if (hikari.getMetricRegistry() == null && hikari.getMetricsTrackerFactory() == null) {
				try {
					hikari.setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory(this.registry));
				}
				catch (Exception ex) {
					logger.warn(LogMessage.format("Failed to bind Hikari metrics: %s", ex.getMessage()));
				}
			}
		}

	}

}
