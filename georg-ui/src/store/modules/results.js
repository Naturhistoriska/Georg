const state = {
  displayMessage: false,
  isDetailView: false,
  didSearch: false,
  hovedResultId: '',
  unhovedResultId: '',
  results: [],
  selectedResult: {},
  selectedResultId: '',
  detialViewId: '',
  selectedMarkerId: '',
  newMarker: {},
  uncertainty: 0,
}
const getters = {
  detailView: state => state.isDetailView,
  didSearch: state => state.didSearch,
  displayMessage: state => state.didSearch,
  hovedResultId: state => state.hovedResultId,
  unhovedResultId: state => state.unhovedResultId,
  selectedResult: state => state.selectedResult,
  results: state => state.results,
  selectedResultId: state => state.selectedResultId,
  detialViewId: state => state.detialViewId,
  selectedMarkerId: state => state.selectedMarkerId,
  newMarker: state => state.newMarker,
  uncertainty: state => state.uncertainty,
}

const actions = {}
const mutations = {
  setDidSearch: (state, payload) => (state.didSearch = payload),
  setHovedResultId: (state, payload) => (state.hovedResultId = payload),
  setMouseLeaveResultId: (state, payload) => (state.unhovedResultId = payload),
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
