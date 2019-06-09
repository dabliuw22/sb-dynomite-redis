
package com.leyton.util.properties;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynomiteTopologyProperties {

    @NotNull
    private Map<String, List<DynomiteRackProperties>> datacenter;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynomiteRackProperties {

        @NotNull
        @NotEmpty
        private String rack;

        @NotNull
        private Map<String, DynomiteNodeProperties> nodes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DynomiteNodeProperties {

        @NotNull
        @NotEmpty
        private String token;

        @NotNull
        @NotEmpty
        private String host;

        @NotNull
        @NotEmpty
        private String ip;

        @NotNull
        private Integer port;
    }
}
