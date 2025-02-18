<!-- App.vue -->
<template>
  <div style="width: 100vw;">
    <NavBar />
    <div class="container mx-auto p-6">
      <router-view />
    </div>
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue';

export default {
  components: {
    NavBar,
  },
};
</script>
