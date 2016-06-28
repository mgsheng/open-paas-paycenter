package cn.com.open.openpaas.payservice.app.tools;

/**
 * Created by senjyuren on 16-3-22.
 */
public class SettingEntiry{
    private static final String OPEN_API_DOMAIN="https://openapi.alipay.com/gateway.do";
    private static final String APPID="2015051100069126";
    private static final String PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMvPGb+aJQX0RPjs" +
            "x6iZUbcujk9GZhVT1Z7N5hky6rZWkOmO2VLwGaY1zyMwHnPkb3fYcv8lhB/+9LsG" +
            "sPTdSl1qYOyApI1KLyXZTK/qmHHT9uiX1oz02uwNFuSZb9i7FbYth1vEuuM3qnZE" +
            "7WmgNQkmcted9JF0f/0jK9IOqqNBAgMBAAECgYAKQmOWbIj+krxCF5E5YHZnlTVe" +
            "sjmDS1QOiWjSzehYw2TKDQHNlf6EimLh75Mo3E/sJX4sb9QF1Ey3eW/A877BfBDg" +
            "UBDuXJUQXzUL3MJFD0w6+Zsx/xPqQmCl9gYwiR7DMeUKtgUrMmYFQFCELzwlM3st" +
            "uVPcjLXMaXT8M0EeDQJBAOcDRH02CpfwvodqSzJxy0TFZtaTbKX39TwxJFcSFfV3" +
            "fNYDnSdYTYswQmUWpOLP0hoxrcMGlPbhGzPjSACULE8CQQDh2o/OkVHE1aXr4u5l" +
            "Q8OxUIaATGQvstOQL9UA8DmNab4QLrn0Ol8T0p2J4IHeWXa1UFtm34/2amp9Vjkt" +
            "kYNvAkARv3uEjyFTOQi6SJ1MW9e9CdlzxNHFEn7ByBi9o8MSH8L0gkSRoEQc3HFN" +
            "aOb0EflXT9fEsv3A1dyMKPsAKGIbAkEAv9meysOah+9MMCHmi9KSSu6yMg2yFOp8" +
            "2EApWdC1srAeKTTn9NQYq4f/Fn3FE5E/SyllWu+RJKqkpq81hsXStQJADwlOtdyi" +
            "k8N5DvlCSt2rsBsskz4Uiv3KUCwCqq+Lt6g/uFkrTcoBR7GHKOHyyk+l+aJjtxnD" +
            "ONuh2psnu0N1vg==";
    private static final String APP_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    public static String getOpenApiDomain() {
        return OPEN_API_DOMAIN;
    }

    public static String getAPPID() {
        return APPID;
    }

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }
}
