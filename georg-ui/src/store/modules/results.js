const state = {
  displayMessage: false,
  isDetailView: false,
  didSearch: false,
  hoveredResultId: '',
  unhoveredResultId: '',
  results: [],
  selectedResult: {},
  selectedResultId: '',
  detialViewId: '',
  selectedMarkerId: '',
  newMarker: {},
  uncertainty: -1,
}
const getters = {
  detailView: state => state.isDetailView,
  didSearch: state => state.didSearch,
  displayMessage: state => state.didSearch,
  hoveredResultId: state => state.hoveredResultId,
  unhoveredResultId: state => state.unhoveredResultId,
  selectedResult: state => state.selectedResult,
  results: state => state.results,
  selectedResultId: state => state.selectedResultId,
  detialViewId: state => state.detialViewId,
  selectedMarkerId: state => state.selectedMarkerId,
  newMarker: state => state.newMarker,
  uncertainty: state => state.uncertainty,
  isNewMarker: state => state.selectedResult.properties.id === 'newMarker',
  isGbif: state => state.selectedResult.properties.source === 'gbif',
}

const actions = {}
const mutations = {
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
  setUncertainty: (state, payload) => (state.uncertainty = payload),
}

export default {
  state,
  getters,
  actions,
  mutations,
}
