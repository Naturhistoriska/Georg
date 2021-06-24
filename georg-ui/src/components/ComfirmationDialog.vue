<template>
  <v-row justify="center">
    <v-dialog v-model="dialogStatus" max-width="320" persistent>
      <v-card>
        <v-card-title class="text-h5">
          {{ title }}
        </v-card-title>
        <v-card-text>
          {{ subTitle }}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            :aria-label="$t('common.yes')"
            color="blue darken-1"
            text
            right
            @click="updateData"
          >
            {{ $t('common.yes') }}
          </v-btn>
          <v-btn
            :aria-label="$t('common.no')"
            color="blue darken-1"
            text
            @click="cancelUpdate"
          >
            {{ $t('common.no') }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'ComfirmationDialog',
  props: ['dialogType', 'dialogStatus'],
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['selectedBatch']),
    title: function() {
      return this.dialogType === 'batchEdit'
        ? this.$t('batch.editComfirmTitle')
        : ''
    },
    subTitle: function() {
      return this.dialogType === 'batchEdit'
        ? this.$t('batch.editComfirmSubtitle', {
            numOfRecords: this.selectedBatch.length,
          })
        : ''
    },
  },
  methods: {
    updateData() {
      this.$emit('close-dialog', 'save')
    },
    cancelUpdate() {
      this.$emit('close-dialog', 'cancel')
    },
  },
}
</script>
