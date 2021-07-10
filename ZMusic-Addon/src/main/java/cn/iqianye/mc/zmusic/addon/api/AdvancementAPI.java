package cn.iqianye.mc.zmusic.addon.api;

import cn.iqianye.mc.zmusic.addon.Addon;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancements;
import net.minecraft.advancements.critereon.LootDeserializationContext;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/********************************************************************************************************************
 *              _                                                    _            _____ _____   _                   *
 *     /\      | |                                                  | |     /\   |  __ \_   _| (_)                  *
 *    /  \   __| |_   ____ _ _ __   ___ ___ _ __ ___   ___ _ __ ___ | |_   /  \  | |__) || |    _  __ ___   ____ _  *
 *   / /\ \ / _` \ \ / / _` | '_ \ / __/ _ \ '_ ` _ \ / _ \ '_ ` _ \| __| / /\ \ |  ___/ | |   | |/ _` \ \ / / _` | *
 *  / ____ \ (_| |\ V / (_| | | | | (_|  __/ | | | | |  __/ | | | | | |_ / ____ \| |    _| |_ _| | (_| |\ V / (_| | *
 * /_/    \_\__,_| \_/ \__,_|_| |_|\___\___|_| |_| |_|\___|_| |_| |_|\__/_/    \_\_|   |_____(_) |\__,_| \_/ \__,_| *
 *                                                                                            _/ |                  *
 *                                                                                           |__/                   *
 ********************************************************************************************************************/

public class AdvancementAPI {

    private NamespacedKey id;
    private String icon;
    private String title, description;
    private String frame = "task";
    private boolean announce = false, toast = true;

    private JavaPlugin pl;
    private String[] icons;
    private String ver;

    {
        ver = Bukkit.getServer().getClass().getPackage().getName().split("org.bukkit.craftbukkit.v")[1];
        Version version = new Version();
        if (version.isHigherThan("1.12")) {
            icons = new String[]{
                    "minecraft:music_disc_13",
                    "minecraft:music_disc_cat",
                    "minecraft:music_disc_blocks",
                    "minecraft:music_disc_chirp",
                    "minecraft:music_disc_far",
                    "minecraft:music_disc_mall",
                    "minecraft:music_disc_mellohi",
                    "minecraft:music_disc_strad",
                    "minecraft:music_disc_ward",
                    "minecraft:music_disc_wait"
            };
        } else {
            icons = new String[]{
                    "minecraft:record_13",
                    "minecraft:record_cat",
                    "minecraft:record_blocks",
                    "minecraft:record_chirp",
                    "minecraft:record_far",
                    "minecraft:record_mall",
                    "minecraft:record_mellohi",
                    "minecraft:record_strad",
                    "minecraft:record_ward",
                    "minecraft:record_wait"
            };
        }
        Random random = new Random();
        int r = random.nextInt(icons.length);
        this.icon = icons[r];
    }

    public AdvancementAPI(String title) {
        this(new NamespacedKey(Addon.plugin, String.valueOf(System.currentTimeMillis())), title);
    }

    public AdvancementAPI(NamespacedKey id, String title) {
        this.id = id;
        this.title = title;
        this.description = "Zmusic 专用成就";
        this.pl = Addon.plugin;
    }

