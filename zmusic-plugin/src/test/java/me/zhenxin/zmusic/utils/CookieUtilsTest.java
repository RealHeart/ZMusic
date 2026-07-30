package me.zhenxin.zmusic.utils;

import me.zhenxin.zmusic.ZMusic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CookieUtilsTest {

    @TempDir
    static Path dataFolder;

    @BeforeAll
    static void setUpDataFolder() {
        ZMusic.dataFolder = dataFolder.toFile();
    }

    @Test
    void preservesDeviceIdentityAcrossLoginAndRestart() {
        CookieUtils.initCookieManager();
        Map<String, String> initialCookies = parseCookies(CookieUtils.getCookies());

        assertNotNull(initialCookies.get("deviceId"));
        assertNotNull(initialCookies.get("_ntes_nuid"));
        assertNotNull(initialCookies.get("_ntes_nnid"));
        assertNotNull(initialCookies.get("NMTID"));

        CookieUtils.saveCookies("MUSIC_U=first; __csrf=token; Path=/; HttpOnly");
        Map<String, String> loginCookies = parseCookies(CookieUtils.getCookies());

        assertEquals("first", loginCookies.get("MUSIC_U"));
        assertEquals("token", loginCookies.get("__csrf"));
        assertEquals(initialCookies.get("deviceId"), loginCookies.get("deviceId"));
        assertEquals(initialCookies.get("_ntes_nuid"), loginCookies.get("_ntes_nuid"));
        assertEquals(initialCookies.get("_ntes_nnid"), loginCookies.get("_ntes_nnid"));
        assertEquals(initialCookies.get("NMTID"), loginCookies.get("NMTID"));
        assertFalse(loginCookies.containsKey("Path"));
        assertFalse(loginCookies.containsKey("HttpOnly"));

        CookieUtils.initCookieManager();
        assertEquals(loginCookies, parseCookies(CookieUtils.getCookies()));
    }

    @Test
    void encodesCookieAsOneFormParameter() throws Exception {
        String form = NetUtils.appendFormParameter("key=value", "cookie", "MUSIC_U=a+b; token=c&d");

        assertEquals("key=value&cookie=MUSIC_U%3Da%2Bb%3B+token%3Dc%26d", form);
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
}
