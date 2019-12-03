<script>

var initSocket = function(objectWaitingToBeSent){
    var socket = new WebSocket("ws://127.0.0.1:54123/play/scale");
    // Lorsque la connexion est établie.
    socket.onopen = function() {
        /*console.log("Connexion établie.");

        // Lorsque la connexion se termine.
        this.onclose = function(event) {
            //console.log("Connexion terminé.");
        };

        // Lorsque le serveur envoi un message.
        this.onmessage = function(event) {
            //console.log("Message:", event.data);
        };
            */
        if(objectWaitingToBeSent){
            this.send(objectWaitingToBeSent);
        }
    };
   
    return socket;
};

import { GET_ALL_MUSIC_ENUMS } from "../graphql/queries.js"
export default {
    name: 'Chordinator',
    data () {
        return {
            loading: true,
            musicEnums: null, 
            socket: null,
            modeSelected: null,
            messageReceived: null,
            isConnected: false,
        }
    },
  
    methods : {
        playScale : function(){
            if(this.socket.readyState === this.socket.__proto__.OPEN){
                  this.socket.send("TEST");
            }else{
                this.socket= initSocket("TEST");
            }
        }
    },
    async mounted () {
        this.musicEnums = await this.$apollo.query({ query: GET_ALL_MUSIC_ENUMS });
        this.socket = initSocket();
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
            <button  v-on:click="playScale">Play scale</button>
        </div>
            <div> event received from {{ messageReceived }}</div>   
    </div>
</template>