import { defineAdditionalConfig, type DefaultTheme } from 'vitepress'

export default defineAdditionalConfig({
  themeConfig: {
    nav: nav(),
    sidebar: sidebar(),
    search: { options: searchOptions() },

    editLink: {
      pattern: 'https://github.com/starhui-dev/zmusic-plugin/edit/v4/docs/:path',
      text: '在 GitHub 上编辑此页面'
    },

    footer: {
      message:
        '<a href="https://beian.miit.gov.cn" target="_blank" rel="noreferrer">辽ICP备2026013342号-2</a><span class="footer-separator">·</span><a class="beian-link" href="https://beian.mps.gov.cn/#/query/webSearch?code=21010602001361" target="_blank" rel="noreferrer"><img class="beian-icon" src="https://s3.starhui.cc/public/images/mps-beian-icon.png" alt="">辽公网安备21010602001361号</a>',
      copyright:
        '&copy;2026 <a href="https://starhui.cc" target="_blank" rel="noreferrer">星绘科技</a> All Rights Reserved.'
    },

    docFooter: {
      prev: '上一页',
      next: '下一页'
    },

    outline: {
      label: '页面导航'
    },

    lastUpdated: {
      text: '最后更新于'
    },

    notFound: {
      title: '页面未找到',
      quote: '看起来你访问了一个不存在的页面。',
      linkLabel: '前往首页',
      linkText: '带我回首页'
    },

    langMenuLabel: '多语言',
    returnToTopLabel: '回到顶部',
    sidebarMenuLabel: '菜单',
    darkModeSwitchLabel: '主题',
    lightModeSwitchTitle: '切换到浅色模式',
    darkModeSwitchTitle: '切换到深色模式',
    skipToContentLabel: '跳转到内容'
  }
})

function nav(): DefaultTheme.NavItem[] {
  return [
    { text: '首页', link: '/' },
    {
      text: '指南',
      link: '/guide/introduction',
      activeMatch: '^/guide/'
    },
    { text: '常见问题', link: '/faq' },
    { text: 'QQ群', link: 'https://qm.qq.com/q/buxuatfTCo' },
    {
      text: '4.0.0-dev',
      items: [
        { text: '2.11.0 (LTS)', link: '/v2/' },
        { text: '更新日志', link: 'https://github.com/starhui-dev/zmusic-plugin/blob/v4/CHANGELOG.md' }
      ]
    }
  ]
}

function sidebar(): DefaultTheme.Sidebar {
  return [
    {
      text: '指南',
      collapsed: false,
      items: [
        { text: '介绍', link: '/guide/introduction' },
        { text: '快速开始', link: '/guide/getting-started' },
        { text: '命令权限', link: '/guide/command-permissions' },
        { text: '配置文件', link: '/guide/config' }
      ]
    },
    {
      text: '网易云音乐 API',
      collapsed: false,
      items: [
        { text: '公共服务器', link: '/netease-api/public' },
        { text: '标准版部署', link: '/netease-api/standard' },
        { text: '增强版部署', link: '/netease-api/enhanced' }
      ]
    },
    { text: '常见问题', link: '/faq' },
    { text: 'V2 文档', link: '/v2/' }
  ]
}

function searchOptions(): DefaultTheme.LocalSearchOptions {
  return {
    translations: {
      button: {
        buttonText: '搜索文档',
        buttonAriaLabel: '搜索文档'
      },
      modal: {
        noResultsText: '未找到相关结果',
        resetButtonTitle: '清除查询条件',
        footer: {
          selectText: '选择',
          navigateText: '切换',
          closeText: '关闭'
        }
      }
    }
  }
}
