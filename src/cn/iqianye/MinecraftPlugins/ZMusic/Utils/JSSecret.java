package cn.iqianye.MinecraftPlugins.ZMusic.Utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 网易云音乐参数加密（方案二）
 * 使用 ScriptEngine 调用 js 引擎
 */
public class JSSecret {

    public static final String encText = "encText";
    public static final String encSecKey = "encSecKey";
    private static Invocable inv;

    /**
     * 从本地加载修改后的 js 文件到 scriptEngine
     */
    static {
        try {
            // 文件读取
            String js = OtherUtils.readString("D:\\Dev\\Minecraft\\Plugins\\ZMusic\\src\\core.js");
            ScriptEngineManager factory = new ScriptEngineManager();
            // 查找并创建一个ScriptEngine
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            // js代码放入到eval中当做参数就可以执行相应的js代码
            engine.eval(js);
            // 调用js中的方法
            inv = (Invocable) engine;
            System.out.println("Init completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ScriptObjectMirror get_params(String paras) throws Exception {
        ScriptObjectMirror so = (ScriptObjectMirror) inv.invokeFunction("myFunc", paras);
        return so;
    }

    public static HashMap<String, String> getData(String paras) {
        try {
            ScriptObjectMirror so = (ScriptObjectMirror) inv.invokeFunction("myFunc", paras);

            Set<Map.Entry<String, Object>> entries = so.entrySet();
            for (Map.Entry<String, Object> map : entries) {
                System.out.println("key:" + map.getKey());
                System.out.println("value:" + map.getValue());
            }

            HashMap<String, String> data = new HashMap<>();
            data.put("params", so.get(JSSecret.encText).toString());
            data.put("encSecKey", so.get(JSSecret.encSecKey).toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
