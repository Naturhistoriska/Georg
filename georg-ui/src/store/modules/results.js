const state = {
  accuracy: -1,
  displayJsonData: false,
  isAddressSearch: true,
  isDetailView: false,
  hoveredResultId: '',
  message: '',
  newMarker: {},
  results: [],
  selectedResult: {},
  selectedResultId: '',
  selectedMarkerId: '',
  unhoveredResultId: '',
  searchOption: 'address',
  searchCountry: 'SWE',
}

const getters = {
  accuracy: state => state.accuracy,
  detailView: state => state.isDetailView,
  displayJsonData: state => state.displayJsonData,
  hoveredResultId: state => state.hoveredResultId,
  isAddressSearch: state => state.searchOption === 'address',
  isNewMarker: state => state.selectedResult.properties.id === 'newMarker',
  isGbif: state => state.selectedResult.properties.source === 'gbif',
  isWOF: state => state.selectedResult.properties.source === 'whosonfirst',
  message: state => state.message,
  newMarker: state => state.newMarker,
  unhoveredResultId: state => state.unhoveredResultId,
  selectedResult: state => state.selectedResult,
  results: state => state.results,
  selectedResultId: state => state.selectedResultId,
  selectedMarkerId: state => state.selectedMarkerId,
  searchCountry: state => state.searchCountry,
  searchOption: state => state.searchOption,
}

const actions = {}

const mutations = {
  setAccuracy: (state, payload) => (state.accuracy = payload),
  setDetailView: (state, payload) => (state.isDetailView = payload),
  setDisplayJsonData: (state, payload) => (state.displayJsonData = payload),
  setHovedResultId: (state, payload) => (state.hoveredResultId = payload),
  setMessage: (state, payload) => (state.message = payload),
  setNewMarkers: (state, payload) => (state.newMarker = payload),
  setResults: (state, payload) => (state.results = payload),
  setSelectedMarkerId: (state, payload) => (state.selectedMarkerId = payload),
  setSelectedResult: (state, payload) => (state.selectedResult = payload),
  setSelectedResultId: (state, payload) => (state.selectedResultId = payload),
  setSearchCountry: (state, payload) => (state.searchCountry = payload),
  setSearchOption: (state, payload) => (state.searchOption = payload),
}

export default {
  state,
  getters,
  actions,
  mutations,
}
