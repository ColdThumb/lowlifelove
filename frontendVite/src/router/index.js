// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue';
import ArticleList from '../components/ArticleList.vue';
import ArticleDetail from '../components/ArticleDetail.vue';
import CommissionForm from '../components/CommissionForm.vue';
import LoginForm from '../components/LoginForm.vue';
import CommissionDetail from '../components/CommissionDetail.vue';

const routes = [
  { path: '/', component: Home, name: 'Home' }, // 将 Home 设置为默认首页
  { path: '/articles', component: ArticleList, name: 'ArticleList' },
  { path: '/articles/:id', component: ArticleDetail, name: 'ArticleDetail' },
  { path: '/commission', component: CommissionForm, name: 'CommissionForm' },
  {
    path: '/commission/:id',
    component: CommissionDetail,
    name: 'CommissionDetail',
  }, // 动态路由
  { path: '/login', component: LoginForm, name: 'LoginForm' },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
