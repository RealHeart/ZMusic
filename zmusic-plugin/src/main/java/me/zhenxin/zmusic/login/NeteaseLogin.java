package me.zhenxin.zmusic.login;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.zhenxin.zmusic.ZMusic;
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
 * 
 */
public class NeteaseLogin {

    private static final String API = Config.neteaseApiRoot;
    private static final Gson GSON = new Gson();

    public static String key() {
        String result = NetUtils.postNetString(API + "login/qr/key", null, "");
        JsonObject json = GSON.fromJson(result, JsonObject.class);
        if (json == null || !json.has("data") || !json.get("data").isJsonObject()) {
            throw new IllegalStateException("获取 QR key 失败: 响应格式错误");
        }
        JsonObject data = json.getAsJsonObject("data");
        if (!data.has("unikey")) {
            throw new IllegalStateException("获取 QR key 失败: 缺少 unikey 字段");
        }
        return data.get("unikey").getAsString();
    }

    public static String create(String key) throws UnsupportedEncodingException {
        String params = "key=" + key;
        String result = NetUtils.postNetString(API + "login/qr/create", null, params);
        JsonObject json = GSON.fromJson(result, JsonObject.class);
        if (json == null || !json.has("data") || !json.get("data").isJsonObject()) {
            throw new IllegalStateException("创建二维码失败: 响应格式错误");
        }
        JsonObject data = json.getAsJsonObject("data");
        if (!data.has("qrurl")) {
            throw new IllegalStateException("创建二维码失败: 缺少 qrurl 字段");
        }
        String url = data.get("qrurl").getAsString();
        return "https://api.2dcode.biz/v1/create-qr-code?data=" + URLEncoder.encode(url, "UTF-8");
    }

    public static Integer check(String key) {
        String params = "key=" + key + "&noCookie=true";
        String result = NetUtils.postNetString(API + "login/qr/check", null, params);
        JsonObject json = GSON.fromJson(result, JsonObject.class);
        if (json == null || !json.has("code")) {
            return -1;
        }
        if (json.get("code").getAsInt() == 803 && json.has("cookie")) {
            String cookie = json.get("cookie").getAsString();
            CookieUtils.saveCookies(cookie);
        }
        return json.get("code").getAsInt();
    }

    public static void welcome() {
        String nickname = nickname();
        if (!nickname.isEmpty()) {
            ZMusic.log.sendNormalMessage("您已登录网易云音乐, 昵称: " + nickname);
        } else {
            ZMusic.log.sendErrorMessage("您未登录网易云音乐, 自动转为匿名登录.");
            ZMusic.log.sendErrorMessage("匿名登录将无法获取您的个人信息, 仅能播放公开音乐.");
            ZMusic.log.sendErrorMessage("请使用 /zm login 命令登录网易云音乐.");
        }
    }

    public static boolean refresh() {
        if (!CookieUtils.hasCookie("MUSIC_U")) {
            return false;
        }

        String result = NetUtils.postNetString(API + "login/refresh", null, "");
        try {
            JsonObject json = parseJsonObject(result);
            if (getInt(json, "code", -1) != 200) {
                return false;
            }

            String cookie = getString(json, "cookie", null);
            if (cookie != null && !cookie.isEmpty()) {
                CookieUtils.saveCookies(cookie);
            }
            return true;
        } catch (Exception e) {
            ZMusic.log.sendDebugMessage("[网易云登录] 刷新登录状态失败: " + e.getMessage());
            return false;
        }
    }

    public static void login_fromlink(String url) {
        String result = NetUtils.postNetString(url, null, "");
        JsonObject json = parseJsonObject(result);
        int codeResult = getInt(json, "code", -1);
        if (codeResult == 200) {
            String cookie = getString(json, "cookie", null);
            if (cookie != null && !cookie.isEmpty()) {
                CookieUtils.saveCookies(cookie);
                welcome();
            } else {
                ZMusic.log.sendErrorMessage("未获取到 Cookies，登录失败。");
            }
            // } else if (codeResult == 8810) {
            // ↑ 不只是8810。没办法一个一个找出来，于是采用关键词
        } else if (result.contains("安全风险")) {
            ZMusic.log.sendErrorMessage(
                    "被拿下了喵。请使用手机验证码登录，扫码登录，或者使用 raw 登录（从浏览器复制cookies）");
        } else {
            ZMusic.log.sendErrorMessage("登录失败: " + codeResult + "，请检查服务器控制台获得完整错误信息。");
            throw new IllegalStateException("登录失败: " + codeResult + ", 详细信息: \n" + json);
        }
    }

    // 验证码登录的流程是：
    // 1. /captcha/sent?phone=xxx&ctcode=xxx
    // 2. /captcha/verify?phone=xxx&ctcode=xxx&captcha=xxx
    // 3. /login/cellphone?phone=xxx&ctcode=xxx&captcha=xxx

