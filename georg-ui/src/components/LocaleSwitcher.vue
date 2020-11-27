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
          aria-label="Stäng"
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
      selectedLocale: this.currentLocale,
      // selectedLocale: Trans.currentLocale,
      theLocale: this.currentLocale,
    }
  },
  // activated: {
  //   console.log("locale...", this.$i18n.locale)
  // },
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
    //     this.theLocale = selectedLocale
    //   },
    //   get() {
    //     return this.theLocale
    //   },
    // },
  },
  methods: {
    switchLocale(value) {
      this.selectedLocale = value
    },
    closeDialog() {
      this.$emit('close-dialog', this.selectedLocale)
      // const locale = this.selectedLocale
      // if (this.$i18n.locale !== locale) {
      //   const to = this.$router.resolve({ params: { locale } })
      //   return Trans.changeLocale(locale).then(() => {
      //     this.$router.push(to.location)
      //   })
      // }
    },
  },
}
</script>
<style scoped>
.locale-select {
  width: 250px;
}
</style>
