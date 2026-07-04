import { defineAdditionalConfig, type DefaultTheme } from 'vitepress'

export default defineAdditionalConfig({
  themeConfig: {
    nav: nav(),
    sidebar: sidebar(),
    search: { options: searchOptions() },

    editLink: {
      pattern: 'https://github.com/starhui-dev/zmusic-plugin/edit/v4/docs/:path',
      text: 'Edit this page on GitHub'
    },

    footer: {
      message:
        '<a href="https://beian.miit.gov.cn" target="_blank" rel="noreferrer">辽ICP备2026013342号-2</a><span class="footer-separator">·</span><a class="beian-link" href="https://beian.mps.gov.cn/#/query/webSearch?code=21010602001361" target="_blank" rel="noreferrer"><img class="beian-icon" src="https://s3.starhui.cc/public/images/mps-beian-icon.png" alt="">辽公网安备21010602001361号</a>',
      copyright:
        '&copy;2026 <a href="https://starhui.cc" target="_blank" rel="noreferrer">StarHui Technology</a> All Rights Reserved.'
    },

    docFooter: {
      prev: 'Previous page',
      next: 'Next page'
    },

    outline: {
      label: 'On this page'
    },

    lastUpdated: {
      text: 'Last updated'
    },

    notFound: {
      title: 'Page Not Found',
      quote: 'Looks like you have visited a page that does not exist.',
      linkLabel: 'Go to Home',
      linkText: 'Take me home'
    },

    langMenuLabel: 'Language',
    returnToTopLabel: 'Return to top',
    sidebarMenuLabel: 'Menu',
    darkModeSwitchLabel: 'Theme',
    lightModeSwitchTitle: 'Switch to light theme',
    darkModeSwitchTitle: 'Switch to dark theme',
    skipToContentLabel: 'Skip to content'
  }
})

function nav(): DefaultTheme.NavItem[] {
  return [
    { text: 'Home', link: '/en/' },
    {
      text: 'Guide',
      link: '/en/guide/introduction',
      activeMatch: '^/en/guide/'
    },
    { text: 'FAQ', link: '/en/faq' },
    { text: 'Discord', link: 'https://discord.gg/twQgJNufYn' },
    {
      text: '4.0.0-dev',
      items: [
        { text: '2.11.0 (LTS)', link: '/en/v2/' },
        { text: 'Changelog', link: 'https://github.com/starhui-dev/zmusic-plugin/blob/v4/CHANGELOG.md' }
      ]
    }
  ]
}

function sidebar(): DefaultTheme.Sidebar {
  return [
    {
      text: 'Guide',
      collapsed: false,
      items: [
        { text: 'Introduction', link: '/en/guide/introduction' },
        { text: 'Getting Started', link: '/en/guide/getting-started' },
        { text: 'Command Permissions', link: '/en/guide/command-permissions' },
        { text: 'Configuration', link: '/en/guide/config' }
      ]
    },
    {
      text: 'Netease Cloud Music API',
      collapsed: false,
      items: [
        { text: 'Public Servers', link: '/en/netease-api/public' },
        { text: 'Standard Deployment', link: '/en/netease-api/standard' },
        { text: 'Enhanced Deployment', link: '/en/netease-api/enhanced' }
      ]
    },
    { text: 'FAQ', link: '/en/faq' },
    { text: 'V2 Docs', link: '/en/v2/' }
  ]
}

function searchOptions(): DefaultTheme.LocalSearchOptions {
  return {
    translations: {
      button: {
        buttonText: 'Search docs',
        buttonAriaLabel: 'Search docs'
      }
    }
  }
}