    // step 1: send code
    public static void sendCode(String phone, String countrycode) {
        String url = API + "captcha/sent?phone=" + phone + "&ctcode=" + countrycode;
        String result = NetUtils.postNetString(url, null, "");
        JsonObject json = parseJsonObject(result);
        int code = getInt(json, "code", -1);
        if (code == 200) {
            ZMusic.log.sendNormalMessage("发送验证码成功");
        } else {
            ZMusic.log.sendErrorMessage("发送验证码失败: " + getString(json, "message",
                    "未知错误"));
        }
    }

    public static void verify(String phone, String code, String countrycode) {
        // step 2: verify code
        String url_verify = API + "captcha/verify?phone=" + phone + "&ctcode=" + countrycode
                + "&captcha=" + code;
        String result_verify = NetUtils.postNetString(url_verify, null, "");
        JsonObject json_verify = parseJsonObject(result_verify);
        int code_verify = getInt(json_verify, "code", -1);
        if (code_verify != 200) {
            ZMusic.log.sendErrorMessage("验证码错误" + getString(json_verify, "data", "未知错误"));
            return;
        }

        // step 3: login with code
        String url = API + "login/cellphone?phone=" + phone + "&ctcode=" + countrycode + "&captcha=" + code;
        login_fromlink(url);
    }

    public static void password_phone(String phone, String password, String countrycode, boolean isMD5) {
        // throw new UnsupportedOperationException("暂未实现");
        String url = API + "login/cellphone?phone=" + phone + "&ctcode=" +
                countrycode +
                (isMD5 ? "&md5_password=" : "&password=") + password;
        login_fromlink(url);
    }

    public static void password_email(String email, String password, boolean isMD5) {
        // throw new UnsupportedOperationException("暂未实现");
        String url = API + "login?email=" + email +
                (isMD5 ? "&md5_password=" : "&password=") + password;
        login_fromlink(url);
    }

    public static String nickname() {
        JsonObject data = status();
        try {
            int code = data.get("code").getAsInt();
            if (code != 200) {
                return "";
            }
            JsonObject profile = data.getAsJsonObject("profile");
            if (profile == null) {
                return "";
            }
            String nickname = profile.get("nickname").getAsString();
            if (nickname == null || nickname.isEmpty()) {
                return "";
            }
            return nickname;
        } catch (Exception e) {
            return "";
        }
    }

    private static JsonObject status() {
        String result = NetUtils.postNetString(API + "login/status", null, "");
        try {
            JsonObject root = parseJsonObject(result);
            return getObject(root, "data");
        } catch (Exception e) {
            ZMusic.log.sendDebugMessage("[网易云登录] 登录状态接口返回解析失败: " + e.getMessage());
            return new JsonObject();
        }
    }

    private static JsonObject parseJsonObject(String result) {
        try {
            JsonElement element = GSON.fromJson(result, JsonElement.class);
            if (element == null || !element.isJsonObject()) {
                throw new IllegalStateException("接口返回不是 JSON 对象: " + String.valueOf(result));
            }
            return element.getAsJsonObject();
        } catch (Exception e) {
            throw new IllegalStateException("接口返回解析失败: " + String.valueOf(result), e);
        }
    }

    private static JsonObject getObject(JsonObject parent, String key) {
        // throw new UnsupportedOperationException("暂未实现");
        if (parent == null || !parent.has(key) || parent.get(key) == null ||
                !parent.get(key).isJsonObject()) {
            return new JsonObject();
        }
        return parent.getAsJsonObject(key);
    }

    private static String getString(JsonObject obj, String key, String defaultValue) {
        if (obj == null || !obj.has(key) || obj.get(key) == null ||
                obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        try {
            return obj.get(key).getAsString();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    private static int getInt(JsonObject obj, String key, int defaultValue) {
        if (obj == null || !obj.has(key) || obj.get(key) == null ||
                obj.get(key).isJsonNull()) {
            return defaultValue;
        }
        try {
            return obj.get(key).getAsInt();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static void loginRaw(String rawCookies) {
        if (rawCookies == null || rawCookies.isEmpty()) {
            ZMusic.log.sendErrorMessage("Cookies 不能为空！");
            return;
        }
        if (!rawCookies.contains("=")) {
            ZMusic.log.sendErrorMessage("无效的 Cookies 格式！");
            return;
        }
        String normalized = normalizeRawCookie(rawCookies);
        if (normalized.isEmpty() || !normalized.contains("=")) {
            ZMusic.log.sendErrorMessage("无效的 Cookies 格式！");
            return;
        }
        try {
            CookieUtils.saveCookies(normalized);
            ZMusic.log.sendNormalMessage("Cookies 已成功保存。");
            welcome();
        } catch (Exception e) {
            ZMusic.log.sendErrorMessage("保存 Cookies 时发生错误: " + e.getMessage());
        }
    }

    private static String normalizeRawCookie(String rawCookies) {
        // throw new UnsupportedOperationException("暂未实现");
        String cookie = rawCookies.trim();
        if (cookie.regionMatches(true, 0, "cookie:", 0, 7)) {
            cookie = cookie.substring(7).trim();
        }
        cookie = cookie.replace('\n', ';').replace('\r', ';');
        while (cookie.contains(";;")) {
            cookie = cookie.replace(";;", ";");
        }
        return cookie;
    }
}
