package me.zhenxin.zmusic.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.utils.CookieUtils;
import me.zhenxin.zmusic.utils.NetUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 网易云登录
 *
 * @author 真心
 * @email qgzhenxin@qq.com
 * @since 2023/3/21 11:17
 */
public class NeteaseLogin {
    private static final String API = Config.neteaseApiRoot;
    private static final Gson GSON = new Gson();

    public static String create(String key) throws UnsupportedEncodingException {
        String result = NetUtils.getNetString(API + "login/qr/create?key=" + key + "&timestamp=" + time(), null);
        JsonObject json = GSON.fromJson(result, JsonObject.class);
        JsonObject data = json.getAsJsonObject("data");
        String url = data.get("qrurl").getAsString();
        return "https://cli.im/api/qrcode/code?text=" + URLEncoder.encode(url, "UTF-8");
    }

    public static Integer check(String key) {
        String result = NetUtils.getNetString(API + "login/qr/check?key=" + key + "&timestamp=" + time(), null);
        JsonObject json = GSON.fromJson(result, JsonObject.class);
        return json.get("code").getAsInt();
    }

    public static String key() {
        String result = NetUtils.getNetString(API + "login/qr/key?timestamp=" + time(), null);
        JsonObject json = GSON.fromJson(result, JsonObject.class);
        JsonObject data = json.getAsJsonObject("data");
        return data.get("unikey").getAsString();
    }

    public static String refresh() {
        String cookie = "";
        String result = NetUtils.getNetString(API + "login/refresh?timestamp=" + time(), null);
        JsonObject data = GSON.fromJson(result, JsonObject.class);
        int code = data.get("code").getAsInt();
        if (code == 200) {
            cookie = data.get("cookie").getAsString();
        }
        if (!cookie.isEmpty()) {
            CookieUtils.saveCookies();
            return "刷新登录状态成功!";
        } else {
            return "刷新登录状态失败, 建议执行重新登录操作!";
        }
    }

    public static void anonymous() {
        NetUtils.getNetString(API + "register/anonimous?timestamp=" + time(), null);
        CookieUtils.saveCookies();
    }

    public static Boolean isLogin() {
        String result = NetUtils.getNetString(API + "login/status?timestamp=" + time(), null);
        JsonObject root = GSON.fromJson(result, JsonObject.class);
        try {
            JsonObject data = root.getAsJsonObject("data");
            data.get("profile").getAsJsonObject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String welcome() {
        String name = nickname();
        if (name.isEmpty()) {
            return "昵称获取失败!";
        } else {
            return "登录成功! 欢迎您, " + name + "!";
        }
    }

    private static String nickname() {
        String result = NetUtils.getNetString(API + "user/account?timestamp=" + time(), null);
        JsonObject data = GSON.fromJson(result, JsonObject.class);
        int code = data.get("code").getAsInt();
        if (code != 200) {
            return "";
        }
        try {
            JsonObject profile = data.get("profile").getAsJsonObject();
            return profile.get("nickname").getAsString();
        } catch (Exception e) {
            return data.get("account").getAsJsonObject().get("userName").getAsString();
        }
    }

    private static Long time() {
        return System.currentTimeMillis();
    }
}
