package me.zhenxin.zmusic.command;

import me.zhenxin.zmusic.ZMusic;
import me.zhenxin.zmusic.api.ProgressBar;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.config.LoadConfig;
import me.zhenxin.zmusic.data.PlayerData;
import me.zhenxin.zmusic.language.LoadLang;
import me.zhenxin.zmusic.login.NeteaseLogin;
import me.zhenxin.zmusic.music.PlayList;
import me.zhenxin.zmusic.music.PlayListPlayer;
import me.zhenxin.zmusic.music.PlayMusic;
import me.zhenxin.zmusic.music.SearchMusic;
import me.zhenxin.zmusic.notice.NoticeService;
import me.zhenxin.zmusic.utils.HelpUtils;
import me.zhenxin.zmusic.utils.OtherUtils;
import me.zhenxin.zmusic.utils.Vault;
import me.zhenxin.zmusic.login.LoginMethod;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Cmd {

    static Map<Object, Integer> cooldown = new ConcurrentHashMap<>();
    static List<Object> cooldownStats = Collections.synchronizedList(new ArrayList<>());

    public static boolean cmd(Object sender, String[] args) { // 指令输出
        if (ZMusic.isEnableEd) {
            if (ZMusic.isEnable) {
                if (args.length > 0 && args[0].equalsIgnoreCase("notice")) {
                    handleNoticeCommand(sender, args);
                    return true;
                }
                boolean isUse;
                boolean isAdmin;
                boolean isPlayAll;
                if (ZMusic.player.isPlayer(sender)) {
                    isUse = ZMusic.player.hasPermission(sender, "zmusic.use");
                    isAdmin = ZMusic.player.hasPermission(sender, "zmusic.admin");
                    isPlayAll = ZMusic.player.hasPermission(sender, "zmusic.playall");
                } else {
                    isUse = true;
                    isAdmin = true;
                    isPlayAll = true;
                }
                if (isUse) {
                    if (args.length == 0) {
                        ZMusic.message.sendNull(sender);
                    } else {
                        switch (args[0].toLowerCase()) {
                            case "music":
                                handleMusicCommand(sender, args);
                                break;
                            case "play":
                                handlePlayCommand(sender, args);
                                break;
                            case "search":
                                handleSearchCommand(sender, args);
                                break;
                            case "stop":
                                handleStopCommand(sender);
                                break;
                            case "loop":
                                handleLoopCommand(sender);
                                break;
                            case "playlist":
                                handlePlaylistCommand(sender, args);
                                break;
                            case "url":
                                handleUrlCommand(sender, args);
                                break;
                            case "playall":
                                handlePlayAllCommand(sender, args, isPlayAll, isAdmin);
                                break;
                            case "stopall":
                                handleStopAllCommand(sender, isAdmin);
                                break;
                            case "login":
                                handleLoginCommand(sender, args, isAdmin);
                                break;
                            case "help":
                                handleHelpCommand(sender, args);
                                break;
                            case "reload":
                                handleReloadCommand(sender, isAdmin);
                                break;
                            case "update":
                                handleUpdateCommand(sender, isAdmin);
                                break;
                            case "test":
                                handleTestCommand(sender);
                                break;
                            default:
                                ZMusic.message.sendNull(sender);
                                break;
                        }
                    }
                } else {
                    ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.use 权限此使用命令.", sender);
                }
            } else {
                ZMusic.message.sendErrorMessage("错误: 请删除AudioBuffer/AllMusic插件.", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 请等待插件加载完毕.", sender);
        }
        return true;
    }

    public static List<String> tab(Object sender, String[] args) {
        boolean isAdmin;
        if (ZMusic.player.isPlayer(sender)) {
            isAdmin = ZMusic.player.hasPermission(sender, "zmusic.admin");
        } else {
            isAdmin = true;
        }

        String[] commandList = new String[0];
        if (args.length == 0) {
            return new ArrayList<>();
        } else if (args.length >= 1) {
            if (args.length == 1) {
                if (isAdmin) {
                    commandList = new String[] { "help", "play", "playlist", "music", "stop", "loop", "login",
                            "search", "url", "playAll", "stopAll", "update", "reload" };
                } else {
                    commandList = new String[] { "help", "play", "playlist", "music", "stop", "loop", "search",
                            "url" };
                }
                return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
            } else if (args[0].equalsIgnoreCase("play") ||
                    args[0].equalsIgnoreCase("music") ||
                    args[0].equalsIgnoreCase("search") ||
                    args[0].equalsIgnoreCase("playAll")) {
                if (args.length == 2) {
                    commandList = new String[] { "qq", "163", "netease", "kuwo", "bilibili" };
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                }
                return new ArrayList<>();
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 2) {
                    if (isAdmin) {
                        commandList = new String[] { "play", "playlist", "music", "search", "url", "admin" };
                    } else {
                        commandList = new String[] { "play", "playlist", "music", "search", "url" };
                    }
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                }
                return new ArrayList<>();
            } else if (args[0].equalsIgnoreCase("playlist")) {
                if (args.length == 2) {
                    commandList = new String[] { "qq", "netease", "163", "type", "global", "next", "prev", "jump" };
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("type")) {
                        commandList = new String[] { "random", "normal", "loop" };
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[2]))
                                .collect(Collectors.toList());
                    } else if (args[1].equalsIgnoreCase("global")) {
                        commandList = new String[] { "qq", "netease", "163" };
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[2]))
                                .collect(Collectors.toList());
                    } else {
                        commandList = new String[] { "import", "play", "list", "update", "show" };
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[2]))
                                .collect(Collectors.toList());
                    }
                } else if (args.length == 4) {
                    if (args[2].equalsIgnoreCase("qq") ||
                            args[2].equalsIgnoreCase("163") ||
                            args[2].equalsIgnoreCase("netease")) {
                        commandList = new String[] { "import", "play", "list", "update", "show" };
                        return Arrays.stream(commandList).filter(s -> s.startsWith(args[3]))
                                .collect(Collectors.toList());
                    }
                    return new ArrayList<>();
                }
                return new ArrayList<>();
            } else if (args[0].equalsIgnoreCase("login")) {
                if (!isAdmin) {
                    return new ArrayList<>();
                }
                if (args.length == 2) {
                    commandList = new String[] { "qr", "phone", "email", "sendcode", "verify", "status", "raw" };
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[1])).collect(Collectors.toList());
                } else if (args.length == 3 && (args[1].equalsIgnoreCase("phone")
                        || args[1].equalsIgnoreCase("sendcode")
                        || args[1].equalsIgnoreCase("verify"))) {
                    commandList = new String[] { "+86", "+852", "+853", "+886" };
                    return Arrays.stream(commandList).filter(s -> s.startsWith(args[2])).collect(Collectors.toList());
                }
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }
        return Arrays.stream(commandList).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
    }

    private static void handleMusicCommand(Object sender, String[] args) {
        // Extracted logic for "music" command
        if (ZMusic.player.isPlayer(sender)) {
            if (args.length >= 2) {
                int cooldownSec = Config.cooldown;
                Runnable startPlay = () -> {
                    List<Object> players = ZMusic.player.getOnlinePlayerList();
                    PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "music", players);
                };
                if (!ZMusic.player.hasPermission(sender, "zmusic.bypass")) {
                    if (!cooldownStats.contains(sender)) {
                        if (!takeMoneyIfNecessary(sender)) {
                            return;
                        }
                        ZMusic.runTask.runAsync(startPlay);
                        if (cooldownSec > 0) {
                            cooldownStats.add(sender);
                            cooldown.put(sender, cooldownSec);
                            Timer timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    int sec = cooldown.get(sender);
                                    if (sec != 1) {
                                        sec--;
                                        cooldown.put(sender, sec);
                                    } else {
                                        cooldownStats.remove(sender);
                                        cancel();
                                    }
                                }
                            };
                            timer.schedule(timerTask, 1000L, 1000L);
                        }
                    } else {
                        ZMusic.message.sendErrorMessage("冷却时间未到,还有§e " + cooldown.get(sender) + "§c 秒", sender);
                    }
                } else {
                    ZMusic.runTask.runAsync(startPlay);
                }
            } else {
                HelpUtils.sendHelp("music", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static boolean takeMoneyIfNecessary(Object sender) {
        if (ZMusic.isBC || ZMusic.isVelocity || !Config.realSupportVault || Config.money <= 0) {
            return true;
        }
        return Vault.take(sender);
    }

    private static void handlePlayCommand(Object sender, String[] args) {
        // Extracted logic for "play" command
        if (ZMusic.player.isPlayer(sender)) {
            if (args.length >= 2) {
                ZMusic.runTask.runAsync(() -> {
                    PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "self", null);
                });
            } else {
                HelpUtils.sendHelp("play", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void handleSearchCommand(Object sender, String[] args) {
        // Extracted logic for "search" command
        if (ZMusic.player.isPlayer(sender)) {
            if (args.length >= 2) {
                ZMusic.runTask.runAsync(() -> {
                    SearchMusic.sendList(OtherUtils.argsXin1(args), args[1], sender);
                });
            } else {
                HelpUtils.sendHelp("search", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void handleStopCommand(Object sender) {
        // Extracted logic for "stop" command
        if (ZMusic.player.isPlayer(sender)) {
            PlayListPlayer plp = PlayerData.getPlayerPlayListPlayer(sender);
            if (plp != null) {
                plp.isStop = true;
                PlayerData.setPlayerPlayListPlayer(sender, null);
            }
            OtherUtils.resetPlayerStatus(sender);
            ZMusic.message.sendNormalMessage("停止播放音乐成功!", sender);
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void handleLoopCommand(Object sender) {
        // Extracted logic for "loop" command
        if (ZMusic.player.isPlayer(sender)) {
            if (PlayerData.getPlayerLoopPlay(sender) != null && PlayerData.getPlayerLoopPlay(sender)) {
                PlayerData.setPlayerLoopPlay(sender, false);
                ZMusic.message.sendNormalMessage("循环播放已关闭!", sender);
            } else {
                PlayerData.setPlayerLoopPlay(sender, true);
                ZMusic.message.sendNormalMessage("循环播放已开启!", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void handlePlaylistCommand(Object sender, String[] args) {
        // Extracted logic for "playlist" command
        if (ZMusic.player.isPlayer(sender)) {
            if (args.length >= 2) {
                ZMusic.runTask.runAsync(() -> {
                    PlayList.subCommand(args, sender);
                });
            } else {
                HelpUtils.sendHelp("playlist", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void handleUrlCommand(Object sender, String[] args) {
        // Extracted logic for "url" command
        if (ZMusic.player.isPlayer(sender)) {
            if (args.length == 2) {
                ZMusic.runTask.runAsync(() -> {
                    ZMusic.music.stop(sender);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ZMusic.music.play(args[1], sender);
                    ZMusic.message.sendNormalMessage("播放成功!", sender);
                });
            } else {
                HelpUtils.sendHelp("url", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void handlePlayAllCommand(Object sender, String[] args, boolean isPlayAll, boolean isAdmin) {
        // Extracted logic for "playall" command
        if (isPlayAll || isAdmin) {
            List<Object> players = ZMusic.player.getOnlinePlayerList();
            if (args.length >= 2) {
                ZMusic.runTask.runAsync(() -> {
                    PlayMusic.play(OtherUtils.argsXin1(args), args[1], sender, "all", players);
                });
            } else {
                HelpUtils.sendHelp("admin", sender);
            }
        } else {
            ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
        }
    }

    private static void handleStopAllCommand(Object sender, boolean isAdmin) {
        // Extracted logic for "stopall" command
        if (isAdmin) {
            List<Object> players = ZMusic.player.getOnlinePlayerList();
            for (Object player : players) {
                OtherUtils.resetPlayerStatus(player);
            }
            ZMusic.message.sendNormalMessage("强制全部玩家停止播放音乐成功!", sender);
        } else {
            ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
        }
    }

    private static void handleLoginCommand(Object sender, String[] args, boolean isAdmin) {
        if (!isAdmin) {
            ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
            return;
        }

        if (args.length < 2) {
            ZMusic.message.sendErrorMessage("用法: /zm login <qr|phone|email|sendcode|verify|status|raw>", sender);
            return;
        }

        LoginMethod loginMethod = LoginMethod.fromToken(args[1]);
        if (loginMethod == null) {
            if ("status".equalsIgnoreCase(args[1])) {
                handleLoginStatus(sender);
                return;
            }
            ZMusic.message.sendErrorMessage("未知登录子命令: " + args[1], sender);
            ZMusic.message.sendErrorMessage("用法: /zm login <qr|phone|email|sendcode|verify|status|raw>", sender);
            return;
        }

        // NOTE: case "status" is handled separately above.
        switch (loginMethod) {
            case QR:
                loginByQRCode(sender);
                break;
            case PHONE:
                ParsedPhoneInput phoneLoginInput = parseCtCodeAndPhone(args, 2, sender,
                        "用法: /zm login phone +<ctcode=86> [phone] [password]");
                if (phoneLoginInput == null || args.length <= phoneLoginInput.nextIndex) {
                    ZMusic.message.sendErrorMessage("用法: /zm login phone +<ctcode=86> [phone] [password]", sender);
                    return;
                }

                loginByPhone(sender, phoneLoginInput.phone, args[phoneLoginInput.nextIndex], phoneLoginInput.ctCode);
                break;
            case EMAIL:
                if (args.length < 4) {
                    ZMusic.message.sendErrorMessage("用法: /zm login email [username] [password]", sender);
                    return;
                }
                loginByEmail(sender, args[2], args[3]);
                break;
            case SEND_CODE:
                ParsedPhoneInput sendCodeInput = parseCtCodeAndPhone(args, 2, sender,
                        "用法: /zm login sendcode +<ctcode=86> [phone]");
                if (sendCodeInput == null) {
                    return;
                }

                loginSendCode(sender, sendCodeInput.phone, sendCodeInput.ctCode);
                break;
            case VERIFY:
                ParsedPhoneInput verifyInput = parseCtCodeAndPhone(args, 2, sender,
                        "用法: /zm login verify +<ctcode=86> [phone] [code]");
                if (verifyInput == null || args.length <= verifyInput.nextIndex) {
                    ZMusic.message.sendErrorMessage("用法: /zm login verify +<ctcode=86> [phone] [code]", sender);
                    return;
                }

                loginByVerifyCode(sender, verifyInput.phone, verifyInput.ctCode, args[verifyInput.nextIndex]);
                break;
            case RAW:
                if (args.length < 3) {
                    String rawHelpMsg = "用法: /zm login raw [cookie1|cookie2|...]\n" +
                            "可以在浏览器控制台执行下面的命令: \n" +
                            "console.log(document.cookie);\n" +
                            "然后粘贴。\n" +
                            "如果超过256字符上限，请粘贴到命令方块内。\n\n" +
                            "有效cookie的字段有: \n" +
                            "MUSIC_R_T; MUSIC_U; MUSIC_A_T; __csrf; MUSIC_SNS; MUSIC_R_U; __remember_me";

                    ZMusic.message.sendErrorMessage(rawHelpMsg, sender);
                    return;
                }

                String rawCookies = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                NeteaseLogin.loginRaw(rawCookies);
                ZMusic.message.sendNormalMessage("Cookies 已提交处理。", sender);
                break;
            default:
                ZMusic.message.sendErrorMessage("未知登录方式", sender);
                break;
        }
    }

    private static void handleLoginStatus(Object sender) {
        String nickname = NeteaseLogin.nickname();
        if (nickname.isEmpty()) {
            ZMusic.message.sendNormalMessage("您当前未登录网易云音乐。", sender);
        } else {
            ZMusic.message.sendNormalMessage("您已登录网易云音乐，昵称: " + nickname, sender);
        }
    }

    private static void handleHelpCommand(Object sender, String[] args) {
        // Extracted logic for "help" command
        if (args.length == 2) {
            HelpUtils.sendHelp(args[1], sender);
        } else {
            HelpUtils.sendHelp("main", sender);
        }
    }

    private static void handleReloadCommand(Object sender, boolean isAdmin) {
        // Extracted logic for "reload" command
        if (isAdmin) {
            new LoadConfig().reload(sender);
            ZMusic.runTask.runAsync(() -> {
                new LoadLang().load();
                if (ZMusic.notice != null) {
                    ZMusic.notice.refresh();
                }
            });
        } else {
            ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
        }
    }

    private static void handleNoticeCommand(Object sender, String[] args) {
        if (!ZMusic.player.isPlayer(sender)) {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
            return;
        }
        if (!ZMusic.player.hasPermission(sender, "zmusic.admin")) {
            ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限使用此命令.", sender);
            return;
        }
        if (args.length != 3 || !args[1].equalsIgnoreCase("read")) {
            ZMusic.message.sendErrorMessage("无效的公告操作.", sender);
            return;
        }
        if (ZMusic.notice == null) {
            ZMusic.message.sendErrorMessage("公告功能当前不可用.", sender);
            return;
        }

        NoticeService.MarkReadResult result = ZMusic.notice.markRead(sender, args[2]);
        switch (result) {
            case MARKED:
                ZMusic.message.sendNormalMessage("已标记为已读，之后不再提醒此条公告.", sender);
                break;
            case ALREADY_READ:
                ZMusic.message.sendNormalMessage("此条公告已经标记为已读.", sender);
                break;
            case NOT_FOUND:
                ZMusic.message.sendErrorMessage("公告不存在或已下架.", sender);
                break;
            case FAILED:
                ZMusic.message.sendErrorMessage("保存公告已读状态失败.", sender);
                break;
            default:
                throw new IllegalStateException("未知公告已读结果: " + result);
        }
    }

    private static void handleUpdateCommand(Object sender, boolean isAdmin) {
        // Extracted logic for "update" command
        if (isAdmin) {
            OtherUtils.checkUpdate(sender, true);
        } else {
            ZMusic.message.sendErrorMessage("权限不足，你需要 zmusic.admin 权限此使用命令.", sender);
        }
    }

    private static void handleTestCommand(Object sender) {
        // Extracted logic for "test" command
        if (ZMusic.player.isPlayer(sender)) {
            ZMusic.runTask.runAsync(() -> {
                ProgressBar progressBar = new ProgressBar('■', '□', 100 - 1);
                for (int i = 0; i < 100; i++) {
                    progressBar.setProgress(i);
                    try {
                        ZMusic.message.sendActionBarMessage(progressBar.getString(), sender);
                        Thread.sleep(10);
                    } catch (Exception ignored) {
                    }
                }
            });
        } else {
            ZMusic.message.sendErrorMessage("错误: 该命令只能由玩家使用", sender);
        }
    }

    private static void loginByQRCode(Object sender) {
        ZMusic.runTask.runAsync(() -> {
            try {
                String key = NeteaseLogin.key();
                String qrcode = NeteaseLogin.create(key);
                ZMusic.message.sendNormalMessage("请打开如下网址扫描二维码登录:", sender);
                ZMusic.message.sendNormalMessage(qrcode, sender);
                ZMusic.runTask.runAsync(() -> {
                    boolean cancel = false;
                    while (!cancel) {
                        try {
                            Thread.sleep(3000);
                            int code = NeteaseLogin.check(key);
                            switch (code) {
                                case 800:
                                    ZMusic.message.sendErrorMessage("二维码已过期!", sender);
                                    cancel = true;
                                    break;
                                case 802:
                                    ZMusic.message.sendNormalMessage("请在手机上确认登录!", sender);
                                    break;
                                case 803:
                                    ZMusic.message.sendNormalMessage(
                                            "您已登录网易云音乐, 昵称: " + NeteaseLogin.nickname(),
                                            sender);
                                    cancel = true;
                                    break;
                                default:
                            }
                        } catch (InterruptedException e) {
                            ZMusic.message.sendErrorMessage("登录失败! 请检查后台错误. (中断信号)",
                                    sender);
                            e.printStackTrace();
                            cancel = true;
                        }
                    }
                });
            } catch (UnsupportedEncodingException e) {
                ZMusic.message.sendErrorMessage("登录失败! 请检查后台错误. (不支持的编码)", sender);
                e.printStackTrace();
            } catch (NullPointerException e) {
                ZMusic.message.sendErrorMessage("登录失败! 请检查后台错误. (空指针)", sender);
                e.printStackTrace();
            }
        });
    }

    private static void loginByPhone(Object sender, String username, String password, String ctCode) {
        ZMusic.message.sendErrorMessage("警告: 密码登录容易引发风控，建议使用二维码登录。", sender);
        ZMusic.message.sendErrorMessage("安全提示: 密码可能会被服务器日志记录，请确保服务器安全。", sender);

        String md5 = OtherUtils.md5(password);
        NeteaseLogin.password_phone(username, md5, ctCode, true);
    }

    private static void loginByEmail(Object sender, String username, String password) {
        ZMusic.message.sendErrorMessage("警告: 密码登录容易引发风控，建议使用二维码登录。", sender);
        ZMusic.message.sendErrorMessage("安全提示: 密码可能会被服务器日志记录，请确保服务器安全。", sender);

        String md5 = OtherUtils.md5(password);
        NeteaseLogin.password_email(username, md5, true);
    }

    private static void loginSendCode(Object sender, String phone, String ctCode) {
        NeteaseLogin.sendCode(phone, ctCode);
        ZMusic.message.sendNormalMessage("验证码已发送至: " + phone + ", 请查收后使用 verify 校验", sender);
    }

    private static void loginByVerifyCode(Object sender, String phone, String ctCode, String code) {
        NeteaseLogin.verify(phone, code, ctCode);
        ZMusic.message.sendNormalMessage("验证码验证请求已提交: " + phone, sender);

    }

    private static ParsedPhoneInput parseCtCodeAndPhone(String[] args, int startIndex, Object sender, String usage) {
        if (args.length <= startIndex) {
            ZMusic.message.sendErrorMessage(usage, sender);
            return null;
        }

        String ctCode = "86";
        int phoneIndex = startIndex;
        String firstToken = String.valueOf(args[startIndex]);
        if (firstToken.startsWith("+")) {
            ctCode = firstToken.substring(1);
            phoneIndex = startIndex + 1;
        }

        if (ctCode.isEmpty() || args.length <= phoneIndex) {
            ZMusic.message.sendErrorMessage(usage, sender);
            return null;
        }

        String phone = String.valueOf(args[phoneIndex]);
        if (phone.isEmpty()) {
            ZMusic.message.sendErrorMessage(usage, sender);
            return null;
        }

        return new ParsedPhoneInput(ctCode, phone, phoneIndex + 1);
    }

    private static class ParsedPhoneInput {
        private final String ctCode;
        private final String phone;
        private final int nextIndex;

        private ParsedPhoneInput(String ctCode, String phone, int nextIndex) {
            this.ctCode = ctCode;
            this.phone = phone;
            this.nextIndex = nextIndex;
        }

    }
}
