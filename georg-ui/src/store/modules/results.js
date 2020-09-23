const state = {
  accuracy: -1,
  addDinPlats: false,
  displayJsonData: false,
  isAddressSearch: true,
  isDetailView: false,
  isErrorMsg: false,
  hoveredResultId: '',
  message: '',
  newMarker: {},
  results: [],
  reBuildMarker: false,
  selectedResult: {},
  selectedResultId: '',
  selectedMarker: {},
  searchOption: 'address',
  searchCountry: 'SWE',
  searchText: '',
  searchCoordinates: '',
}

const getters = {
  accuracy: state => state.accuracy,
  addDinPlats: state => state.addDinPlats,
  detailView: state => state.isDetailView,
  displayJsonData: state => state.displayJsonData,
  hoveredResultId: state => state.hoveredResultId,
  isAddressSearch: state => state.searchOption === 'address',
  isNewMarker: state => state.selectedMarker.properties.id === 'newMarker',
  isGbif: state => state.selectedMarker.properties.source === 'gbif',
  isWOF: state => state.selectedMarker.properties.source === 'whosonfirst',
  isErrorMsg: state => state.isErrorMsg,
  message: state => state.message,
  newMarker: state => state.newMarker,
  // unhoveredResultId: state => state.unhoveredResultId,
  selectedResult: state => state.selectedResult,
  results: state => state.results,
  reBuildMarker: state => state.reBuildMarker,
  selectedResultId: state => state.selectedResultId,
  selectedMarker: state => state.selectedMarker,
  searchCountry: state => state.searchCountry,
  searchOption: state => state.searchOption,
  searchText: state => state.searchText,
  searchCoordinates: state => state.searchCoordinates,
}

const actions = {}

const mutations = {
  setAccuracy: (state, payload) => (state.accuracy = payload),
  setAddDinPlats: (state, payload) => (state.addDinPlats = payload),
  setDetailView: (state, payload) => (state.isDetailView = payload),
  setDisplayJsonData: (state, payload) => (state.displayJsonData = payload),
  setHovedResultId: (state, payload) => (state.hoveredResultId = payload),
  setIsErrorMsg: (state, payload) => (state.isErrorMsg = payload),
  setMessage: (state, payload) => (state.message = payload),
  setNewMarkers: (state, payload) => (state.newMarker = payload),
  setReBuildMarker: (state, payload) => (state.reBuildMarker = payload),
  setResults: (state, payload) => (state.results = payload),
  setSelectedMarker: (state, payload) => (state.selectedMarker = payload),
  setSelectedResult: (state, payload) => (state.selectedResult = payload),
  setSelectedResultId: (state, payload) => (state.selectedResultId = payload),
  setSearchCountry: (state, payload) => (state.searchCountry = payload),
  setSearchOption: (state, payload) => (state.searchOption = payload),
  setSearchText: (state, payload) => (state.searchText = payload),
  setSearchCoordinates: (state, payload) => (state.searchCoordinates = payload),
}

export default {
  state,
  getters,
  actions,
  mutations,
}
