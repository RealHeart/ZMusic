<template>
  <ExternalLinkCard :links="links" :note="t.note" />
</template>

<script lang="ts" setup>
import { siCodeberg, siSpigotmc, siCloudnativebuild } from 'simple-icons'
import ExternalLinkCard from './ExternalLinkCard.vue'
import { useI18n } from '../utils/i18n'

const t = useI18n()

const links = [
  {
    url: 'https://codeberg.org/starhui-dev/zmusic-plugin/releases',
    label: 'Codeberg',
    icon: 'codeberg',
    svg: siCodeberg.svg
  },
  {
    url: 'https://cnb.cool/starhui-dev/zmusic-plugin/-/releases',
    label: 'CNB',
    icon: 'cnb',
    svg: siCloudnativebuild.svg
  },
  {
    url: 'https://www.spigotmc.org/resources/zmusic.83027/',
    label: 'SpigotMC',
    icon: 'spigotmc',
    svg: siSpigotmc.svg
  }
]
</script>
