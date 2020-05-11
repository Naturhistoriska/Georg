import axios from 'axios'

const baseUrl = process.env.VUE_APP_GEORG_API
export default class Service {
  async fetchAddressResults(address) {
    const url = `${baseUrl}geoCoding?address=${address}`
    const response = await axios.get(url)
    return response.data
  }
}
