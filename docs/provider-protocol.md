# ZMusic Provider 协议

本文档定义 ZMusic Plugin 与 ZMusic API 之间的音乐 Provider、账号绑定、搜索和播放协议。通信复用 `zmusic.plugin.v1` WebSocket envelope，业务协议版本为 `1`。

## 设计边界

- Plugin 不包含网易云音乐、QQ 音乐等平台的私有接口实现。
- 第三方平台的应用密钥和用户授权令牌只保存在 ZMusic API 的服务端密钥存储中。
- 搜索响应不得包含音频 URL、平台访问令牌或可还原的平台凭据。
- `trackToken` 是由 ZMusic API 签发的短期、不透明、一次播放用途引用。Plugin 不解析其内容。
- 每次播放都由 API 重新校验账号绑定、平台授权、地区、会员权益和内容可用性。
- API 只能返回其 Provider 当前获准提供的能力。不可播放歌曲必须返回 `playable: false`。
- 音频、歌词和封面的使用范围分别由 Provider 的正式授权决定，不得互相推定。

## Envelope

所有消息均使用以下 envelope：

```json
{
  "id": "message-uuid",
  "type": "plugin.search",
  "protocolVersion": 1,
  "timestamp": 1786550400000,
  "data": {}
}
```

Plugin 发起的 Provider 请求都包含：

```json
{
  "requestId": "request-uuid",
  "playerUuid": "minecraft-player-uuid"
}
```

API 响应必须原样返回 `requestId`，且响应消息类型必须与原请求匹配。Plugin 只接受当前连接上仍处于挂起状态的请求，迟到或重复响应会被忽略，类型不匹配的响应会被拒绝且不会消费原请求。默认超时为 15 秒，连接关闭或配置重载会立即终止所有挂起请求。

成功响应：

```json
{
  "requestId": "request-uuid",
  "status": "ok"
}
```

失败响应：

```json
{
  "requestId": "request-uuid",
  "status": "error",
  "code": "account_required",
  "message": "请先绑定网易云音乐账号",
  "retryable": false
}
```

`message` 可以直接展示给玩家，不得包含密钥、令牌、内部地址或堆栈。

## Provider 列表

请求类型：`plugin.provider_list`

响应类型：`api.provider_list_result`

```json
{
  "requestId": "request-uuid",
  "status": "ok",
  "providers": [
    {
      "id": "netease",
      "name": "网易云音乐",
      "capabilities": ["search", "playback", "account_binding", "lyrics"],
      "available": true,
      "unavailableReason": null
    }
  ]
}
```

Provider `id` 必须匹配 `[a-z0-9][a-z0-9._-]{0,63}`。能力值为 `search`、`playback`、`account_binding`、`lyrics`。
单次响应最多包含 50 个 Provider。

## 账号状态

请求类型：`plugin.account_list`

响应类型：`api.account_list_result`

```json
{
  "requestId": "request-uuid",
  "status": "ok",
  "accounts": [
    {
      "providerId": "netease",
      "providerName": "网易云音乐",
      "status": "bound",
      "accountName": "用户展示名"
    }
  ]
}
```

账号状态为 `unbound`、`pending`、`bound`、`expired` 或 `unavailable`。`accountName` 只能使用平台允许展示的非敏感名称。
单次响应最多包含 50 个账号状态。

## 发起绑定

请求类型：`plugin.account_bind`

```json
{
  "requestId": "request-uuid",
  "playerUuid": "minecraft-player-uuid",
  "providerId": "netease"
}
```

响应类型：`api.account_bind_result`

```json
{
  "requestId": "request-uuid",
  "status": "ok",
  "providerId": "netease",
  "verificationUri": "https://account.zmusic.example/bind/session-id",
  "userCode": "ABCD-EFGH",
  "expiresAt": 1786550700000
}
```

- `verificationUri` 必须是 HTTPS；Plugin 会拒绝其他协议。
- 绑定会话必须与请求中的 Minecraft UUID、服务器设备和 Provider 绑定。
- 浏览器回调必须使用随机 `state`、短过期时间和一次性消费。
- 平台授权令牌必须加密存储，不得返回 Plugin 或 Mod。
- 完成绑定后，API 应通过既有玩家快照或后续查询反映最新状态。

## 解除绑定

请求类型：`plugin.account_unbind`

```json
{
  "requestId": "request-uuid",
  "playerUuid": "minecraft-player-uuid",
  "providerId": "netease"
}
```

响应类型：`api.account_unbind_result`。成功时只需返回通用成功响应。API 必须撤销可撤销的平台令牌并删除本地凭据；审计记录中不得保留令牌正文。

## 搜索

请求类型：`plugin.search`

```json
{
  "requestId": "request-uuid",
  "playerUuid": "minecraft-player-uuid",
  "providerId": "netease",
  "query": "起风了",
  "limit": 10
}
```

`providerId` 可省略，表示由 API 搜索所有可用 Provider。`query` 最长 128 个字符，`limit` 范围为 1 到 20。响应不得超过请求的 `limit`，且协议硬上限为 20 条。

响应类型：`api.search_result`

