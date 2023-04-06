package cn.iqianye.mc.zmusic.utils;

import cn.iqianye.mc.zmusic.ZMusic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.var;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;

/**
 * Cookie 工具类
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/3/21 12:25
 */
public class CookieUtils {
    private static final Gson GSON = new Gson();
    ;
    private static CookieManager cookieManager;

    public static CookieManager initCookieManager() {
        cookieManager = new CookieManager();
        var file = new File(ZMusic.dataFolder, "cookies.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var str = OtherUtils.readFileToString(file);
        if (!str.isEmpty()) {
            var cookies = GSON.fromJson(str, JsonArray.class);
            for (JsonElement element : cookies) {
                var obj = element.getAsJsonObject();
                var name = obj.get("name").getAsString();
                var value = obj.get("value").getAsString();
                var domain = obj.get("domain").getAsString();
                var maxAge = obj.get("maxAge").getAsLong();
                var path = obj.get("path").getAsString();
                var secure = obj.get("secure").getAsBoolean();
                var httpOnly = obj.get("httpOnly").getAsBoolean();
                var version = obj.get("version").getAsInt();
                var cookie = new HttpCookie(name, value);
                cookie.setDomain(domain);
                cookie.setMaxAge(maxAge);
                cookie.setPath(path);
                cookie.setSecure(secure);
                cookie.setHttpOnly(httpOnly);
                cookie.setVersion(version);
                var uri = URI.create(domain + path);
                cookieManager.getCookieStore().add(uri, cookie);
            }
            CookieManager.setDefault(cookieManager);
        }
        return cookieManager;
    }

    public static void saveCookies() {
        var cookies = cookieManager.getCookieStore().getCookies();
        var file = new File(ZMusic.dataFolder, "cookies.json");
        var array = new JsonArray();
        for (HttpCookie cookie : cookies) {
            var object = new JsonObject();
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
