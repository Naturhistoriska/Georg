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
      <Uncertainty
        v-bind:isBatch="true"
        @change-uncertainty="handleEditUncertainty"
      />
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
  </v-card>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
import ItemIcon from './baseComponents/ItemIcon'
import Uncertainty from '../components/Uncertainty'
export default {
  name: 'BatchEdit',
  components: {
    ItemIcon,
    Uncertainty,
  },
  data() {
    return {
      dataChanged: false,
      disable: true,
      subtitle: '',
      suggestedLocality: null,
      title: '',
      uncertainty: null,
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
      // this.uncertainty = this.selectedBatch[0].uncertainty
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
          const {
            coordinates,
            country,
            gid,
            name,
            region,
            source,
          } = item.properties

          let fullName = region !== undefined ? `${name}, ${region}` : `${name}`
          fullName =
            country !== undefined ? `${fullName}, ${country}` : fullName

          const { dd, dms } = coordinates
          const dmsString = `${dms[0]} ${dms[1]}`
          const uncertainty =
            source === 'gbif' || source === 'swe-virtual-herbarium'
              ? item.properties.addendum.georg.coordinateUncertaintyInMeters
              : undefined
          console.log('what,..', fullName, source, uncertainty)
          const element = {
            fullName,
            id: gid,
            name,
            lat: dd[0],
            lng: dd[1],
            dms: dmsString,
            uncertainty,
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
        if (this.selectedBatch.length === 1) {
          return this.selectedBatch[0].suggestedLocality
        }
        return this.items.length > 0 ? this.items[0].fullName : ''
      },
    },
  },
  methods: {
    ...mapMutations(['setAccuracy', 'setEditView']),
    onSelect() {
      this.disable = false
      this.dataChanged = true
      const { uncertainty } = this.select
      if (uncertainty) {
        this.uncertainty = uncertainty
        this.setAccuracy(uncertainty)
      }
    },
    handleSave() {
      const { fullName, lat, lng, dms } = this.select
      if (this.dataChanged) {
        this.selectedBatch.forEach(element => {
          element.suggestedLocality = fullName
          element.lat = lat
          element.lng = lng
          element.dms = dms
          element.uncertainty =
            this.uncertainty != null ? this.uncertainty : element.uncertainty
        })
      } else {
        this.selectedBatch.forEach(element => {
          element.uncertainty =
            this.uncertainty != null ? this.uncertainty : element.uncertainty
        })
      }
      this.dataChanged = false
      this.setEditView(false)
    },
    onCancel() {
      this.setEditView(false)
    },
    handleEditUncertainty(value) {
      this.disable = false
      this.uncertainty = value
    },
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
