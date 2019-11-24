<template>
<Chordinator></Chordinator>
</template>

<script>
import Chordinator from './components/Chordinator'
export default {
  name: 'Chordinator-parent',
  components: {
    Chordinator
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
