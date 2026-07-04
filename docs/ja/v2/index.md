---
title: V2 ドキュメント
---

# v2 ドキュメント {#v2-docs}

ZMusic ヘルプドキュメントへようこそ。ここには必要なすべてのヘルプがあります。音声が再生されないなどの問題をトラブルシューティングする必要がある場合は、[こちら](/ja/v2/faq)をクリックしてください。

## 紹介 {#introduction}

これは強力な音楽システムで、以下の機能をサポートしています。

- 全サーバーでのリクエスト
- 個別再生
- 歌詞表示
- 歌詞翻訳表示
- 複数の検索ソース（NetEase/Bilibili）
- キーワード検索
- 個人プレイリスト
- 全サーバープレイリスト
- プレイリスト再生（NetEase）
- 音量調整（1.12以降対応）
- BungeeCord 対応

## ダウンロード {#download}

### GitHub {#download-github}

<DownloadTable />

### その他のチャンネル {#download-other}

<ExternalLinks />

## Mod ダウンロード {#download-mod}

### GitHub {#download-mod-github}

<ModDownload />

### その他のチャンネル {#download-mod-other}

<ModExternalLinks />

## BungeeCord について {#bungeecord}

- BungeeCord 側のみに ZMusic-Plugin プラグインをインストールする必要があります（子サーバーには不要）
- BungeeCord は現在 1.9-1.19 のサーバーのみサポートしています
- BungeeCord は一時的に経済システムをサポートしていません
- 子サーバーで Papi 変数や進行状況表示などの機能を使用するには、子サーバーに ZMusic-Addon プラグインをインストールするだけです

## 動画 {#video}