    public AdvancementAPI(NamespacedKey id, String title, String frame) {
        this.id = id;
        this.title = title;
        this.description = "Zmusic 专用成就";
        this.frame = frame;
        this.pl = Addon.plugin;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public Advancement getAdvancement() {
        return Bukkit.getAdvancement(id);
    }

    private void add() {
        switch (ver) {
            case "1_12_R1":
                loadAdvancement_1_12_R1(id, getJson());
                break;
            case "1_13_R2":
                loadadvancement_1_13_R2(id, getJson());
                break;
            case "1_14_R1":
                loadAdvancement_1_14_R1(id, getJson());
                break;
            case "1_15_R1":
                loadAdvancement_1_15_R1(id, getJson());
                break;
            case "1_16_R1":
                loadAdvancement_1_16_R1(id, getJson());
                break;
            case "1_16_R2":
                loadAdvancement_1_16_R2(id, getJson());
                break;
            case "1_16_R3":
                loadAdvancement_1_16_R3(id, getJson());
                break;
            case "1_17_R1":
                loadAdvancement_1_17_R1(id, getJson());
                break;
        }
    }

    private void remove() {
        switch (ver) {
            case "1_12_R1":
                removeAdvancement_1_12_R1(getAdvancement());
                break;
            case "1_13_R2":
                removeAdvancement_1_13_R2(getAdvancement());
                break;
            case "1_14_R1":
                removeAdvancement_1_14_R1(getAdvancement());
                break;
            case "1_15_R1":
                removeAdvancement_1_15_R1(getAdvancement());
                break;
            case "1_16_R1":
                removeAdvancement_1_16_R1(getAdvancement());
                break;
            case "1_16_R2":
                removeAdvancement_1_16_R2(getAdvancement());
                break;
            case "1_16_R3":
                removeAdvancement_1_16_R3(getAdvancement());
                break;
            case "1_17_R1":
                removeAdvancement_1_17_R1(getAdvancement());
                break;
        }
    }

    //爷就不写反射 这里 是真的不好写
    // 方法格式： load/removeadvancement+版本号 不打算支持1.13R1
    public void loadAdvancement_1_12_R1(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_12_R1.Advancement.SerializedAdvancement serializedAdvancement = (net.minecraft.server.v1_12_R1.Advancement.SerializedAdvancement) net.minecraft.server.v1_12_R1.ChatDeserializer.a(net.minecraft.server.v1_12_R1.AdvancementDataWorld.DESERIALIZER, json, net.minecraft.server.v1_12_R1.Advancement.SerializedAdvancement.class);
        if (serializedAdvancement != null)
            net.minecraft.server.v1_12_R1.AdvancementDataWorld.REGISTRY.a(new HashMap<>(Collections.singletonMap(org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey.toMinecraft(key), serializedAdvancement)));
    }

    public void removeAdvancement_1_12_R1(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(id);
        for (Map.Entry entry : net.minecraft.server.v1_12_R1.AdvancementDataWorld.REGISTRY.advancements.entrySet()) {
            if (((net.minecraft.server.v1_12_R1.Advancement) entry.getValue()).getName().getKey().equals(advancement.getKey().getKey().toLowerCase())) {
                net.minecraft.server.v1_12_R1.AdvancementDataWorld.REGISTRY.advancements.remove(entry.getKey());
                break;
            }
        }
    }

    public void loadadvancement_1_13_R2(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_13_R2.Advancement.SerializedAdvancement serializedAdvancement = (net.minecraft.server.v1_13_R2.Advancement.SerializedAdvancement) net.minecraft.server.v1_13_R2.ChatDeserializer.a(net.minecraft.server.v1_13_R2.AdvancementDataWorld.DESERIALIZER, json, net.minecraft.server.v1_13_R2.Advancement.SerializedAdvancement.class);
        if (serializedAdvancement != null)
            net.minecraft.server.v1_13_R2.AdvancementDataWorld.REGISTRY.a(new HashMap<>(Collections.singletonMap(org.bukkit.craftbukkit.v1_13_R2.util.CraftNamespacedKey.toMinecraft(key), serializedAdvancement)));
    }

    public void removeAdvancement_1_13_R2(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(id);
        for (Map.Entry entry : net.minecraft.server.v1_13_R2.AdvancementDataWorld.REGISTRY.advancements.entrySet()) {
            if (((net.minecraft.server.v1_13_R2.Advancement) entry.getValue()).getName().getKey().equals(advancement.getKey().getKey().toLowerCase())) {
                net.minecraft.server.v1_12_R1.AdvancementDataWorld.REGISTRY.advancements.remove(entry.getKey());
                break;
            }
        }
    }

    public void loadAdvancement_1_14_R1(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_14_R1.Advancement.SerializedAdvancement serializedAdvancement = (net.minecraft.server.v1_14_R1.Advancement.SerializedAdvancement) net.minecraft.server.v1_14_R1.ChatDeserializer.a(net.minecraft.server.v1_14_R1.AdvancementDataWorld.DESERIALIZER, json, net.minecraft.server.v1_14_R1.Advancement.SerializedAdvancement.class);
        if (serializedAdvancement != null)
            try {
                net.minecraft.server.v1_14_R1.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_14_R1.CraftServer) Bukkit.getServer()).getServer();
                net.minecraft.server.v1_14_R1.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
                advancements.a(new HashMap<>(Collections.singletonMap(org.bukkit.craftbukkit.v1_14_R1.util.CraftNamespacedKey.toMinecraft(key), serializedAdvancement)));
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
    }

