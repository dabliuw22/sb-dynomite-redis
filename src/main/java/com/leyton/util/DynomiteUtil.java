
package com.leyton.util;

import java.util.List;
import java.util.stream.Collectors;

import com.netflix.dyno.connectionpool.Host;

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
        return nodes.stream()
                .map(node -> new Host(node.getHostname(), node.getIpAddress(), node.getRack(),
                        node.getStatus()))
                .collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }
}
