package me.zhenxin.zmusic.utils;

import me.zhenxin.zmusic.ZMusic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Cookie 工具类
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/3/21 12:25
 */
public class CookieUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Set<String> COOKIE_ATTRIBUTES = new HashSet<>(Arrays.asList(
            "domain", "expires", "httponly", "max-age", "path", "samesite", "secure"));
    private static final Map<String, String> COOKIES = new LinkedHashMap<>();

    private static File cookieFile;
    private static String cookieString = "";

    public static synchronized void initCookieManager() {
        cookieFile = new File(ZMusic.dataFolder, "cookies.txt");
        if (!cookieFile.exists()) {
            try {
                cookieFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        COOKIES.clear();
        COOKIES.putAll(parseCookies(OtherUtils.readFileToString(cookieFile)));
        ensureDeviceCookies();
        persistCookies();
    }

    public static synchronized void saveCookies(String cookie) {
        COOKIES.putAll(parseCookies(cookie));
        ensureDeviceCookies();
        persistCookies();
    }

    public static synchronized boolean hasCookie(String name) {
        String value = COOKIES.get(name);
        return value != null && !value.isEmpty();
    }

    public static synchronized String getCookies() {
        return cookieString;
    }

    private static Map<String, String> parseCookies(String cookieString) {
        Map<String, String> cookies = new LinkedHashMap<>();
        if (cookieString == null || cookieString.trim().isEmpty()) {
            return cookies;
        }

        for (String part : cookieString.split(";")) {
            String cookie = part.trim();
            int separator = cookie.indexOf('=');
            if (separator <= 0) {
                continue;
            }

            String name = cookie.substring(0, separator).trim();
            if (COOKIE_ATTRIBUTES.contains(name.toLowerCase(Locale.ROOT))) {
                continue;
            }
            cookies.put(name, cookie.substring(separator + 1).trim());
        }
        return cookies;
    }

    private static void ensureDeviceCookies() {
        putIfAbsent("deviceId", randomHex(25));
        putIfAbsent("_ntes_nuid", randomHex(32));
        putIfAbsent("_ntes_nnid", COOKIES.get("_ntes_nuid") + "," + System.currentTimeMillis());
        putIfAbsent("NMTID", randomHex(16));
    }

    private static void putIfAbsent(String name, String value) {
        if (!hasCookie(name)) {
            COOKIES.put(name, value);
        }
    }

    private static String randomHex(int byteCount) {
        byte[] bytes = new byte[byteCount];
        RANDOM.nextBytes(bytes);
        StringBuilder result = new StringBuilder(byteCount * 2);
        for (byte value : bytes) {
            result.append(String.format("%02X", value & 0xFF));
        }
        return result.toString();
    }

    private static void persistCookies() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> cookie : COOKIES.entrySet()) {
            if (result.length() > 0) {
                result.append("; ");
            }
            result.append(cookie.getKey()).append('=').append(cookie.getValue());
        }
        cookieString = result.toString();

        Path target = cookieFile.toPath();
        Path temporary = target.resolveSibling(target.getFileName() + ".tmp");
        try {
            Files.write(temporary, cookieString.getBytes(StandardCharsets.UTF_8));
            try {
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            ZMusic.log.sendDebugMessage("[CookieUtils] 保存Cookies失败: " + e.getMessage());
        }
    }
}
