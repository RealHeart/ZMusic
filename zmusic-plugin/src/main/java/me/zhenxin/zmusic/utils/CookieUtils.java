package me.zhenxin.zmusic.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Cookie 工具类
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/3/21 12:25
 */
public class CookieUtils {
    private static final Gson GSON = new Gson();

    public static void initCookieManager() {
        CookieManager cookieManager = new CookieManager();
        File file = new File(ZMusic.dataFolder, "cookies.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String str = OtherUtils.readFileToString(file);
        if (!str.isEmpty()) {
            JsonArray cookies = GSON.fromJson(str, JsonArray.class);
            for (JsonElement element : cookies) {
                JsonObject obj = element.getAsJsonObject();
                String name = obj.get("name").getAsString();
                String value = obj.get("value").getAsString();
                String domain = obj.get("domain").getAsString();
                long maxAge = obj.get("maxAge").getAsLong();
                String path = obj.get("path").getAsString();
                boolean secure = obj.get("secure").getAsBoolean();
                boolean httpOnly = obj.get("httpOnly").getAsBoolean();
                int version = obj.get("version").getAsInt();
                HttpCookie cookie = new HttpCookie(name, value);
                cookie.setDomain(domain);
                cookie.setMaxAge(maxAge);
                cookie.setPath(path);
                cookie.setSecure(secure);
                cookie.setHttpOnly(httpOnly);
                cookie.setVersion(version);
                URI uri = URI.create(domain + path);
                cookieManager.getCookieStore().add(uri, cookie);
            }
        }
        CookieManager.setDefault(cookieManager);
    }

    public static void saveCookies() {
        CookieManager manager = (CookieManager) CookieManager.getDefault();
        List<HttpCookie> cookies = manager.getCookieStore().getCookies();
        File file = new File(ZMusic.dataFolder, "cookies.json");
        JsonArray array = new JsonArray();
        for (HttpCookie cookie : cookies) {
            JsonObject object = new JsonObject();
            object.addProperty("name", cookie.getName());
            object.addProperty("value", cookie.getValue());
            object.addProperty("domain", cookie.getDomain());
            object.addProperty("maxAge", cookie.getMaxAge());
            object.addProperty("path", cookie.getPath());
            object.addProperty("secure", cookie.getSecure());
            object.addProperty("httpOnly", cookie.isHttpOnly());
            object.addProperty("version", cookie.getVersion());
            array.add(object);
        }
        try {
            OtherUtils.saveStringToLocal(file, GSON.toJson(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
