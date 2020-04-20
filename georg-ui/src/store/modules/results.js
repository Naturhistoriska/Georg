const state = {
  displayMessage: false,
  isDetailView: false,
  didSearch: false,
  hovedResultId: "",
  unhovedResultId: "",
  results: [],
  selectedResultId: ""
};
const getters = {
  detailView: state => state.isDetailView,
  displayMessage: state => state.didSearch && state.results.length === 0,
  hovedResultId: state => state.hovedResultId,
  unhovedResultId: state => state.unhovedResultId,
  results: state => state.results,
  selectedResultId: state => state.selectedResultId
};

const actions = {};
const mutations = {
  setDetailView: (state, payload) => (state.isDetailView = payload),
  setDidSearch: (state, payload) => (state.didSearch = payload),
  setHovedResultId: (state, payload) => (state.hovedResultId = payload),
  setMouseLeaveResultId: (state, payload) => (state.unhovedResultId = payload),
  setResults: (state, payload) => (state.results = payload),
  setSelectedResultId: (state, payload) => (state.selectedResultId = payload)
};

export default {
  state,
  getters,
  actions,
  mutations
};
