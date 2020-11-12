<template>
  <v-list-item-group>
    <v-list-item :href="externallink" target="_blank">
      <ItemIcon
        v-bind:iconColor="blueDarkenColor"
        v-bind:iconName="importIconName"
      />
      <ItemContent v-bind:title="dataFromSource" />
      <BaseIconButton
        v-bind:iconName="openNewIconName"
        v-bind:href="externallink"
      />
    </v-list-item>
  </v-list-item-group>
</template>
<script>
import BaseIconButton from './baseComponents/BaseIconButton'
import ItemContent from './baseComponents/ItemContent'
import ItemIcon from './baseComponents/ItemIcon'

export default {
  name: 'DataSourceLinks',
  components: {
    BaseIconButton,
    ItemContent,
    ItemIcon,
  },
  props: ['dataFromSource', 'externallink'],
  created() {
    this.blueDarkenColor = 'blue darken-2'
    this.importIconName = 'mdi-database-import'
    this.openNewIconName = 'mdi-open-in-new'
  },
}
</script>
