<template>
  <v-sheet class="ml-6 mr-6 mt-0 pa-0" max-width="380">
    <v-row>
      <v-btn
        color="primary"
        :disabled="disable"
        outlined
        small
        @click="openEdit"
      >
        {{ $t('common.edit') }}
      </v-btn>
      <v-btn
        color="primary"
        :disabled="disable"
        outlined
        small
        class="ml-2"
        @click="onDelete"
      >
        {{ $t('common.delete') }}
      </v-btn>
      <v-spacer></v-spacer>
      <download-csv :data="batchData" name="georgData.csv" class="btn">
        <v-btn color="primary" small>
          {{ $t('batch.download') }}
        </v-btn>
      </download-csv>
    </v-row>
  </v-sheet>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'BatchAction',
  data() {
    return {
      disable: true,
    }
  },
  watch: {
    selectedBatch() {
      this.disable = this.selectedBatch.length > 0 ? false : true
    },
  },
  computed: {
    ...mapGetters(['batchData', 'selectedBatch']),
  },
  methods: {
    ...mapMutations(['setBatchData', 'setEditView']),
    openEdit() {
      this.setEditView(true)
      this.$emit('edit-batch')
    },
    onDelete() {
      let editData = this.batchData.filter(batch => {
        return !this.selectedBatch.includes(batch)
      })
      this.setBatchData(editData)
    },
  },
}
</script>
