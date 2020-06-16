import { shallowMount } from '@vue/test-utils'
import Home from '@/views/Home.vue'

describe('Home', () => {
  it('renders results', () => {
    const wrapper = shallowMount(Home, {
      data() {
        return {
          results: 'Hello World',
        }
      },
    })

    // see if the message renders
    expect(wrapper.find('.results').text()).toEqual('Hello World')

    // assert the error is rendered
    expect(wrapper.find('.error').exists()).toBeTruthy()

    // update the `username` and assert error is no longer rendered
    wrapper.setData({ result: 'Lachlan' })
    expect(wrapper.find('.error').exists()).toBeFalsy()
  })
})
