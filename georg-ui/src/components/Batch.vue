<template>
  <div>
    <v-card id="batch" :style="width">
      <AdjustFilter
        v-if="adjustFilter"
        @close-adjust-filter="closeAdjustFilter"
      />
      <div class="mt-n5 ml-n4 pa-0" v-else>
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
              :value="currentFile"
              :disabled="loading"
              prepend-inner-icon="attach_file"
              prepend-icon=""
              show-size
              :loading="loading"
              @change="upload"
              @click="onClick"
              @click:clear="clear"
            >
              <template v-slot:append-outer>
                <v-progress-circular
                  v-if="loading"
                  color="grey"
                  indeterminate
                  small
                />
              </template>
            </v-file-input>
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
                <router-link :to="aboutBatchLink" class="routerlink">{{
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
            v-bind:isEdit="editView"
            v-bind:isBatch="true"
            v-if="currentFile && !loading"
          />
        </v-sheet>
        <!-- <div v-if="displayResults && !editView"> -->
        <div v-if="showResults && !editView">
          <BatchController @adjust-filter="openAdjustFilter" />
          <v-divider class="mt-2"></v-divider>
          <v-row class="ml-2 mr-0 mt-n2 pa-0">
            <BatchData
              @on-table-click="expandTable"
              @on-list-click="collapseTable"
            />
          </v-row>
          <BatchAction @edit-batch="handleEdit" />
        </div>
      </div>
    </v-card>
    <BatchEdit v-if="displayResults && editView" />
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
// import Service from '../Service'
// const service = new Service()
export default {
  name: 'Batch',
  components: {
    AdjustFilter: () => import('../components/AdjustFilter'),
    ActionIconButton: () => import('./baseComponents/ActionIconButton'),
    BatchAction: () => import('../components/BatchAction'),
    BatchController: () => import('../components/BatchController'),
    BatchData: () => import('../components/BatchData'),
    BatchEdit: () => import('../components/BatchEdit'),
    ResultHeader: () => import('../components/ResultHeader'),
  },
  props: ['width'],
  data() {
    return {
      adjustFilter: false,
      // editMode: false,
      currentFile: undefined,
      // displayResults: false,
      showResult: false,
      // fileInfos: [],
      loading: false,
      fileURL: null,
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
      // this.displayResults = this.currentFile ? true : false
    },
    isErrorMsg() {
      this.loading = false
      // this.displayResults = !this.isErrorMsg
      if (this.isErrorMsg) {
        this.setCurrentBatch([])
      }
    },
  },
  mounted() {
    if (!this.currentFile) {
      this.currentFile = this.uploadedFile
    }
    if (this.currentFile) {
      this.setDisplayResults(true)
      // this.displayResults = true
    }
    // this.setReBuildMarker(true)
  },
  computed: {
    ...mapGetters([
      'batchData',
      'displayResults',
      'isErrorMsg',
      'editView',
      'reBuildMarker',
      'selectedBatch',
      'uploadedFile',
    ]),
    aboutBatchLink() {
      const locale = this.$i18n.locale
      return locale === 'sv'
        ? `/${locale}/om#aboutBatch`
        : `/${locale}/about#aboutBatch`
    },
    showResults: function() {
      if (
        this.currentFile &&
        !this.isErrorMsg &&
        !this.loading &&
        this.displayResults
      ) {
        return true
      }
      return false
    },
    // showResults: {
    //   set(displayResult) {
    //     this.displayResults = displayResult
    //   },
    //   get() {
    //     if (this.editView) {
    //       return false
    //     }
    //     if (
    //       this.currentFile &&
    //       !this.isErrorMsg &&
    //       !this.loading &&
    //       this.displayResults
    //     ) {
    //       return true
    //     }
    //     return this.displayResults
    //   },
    // },

    // showResults() {
    //   if (this.editView) {
    //     return false
    //   }
    //   if (this.currentFile && !this.isErrorMsg && !this.loading) {
    //     return true
    //   }
    //   return false
    // },
  },
  methods: {
    ...mapMutations([
      'setAccuracy',
      'setBatchData',
      'setCurrentBatch',
      'setDisplayResults',
      'setFilteredData',
      'setFilters',
      'setIsErrorMsg',
      'setMsgKey',
      'setReBuildMarker',
      'setUploadedFile',
    ]),
    closeAdjustFilter() {
      this.adjustFilter = !this.adjustFilter
    },
    openAdjustFilter() {
      this.adjustFilter = !this.adjustFilter
      this.$emit('open-adjustFilter')
    },
    // handleBatchEdit() {
    //   this.$emit('batch-edit')
    //   this.editMode = true
    // },
    // backToTable() {
    //   this.editMode = false
    // },
    // handleDisplayResult() {
    //   this.displayResults = !this.displayResults
    //   this.showResults = this.displayResults
    // },
    expandTable() {
      this.$emit('expand-table')
    },
    collapseTable() {
      this.$emit('collapse-table')
    },
    clear() {
      this.currentFile = null
      this.setDisplayResults(false)
      this.setMsgKey('')
      this.setBatchData([])
      this.setCurrentBatch([])
      this.setFilteredData([])
      this.setFilters({})
      this.setIsErrorMsg(false)
      // this.$emit('clear-file')
    },
    onClick() {
      this.showHelpText = false
    },
    async upload(file) {
      if (file && file.name) {
        this.currentFile = file
        if (!this.currentFile) {
          this.loading = false
          this.message = 'Please select a file!'
          return
        }
        this.$emit('upload', this.currentFile)
        this.loading = true
        this.setUploadedFile(file)
        this.setDisplayResults(true)
      }
    },
    handleEdit() {
      this.setAccuracy(-1)
      this.$emit('batch-edit')
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
