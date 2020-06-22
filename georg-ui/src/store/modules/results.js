const state = {
  accuracy: -1,
  detialViewId: '',
  didSearch: false,
  displayMessage: false,
  isDetailView: false,
  hoveredResultId: '',
  newMarker: {},
  results: [],
  selectedResult: {},
  selectedResultId: '',
  selectedMarkerId: '',
  unhoveredResultId: '',
}
const getters = {
  accuracy: state => state.accuracy,
  detailView: state => state.isDetailView,
  detialViewId: state => state.detialViewId,
  displayMessage: state => state.didSearch,
  didSearch: state => state.didSearch,
  hoveredResultId: state => state.hoveredResultId,
  isNewMarker: state => state.selectedResult.properties.id === 'newMarker',
  isGbif: state => state.selectedResult.properties.source === 'gbif',
  isWOF: state => state.selectedResult.properties.source === 'whosonfirst',
  newMarker: state => state.newMarker,
  unhoveredResultId: state => state.unhoveredResultId,
  selectedResult: state => state.selectedResult,
  results: state => state.results,
  selectedResultId: state => state.selectedResultId,
  selectedMarkerId: state => state.selectedMarkerId,
}

const actions = {}

const mutations = {
  setAccuracy: (state, payload) => (state.accuracy = payload),
  setDidSearch: (state, payload) => (state.didSearch = payload),
  setHovedResultId: (state, payload) => (state.hoveredResultId = payload),
  setMouseLeaveResultId: (state, payload) =>
    (state.unhoveredResultId = payload),
  setResults: (state, payload) => (state.results = payload),
  setSelectedResult: (state, payload) => (state.selectedResult = payload),
  setSelectedResultId: (state, payload) => (state.selectedResultId = payload),
  setDetailView: (state, payload) => (state.isDetailView = payload),
  setDetialViewId: (state, payload) => (state.detialViewId = payload),
  setSelectedMarkerId: (state, payload) => (state.selectedMarkerId = payload),
  setNewMarkers: (state, payload) => (state.newMarker = payload),
}

export default {
  state,
  getters,
  actions,
  mutations,
}
