import { defineAdditionalConfig, type DefaultTheme } from 'vitepress'

export default defineAdditionalConfig({
  themeConfig: {
    nav: nav(),
    sidebar: sidebar()
  }
})

function nav(): DefaultTheme.NavItem[] {
  return [
    { text: 'Home', link: '/en/' },
    { text: 'Guide', link: '/en/guide/introduction' },
    { text: 'FAQ', link: '/en/faq' },
    { text: 'Discord', link: 'https://discord.gg/twQgJNufYn' },
    {
      text: '2.11.0',
      items: [
        { text: '4.0.0-dev', link: '/en/' },
        { text: 'Changelog', link: 'https://github.com/starhui-dev/zmusic-plugin/blob/v2/CHANGELOG.md' }
      ]
    }
  ]
}

function sidebar(): DefaultTheme.Sidebar {
  return [
    {
      text: 'V2 Docs',
      items: [
        { text: 'Documentation', link: '/en/v2/' },
        { text: 'FAQ', link: '/en/v2/faq' }
      ]
    }
  ]
}
