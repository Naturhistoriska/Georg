<template>
  <v-sheet class="ml-6 mr-6 mt-0 pa-0" max-width="380">
    <v-row>
      <v-btn
        color="primary"
        :disabled="disableButton"
        outlined
        small
        @click="openEdit"
      >
        {{ $t('common.edit') }}
      </v-btn>
      <v-btn
        color="primary"
        :disabled="disableButton"
        outlined
        small
        class="ml-2"
        @click="onDelete"
      >
        {{ $t('common.delete') }}
      </v-btn>
      <v-spacer></v-spacer>
      <download-csv :data="batch" name="georgData.csv" class="btn">
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
      disableButton: true,
      disableEdit: true,
      disableDelete: true,
    }
  },
  watch: {
    selectedBatch() {
      this.disableButton = this.selectedBatch.length <= 0
    },
  },
  computed: {
    ...mapGetters(['batchData', 'filters', 'filteredData', 'selectedBatch']),

    batch() {
      if (this.selectedBatch.length > 0) {
        return this.selectedBatch
      }
      return !this.filters
        ? this.batchData
        : this.filters.hasFilters
        ? this.filteredData
        : this.batchData
    },
  },
  methods: {
    ...mapMutations(['setBatchData', 'setEditView', 'setSelectedBatch']),
    openEdit() {
      this.setEditView(true)
      this.$emit('edit-batch')
    },
    onDelete() {
      let editData = this.batchData.filter(batch => {
        return !this.selectedBatch.includes(batch)
      })
      this.setBatchData(editData)
      this.setSelectedBatch([])
    },
  },
}
</script>
