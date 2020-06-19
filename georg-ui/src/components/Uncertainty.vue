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
        @click="addUncertaintyValue(tag.value)"
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
              v-model="accuracy"
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
      accuracy: null,
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
    if (this.uncertainty >= 0) {
      this.accuracy = this.uncertainty
      this.uncertintyChangedByChip = true
    }
  },
  watch: {
    accuracy: function() {
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
    ...mapGetters(['uncertainty']),
  },
  methods: {
    ...mapMutations(['setUncertainty']),
    setUncertaintyValue() {
      this.setUncertainty(this.accuracy)
      this.disableSetUncertaintyBtn = true
      this.msgClass = 'grey--text'
    },
    addUncertaintyValue(value) {
      this.accuracy = value
      this.setUncertaintyValue()
      this.uncertintyChangedByChip = true
    },
    checkUncertaintyValue() {
      this.accuracy =
        this.accuracy > 10000000
          ? 10000000
          : this.accuracy < 0
          ? 0
          : this.accuracy
    },
  },
}
</script>
