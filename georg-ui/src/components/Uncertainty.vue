<template>
  <v-card class="mt-2" width="400" id="v-card-uncertainty" flat>
    <v-card-title class="grey--text text--darken-2"
      >Din osäkerhetsradie</v-card-title
    >
    <v-card-text>
      <v-chip
        class="mr-4 mt-2"
        v-for="tag in tags"
        :key="tag.label"
        @click="addAccuracyValue(tag.value)"
        >{{ tag.label }}</v-chip
      >
      <v-container class="mb-0 pb-0">
        <v-row>
          <v-col cols="5" class="mt-0 pt-0 pl-1">
            <v-text-field
              hide-details
              single-line
              suffix="meter"
              type="number"
              color="red darken-2"
              v-model="accuracyValue"
              label="Radie ?"
              min="0"
              max="10000000"
            ></v-text-field>
          </v-col>
          <v-col cols="7">
            <v-subheader :class="msgClass">(min: 0, max: 10000000)</v-subheader>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions>
      <v-btn
        @click="setUncertaintyValue"
        color="red darken-2"
        text
        :disabled="disableSetUncertaintyBtn"
        >Sätt osäkerhet</v-btn
      >
    </v-card-actions>
  </v-card>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Uncertainty',
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
  },
  computed: {
    ...mapGetters(['accuracy', 'selectedResult']),
  },
  methods: {
    ...mapMutations(['setAccuracy']),
    setUncertaintyValue() {
      this.selectedResult.properties.coordinateUncertaintyInMeters = this.accuracyValue
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
