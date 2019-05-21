
package com.leyton.util;

public class DynomiteConstant {

    private DynomiteConstant() {
    }

    public static class ExpireTimeUnits {

        private ExpireTimeUnits() {
        }

        public static final String SECONDS = "EX";

        public static final String MILLISECONDS = "PX";
    }

    public static class SetOperation {

        private SetOperation() {
        }

        public static final String EXISTING_KEY = "XX";

        public static final String NON_EXISTENT_KEY = "NX";
    }
}
