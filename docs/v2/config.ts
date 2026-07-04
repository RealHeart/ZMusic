import { defineAdditionalConfig, type DefaultTheme } from 'vitepress'

export default defineAdditionalConfig({
  themeConfig: {
    nav: nav(),
    sidebar: sidebar()
  }
})

function nav(): DefaultTheme.NavItem[] {
  return [
    { text: '首页', link: '/' },
    { text: '指南', link: '/guide/introduction' },
    { text: '常见问题', link: '/faq' },
    { text: 'QQ群', link: 'https://qm.qq.com/q/buxuatfTCo' },
    {
      text: '2.11.0',
      items: [
        { text: '4.0.0-dev', link: '/' },
        { text: '更新日志', link: 'https://github.com/starhui-dev/zmusic-plugin/blob/v2/CHANGELOG.md' }
      ]
    }
  ]
}

function sidebar(): DefaultTheme.Sidebar {
  return [
    {
      text: 'V2 文档',
      items: [
        { text: '使用文档', link: '/v2/' },
        { text: '常见问题', link: '/v2/faq' }
      ]
    }
  ]
}
