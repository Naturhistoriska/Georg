<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>{{ $t('accessibility.accessibility') }}</h1>
        <p class="title font-weight-light text-darken-3">
          {{ $t('accessibility.accessibilityText') }}
        </p>
        <h2>{{ $t('accessibility.how') }}</h2>
        <p>
          {{ $t('accessibility.howText') }}
        </p>
        <h2>{{ $t('accessibility.what') }}</h2>
        <p>
          {{ $t('accessibility.whatText') }}
          <a href="mailto: georg@nrm.se">georg@nrm.se</a>
        </p>
        <h2>{{ $t('accessibility.report') }}</h2>
        <p>
          {{ $t('accessibility.reportText') }}
          <a href="https://www.nrm.se/ommuseet/aterkoppling.9005462.html">{{
            $t('accessibility.reportLink')
          }}</a>
          {{ $t('accessibility.reportText1') }}
        </p>
        <h2>{{ $t('accessibility.supervision') }}</h2>
        <p>
          {{ $t('accessibility.supervisionText') }}
          <a href="https://www.digg.se/tdosanmalan">{{
            $t('accessibility.supervisionLink')
          }}</a>
          {{ $t('accessibility.supervisionText1') }}
        </p>

        <h2>{{ $t('accessibility.techInfo') }}</h2>
        <p>
          {{ $t('accessibility.techInfoText') }}
        </p>
        <h2>{{ $t('accessibility.contentNotAvailable') }}</h2>
        <h3>{{ $t('accessibility.contentNotCover') }}</h3>
        <p>
          {{ $t('accessibility.contentText') }}
        </p>
        <p>
          {{ $t('accessibility.contentText1') }}
        </p>
        <h2>{{ $t('accessibility.test') }}</h2>
        <p>{{ $t('accessibility.testText1') }}georg.nrm.se.</p>
        <p>{{ $t('accessibility.testText2') }}</p>
        <p>{{ $t('accessibility.testText3') }}</p>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  name: 'Contact',
  data() {
    return {
      version: 0,
    }
  },

  mounted() {
    this.setVersion()
  },
  methods: {
    setVersion() {
      this.version = process.env.VUE_APP_VERSION
    },
  },
}
</script>
