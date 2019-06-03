
package com.leyton.util;

import com.netflix.dyno.connectionpool.Host.Status;

public class DynomiteNode {

    private String token;

    private String hostname;

    private String ipAddress;

    private int port;

    private String rack;

    private String datacenter;

    private Status status;

    public DynomiteNode(String token, String hostname, String ipAddress, int port, String rack,
            String datacenter, Status status) {
        this.token = token;
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.port = port;
        this.rack = rack;
        this.datacenter = datacenter;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getDatacenter() {
        return datacenter;
    }

    public void setDatacenter(String datacenter) {
        this.datacenter = datacenter;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DynomiteNode [token=" + token + ", hostname=" + hostname + ", ipAddress="
                + ipAddress + ", port=" + port + ", rack=" + rack + ", datacenter=" + datacenter
                + ", status=" + status + "]";
    }
}