[クリックして Bilibili でデモ動画を見る](https://www.bilibili.com/video/av92156922)

## フィードバック {#feedback}

- [GitHub](https://github.com/starhui-dev/zmusic-plugin) で Issues を提出
- QQ グループ：[1032722724](https://jq.qq.com/?_wv=1027&k=5oIs7cc) でフィードバック

# コマンド {#commands}

## メインコマンド {#main-commands}

- `/zm` メインコマンド
- `/zm help` ヘルプを表示
- `/zm play` 音楽を再生
- `/zm music` 全サーバーリクエスト
- `/zm search` 音楽を検索
- `/zm playlist` プレイリストシステム
- `/zm stop` 再生を停止
- `/zm login` NetEase Cloud Music にログイン

## 再生 {#play}

曲名で音楽を検索し、直接再生します。

### コマンド {#command}

`/zm play [検索ソース] [曲名]`

[検索ソースの説明](#search-source)

### 例 {#example}

`/zm play netease 你的猫咪`

## リクエスト {#music}

曲名で音楽を検索し、全サーバーに送信後、プレイヤーがクリックして再生します。

### コマンド {#command-1}

`/zm music [検索ソース] [曲名]`

[検索ソースの説明](#search-source) [曲名 ID 化の説明](#song-id)

### 例 {#example-1}

`/zm music netease 你的猫咪`

## 検索 {#search}

曲名で音楽を検索し、10 曲のリストを返します。

### コマンド {#command-2}

`/zm search [検索ソース] [曲名]`

[検索ソースの説明](#search-source) [曲名 ID 化の説明](#song-id)

### 例 {#example-2}

`/zm search netease 你的猫咪`

## プレイリスト {#playlist}

プレイリストをインポートしてサーバーに保存し、プレイリストの再生を便利にします。

### コマンド {#command-3}

`/zm playlist [プラットフォーム] [サブコマンド]`

現在以下のプラットフォームをサポートしています。

- netease/163 - NetEase Cloud Music

プラットフォームが type の場合、プレイリストの再生方法を設定します。現在サポートしているもの:

- normal - 順次再生
- loop - ループ再生
- random - ランダム再生

例:
`/zm playlist type random`

プラットフォームが global の場合、グローバルプレイリストモードです。

- サブコマンドは通常モードと同じ

例:
`/zm playlist global netease list`

`サブコマンド` は対応するプラットフォームのサブコマンドです。

- `import` プレイリストのリンクを通じてプレイリストをインポート
- パラメータ: `プレイリストリンク`
- `list` 指定したプラットフォームのプレイリスト一覧を取得
- `play` プレイリスト ID でプレイリストを再生（list で取得可能）
- パラメータ: `プレイリストID`

### 例

インポート:

- `/zm playlist 163 import https://music.163.com/#/playlist?id=363046232`

再生:

- `/zm playlist 163 play 363046232`

## 管理者 {#admin}

管理者関連の操作、全サーバー強制再生、設定の再読み込みなどです。

### コマンド {#command-4}

`/zm playall [検索ソース] [曲名]` 全サーバー強制再生
`/zm stopAll` 全サーバー再生を強制停止
`/zm reload` 設定ファイルを再読み込み

[検索ソースの説明](#search-source) [曲名 ID 化の説明](#song-id)

### 例 {#example-4}

`/zm playAll netease 你的猫咪`

# 権限 {#permissions}

## 一般プレイヤー権限 {#player-permissions}

`zmusic.use` play、stop などの一般コマンドを使用可能

## 管理者権限 {#admin-permissions}

`zmusic.admin` playAll、stopAll などの管理者コマンドを使用可能

# 設定ファイル {#configuration}

```json
{
  // 設定ファイルのバージョン（変更しないでください）
  "version": 11,
  // アップデートを確認するかどうか
  "check-update": true,
  // プラグインのメッセージプレフィックス
  "prefix": "&bZMusic &e>>> &r",
  // デバッグモードを有効にするかどうか
  "debug": false,
  // API設定
  "api": {
    // NetEase Cloud Music APIアドレス
    //
    // オープンソースプロジェクトNeteaseCloudMusicApiを使用
    // 自己デプロイを推奨、Node.js環境が必要
    // URL: https://github.com/Binaryify/NeteaseCloudMusicApi
    "netease": "https://ncm.zhenxin.me/"
  },
  // 作者のNetEase Cloud Musicアカウントをフォローするかどうか
  "netease-follow": true,
  // ZMusic VIP 設定
  "vip": {
    // 認証アカウント
    "account": "1307993674",
    "secret": "none"
  },
  // リクエスト設定
  "music": {
    // リクエストで差し引かれるコイン（0に設定すると差し引かれません）
    // zmusic.bypassを持つプレイヤーは差し引かれません
    "money": 10,
    // リクエストのクールダウン時間（0に設定するとクールダウンなし）
    // zmusic.bypassを持つプレイヤーはクールダウンなし
    "cooldown": 5
  },
  // 歌詞設定
  "lyric": {
    // 歌詞を有効にするかどうか
    "enable": true,
    // 歌詞翻訳を表示するかどうか
    "showLyricTr": true,
    // 歌詞の色
    "color": "&b",
    // 以下は表示方法の設定、同時に有効にできます
    // BossBarで歌詞を表示するかどうか（1.8以下は非対応）
    "bossBar": true,
    // ActionBarで歌詞を表示するかどうか
    "actionBar": false,
    // Titleで歌詞を表示するかどうか
    "subTitle": false,
    // チャットメッセージで歌詞を表示するかどうか
    "chatMessage": false,
    // Hud 設定（1.12以降のみ対応）
    "hud": {
      // Hudを有効にするかどうか
      "enable": true,
      // 情報のX座標
      "infoX": 2,
      // 情報のY座標
      "infoY": 12,
      // 歌詞のX座標
      "lyricX": 2,
      // 歌詞のY座標
      "lyricY": 72
    }
  }
}
```

# 変数 {#variables}

- `%zmusic_playing_name%` 現在再生中の曲名を取得
- `%zmusic_playing_singer%` 現在再生中の歌手を取得
- `%zmusic_playing_lyric%` 現在の時間に表示される歌詞を取得
- `%zmusic_time_current%` 現在再生中の音楽の時間を取得
- `%zmusic_time_max%` 現在再生中の音楽の最大時間を取得
- `%zmusic_playing_platform%` 現在再生中の音楽のプラットフォームを取得
- `%zmusic_playing_source%` 現在再生中の音楽のソースを取得

# 前提プラグイン {#dependencies}

## 全バージョンで使用 {#all-versions}

- [`PlaceholderAPI`](https://www.spigotmc.org/resources/placeholderapi.6245/) [オプション] 上記の変数を使用する場合にインストール
- [`Vault`](https://www.spigotmc.org/resources/vault.34315/) [オプション] リクエスト課金を使用する場合にインストール

## 1.5, 1.6 バージョンで使用 {#version-1-5-1-6}

- ~~[`AudioBuffer`](https://www.mcbbs.net/thread-832205-1-1.html) [必須] 音楽再生に使用、スレッドに対応する Mod があり、クライアントにインストールが必要~~

## 1.4 以下のバージョンで使用 {#version-1-4-below}

- ~~[`AudioBuffer`](https://www.mcbbs.net/thread-832205-1-1.html) [必須] 音楽再生に使用、スレッドに対応する Mod があり、クライアントにインストールが必要~~
- ~~[`BossBarAPI`](https://www.mcbbs.net/thread-729531-1-1.html) [オプション] BossBar で歌詞を表示する場合にインストール~~
- ~~[`ActionBarAPI`](https://www.spigotmc.org/resources/actionbarapi-1-8-1-14-2.1315/) [オプション] ActionBar で歌詞を表示する場合にインストール~~

# 検索ソースの説明 {#search-source}

`検索ソース` は音楽を検索するプラットフォームです。現在以下のプラットフォームをサポートしています。

- netease/163 - NetEase Cloud Music
- kuwo - Kuwo Music
- bilibili - Bilibili Music

**QQ Music API は完全に削除されました。Kugou Music は再生時に問題が発生する可能性があります**

# 曲名 ID 化の説明 {#song-id}

- 曲名を `-id:音楽ID` に置き換えるだけ
- 現在 NetEase と Bilibili Music をサポート
- 例: `/zm play bilibili -id:374305`
