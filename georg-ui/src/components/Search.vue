<template>
  <div class="ma-0 pa-0">
    <v-card-text class="ma-0 pa-0">
      <v-text-field
        single-line
        filled
        dense
        hide-details
        v-model="address"
        placeholder="Sök plats"
        append-icon="search"
        autofocus
        clearable
        :loading="loading"
        @click:clear="clearSearch"
        @click:append="search"
        @keyup.enter="search"
      ></v-text-field>
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
  </div>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'
import Service from '../Service'
const service = new Service()

export default {
  name: 'Search',
  props: [],
  data() {
    return {
      address: '',
      isSearch: false,
      loading: false,
      linkText: '< TILLBAKA TILL TRÄFFLISTAN',
      message: '',
    }
  },

  computed: {
    ...mapGetters(['detailView']),
  },

  methods: {
    ...mapMutations(['setResults', 'setDetailView', 'setSelectedResultId']),
    clearSearch() {
      this.setResults([])
      this.setDetailView(false)
      this.setSelectedResultId('')
      this.message = ''
    },
    onclick() {
      this.setDetailView(false)
    },
    search() {
      this.loading = true
      service
        .fetchAddressResults(this.address)
        .then(response => {
          this.results = response.features.filter(function(result) {
            return result.properties.country != null
          })
          this.loading = false
          this.setResults(this.results)
          this.setDetailView(false)
          this.setSelectedResultId('')
          this.message =
            this.results.length > 0
              ? this.results.length + ' träffar'
              : 'Sökningen gav inga träffar'
        })
        .catch(function() {})
    },
  },
}
</script>

<style scoped>
#backResultListLink {
  margin-left: -10px;
}
</style>
