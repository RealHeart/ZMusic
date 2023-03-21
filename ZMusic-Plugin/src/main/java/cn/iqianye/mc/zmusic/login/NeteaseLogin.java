package cn.iqianye.mc.zmusic.login;

import cn.iqianye.mc.zmusic.config.Config;
import cn.iqianye.mc.zmusic.utils.CookieUtils;
import cn.iqianye.mc.zmusic.utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.var;

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
    private static final Gson GSON = new Gson().newBuilder().create();

    public static String create(String key) throws UnsupportedEncodingException {
        var result = NetUtils.getNetString(API + "login/qr/create?key=" + key + "&timestamp=" + time(), null);
        var json = GSON.fromJson(result, JsonObject.class);
        var data = json.getAsJsonObject("data");
        var url = data.get("qrurl").getAsString();
        return "https://cli.im/api/qrcode/code?text=" + URLEncoder.encode(url, "UTF-8");
    }

    public static Integer check(String key) {
        var result = NetUtils.getNetString(API + "login/qr/check?key=" + key + "&timestamp=" + time(), null);
        var json = GSON.fromJson(result, JsonObject.class);
        return json.get("code").getAsInt();
    }

    public static String key() {
        var result = NetUtils.getNetString(API + "login/qr/key?timestamp=" + time(), null);
        var json = GSON.fromJson(result, JsonObject.class);
        var data = json.getAsJsonObject("data");
        return data.get("unikey").getAsString();
    }

    public static String refresh() {
        var cookie = "";
        var result = NetUtils.getNetString(API + "login/refresh?timestamp=" + time(), null);
        var data = GSON.fromJson(result, JsonObject.class);
        var code = data.get("code").getAsInt();
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
        var result = NetUtils.getNetString(API + "login/status?timestamp=" + time(), null);
        var data = GSON.fromJson(result, JsonObject.class);
        try {
            data.get("profile").getAsJsonObject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String welcome() {
        var name = nickname();
        if (name.isEmpty()) {
            return "昵称获取失败!";
        } else {
            return "登录成功! 欢迎您, " + name + "!";
        }
    }

    private static String nickname() {
        var result = NetUtils.getNetString(API + "user/account?timestamp=" + time(), null);
        var data = GSON.fromJson(result, JsonObject.class);
        var code = data.get("code").getAsInt();
        if (code != 200) {
            return "";
        }
        try {
            var profile = data.get("profile").getAsJsonObject();
            return profile.get("nickname").getAsString();
        } catch (Exception e) {
            return data.get("account").getAsJsonObject().get("userName").getAsString();
        }
    }

    private static Long time() {
        return System.currentTimeMillis();
    }
}
