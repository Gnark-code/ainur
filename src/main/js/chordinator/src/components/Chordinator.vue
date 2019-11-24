<script>
import { GET_ALL_MUSIC_ENUMS } from "../graphql/queries.js"
export default {
    name: 'Chordinator',
    data () {
        return {
            getMusicEnums: null
        }
    },
    async mounted () {
        this.loading = true
        this.getMusicEnums = await this.$apollo.query({ query: GET_ALL_MUSIC_ENUMS })
        this.loading = false
    }
}
</script>
<template>
    <div class="graphql-test">
        <h3 v-if="loading">Loading...</h3>
        <h4 v-if="!loading">{{ getMusicEnums }}</h4>
    </div>
</template>