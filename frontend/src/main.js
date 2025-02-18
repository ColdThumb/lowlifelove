// src/main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './assets/styles.css'; // 导入全局样式

createApp(App).use(router).mount('#app');