    public void removeAdvancement_1_14_R1(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(id);
        try {
            net.minecraft.server.v1_14_R1.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_14_R1.CraftServer) Bukkit.getServer()).getServer();
            net.minecraft.server.v1_14_R1.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
            for (Map.Entry entry : advancements.advancements.entrySet()) {
                if (((net.minecraft.server.v1_14_R1.Advancement) entry.getValue()).getName().getKey().equals(advancement.getKey().getKey().toLowerCase())) {
                    advancements.advancements.remove(entry.getKey());
                    break;
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void loadAdvancement_1_15_R1(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_15_R1.Advancement.SerializedAdvancement serializedAdvancement = (net.minecraft.server.v1_15_R1.Advancement.SerializedAdvancement) net.minecraft.server.v1_15_R1.ChatDeserializer.a(net.minecraft.server.v1_15_R1.AdvancementDataWorld.DESERIALIZER, json, net.minecraft.server.v1_15_R1.Advancement.SerializedAdvancement.class);
        if (serializedAdvancement != null)
            try {
                net.minecraft.server.v1_15_R1.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_15_R1.CraftServer) Bukkit.getServer()).getServer();
                net.minecraft.server.v1_15_R1.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
                advancements.a(new HashMap<>(Collections.singletonMap(org.bukkit.craftbukkit.v1_15_R1.util.CraftNamespacedKey.toMinecraft(key), serializedAdvancement)));
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
    }

    public void removeAdvancement_1_15_R1(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(id);
        try {
            net.minecraft.server.v1_15_R1.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_15_R1.CraftServer) Bukkit.getServer()).getServer();
            net.minecraft.server.v1_15_R1.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
            for (Map.Entry entry : advancements.advancements.entrySet()) {
                if (((net.minecraft.server.v1_15_R1.Advancement) entry.getValue()).getName().getKey().equals(advancement.getKey().getKey().toLowerCase())) {
                    advancements.advancements.remove(entry.getKey());
                    break;
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void loadAdvancement_1_16_R1(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_16_R1.MinecraftKey minecraftKey = org.bukkit.craftbukkit.v1_16_R1.util.CraftNamespacedKey.toMinecraft(key);
        JsonElement jsonElement = (JsonElement) net.minecraft.server.v1_16_R1.AdvancementDataWorld.DESERIALIZER.fromJson(json, JsonElement.class);
        JsonObject jsonObject = net.minecraft.server.v1_16_R1.ChatDeserializer.m(jsonElement, "advancement");
        net.minecraft.server.v1_16_R1.Advancement.SerializedAdvancement serializedAdvancement = net.minecraft.server.v1_16_R1.Advancement.SerializedAdvancement.a(jsonObject, new net.minecraft.server.v1_16_R1.LootDeserializationContext(minecraftKey, net.minecraft.server.v1_16_R1.MinecraftServer.getServer().aI()));
        if (serializedAdvancement != null)
            (net.minecraft.server.v1_16_R1.MinecraftServer.getServer().getAdvancementData()).REGISTRY.a(Maps.newHashMap(Collections.singletonMap(minecraftKey, serializedAdvancement)));
    }

    public void removeAdvancement_1_16_R1(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(advancement.getKey());
        try {
            net.minecraft.server.v1_16_R1.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_16_R1.CraftServer) Bukkit.getServer()).getServer();
            net.minecraft.server.v1_16_R1.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
            for (Map.Entry entry : advancements.advancements.entrySet()) {
                if (((net.minecraft.server.v1_16_R1.Advancement) entry.getValue()).getName().equals(advancement.getKey().getKey().toLowerCase()))
                    ;
                advancements.advancements.remove(entry.getKey());
                break;
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }

    public void loadAdvancement_1_16_R2(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_16_R2.MinecraftKey minecraftKey = org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey.toMinecraft(key);
        JsonElement jsonElement = net.minecraft.server.v1_16_R2.AdvancementDataWorld.DESERIALIZER.fromJson(json, JsonElement.class);
        JsonObject jsonObject = net.minecraft.server.v1_16_R2.ChatDeserializer.m(jsonElement, "advancement");
        net.minecraft.server.v1_16_R2.Advancement.SerializedAdvancement serializedAdvancement = net.minecraft.server.v1_16_R2.Advancement.SerializedAdvancement.a(jsonObject, new net.minecraft.server.v1_16_R2.LootDeserializationContext(minecraftKey, net.minecraft.server.v1_16_R2.MinecraftServer.getServer().getLootPredicateManager()));
        if (serializedAdvancement != null)
            (net.minecraft.server.v1_16_R2.MinecraftServer.getServer().getAdvancementData()).REGISTRY.a(Maps.newHashMap(Collections.singletonMap(minecraftKey, serializedAdvancement)));
    }

    public void removeAdvancement_1_16_R2(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(advancement.getKey());
        try {
            net.minecraft.server.v1_16_R2.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_16_R2.CraftServer) Bukkit.getServer()).getServer();
            net.minecraft.server.v1_16_R2.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
            for (Map.Entry entry : advancements.advancements.entrySet()) {
                if (((net.minecraft.server.v1_16_R2.Advancement) entry.getValue()).getName().equals(advancement.getKey().getKey().toLowerCase()))
                    ;
                advancements.advancements.remove(entry.getKey());
                break;
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }


    public void loadAdvancement_1_16_R3(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        net.minecraft.server.v1_16_R3.MinecraftKey minecraftKey = org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey.toMinecraft(key);
        JsonElement jsonElement = (JsonElement)net.minecraft.server.v1_16_R3.AdvancementDataWorld.DESERIALIZER.fromJson(json, JsonElement.class);
        JsonObject jsonObject = net.minecraft.server.v1_16_R3.ChatDeserializer.m(jsonElement, "advancement");
        net.minecraft.server.v1_16_R3.Advancement.SerializedAdvancement serializedAdvancement = net.minecraft.server.v1_16_R3.Advancement.SerializedAdvancement.a(jsonObject, new net.minecraft.server.v1_16_R3.LootDeserializationContext(minecraftKey, net.minecraft.server.v1_16_R3.MinecraftServer.getServer().getLootPredicateManager()));
        if (serializedAdvancement != null)
            (net.minecraft.server.v1_16_R3.MinecraftServer.getServer().getAdvancementData()).REGISTRY.a(Maps.newHashMap(Collections.singletonMap(minecraftKey, serializedAdvancement)));
    }

    public void removeAdvancement_1_16_R3(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(advancement.getKey());
        try {
            net.minecraft.server.v1_16_R3.DedicatedServer dedicatedServer = ((org.bukkit.craftbukkit.v1_16_R3.CraftServer)Bukkit.getServer()).getServer();
            net.minecraft.server.v1_16_R3.Advancements advancements = (dedicatedServer.getAdvancementData()).REGISTRY;
            for (Map.Entry entry : advancements.advancements.entrySet()) {
                if (((net.minecraft.server.v1_16_R3.Advancement)entry.getValue()).getName().getKey().equals(advancement.getKey().getKey().toLowerCase())) {
                    advancements.advancements.remove(entry.getKey());
                    break;
                }
            }
        } catch (Exception|Error exception) {
            exception.printStackTrace();
        }
    }

    public void loadAdvancement_1_17_R1(NamespacedKey key, String json) {
        if (Bukkit.getAdvancement(key) != null)
            return;
        MinecraftKey minecraftKey = CraftNamespacedKey.toMinecraft(key);
        JsonElement jsonElement = (JsonElement)net.minecraft.server.AdvancementDataWorld.b.fromJson(json, JsonElement.class);
        JsonObject jsonObject = ChatDeserializer.m(jsonElement, "advancement");
        net.minecraft.advancements.Advancement.SerializedAdvancement serializedAdvancement = net.minecraft.advancements.Advancement.SerializedAdvancement.a(jsonObject, new LootDeserializationContext(minecraftKey, MinecraftServer.getServer().getLootPredicateManager()));
        if (serializedAdvancement != null)
            (MinecraftServer.getServer().getAdvancementData()).c.a(Maps.newHashMap(Collections.singletonMap(minecraftKey, serializedAdvancement)));
    }

    public void removeAdvancement_1_17_R1(Advancement advancement) {
        Bukkit.getUnsafe().removeAdvancement(advancement.getKey());
        try {
            DedicatedServer dedicatedServer = ((CraftServer)Bukkit.getServer()).getServer();
            Advancements advancements = (dedicatedServer.getAdvancementData()).c;
            for (Map.Entry entry : advancements.b.entrySet()) {
                if (((net.minecraft.advancements.Advancement)entry.getValue()).getName().getKey().equals(advancement.getKey().getKey().toLowerCase())) {
                    advancements.b.remove(entry.getKey());
                    break;
                }
            }
        } catch (Exception|Error exception) {
            exception.printStackTrace();
        }
    }

    /*private void remove() {
        Bukkit.getUnsafe().removeAdvancement(id);
        Bukkit.reloadData();
    }*/

    //给予成就
    /*private void grant(Player p) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        progress = p.getAdvancementProgress(advancement);
        if (!progress.isDone()) {
            for(String criteria : progress.getRemainingCriteria()){
                {
                    progress.awardCriteria(criteria);
                }
            }
        }
    }*/

    public AdvancementAPI grant(Player... p) {
        Advancement advancement = getAdvancement();
        byte b;
        int i;
        Player[] players;
        for (i = (players = p).length, b = 0; b < i; ) {
            Player player = players[b];
            if (!player.getAdvancementProgress(advancement).isDone()) {
                Collection<String> collection = player.getAdvancementProgress(advancement).getRemainingCriteria();
                for (String str : collection)
                    player.getAdvancementProgress(getAdvancement()).awardCriteria(str);
            }
            b++;
        }
        return this;
    }


/*
    //撤销成就
    public void revoke(Player p) {
        Advancement advancement = Bukkit.getAdvancement(id);
        AdvancementProgress progress;
        progress = p.getAdvancementProgress(advancement);
        if (progress.isDone()) {
            new BukkitRunnable(){
                @Override
                public void run() {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"advancement revoke " + p.getName() + " only " +  id);
                }
            }.runTask(pl);
        }
    }*/

    public AdvancementAPI revoke(Player... p) {
        Advancement advancement = getAdvancement();
        if (advancement == null)
            return this;
        byte b;
        int i;
        Player[] players;
        for (i = (players = p).length, b = 0; b < i; ) {
            Player player = players[b];
            if (player.getAdvancementProgress(advancement) != null && player.getAdvancementProgress(advancement).isDone()) {
                Collection<String> collection = player.getAdvancementProgress(advancement).getAwardedCriteria();
                for (String str : collection)
                    player.getAdvancementProgress(getAdvancement()).revokeCriteria(str);
            }
            b++;
        }
        return this;
    }

    public String getJson() {
        JsonObject json = new JsonObject();
        JsonObject icon = new JsonObject();
        icon.addProperty("item", this.icon);
        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.addProperty("title", this.title);
        display.addProperty("description", this.description);
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame", this.frame);
        display.addProperty("announce_to_chat", announce);
        display.addProperty("show_toast", toast);
        display.addProperty("hidden", true);

        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();

        trigger.addProperty("trigger", "minecraft:impossible");
        criteria.add("impossible", trigger);

        json.add("criteria", criteria);
        json.add("display", display);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }


    public void sendAdvancement(Object p) {
        Player player = (Player) p;
        add();
        grant(player);
        Bukkit.getScheduler().runTaskLater(pl, () -> {
            revoke(player);
            remove();
        }, 20L);
    }
}
