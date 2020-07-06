<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-autocomplete
        v-model="model"
        :items="items"
        :loading="isLoading"
        :search-input.sync="search"
        @click:clear="clearSearch"
        autofocus
        auto-select-first
        clearable
        hide-no-data
        hide-selected
        item-text="name"
        item-value="API"
        placeholder="Sök plats"
        prepend-inner-icon="search"
        return-object
      ></v-autocomplete>
    </v-card-text>
    <v-card-actions v-if="detailView">
      <v-btn
        small
        color="grey darken-2"
        id="backResultListLink"
        text
        @click.prevent="onclick()"
        >{{ linkText }}</v-btn
      >
    </v-card-actions>
    <v-card-actions v-else>
      <div id="message" class="pt-2 grey--text text--darken-3 body-2">
        {{ message }}
      </div>
    </v-card-actions>
    <!-- <v-expand-transition>
      <v-list v-if="model" class="red lighten-3">
        <v-list-item v-for="(field, i) in fields" :key="i">
          <v-list-item-content>
            <v-list-item-title v-text="field.value"></v-list-item-title>
            <v-list-item-subtitle v-text="field.key"></v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-expand-transition>-->
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import Service from '../Service'
const service = new Service()

export default {
  name: 'AutocompleteSearch',

  data: () => ({
    descriptionLimit: 60,
    entries: [],
    isLoading: false,
    linkText: '< TILLBAKA TILL TRÄFFLISTAN',
    message: '',
    model: null,
    search: null,
  }),

  computed: {
    ...mapGetters(['detailView']),
    items() {
      return this.entries.map(entry => {
        const name = entry.properties.name
        return Object.assign({}, entry, { name })
      })
    },
  },

  watch: {
    model(value) {
      if (value !== null && value !== undefined) {
        this.searchAddress(value)
      }
    },
    search(value) {
      this.autoCompleteSearch(value)
    },
  },
  methods: {
    ...mapMutations([
      'setResults',
      'setDidSearch',
      'setDetailView',
      'setSelectedResultId',
    ]),
    searchAddress({ name }) {
      this.isLoading = true
      service
        .fetchAddressResults(name)
        .then(response => {
          this.results = response.features.filter(function(result) {
            return result.properties.country != null
          })
          this.setResults(this.results)
          this.setDidSearch(true)
          this.setDetailView(false)
          this.setSelectedResultId('')
          this.message =
            this.results.length > 0
              ? this.results.length + ' träffar'
              : 'Sökningen gav inga träffar'
        })
        .catch(function() {})
        .finally(() => (this.isLoading = false))
    },
    autoCompleteSearch(value) {
      if (!this.isEmpty(value) && value.length >= 3) {
        this.isLoading = true
        service
          .autoCompleteSearch(value)
          .then(response => {
            const { features } = response
            this.entries = features
          })
          .catch(function() {})
          .finally(() => (this.isLoading = false))
      }
    },
    clearSearch() {
      this.setResults([])
      this.setDidSearch(false)
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.message = ''
    },
    onclick() {
      this.setDetailView(false)
    },
    isEmpty: function(value) {
      return (
        value === null ||
        value === undefined ||
        (value.constructor === Object && Object.keys(value).length === 0)
      )
    },
  },
}
</script>
