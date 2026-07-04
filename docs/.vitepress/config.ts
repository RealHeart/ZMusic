import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vitepress'

export default defineConfig({
  lastUpdated: true,
  vite: {
    resolve: {
      alias: [
        {
          find: /^.*\/VPNavBarMenuLink\.vue$/,
          replacement: fileURLToPath(
            new URL('./theme/components/VPNavBarMenuLink.vue', import.meta.url)
          )
        },
        {
          find: /^.*\/VPMenuLink\.vue$/,
          replacement: fileURLToPath(new URL('./theme/components/VPMenuLink.vue', import.meta.url))
        },
        {
          find: /^.*\/VPSidebarItem\.vue$/,
          replacement: fileURLToPath(
            new URL('./theme/components/VPSidebarItem.vue', import.meta.url)
          )
        }
      ]
    }
  },
  head: [
    ['meta', { name: 'viewport', content: 'width=device-width,initial-scale=1,user-scalable=no' }],
    ['script', { src: 'https://cdn.zhenxin.me/static/js/autoGray.js' }],
    [
      'script',
      {
        defer: '',
        src: 'https://umami.zhenxin.me/script.js',
        'data-website-id': '1d01764d-0e67-4a79-969a-1a431153fb22'
      }
    ],
    ['meta', { name: 'author', content: 'ZhenXin' }],
    [
      'meta',
      {
        name: 'keywords',
        content:
          'ZMusic, Minecraft, Plugin, Music, 点歌插件, 网易云音乐, BungeeCord, Spigot, Velocity'
      }
    ]
  ],
  sitemap: {
    hostname: 'https://docs.zmusic.cc'
  },
  themeConfig: {
    socialLinks: [
      { icon: 'github', link: 'https://github.com/starhui-dev/zmusic-plugin' }
    ],
    i18nRouting: true,
    repo: 'starhui-dev/zmusic-plugin',
    modRepo: 'starhui-dev/zmusic-client'
  },
  locales: {
    root: {
      label: '简体中文',
      lang: 'zh-CN',
      title: 'ZMusic 使用文档',
      description: 'ZMusic - 多功能、易上手的 Minecraft 跨平台点歌插件'
    },
    en: {
      label: 'English',
      lang: 'en-US',
      title: 'ZMusic Docs',
      description: 'ZMusic - Versatile, easy-to-use cross-platform music player plugin for Minecraft'
    },
    ja: {
      label: '日本語',
      lang: 'ja-JP',
      title: 'ZMusic ドキュメント',
      description: 'ZMusic - 多機能で使いやすい Minecraft クロスプラットフォーム音楽再生プラグイン'
    }
  }
})
