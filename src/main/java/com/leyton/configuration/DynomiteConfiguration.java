
package com.leyton.configuration;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.leyton.util.DynomiteNode;
import com.leyton.util.DynomiteUtil;
import com.leyton.util.properties.DynomiteTopologyProperties;
import com.netflix.dyno.connectionpool.ConnectionPoolConfiguration.LoadBalancingStrategy;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.RetryPolicy.RetryPolicyFactory;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.jedis.DynoJedisClient;

@Configuration
public class DynomiteConfiguration {

    @Value(
            value = "${spring.application.name}")
    private String applicationName;

    @Value(
            value = "${dynomite.cluster.name}")
    private String clusterName;

    @Value(
            value = "${dynomite.client.time-out}")
    private int timeOut;

    @Value(
            value = "${dynomite.client.max-connections}")
    private int maxConnections;

    @Value(
            value = "${dynomite.client.retry-factor}")
    private int retryFactor;

    @Bean
    @ConfigurationProperties(
            prefix = "dynomite.topology")
    public DynomiteTopologyProperties dynomiteTopologyProperties() {
        return new DynomiteTopologyProperties();
    }

    @Bean
    public List<DynomiteNode> dynomiteNodes(DynomiteTopologyProperties properties) {
        return DynomiteUtil.getNodes(properties);
    }

    @Bean
    public TokenMapSupplier tokenMapSupplier(List<DynomiteNode> nodes) {
        return new AbstractTokenMapSupplier() {
            @Override
            public String getTopologyJsonPayload(String hostname) {
                return DynomiteUtil.getJsonTopology(nodes);
            }

            @Override
            public String getTopologyJsonPayload(Set<Host> activeHosts) {
                return DynomiteUtil.getJsonTopology(nodes);
            }
        };
    }

    @Bean
    public HostSupplier hostSupplier(List<DynomiteNode> nodes) {
        return () -> DynomiteUtil.getHost(nodes);
    }

    @Bean
    public RetryPolicyFactory retryPolicyFactory() {
        return new RetryNTimes.RetryFactory(retryFactor, true);
    }

    @Bean
    public ConnectionPoolConfigurationImpl connectionPoolConfiguration(
            TokenMapSupplier tokenMapSupplier, RetryPolicyFactory retryPolicyFactory) {
        return new ConnectionPoolConfigurationImpl(applicationName)
                .withTokenSupplier(tokenMapSupplier)
                .setLoadBalancingStrategy(LoadBalancingStrategy.RoundRobin)
                .setRetryPolicyFactory(retryPolicyFactory).setConnectTimeout(timeOut)
                .setMaxConnsPerHost(maxConnections);
    }

    @Bean
    public DynoJedisClient dynoJedisClient(HostSupplier hostSupplier,
            ConnectionPoolConfigurationImpl connectionPoolConfiguration) {
        return new DynoJedisClient.Builder().withApplicationName(applicationName)
                .withDynomiteClusterName(clusterName).withHostSupplier(hostSupplier)
                .withCPConfig(connectionPoolConfiguration).build();
    }
}
