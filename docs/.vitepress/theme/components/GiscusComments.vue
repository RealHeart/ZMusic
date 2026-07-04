<script setup lang="ts">
import { useData } from 'vitepress'
import Giscus from '@giscus/vue'

const { isDark, lang } = useData()

const giscusLangMap: Record<string, string> = {
  'zh-CN': 'zh-CN',
  'en-US': 'en',
  'ja-JP': 'ja'
}
</script>

<template>
  <div>
    <Giscus
      repo="starhui-dev/zmusic-comments"
      repo-id="R_kgDORwUV4A"
      category="comments"
      category-id="DIC_kwDORwUV4M4C5PI7"
      mapping="pathname"
      strict="0"
      reactions-enabled="1"
      emit-metadata="0"
      input-position="bottom"
      :theme="isDark ? 'dark' : 'light'"
      :lang="giscusLangMap[lang] || 'en'"
      loading="lazy"
    />
  </div>
</template>
