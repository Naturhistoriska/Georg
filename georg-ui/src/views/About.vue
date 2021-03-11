<template>
  <v-container fluid>
    <v-row>
      <v-col class="col-lg-6 col-md-12 pl-md-10 pr-md-12">
        <h1>{{ $t('about.about') }}</h1>
        <p class="title font-weight-light text-darken-3">
          {{ $t('about.aboutText') }}
        </p>
        <h2>{{ $t('about.howToWorks') }}</h2>
        <p>
          {{ $t('about.howToWorksText') }}
        </p>
        <h3>{{ $t('about.howToWorkInpractice') }}</h3>
        <ol>
          <li class="pt-1">
            {{ $t('about.howToWorkInpracticeText1') }}
          </li>
          <li class="pt-1">
            {{ $t('about.howToWorkInpracticeText2') }}
          </li>
          <li class="pt-1">
            {{ $t('about.howToWorkInpracticeText3') }}
          </li>
        </ol>
        <p class="mt-4">
          {{ $t('about.howToWorkInpracticeText4') }}
        </p>
        <p>
          {{ $t('about.howToWorkInpracticeText5') }}
          <a
            href="https://github.com/Naturhistoriska/Georg/tree/master/georg-connector-example"
            >{{ $t('about.fileMakerExampleLink') }}</a
          >.
        </p>
        <h3>{{ $t('about.placesInNordics') }}</h3>
        <p>
          {{ $t('about.placesInNordicsText1') }}
        </p>
        <p class="font-italic">
          {{ $t('about.placesInNordicsText2') }}
        </p>
        <h2>{{ $t('about.dataSource') }}</h2>
        <p>
          {{ $t('about.dataSourceText') }}
        </p>
        <h3>{{ $t('about.gbif') }}</h3>
        <p>
          {{ $t('about.gbifText') }}
          <span class="font-weight-medium">{{
            $t('about.occurrenceDataset')
          }}</span>
          {{ $t('about.gbifText1') }}
        </p>
        <h4>
          {{ $t('about.nhrsNrm') }}
        </h4>
        <p>
          {{ $t('about.nhrsNrmLinkText') }} (<a
            href="https://doi.org/10.15468/fpzyjx"
            >https://doi.org/10.15468/fpzyjx</a
          >).
        </p>
        <h4>{{ $t('about.phanerogamic') }}</h4>
        <p>
          {{ $t('about.phanerogamicLinkText') }} (<a
            href="https://doi.org/10.15468/yo3mmu"
            >https://doi.org/10.15468/yo3mmu</a
          >).
        </p>
        <h4>{{ $t('about.botanyUPS') }}</h4>
        <p>
          {{ $t('about.botanyUPSLinkText') }} (<a
            href="https://doi.org/10.15468/ufmslw"
            >https://doi.org/10.15468/ufmslw</a
          >).
        </p>
        <h3>{{ $t('about.oa') }}</h3>
        <p>
          <a href="https://openaddresses.io/">{{ $t('about.oaLinkText') }}</a>
          {{ $t('about.oaDescription') }}
        </p>
        <h3>{{ $t('about.osm') }}</h3>
        <p>
          <a href="https://www.openstreetmap.org/about">{{
            $t('about.osmLinkText')
          }}</a>
          {{ $t('about.osmDescription') }}
        </p>
        <h3>{{ $t('about.svh') }}</h3>
        <p>
          <a href="http://herbarium.emg.umu.se/">{{
            $t('about.svhLinkText')
          }}</a>
          {{ $t('about.svhDescription') }}
          <a href="https://github.com/mossnisse/Virtuella-Herbariet">{{
            $t('about.svhGithubLinkText')
          }}</a
          >.
        </p>
        <h3>{{ $t('about.wof') }}</h3>
        <p>
          <a href="https://whosonfirst.org/what">{{
            $t('about.wofLinkText')
          }}</a
          >{{ $t('about.wofDescription') }}
        </p>
        <h2>{{ $t('about.browser') }}</h2>
        <p>
          {{ $t('about.browserDescription') }}
        </p>
        <h2 id="aboutBatch">About batch upload</h2>
        <p>
          Batch upload allows user to upload csv file with locality information
          for georeference, and get the best match suggestion for each locality
          in csv back from API. You can download sample csv templete from Georg
          github. To download sample csv file from github, run:
        </p>
        <p>
          cURL -o /{path_to_directory}/sample.csv
          https://raw.githubusercontent.com/Naturhistoriska/Georg/master/data/georg_batch.csv
        </p>
        <h2>{{ $t('about.api') }}</h2>
        <p>
          {{ $t('about.apiText') }}
        </p>
        <p>
          {{ $t('about.apiDocumention') }}
          <a
            href="https://georg.nrm.se/swagger-ui/index.html?url=/api/swagger.json"
          >
            {{ $t('about.apiLinkText') }} </a
          >.
        </p>
        <h2>{{ $t('about.version') }}</h2>
        <p>{{ $t('about.version') }}: {{ version }}</p>
        <p>
          {{ $t('about.project') }}
          <a href="https://github.com/Naturhistoriska/Georg">{{
            $t('about.github')
          }}</a
          >.
        </p>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  name: 'About',
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
