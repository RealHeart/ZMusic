package me.zhenxin.zmusic.login;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.utils.CookieUtils;
import me.zhenxin.zmusic.utils.log.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NeteaseLoginTest {

    @TempDir
    static Path dataFolder;

    private static HttpServer server;

    @BeforeAll
    static void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/login/refresh", NeteaseLoginTest::handleRefresh);
        server.start();

        ZMusic.dataFolder = dataFolder.toFile();
        ZMusic.thisVer = "test";
        ZMusic.log = new NoOpLog();
        Config.neteaseApiRoot = "http://127.0.0.1:" + server.getAddress().getPort() + "/";
        CookieUtils.initCookieManager();
    }

    @AfterAll
    static void tearDown() {
        server.stop(0);
    }

    @Test
    void refreshesLoginCookieWithoutChangingDeviceIdentity() {
        String deviceId = parseCookies(CookieUtils.getCookies()).get("deviceId");
        CookieUtils.saveCookies("MUSIC_U=old; __csrf=old-token");

        assertTrue(NeteaseLogin.refresh());

        Map<String, String> cookies = parseCookies(CookieUtils.getCookies());
        assertEquals("new", cookies.get("MUSIC_U"));
        assertEquals("new-token", cookies.get("__csrf"));
        assertEquals(deviceId, cookies.get("deviceId"));
    }

    private static void handleRefresh(HttpExchange exchange) throws IOException {
        String requestBody;
        try (InputStream input = exchange.getRequestBody()) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = input.read(buffer)) >= 0) {
                output.write(buffer, 0, length);
            }
            requestBody = new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
        if (!requestBody.contains("cookie=") || !requestBody.contains("MUSIC_U%3Dold")) {
            exchange.sendResponseHeaders(400, -1);
            exchange.close();
            return;
        }

        byte[] response = ("{\"code\":200,\"cookie\":"
                + "\"MUSIC_U=new; __csrf=new-token; Path=/; HttpOnly\"}")
                .getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    private static Map<String, String> parseCookies(String cookieString) {
        Map<String, String> cookies = new LinkedHashMap<>();
        for (String part : cookieString.split(";")) {
            String cookie = part.trim();
            int separator = cookie.indexOf('=');
            if (separator > 0) {
                cookies.put(cookie.substring(0, separator), cookie.substring(separator + 1));
            }
        }
        return cookies;
    }

    private static class NoOpLog implements Log {

        @Override
        public void sendNormalMessage(String message) {
        }

        @Override
        public void sendDebugMessage(String message) {
        }

        @Override
        public void sendErrorMessage(String message) {
        }

        @Override
        public Object getSender() {
            return null;
        }
    }
}
