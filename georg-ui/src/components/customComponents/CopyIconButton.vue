<template>
  <BaseIconButton
    v-bind:name="name"
    v-bind:btnClass="{ 'show-btn': expand === true, 'show-btn-hover': hover }"
    @focus="expand = true"
    @blur="expand = false"
    v-clipboard="value"
  />
  <!-- <v-btn
    icon
    small
    color="transparent"
    :class="{ 'show-btn': expand == true, 'show-btn-hover': hover }"
    @focus="expand = true"
    @blur="expand = false"
    v-clipboard="latLonDms"
  >
    <v-icon small>mdi-content-copy</v-icon>
  </v-btn> -->
</template>
<script>
import BaseIconButton from '../baseComponents/BaseIcon'
export default {
  name: 'IconButton',
  components: {
    BaseIconButton,
  },
  props: ['hover', 'value'],
  data() {
    return {
      name: 'mdi-content-copy',
      expand: false,
    }
  },
  computed: {},
}
</script>
<style>
.show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
.v-application .show-btn-hover.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}

.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
</style>
