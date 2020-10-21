<template>
  <v-list-item-group>
    <v-list-item :href="externallink" target="_blank">
      <v-list-item-icon>
        <BaseIcon v-bind:color="blueDarkenColor">
          {{ importIconName }}
        </BaseIcon>
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title>Data från {{ dataFromSource }}</v-list-item-title>
      </v-list-item-content>
      <BaseIconButton
        v-bind:iconName="openNewIconName"
        v-bind:href="externallink"
      />
    </v-list-item>
  </v-list-item-group>
</template>
<script>
import BaseIcon from './baseComponents/BaseIcon'
import BaseIconButton from './baseComponents/BaseIconButton'
export default {
  name: 'DataSourceLinks',
  components: {
    BaseIcon,
    BaseIconButton,
  },
  props: ['dataFromSource', 'externallink'],
  data() {
    return {
      blueDarkenColor: 'blue darken-2',
      importIconName: 'mdi-database-import',
      openNewIconName: 'mdi-open-in-new',
    }
  },
}
</script>
