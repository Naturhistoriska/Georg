<template>
  <v-card
    class="mt-2 overflow-y-auto"
    :class="{ 'card-sm': $vuetify.breakpoint.smAndUp }"
    id="v-card-edit"
  >
    <v-card-title class="blue--text v-card__title, text--darken-2 ">
      {{ title }}
    </v-card-title>
    <v-card-subtitle v-html="subtitle"> </v-card-subtitle>
    <v-list-item-group active-class="white-bg">
      <v-list-item>
        <ItemIcon v-bind:iconColor="iconColor" v-bind:iconName="iconName" />
        <v-list-item-content>
          <v-combobox
            v-model="select"
            :items="items"
            auto-select-first
            dense
            :label="$t('batch.suggestedLocality')"
            item-value="id"
            item-text="fullName"
            single
            @change="onSelect"
          >
            <!-- <template v-slot:item="{ item }">
              <ItemContent
                v-bind:tleClass="tleClass"
                v-bind:title="item.fullName"
              />
            </template> -->
          </v-combobox>
        </v-list-item-content>
      </v-list-item>
      <Uncertainty />
      <v-sheet class="ml-6 mr-6 mt-0 mb-6 pa-0" max-width="380">
        <v-row>
          <v-btn color="primary" small :disabled="disable" @click="handleSave">
            {{ $t('common.save') }}
          </v-btn>
          <v-btn color="primary" outlined small class="ml-2" @click="onCancel">
            {{ $t('common.cancel') }}
          </v-btn>
          <v-spacer></v-spacer>
        </v-row>
      </v-sheet>
    </v-list-item-group>

    <!-- <v-combobox
      v-model="select"
      :items="items"
      auto-select-first
      dense
      :label="$t('batch.suggestedLocality')"
      single
      prepend-icon="mdi-map-marker"
    >
      <template v-slot:prepend>
        <v-icon color="blue darken-2">mdi-map-marker</v-icon>
      </template>
   <template v-slot:item="{ item }">
          <ItemContent
            v-bind:tleClass="tleClass"
            v-bind:title="item.fullName"
          />
        </template>
    </v-combobox> -->
    <!-- <v-list-item role="option" @keypress.prevent="onclick()">
      <ItemIcon v-bind:iconColor="iconColor" v-bind:iconName="edit" />
      <v-list-item-content @click.prevent="onclick()">
        uncertainty
      </v-list-item-content>
    </v-list-item> -->

    <!-- <div
      v-if="selectedBatch.length"
      id="scroll-target"
      class="overflow-y-auto"
      :style="height"
    >
      <div id="editList">
        batch....
        <template v-for="batch in selectedBatch">
          batch....d...d
          <Edit v-bind:batch="batch" :key="batch.id" />
        </template>
      </div>
    </div> -->
  </v-card>
</template>
<script>
import { mapGetters } from 'vuex'
// import ItemContent from './baseComponents/ItemContent'
import ItemIcon from './baseComponents/ItemIcon'
import Uncertainty from '../components/Uncertainty'
export default {
  name: 'BatchEdit',
  components: {
    // ItemContent,
    ItemIcon,
    Uncertainty,
  },
  data() {
    return {
      disable: true,
      title: '',
      subtitle: '',
      suggestedLocality: null,
    }
  },
  created() {
    this.iconColor = 'blue darken-2'
    this.iconName = 'mdi-map-marker'
    this.tleClass = 'font-weight-medium body-2 text-truncate'
  },
  mounted() {
    if (this.selectedBatch.length === 1) {
      this.title = this.$t('common.edit')
      this.subtitle = this.$t('batch.batchEditSubtitle', {
        id: this.selectedBatch[0].id,
        source: this.selectedBatch[0].sourceLocality,
      })
    } else {
      this.title = this.$t('batch.multipleEdit')
      this.subtitle = this.selectedBatch[1].id
    }
  },
  computed: {
    ...mapGetters(['editData', 'selectedBatch']),
    items() {
      let elements = []
      this.editData.map(entry => {
        entry.data.map(item => {
          // const { gid, name, region, source } = item.properties
          const { coordinates, country, gid, name, region } = item.properties

          let fullName = region !== undefined ? `${name}, ${region}` : `${name}`
          fullName =
            country !== undefined ? `${fullName}, ${country}` : fullName

          const { dd, dms } = coordinates
          const dmsString = `${dms[0]} ${dms[1]}`
          // const uncertainty =
          //   source === 'gbif' || source === 'swe-virtual-herbarium'
          //     ? entry.properties.addendum.georg.coordinateUncertaintyInMeters
          //     : undefined
          const element = {
            fullName,
            id: gid,
            name,
            lat: dd[0],
            lng: dd[1],
            dms: dmsString,
          }
          elements.push(element)
        })
      })
      return elements
    },

    select: {
      set(select) {
        this.suggestedLocality = select
      },
      get() {
        if (this.suggestedLocality) {
          return this.suggestedLocality
        }
        return this.items.length > 0 ? this.items[0].fullName : ''
      },
    },
  },
  methods: {
    onSelect() {
      this.disable = false
    },
    handleSave() {
      const { fullName, lat, lng, dms } = this.select
      this.selectedBatch.forEach(element => {
        element.suggestedLocality = fullName
        element.lat = lat
        element.lng = lng
        Element.dms = dms
      })
    },
    onCancel() {},
  },
}
</script>
<style scoped>
#v-card-edit {
  z-index: 2;
  min-width: 300px;
}
::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 7px;
}
::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background-color: rgba(0, 0, 0, 0.5);
  box-shadow: 0 0 1px rgba(255, 255, 255, 0.5);
}
.card-sm {
  width: 400px;
}
</style>
