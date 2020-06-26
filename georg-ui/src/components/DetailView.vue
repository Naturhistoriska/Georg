<template>
  <GbifDetail v-if="isGbif" />
  <Detail v-else />
</template>
<script>
import { mapGetters } from 'vuex'
import Detail from './Detail'
import GbifDetail from './GbifDetail'

export default {
  name: 'DetailView',
  components: {
    Detail,
    GbifDetail,
  },
  computed: {
    ...mapGetters(['isGbif']),
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
