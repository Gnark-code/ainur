<script>

import { GET_ALL_MUSIC_ENUMS } from "../graphql/queries.js"
export default {
    name: 'Chordinator',
    data () {
        return {
            loading: true,
            musicEnums: null, 
            socket: new WebSocket("ws://127.0.0.1:54123/play/scale"),
            modeSelected: null,
            isConnected: false,
        }
    },
  
    playScale (){
        this.socket.send("TEST");
    },
    async mounted () {
        this.musicEnums = await this.$apollo.query({ query: GET_ALL_MUSIC_ENUMS })
        this.loading = false
    }
}
</script>
<template>
    <div class="graphql-test">
        <h3 v-if="loading">Loading...</h3>
        <h4 v-if="!loading">{{ musicEnums }}</h4>
        <div>
            <select v-if="!loading" v-model="modeSelected">
                <option disabled value="">Choose your mode</option>
                <option v-for="mode in musicEnums.data.getMusicEnums.modes" :key="mode" >{{ mode }}</option>
            </select>
            <span>Selected : {{ modeSelected }}</span>
                <p v-if="isConnected">We're connected to the server!</p>
            <button @click="playScale()">Play scale</button>
        </div>
    </div>
</template>