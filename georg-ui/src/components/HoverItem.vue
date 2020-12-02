<template>
  <v-hover v-slot:default="{ hover }">
    <v-list-item
      class="selectable-text"
      :dense="dense"
      :class="{ highlight: expand == true, hoveritem: dense == true }"
      @focus="expand = true"
      @blur="expand = false"
      @click="onClick(value)"
    >
      <ItemIcon v-bind:iconColor="iconColor" v-bind:iconName="iconName" />
      <v-list-item-content v-if="hastitle">
        <v-list-item-title
          >{{ value }}
          <span class="text--secondary">{{ spanvalue }}</span>
        </v-list-item-title>
        <v-list-item-subtitle v-if="subtitle">{{
          subtitle
        }}</v-list-item-subtitle>
      </v-list-item-content>
      <v-list-item-content v-else>
        {{ value }}
      </v-list-item-content>
      <v-list-item-action>
        <v-btn
          icon
          small
          color="transparent"
          :class="{ 'show-btn': expand == true, 'show-btn-hover': hover }"
          @focus="expand = true"
          @blur="expand = false"
          v-clipboard="value"
          :aria-label="$t('result.copy', { value: value })"
        >
          <BaseIcon v-bind:small="true">{{ copyIconName }}</BaseIcon>
        </v-btn>
      </v-list-item-action>
    </v-list-item>
  </v-hover>
</template>
<script>
import BaseIcon from './baseComponents/BaseIcon'
import ItemIcon from './baseComponents/ItemIcon'
export default {
  name: 'Coordinates',
  components: {
    BaseIcon,
    ItemIcon,
  },
  props: [
    'dense',
    'iconColor',
    'iconName',
    'hastitle',
    'spanvalue',
    'subtitle',
    'value',
  ],
  data() {
    return {
      copyIconName: 'mdi-content-copy',
      expand: false,
    }
  },
  methods: {
    onClick(value) {
      this.$emit('copy', value)
    },
  },
}
</script>
<style>
.hoveritem.v-list-item--dense {
  min-height: 40px !important;
  max-height: 40px !important;
}
.hoveritem.v-list-item--dense .v-list-item__title {
  font-size: 1rem !important;
  font-weight: 400 !important;
}
.highlight::before {
  background-color: currentColor !important;
  opacity: 0.06 !important;
}
.v-application .show-btn-hover.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
</style>
