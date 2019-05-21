
package com.leyton.configuration;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.dyno.connectionpool.ConnectionPoolConfiguration.LoadBalancingStrategy;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;
import com.netflix.dyno.connectionpool.HostSupplier;
import com.netflix.dyno.connectionpool.TokenMapSupplier;
import com.netflix.dyno.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.dyno.connectionpool.impl.lb.AbstractTokenMapSupplier;
import com.netflix.dyno.jedis.DynoJedisClient;

@Configuration
public class DynomiteConfiguration {

    private static final String TOPOLOGY = "[\n"
            + "    {\"token\": \"0\", \"hostname\": \"127.0.0.1\", \"ip\": \"127.0.0.1\", \"port\": \"8379\", \"zone\": \"rack1\",  \"dc\": \"dc1\"},\n"
            + "    {\"token\": \"2147483647\", \"hostname\": \"127.0.0.1\", \"ip\": \"127.0.0.1\", \"port\": \"8380\", \"zone\": \"rack1\",  \"dc\": \"dc1\"},\n"
            + "    {\"token\": \"0\", \"hostname\": \"127.0.0.1\", \"ip\": \"127.0.0.1\", \"port\": \"8381\", \"zone\": \"rack2\",  \"dc\": \"dc1\"},\n"
            + "    {\"token\": \"2147483647\", \"hostname\": \"127.0.0.1\", \"ip\": \"127.0.0.1\", \"port\": \"8382\", \"zone\": \"rack1\",  \"dc\": \"dc1\"},\n"
            + "]";

    private static final String LOCALHOST = "127.0.0.1";

    private static final String IP = "127.0.0.1";

    private static final String DATACENTER_ONE = "dc1";

    private static final String RACK_ONE = "rack1";

    private static final String RACK_TWO = "rack2";

    @Value(
            value = "${spring.application.name}")
    private String applicationName;

    @Bean
    public TokenMapSupplier tokenMapSupplier() {
        return new AbstractTokenMapSupplier() {
            @Override
            public String getTopologyJsonPayload(String hostname) {
                return TOPOLOGY;
            }

            @Override
            public String getTopologyJsonPayload(Set<Host> activeHosts) {
                return TOPOLOGY;
            }
        };
    }

    @Bean
    public HostSupplier hostSupplier() {
        return () -> Arrays.asList(
                new Host(LOCALHOST, IP, 8379, RACK_ONE, DATACENTER_ONE, Status.Up),
                new Host(LOCALHOST, IP, 8381, RACK_TWO, DATACENTER_ONE, Status.Up));
    }

    @Bean
    public ConnectionPoolConfigurationImpl
            connectionPoolConfiguration(TokenMapSupplier tokenMapSupplier) {
        return new ConnectionPoolConfigurationImpl(applicationName)
                .withTokenSupplier(tokenMapSupplier)
                .setLoadBalancingStrategy(LoadBalancingStrategy.RoundRobin).setConnectTimeout(5000);
    }

    @Bean
    public DynoJedisClient dynoJedisClient(HostSupplier hostSupplier,
            ConnectionPoolConfigurationImpl connectionPoolConfiguration) {
        return new DynoJedisClient.Builder().withApplicationName(applicationName)
                .withHostSupplier(hostSupplier).withCPConfig(connectionPoolConfiguration).build();
    }
}
