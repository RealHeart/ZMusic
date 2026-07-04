import { defineAdditionalConfig, type DefaultTheme } from 'vitepress'

export default defineAdditionalConfig({
  themeConfig: {
    nav: nav(),
    sidebar: sidebar()
  }
})

function nav(): DefaultTheme.NavItem[] {
  return [
    { text: 'ホーム', link: '/ja/' },
    { text: 'ガイド', link: '/ja/guide/introduction' },
    { text: 'よくある質問', link: '/ja/faq' },
    { text: 'Discord', link: 'https://discord.gg/twQgJNufYn' },
    {
      text: '2.11.0',
      items: [
        { text: '4.0.0-dev', link: '/ja/' },
        { text: '変更履歴', link: 'https://github.com/starhui-dev/zmusic-plugin/blob/v2/CHANGELOG.md' }
      ]
    }
  ]
}

function sidebar(): DefaultTheme.Sidebar {
  return [
    {
      text: 'V2 ドキュメント',
      items: [
        { text: 'ドキュメント', link: '/ja/v2/' },
        { text: 'よくある質問', link: '/ja/v2/faq' }
      ]
    }
  ]
}
