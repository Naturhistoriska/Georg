import axios from 'axios'

const baseUrl = process.env.VUE_APP_GEORG_API
const gbifApi = process.env.VUE_APP_GBIF_API

export default class Service {
  async fetchAddressResults(address, countryCode) {
    const url = `${baseUrl}geoCoding?address=${address}&countryCode=${countryCode}`
    // const url = `${baseUrl}search?text=${address}`
    const response = await axios.get(url)
    return response.data
  }

  async autoCompleteSearch(value, countryCode) {
    const url = `${baseUrl}search?text=${value}&countryCode=${countryCode}`
    const response = await axios.get(url)
    return response.data
  }

  async reverseGeoCodingResults(lat, lng) {
    const url = `${baseUrl}reverse?lat=${lat}&lng=${lng}`
    // const url = `${baseUrl}search?text=${address}`
    const response = await axios.get(url)
    return response.data
  }

  async fetchDatasetTitle(datasetKey) {
    const url = `${gbifApi}dataset/${datasetKey}`
    const response = await axios.get(url)
    return response.data
  }

  async fetchOccurrenceKey(dataset, occurrenceId) {
    const url = `${gbifApi}occurrence/search?datesetKey=${dataset}&occurrenceID=${occurrenceId}`
    const response = await axios.get(url)
    return response.data
  }
}
