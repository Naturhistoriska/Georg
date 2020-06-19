<template>
  <GbifDetail v-if="isGbif" />
  <NewMarkerDetail v-else-if="isNewMarker" />
  <Detail v-else />
</template>
<script>
import { mapGetters } from 'vuex'
import Detail from './Detail'
import GbifDetail from './GbifDetail'
import NewMarkerDetail from './NewMarkerDetail'

export default {
  name: 'DetailView',
  components: {
    Detail,
    GbifDetail,
    NewMarkerDetail,
  },
  computed: {
    ...mapGetters(['isGbif', 'isNewMarker']),
  },
}
</script>
<style scoped>
#v-card-detail {
  z-index: 2;
}
.alertText {
  font-size: 14px;
}

#v-card-detail .v-card__title {
  word-break: break-word;
}
</style>
