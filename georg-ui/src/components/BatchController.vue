<template>
  <div class="text-left pl-3">
    <v-chip
      color="grey darken-2"
      outlined
      small
      class="ma-2"
      @click="adjustFilter"
      @keypress="adjustFilter"
      role="button"
      :aria-label="$t('batch.adjustFilter')"
    >
      <v-icon small left>mdi-filter-variant</v-icon>
      {{ $t('batch.adjustFilter') }}
    </v-chip>

    <v-chip
      color="grey darken-2"
      outlined
      small
      class="ma-2"
      @click="copyBatch"
      @keypress="adjustFilter"
      role="button"
      :aria-label="$t('batch.copyBatchLink')"
    >
      <v-icon small left>
        mdi-link-variant
      </v-icon>
      {{ $t('batch.copyBatchLink') }}
    </v-chip>
  </div>
</template>
<script>
export default {
  name: 'BatchController',
  methods: {
    adjustFilter() {
      this.$emit('adjust-filter')
    },
    copyBatch() {},
  },
}
</script>
