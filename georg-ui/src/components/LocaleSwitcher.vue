<template>
  <v-dialog v-model="dialogStatus" max-width="320" persistent>
    <v-card>
      <v-card-title>
        {{ $t('common.languageSetting') }}
      </v-card-title>
      <v-list-item-content>
        <v-col cols="auto" class="pl-4 ml-4">
          <v-select
            class="locale-select"
            v-model="selectedLocale"
            :items="supportedLocales"
            item-text="value"
            item-value="id"
            menu-props="auto"
            :label="$t('common.selectLanguage')"
            @change="switchLocale"
            hide-details
            prepend-inner-icon="mdi-web"
          ></v-select>
        </v-col>
      </v-list-item-content>
      <v-card-actions>
        <v-btn
          :aria-label="$t('common.close')"
          right
          color="blue darken-1"
          text
          @click="closeDialog"
        >
          {{ $t('common.ok') }}
        </v-btn>
        <v-spacer></v-spacer>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script>
import { Trans } from '@/plugins/Translation'
export default {
  name: 'LocaleSwitcher',
  props: ['currentLocale', 'dialogStatus'],
  data() {
    return {
      selectedLocale: Trans.currentLocale,
      theLocale: this.currentLocale,
    }
  },
  watch: {
    $route(to) {
      const { locale } = to.params
      this.selectedLocale = locale
      // this.drawer = name !== 'Home' && name !== 'Search'
      // this.routeName = name
    },
  },
  computed: {
    supportedLocales() {
      let supportLocale = []
      Trans.supportedLocales.forEach(e => {
        const item = new Object()
        item.id = e
        item.value = this.$t(`common.${e}`)
        supportLocale.push(item)
      })
      return supportLocale
    },
    // selectedLocale: {
    //   set(selectedLocale) {
    //     console.log('set....', selectedLocale)
    //     this.theLocale = selectedLocale
    //     // this.localeChanged = false
    //   },
    //   get() {
    //     console.log('what is locale...', this.theLocale, this.currentLocale)
    //     // this.theLocale = this.currentLocale
    //     // if (this.localeChanged) {
    //     //   return this.selectedLocale
    //     // }
    //     return this.theLocale
    //   },
    // },
  },
  methods: {
    switchLocale() {},
    closeDialog() {
      this.$emit('close-dialog', this.selectedLocale)
    },
  },
}
</script>
<style scoped>
.locale-select {
  width: 250px;
}
</style>
