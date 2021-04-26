<template v-model="batch">
  <v-data-table
    :footer-props="{
      itemsPerPageText: '',
      itemsPerPageOptions: [],
    }"
    :headers="currentHeader"
    :items="batch"
    :loading="loading"
    :single-expand="singleExpand"
    :single-select="singleSelect"
    :show-expand="showExpand"
    class="mt-5"
    dense
    hide-default-header
    mobile-breakpoint="0"
    show-select
    v-model="selected"
    @current-items="currentData"
    @input="handleClick"
    :options.sync="options"
  >
    <template v-slot:header="{ props: { headers } }">
      <thead class="v-data-table-header" align="end">
        <tr>
          <th
            v-for="item in headers"
            :key="item.id"
            class="column"
            @click="item.sortable ? changeSort(item.value) : ''"
          >
            <v-simple-checkbox
              v-if="!item.text"
              v-model="checked"
              :indeterminate="indeterminate"
              :ripple="false"
              @click="toggleAll"
            ></v-simple-checkbox>
            <v-icon v-if="item.icon" small @click="onListClick"
              >mdi-format-list-bulleted</v-icon
            >
            <v-icon v-if="item.icon" small @click="onTableClick" class="pl-3"
              >mdi-table</v-icon
            >
            <span v-else>{{ item.text }}</span>
          </th>
        </tr>
      </thead>
    </template>
    <template v-slot:expanded-item="{ headers, item }">
      <td :colspan="headers.length" class="ma-0 pb-2 pt-2">
        <p v-html="$t('batch.source', { source: item.sourceLocality })"></p>
        <p
          v-if="item.latLngDMS"
          v-html="
            $t('batch.result', {
              label: item.suggestedLocality,
              lat: item.lat,
              lng: item.lng,
              uncertainty: item.uncertainty,
            })
          "
        ></p>
        <p v-else v-html="$t('batch.noResult')"></p>
      </td>
    </template>
  </v-data-table>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'BatchData',
  // props: ['batch'],
  data() {
    return {
      alreadyMounted: false,
      checked: false,
      indeterminate: false,
      singleSelect: false,
      selected: [],
      batch: [],
      // expanded: [],
      singleExpand: true,
      currentPage: 0,
      options: { rowsPerPage: 10 },
      showExpand: true,
      fullHeaders: [
        {
          text: 'ID',
          align: 'start',
          value: 'id',
        },
        {
          text: `${this.$t('batch.sourceInput')}`,
          value: 'sourceLocality',
        },
        {
          text: `${this.$t('batch.suggestedLocality')}`,
          value: 'suggestedLocality',
        },
        {
          text: `${this.$t('batch.coordinatesDMS')}`,
          value: 'latLngDMS',
        },
        {
          text: `${this.$t('batch.uncertainty')}`,
          value: 'uncertainty',
        },
        { text: true, icon: true, value: '' },
      ],
      headers: [
        {
          text: 'ID',
          checkbox: true,
          align: 'start',
          value: 'id',
        },
        {
          text: `${this.$t('batch.sourceInput')}`,
          value: 'sourceLocality',
        },
        // {
        //   text: 'Datasource',
        //   value: 'dataSources',
        // },
        { text: true, icon: true, value: 'data-table-expand' },
      ],
      currentHeader: [],
      loading: false,
    }
  },
  // created() {
  // console.log('create....', this.batchData.length)
  // this.batch = !this.filters
  // ? this.batchData
  // : this.filters.hasFilters
  // ? this.filteredData
  // : this.batchData
  // },
  mounted() {
    this.alreadyMounted = true
    this.currentHeader = this.headers
    this.selected = this.selectedBatch
    this.batch = !this.filters
      ? this.batchData
      : this.filters.hasFilters
      ? this.filteredData
      : this.batchData
    // // store current page number first
    // this.prevPage = this.pagination.page

    // this.$nextTick().then(() => {
    //   this.batch = !this.filters
    //     ? this.batchData
    //     : this.filters.hasFilters
    //     ? this.filteredData
    //     : this.batchData
    // })
  },
  watch: {
    // batchData: function() {
    //   this.batch = this.batchData
    // },
    selected: function() {
      this.indeterminate =
        this.selected.length !== 0 &&
        this.selected.length !== this.batchData.length
    },

    options: {
      handler() {
        try {
          // // this.pagination.page = this.currentPage
          // console.log('********pagination watcher fired********')
          // //console.log('watch totalItems', this.pagination.totalItems)
          // console.log(
          //   'options page',
          //   this.options.page,
          //   'currnet page',
          //   this.currentPage
          // )
          // console.log('initialize', this.init)
          if (this.options.page !== this.currentPage) {
            this.currentPage = this.options.page
          }
        } catch (error) {
          if (error) {
            return
          }
        }
      },
      deep: true,
    },
  },
  computed: {
    ...mapGetters([
      'batchData',
      'currentBatch',
      'filters',
      'filteredData',
      'isErrorMsg',
      'selectedBatch',
    ]),
    pages() {
      if (this.options.rowsPerPage == null || this.options.totalItems == null) {
        return 0
      }
      return Math.ceil(this.options.totalItems / this.options.rowsPerPage)
    },
  },

  methods: {
    ...mapMutations([
      'setCurrentBatch',
      'setReBuildMarker',
      'setSelectedBatch',
    ]),

    toggleAll() {
      if (this.checked) {
        this.selected = this.batchData
      } else {
        this.selected = []
      }
    },
    currentData(data) {
      console.log('set batch data', data.length, this.alreadyMounted)
      if (this.alreadyMounted) {
        this.setCurrentBatch(data)
      }

      // this.setReBuildMarker(true)
    },
    handleClick() {
      this.setSelectedBatch(this.selected)
    },
    onListClick() {
      this.currentHeader = this.headers
      this.showExpand = true
      this.$emit('on-list-click')
    },
    onTableClick() {
      this.showExpand = false
      this.currentHeader = this.fullHeaders
      this.$emit('on-table-click')
    },
  },
}
</script>
<style scoped>
table.v-table thead tr {
  height: 120px;
}
</style>
