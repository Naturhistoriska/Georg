<template>
  <v-list-item-group active-class="white-bg">
    <v-list-item>
      <ItemIcon
        v-bind:iconColor="redDarkenColor"
        v-bind:iconName="markerWithRadius"
      />
      <ItemTitle>{{ $t('result.uncertainty') }}</ItemTitle>
      <ItemAction />
    </v-list-item>
    <v-card class="ml-16" id="v-card-uncertainty" flat>
      <v-chip-group>
        <v-chip
          class="mr-3 ml-1 mt-1 mb-0"
          v-for="tag in tags"
          :key="tag.label"
          role="button"
          :aria-label="$t('result.setUncertaintyTo', { label: tag.label })"
          @click="addAccuracyValue(tag.value)"
          @keypress="addAccuracyValue(tag.value)"
          >{{ tag.label }}</v-chip
        >
      </v-chip-group>
      <v-container class="mt-0 pt-1 mb-0 pb-0">
        <v-row>
          <v-col cols="6" class="mt-0 pt-0 pl-1">
            <v-text-field
              hide-details
              single-line
              suffix="meter"
              type="number"
              color="red darken-2"
              v-model="accuracyValue"
              :label="$t('result.radiusLabel')"
              min="0"
              max="10000000"
              @keyup.enter="setUncertaintyValue"
            ></v-text-field>
          </v-col>
          <v-col cols="6" class="mt-1 ml-n6 pb-0">
            <v-btn
              @click="setUncertaintyValue"
              @keypress="setUncertaintyValue"
              :aria-label="$t('result.setUncertainty')"
              color="red darken-2"
              text
              :disabled="disableSetUncertaintyBtn"
              >{{ $t('result.setUncertainty') }}</v-btn
            >
          </v-col>
        </v-row>
      </v-container>
    </v-card>
  </v-list-item-group>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import ItemAction from './baseComponents/ItemAction'
import ItemIcon from './baseComponents/ItemIcon'
import ItemTitle from './baseComponents/ItemTitle'
export default {
  name: 'Uncertainty',
  components: {
    ItemAction,
    ItemIcon,
    ItemTitle,
  },
  data() {
    return {
      accuracyValue: null,
      disableSetUncertaintyBtn: true,
      msgClass: 'grey--text',
      tags: [
        { label: '100 m', value: 100 },
        { label: '1 km', value: 1000 },
        { label: '10 km', value: 10000 },
        { label: '100 km', value: 100000 },
      ],
      uncertintyChangedByChip: false,
    }
  },
  created() {
    this.redDarkenColor = 'red darken-2'
    this.markerWithRadius = 'mdi-map-marker-radius'
    this.iconToggleGbifData = 'mdi-chevron-down'
    this.importIconName = 'mdi-database-import'
    this.occurrenceDataset = 'GBIF Occurrence dataset'
    this.occurrenceId = 'GBIF Occurrence ID'
    this.opennewicon = 'mdi-open-in-new'
  },
  mounted() {
    if (this.accuracy >= 0) {
      this.accuracyValue = this.accuracy
      this.uncertintyChangedByChip = true
    }
  },
  watch: {
    accuracyValue: function() {
      this.$nextTick(() => {
        this.checkUncertaintyValue()
        if (!this.uncertintyChangedByChip) {
          this.disableSetUncertaintyBtn = false
          this.msgClass = 'red--text darken-2'
        }
        this.uncertintyChangedByChip = false
      })
    },
    accuracy: function() {
      this.accuracyValue = this.accuracy < 0 ? null : this.accuracy
    },
  },
  computed: {
    ...mapGetters(['accuracy', 'selectedMarker']),
  },
  methods: {
    ...mapMutations(['setAccuracy']),
    setUncertaintyValue() {
      this.selectedMarker.properties.coordinateUncertaintyInMeters = this.accuracyValue
      this.setAccuracy(this.accuracyValue)
      this.disableSetUncertaintyBtn = true
      this.msgClass = 'grey--text'
    },
    addAccuracyValue(value) {
      this.accuracyValue = value
      this.setUncertaintyValue()
      this.uncertintyChangedByChip = true
    },
    checkUncertaintyValue() {
      this.accuracyValue =
        this.accuracyValue > 10000000
          ? 10000000
          : this.accuracyValue < 0
          ? 0
          : this.accuracyValue
    },
  },
}
</script>
<style>
/* .white-bg:before {
  opacity: 0 !important;
} */

/* .highlight::before {
  background-color: currentColor !important;
  opacity: 0.06 !important;
}

.v-application .show-btn-hover.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}

.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
} */
</style>
