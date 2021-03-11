<template>
  <div class="text-center">
    <v-chip
      color="grey darken-2"
      outlined
      small
      class="mr-2"
      @click="adjustFilter"
      @keypress="adjustFilter"
      role="button"
      :aria-label="$t('batch.adjustFilter')"
    >
      <v-icon small left>mdi-filter-variant</v-icon>
      {{ $t('batch.adjustFilter') }}
    </v-chip>

    <v-chip class="ma-2" outlined>
      <v-icon left>
        mdi-link-variant
      </v-icon>
      {{ $t('batch.copyBatchLink') }}
    </v-chip>
  </div>
</template>
<script>
export default {
  name: 'BatchController',
  methods: {},
}
</script>
