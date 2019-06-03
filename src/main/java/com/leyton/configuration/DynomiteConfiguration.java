
package com.leyton.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.leyton.util.DynomiteNode;
import com.leyton.util.DynomiteUtil;
import com.netflix.dyno.connectionpool.ConnectionPoolConfiguration.LoadBalancingStrategy;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.RetryPolicy.RetryPolicyFactory;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.connectionpool.impl.RetryNTimes;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.jedis.DynoJedisClient;

@Configuration
public class DynomiteConfiguration {

    private static final String LOCALHOST = "127.0.0.1";

    private static final String IP = "127.0.0.1";

    private static final String DATACENTER_ONE = "dc1";

    private static final String RACK_ONE = "rack1";

    private static final String RACK_TWO = "rack2";

    private static final int TIME_OUT = 3000;

    private static final int MAX_CONNECTION_PER_HOST = 5;

    private static final int RETRY_FACTOR = 1;

    @Value(
            value = "${spring.application.name}")
    private String applicationName;

    @Bean
    public List<DynomiteNode> dynomiteNodes() {
        return Arrays.asList(
                new DynomiteNode("0", LOCALHOST, IP, 8379, RACK_ONE, DATACENTER_ONE, Status.Up),
                new DynomiteNode("2147483647", LOCALHOST, IP, 8380, RACK_ONE, DATACENTER_ONE,
                        Status.Up),
                new DynomiteNode("0", LOCALHOST, IP, 8381, RACK_TWO, DATACENTER_ONE, Status.Up),
                new DynomiteNode("2147483647", LOCALHOST, IP, 8382, RACK_TWO, DATACENTER_ONE,
                        Status.Up));
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
        return new RetryNTimes.RetryFactory(RETRY_FACTOR, true);
    }

    @Bean
    public ConnectionPoolConfigurationImpl connectionPoolConfiguration(
            TokenMapSupplier tokenMapSupplier, RetryPolicyFactory retryPolicyFactory) {
        return new ConnectionPoolConfigurationImpl(applicationName)
                .withTokenSupplier(tokenMapSupplier)
                .setLoadBalancingStrategy(LoadBalancingStrategy.RoundRobin)
                .setRetryPolicyFactory(retryPolicyFactory).setConnectTimeout(TIME_OUT)
                .setMaxConnsPerHost(MAX_CONNECTION_PER_HOST);
    }

    @Bean
    public DynoJedisClient dynoJedisClient(HostSupplier hostSupplier,
            ConnectionPoolConfigurationImpl connectionPoolConfiguration) {
        return new DynoJedisClient.Builder().withApplicationName(applicationName)
                .withHostSupplier(hostSupplier).withCPConfig(connectionPoolConfiguration).build();
    }
}
