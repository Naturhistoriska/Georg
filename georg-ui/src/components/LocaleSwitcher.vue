<template>
  <v-col cols="auto">
    <v-select
      class="locale-select"
      dense
      v-model="defaultLocale"
      :items="supportedLocales"
      @change="switchLocale"
    />

    <!-- <v-list dense>
      <v-list-item
        dense
        v-for="(locale, i) in locales"
        :key="i"
        @click="switchLocale(locales[i])"
      >
        <v-list-item-content>
          <v-list-item-title v-text="locales[i]"></v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list> -->
  </v-col>
</template>
<script>
import { Trans } from '@/plugins/Translation'
export default {
  name: 'LocaleSwitcher',
  data() {
    return {
      // locales: process.env.VUE_APP_I18N_SUPPORTED_LOCALES.split(','),
      defaultLocale: Trans.defaultLocale,
    }
  },
  computed: {
    supportedLocales() {
      return Trans.supportedLocales
    },
  },
  methods: {
    switchLocale(locale) {
      console.log('switchLocale: ', locale)
      if (this.$i18n.locale !== locale) {
        const to = this.$router.resolve({ params: { locale } })
        return Trans.changeLocale(locale).then(() => {
          console.log('to location...', to.location)
          this.$router.push(to.location)
        })

        // this.$i18n.locale = locale
        // const to = this.$router.resolve({ params: { locale } })
        // this.$router.push(to.location)
      }
    },
  },
}
</script>
<style scoped>
/* .v-select__selections input {
  display: none;
}
.v-select__selections input {
  width: 0;
}
.v-input__control {
  width: 10px !important;
} */
.locale-select {
  width: 50px;
}
</style>
