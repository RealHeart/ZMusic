import { defineAdditionalConfig, type DefaultTheme } from 'vitepress'

export default defineAdditionalConfig({
  themeConfig: {
    nav: nav(),
    sidebar: sidebar(),
    search: { options: searchOptions() },

    editLink: {
      pattern: 'https://github.com/starhui-dev/zmusic-plugin/edit/v4/docs/:path',
      text: 'GitHub でこのページを編集'
    },

    footer: {
      message:
        '<a href="https://beian.miit.gov.cn" target="_blank" rel="noreferrer">辽ICP备2026013342号-2</a><span class="footer-separator">·</span><a class="beian-link" href="https://beian.mps.gov.cn/#/query/webSearch?code=21010602001361" target="_blank" rel="noreferrer"><img class="beian-icon" src="https://s3.starhui.cc/public/images/mps-beian-icon.png" alt="">辽公网安备21010602001361号</a>',
      copyright:
        '&copy;2026 <a href="https://starhui.cc" target="_blank" rel="noreferrer">星絵テクノロジー</a> All Rights Reserved.'
    },

    docFooter: {
      prev: '前のページ',
      next: '次のページ'
    },

    outline: {
      label: '目次'
    },

    lastUpdated: {
      text: '最終更新'
    },

    notFound: {
      title: 'ページが見つかりません',
      quote: '存在しないページにアクセスしたようです。',
      linkLabel: 'ホームへ',
      linkText: 'ホームに戻る'
    },

    langMenuLabel: '言語',
    returnToTopLabel: 'トップに戻る',
    sidebarMenuLabel: 'メニュー',
    darkModeSwitchLabel: 'テーマ',
    lightModeSwitchTitle: 'ライトモードに切り替え',
    darkModeSwitchTitle: 'ダークモードに切り替え',
    skipToContentLabel: 'コンテンツへスキップ'
  }
})

function nav(): DefaultTheme.NavItem[] {
  return [
    { text: 'ホーム', link: '/ja/' },
    {
      text: 'ガイド',
      link: '/ja/guide/introduction',
      activeMatch: '^/ja/guide/'
    },
    { text: 'よくある質問', link: '/ja/faq' },
    { text: 'Discord', link: 'https://discord.gg/twQgJNufYn' },
    {
      text: '4.0.0-dev',
      items: [
        { text: '2.11.0 (LTS)', link: '/ja/v2/' },
        { text: '変更履歴', link: 'https://github.com/starhui-dev/zmusic-plugin/blob/v4/CHANGELOG.md' }
      ]
    }
  ]
}

function sidebar(): DefaultTheme.Sidebar {
  return [
    {
      text: 'ガイド',
      collapsed: false,
      items: [
        { text: '紹介', link: '/ja/guide/introduction' },
        { text: 'はじめに', link: '/ja/guide/getting-started' },
        { text: 'コマンド権限', link: '/ja/guide/command-permissions' },
        { text: '設定ファイル', link: '/ja/guide/config' }
      ]
    },
    {
      text: 'NetEase Cloud Music API',
      collapsed: false,
      items: [
        { text: 'パブリックサーバー', link: '/ja/netease-api/public' },
        { text: '標準版デプロイ', link: '/ja/netease-api/standard' },
        { text: '拡張版デプロイ', link: '/ja/netease-api/enhanced' }
      ]
    },
    { text: 'よくある質問', link: '/ja/faq' },
    { text: 'V2 ドキュメント', link: '/ja/v2/' }
  ]
}

function searchOptions(): DefaultTheme.LocalSearchOptions {
  return {
    translations: {
      button: {
        buttonText: 'ドキュメントを検索',
        buttonAriaLabel: 'ドキュメントを検索'
      }
    }
  }
}
