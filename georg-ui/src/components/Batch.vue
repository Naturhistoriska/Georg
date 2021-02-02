<template>
  <div>
    <h3 class="text--darken-2 v-card__title ">
      {{ $t('batch.batch') }}
    </h3>
    <v-card-subtitle>
      {{ $t('batch.batchText') }}
    </v-card-subtitle>
    <v-row class="pl-6">
      <v-file-input
        accept="csv"
        dense
        :label="$t('batch.fileInput')"
        prepend-inner-icon="attach_file"
        prepend-icon=""
        show-size
        :loading="loading"
        @change="upload"
        @click="onClick"
        @click:clear="clear"
      ></v-file-input>
      <ActionIconButton
        v-bind:iconName="questionIcon"
        :aria-label="$t('batch.batchHelpMarker')"
        @on-icon-click="openCloseHelpText"
      />
    </v-row>
    <v-row class="pl-4 pr-3" v-if="showHelpText">
      <v-list-item style="background: #FCF9D4;" class="pa-2">
        <v-list-item-content>
          <v-list-item-subtitle
            v-html="$t('batch.batchHelpText')"
          ></v-list-item-subtitle>
          <router-link to="/sv/om#aboutBatch" class="routerlink">{{
            $t('batch.batchHelpLink')
          }}</router-link>
        </v-list-item-content>
        <v-list-item-action @click.prevent="!showHelpText" class="ma-0 pa-0 ">
          <ActionIconButton
            v-bind:iconName="this.closeIcon"
            :aria-label="$t('batch.batchCloseHelp')"
            @on-icon-click="openCloseHelpText"
          />
        </v-list-item-action>
      </v-list-item>
    </v-row>
    <v-row v-if="currentFile && !loading">
      <BatchData @on-table-click="expandTable" @on-list-click="collapseTable" />
    </v-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Batch',
  components: {
    BatchData: () => import('../components/BatchData'),
    ActionIconButton: () => import('./baseComponents/ActionIconButton'),
  },
  data() {
    return {
      loading: false,
      currentFile: undefined,
      // progress: 0,
      fileInfos: [],
      // data: null,
      showHelpText: false,
    }
  },
  created() {
    this.questionIcon = 'mdi-help-circle'
    this.closeIcon = 'mdi-close-circle'
  },
  watch: {
    batchData() {
      this.loading = false
    },
  },
  computed: {
    ...mapGetters(['batchData']),
  },
  methods: {
    expandTable() {
      this.$emit('expand-table')
    },
    collapseTable() {
      console.log('clapsTable', screen.width + 'px')
      this.$emit('collapse-table')
    },
    clear() {
      // this.loading = false
      this.$emit('clear-file')
    },
    onClick() {
      this.showHelpText = false
    },
    upload(file) {
      this.currentFile = file
      if (!this.currentFile) {
        this.loading = false
        this.message = 'Please select a file!'
        return
      }
      this.$emit('upload', this.currentFile)
      this.loading = true
    },
    openCloseHelpText() {
      this.showHelpText = !this.showHelpText
    },
  },
}
</script>
<style scoped>
.v-list-item__subtitle {
  white-space: normal;
}
.routerlink {
  font-size: 12px;
}

/* .wrap-text {
  -webkit-line-clamp: unset !important;
} */
</style>
