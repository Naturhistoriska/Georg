<template>
  <v-card
    class="mt-2 overflow-y-auto"
    :class="{ 'card-sm': $vuetify.breakpoint.smAndUp }"
    id="v-card-edit"
  >
    <h3 class="blue--text v-card__title, text--darken-2 pl-4 pr-2">
      {{ title }}
    </h3>
    <v-card-subtitle v-html="subtitle"> </v-card-subtitle>
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'BatchEdit',
  data() {
    return {
      title: '',
      subtitle: '',
    }
  },
  mounted() {
    if (this.selectedBatch.length === 1) {
      this.title = this.$t('common.edit')
      this.subtitle = this.$t('batch.batchEditSubtitle', {
        id: this.selectedBatch[0].id,
        source: this.selectedBatch[0].sourceLocality,
      })

      console.log('subtitle...', this.subtitle)
    } else {
      this.title = this.$t('batch.multipleEdit')
      this.subtitle = this.selectedBatch[1].id
    }
  },
  computed: {
    ...mapGetters(['selectedBatch']),
    mytitle: function() {
      console.log('mytitle...', this.selectedBatch.length)
      return this.$t('common.edit')
    },
  },
}
</script>
<style scoped>
#v-card-edit {
  z-index: 2;
  min-width: 300px;
}
::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 7px;
}
::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background-color: rgba(0, 0, 0, 0.5);
  box-shadow: 0 0 1px rgba(255, 255, 255, 0.5);
}
.card-sm {
  width: 400px;
}
</style>
