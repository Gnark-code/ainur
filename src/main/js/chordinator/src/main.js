import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'tachyons'
import { ApolloClient } from 'apollo-client'
import { HttpLink } from 'apollo-link-http'
import { InMemoryCache } from 'apollo-cache-inmemory'
import VueApollo from 'vue-apollo'

Vue.use(VueApollo)
Vue.config.productionTip = false


const httpLink = new HttpLink({
  // You should use an absolute URL here
  uri: 'http://127.0.0.1:54123/ainur/graphql-api'
})

const apolloClient = new ApolloClient({
  link: httpLink,
  cache: new InMemoryCache(),
  connectToDevTools: true
})
const apolloProvider = new VueApollo({
  defaultClient: apolloClient,
  defaultOptions: {
    $loadingKey: 'loading'
  }
})

new Vue({
  render: h => h(App),
  apolloProvider,
  router
}).$mount('#app')