```json
{
  "requestId": "request-uuid",
  "status": "ok",
  "query": "起风了",
  "tracks": [
    {
      "trackToken": "opaque-signed-reference",
      "providerId": "netease",
      "providerName": "网易云音乐",
      "title": "起风了",
      "artists": ["买辣椒也用券"],
      "album": "起风了",
      "playable": true,
      "unavailableReason": null
    }
  ]
}
```

`trackToken` 至少应绑定以下上下文：

- Minecraft 玩家 UUID；
- Plugin 设备或服务器 ID；
- Provider 和平台歌曲 ID；
- 签发时间与过期时间；
- 随机 nonce 或一次性使用状态。

不建议把这些字段以明文 JSON 直接编码为 token；应使用服务端随机句柄或带完整性保护的短期令牌。搜索结果在 Plugin 内最多保存 5 分钟。

## Provider 播放

请求类型：`plugin.provider_play`

```json
{
  "requestId": "request-uuid",
  "playerUuid": "minecraft-player-uuid",
  "providerId": "netease",
  "trackToken": "opaque-signed-reference"
}
```

API 执行以下检查后才能返回播放资源：

1. `trackToken` 有效、未过期、未使用且属于当前玩家、服务器和请求中的 Provider；
2. 玩家账号授权仍然有效；
3. Provider 当前具备 `playback` 能力；
4. 歌曲在玩家地区、账号权益和当前场景下可播放；
5. 播放 URL 来自获准接口，且没有绕过 DRM、会员或平台访问控制；
6. 需要的播放数据回传和授权审计已建立。

响应类型：`api.provider_play_result`

```json
{
  "requestId": "request-uuid",
  "status": "ok",
  "playback": {
    "requestId": "playback-uuid",
    "song": {
      "id": "provider-track-id",
      "source": "netease",
      "title": "起风了",
      "artists": ["买辣椒也用券"]
    },
    "audio": {
      "url": "https://authorized-media.example/short-lived-resource"
    },
    "lyrics": {
      "url": "https://authorized-media.example/short-lived-lyrics"
    }
  }
}
```

Plugin 会忽略响应中的任何目标玩家字段，只把资源投递给原始请求闭包中的玩家。转发给 Mod 的字段采用白名单重建，不会透传 API 响应中的 Cookie、Authorization、Header 或其他未声明字段。`audio.url` 和可选的 `lyrics.url` 必须是无用户凭据、带有效主机名的 HTTPS URL；本机、链路本地、私网、组播及非标准 IP 字面量形式的内部地址会被拒绝。Plugin 不在平台线程解析域名，API 还必须将资源主机限制在 Provider 正式授权的媒体域名白名单内。API 应使用短期资源和最小权限，不能把 Provider 的 Cookie、App Secret 或长期 Token 嵌入 URL。

Plugin 会在首次播放尝试时原子消费本地搜索项；API 仍必须把 `trackToken` 作为一次性令牌执行最终校验。响应中 `song.source` 必须与请求中的 `providerId` 一致。

若现有平台授权不允许将播放 URL 交给 Minecraft Mod，Provider 必须返回失败，不能回退到网页抓取、客户端逆向或社区私有接口。

## 建议错误码

| 错误码 | 含义 | 可重试 |
| --- | --- | --- |
| `provider_unavailable` | Provider 暂停或未获播放能力 | 否 |
| `account_required` | 玩家尚未绑定平台账号 | 否 |
| `account_expired` | 平台授权已过期 | 否 |
| `track_token_expired` | 搜索结果已过期 | 否 |
| `track_unavailable` | 歌曲因版权、地区或权益不可播放 | 否 |
| `rate_limited` | 平台或 ZMusic API 限流 | 是 |
| `provider_timeout` | 上游服务超时 | 是 |
| `internal_error` | 未公开的服务端错误 | 是 |

Plugin 本地校验失败使用 `invalid_request`；API 响应结构无效使用 `invalid_response`；API 未连接或已停止使用 `service_unavailable`。这些错误不会触发第三方 Provider 请求。

## 网易云音乐个人接入限制

网易云音乐个人接入当前仅公开支持官方 `@music163/ncm-cli`，不等同于允许个人开发者自行调用 CLI 内部 API。若使用个人接入路径：

- Provider 必须把 `ncm-cli` 当作官方黑盒工具；
- 不复制、逆向或调用其未公开接口；
- 不从 CLI 内部提取播放地址后转发给 Mod；
- 仅使用 CLI 文档公开的命令和输出；
- 必须遵守登录、不可下载、曲目版权可用性等限制。

由于面板服通常不能安装 Node.js、npm 或额外进程，该路径只适合作为用户自建 sidecar 的可选实验能力，不作为 Plugin 默认 Provider。面向普通面板服的完整 Mod 播放需要网易云音乐厂商授权明确覆盖该终端和数据流。

官方参考：

- [网易云音乐开放平台：个人开发者接入指南](https://developer.music.163.com/st/developer/document?docId=2327e302009c437eb02af48f63d6e514)
- [网易云音乐开放平台：常见问题](https://developer.music.163.com/st/developer/document?docId=3b75ab8e475d41ca93d91ebd4dfd383f)
