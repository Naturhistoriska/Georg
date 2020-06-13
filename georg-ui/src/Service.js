import axios from 'axios'

const baseUrl = process.env.VUE_APP_GEORG_API

export default class Service {
  async fetchAddressResults(address) {
    const url = `${baseUrl}geoCoding?address=${address}`
    // const url = `${baseUrl}search?text=${address}`
    const response = await axios.get(url)
    return response.data
  }

  async fetchOccurrenceKey(dataset, occurrenceId) {
    const url = `${baseUrl}occurancekey?dataset=${dataset}&occurrenceid=${occurrenceId}`
    const response = await axios.get(url)
    return response.data
  }
}
