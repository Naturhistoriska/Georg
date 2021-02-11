<template>
  <div>
    <v-card id="batch" :style="width">
      <div class="mt-n5 ml-n4 pa-0">
        <h3 class="text--darken-2 v-card__title ">
          {{ $t('batch.batch') }}
        </h3>
        <h5 class="v-card__subtitle">{{ $t('batch.batchText') }}</h5>
        <v-sheet class="ml-3 mt-n2 pa-0" max-width="380">
          <v-row class="ma-0 pa-0">
            <v-file-input
              accept=".csv"
              dense
              loader-height="3"
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
          <v-row class="ml-0 mr-0 mt-n2 pa-0" v-if="showHelpText">
            <v-list-item style="background: #FCF9D4;">
              <v-list-item-content>
                <v-list-item-subtitle
                  v-html="$t('batch.batchHelpText')"
                ></v-list-item-subtitle>
                <router-link to="/sv/om#aboutBatch" class="routerlink">{{
                  $t('batch.batchHelpLink')
                }}</router-link>
              </v-list-item-content>
              <v-list-item-action
                @click.prevent="!showHelpText"
                class="ma-0 pa-0 "
              >
                <ActionIconButton
                  v-bind:iconName="this.closeIcon"
                  :aria-label="$t('common.close')"
                  @on-icon-click="openCloseHelpText"
                />
              </v-list-item-action>
            </v-list-item>
          </v-row>
          <ResultHeader
            v-bind:isEdit="editMode"
            v-bind:isBatch="true"
            @display-results="handleDisplayResult"
            @back-results="backToTable"
            v-if="currentFile && !loading"
          />
        </v-sheet>
        <div v-if="displayResults && !editMode">
          <v-divider class="mt-2"></v-divider>
          <v-row class="ml-2 mr-0 mt-n2 pa-0">
            <BatchData
              @on-table-click="expandTable"
              @on-list-click="collapseTable"
            />
          </v-row>
          <BatchAction @on-edit="handleBatchEdit" />
        </div>
      </div>
    </v-card>
    <BatchEdit v-if="displayResults && editMode" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Batch',
  components: {
    ActionIconButton: () => import('./baseComponents/ActionIconButton'),
    BatchAction: () => import('../components/BatchAction'),
    BatchData: () => import('../components/BatchData'),
    BatchEdit: () => import('../components/BatchEdit'),
    ResultHeader: () => import('../components/ResultHeader'),
  },
  data() {
    return {
      editMode: false,
      currentFile: undefined,
      displayResults: false,
      fileInfos: [],
      loading: false,
      showHelpText: false,
    }
  },
  props: ['width'],
  created() {
    this.questionIcon = 'mdi-help-circle'
    this.closeIcon = 'mdi-close-circle'
  },
  watch: {
    batchData() {
      this.loading = false
      this.displayResults = this.currentFile ? true : false
    },
    isErrorMsg() {
      this.loading = false
    },
  },
  computed: {
    ...mapGetters(['batchData', 'isErrorMsg']),
  },
  methods: {
    handleBatchEdit() {
      this.editMode = true
    },
    backToTable() {
      this.editMode = false
    },
    handleDisplayResult() {
      this.displayResults = !this.displayResults
    },
    expandTable() {
      this.$emit('expand-table')
    },
    collapseTable() {
      this.$emit('collapse-table')
    },
    clear() {
      this.displayResults = false
      console.log('clean...', this.displayResults)
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
#batch2 {
  position: relative;
  background: transparent;
  overflow: auto;
  height: 100%; /* To make sure height is ok in Safari*/
}
#batch {
  padding: 12px 16px;
  z-index: 2;
  min-width: 300px;
}

.card-sm {
  width: 400px;
}
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
