
package com.leyton.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.leyton.util.properties.DynomiteTopologyProperties;
import com.netflix.dyno.connectionpool.Host;
import com.netflix.dyno.connectionpool.Host.Status;

import io.vavr.control.Try;

public class DynomiteUtil {

    private DynomiteUtil() {
    }

    public static String getJsonTopology(List<DynomiteNode> nodes) {
        StringBuilder json = new StringBuilder("[");
        int count = 0;
        for (DynomiteNode node : nodes) {
            json.append("{\"token\": \"" + node.getToken() + "\", \"hostname\": \""
                    + node.getHostname() + "\", \"ip\": \"" + node.getIpAddress()
                    + "\", \"port\": \"" + node.getPort() + "\", \"zone\": \"" + node.getRack()
                    + "\", \"dc\": \"" + node.getDatacenter() + "\"}");
            count++;
            if (count < nodes.size())
                json.append(", ");
        }
        json.append("]");
        return json.toString();
    }

    public static List<Host> getHost(List<DynomiteNode> nodes) {
        return Try.of(() -> nodes.stream()
                .map(node -> new Host(node.getHostname(), node.getIpAddress(), node.getRack(),
                        node.getStatus()))
                .collect(Collectors.toSet()).stream().collect(Collectors.toList()))
                .getOrElse(new ArrayList<>());
    }

    public static List<DynomiteNode> getNodes(DynomiteTopologyProperties properties) {
        return Try.of(() -> {
            List<DynomiteNode> result = new ArrayList<>();
            properties.getDatacenter()
                    .forEach(
                            (datacenter,
                                    racks) -> racks.forEach(rack -> rack.getNodes().forEach((name,
                                            node) -> result.add(new DynomiteNode(node.getToken(),
                                                    node.getHost(), node.getIp(), node.getPort(),
                                                    rack.getRack(), datacenter, Status.Up)))));
            return result;
        }).getOrElse(new ArrayList<>());
    }
}
