import axios from 'axios'

const baseUrl = process.env.VUE_APP_GEORG_API
const gbifApi = process.env.VUE_APP_GBIF_API

export default class Service {
  async fetchAddressResults(address, countryCode) {
    // const url = `${baseUrl}geoCoding?address=${address}&countryCode=${countryCode}`
    const url = `${baseUrl}search?text=${address}&countryCode=${countryCode}`
    const response = await axios.get(url)
    return response.data
  }

  async autoCompleteSearch(value, countryCode) {
    const url = `${baseUrl}autocomplete?text=${value}&countryCode=${countryCode}`
    const response = await axios.get(url)
    return response.data
  }

  async reverseGeoCodingResults(lat, lng) {
    const url = `${baseUrl}reverse?lat=${lat}&lng=${lng}`
    const response = await axios.get(url)
    return response.data
  }

  async coordinatesSearch(coordinates) {
    const url = `${baseUrl}coordinates?coordinates=${coordinates}`
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

  async getFiles() {
    return null
  }

  async upload(file) {
    const url = `${baseUrl}upload?type=json`
    let formData = new FormData()
    formData.append('file', file)
    const response = await axios.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
    // return axios.post(url, formData, {
    //   headers: {
    //     'Content-Type': 'multipart/form-data',
    //   },
    //   onUploadProgress,
    // })
  }
}
