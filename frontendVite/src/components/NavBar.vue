<!-- src/components/NavBar.vue -->
<template>
  <nav class="bg-gray-800 w-full p-4">
    <div class="flex justify-between items-center">
      <!-- Logo/网站名称 - 移到最外层 -->
      <router-link to="/" class="flex items-center">
        <span class="text-white text-xl font-bold ml-4">我的文章站</span>
      </router-link>

      <!-- 导航链接 -->
      <div class="flex space-x-8">
        <!-- 首页 -->
        <router-link
          to="/"
          class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition duration-150 ease-in-out"
          :class="{ 'bg-gray-900 text-white': $route.name === 'Home' }"
        >
          首页
        </router-link>

        <!-- 文章列表 -->
        <router-link
          to="/articles"
          class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition duration-150 ease-in-out"
          :class="{ 'bg-gray-900 text-white': $route.name === 'ArticleList' }"
        >
          文章
        </router-link>

        <!-- 约稿 -->
        <router-link
          to="/commission"
          class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition duration-150 ease-in-out"
          :class="{
            'bg-gray-900 text-white': $route.name === 'CommissionForm',
          }"
        >
          约稿
        </router-link>

        <!-- 登录 -->
        <router-link
          to="/Login"
          class="text-gray-300 hover:text-white hover:bg-gray-700 px-3 py-2 rounded-md text-sm font-medium transition duration-150 ease-in-out"
          :class="{ 'bg-gray-900 text-white': $route.name === 'loginForm' }"
        >
          登录
        </router-link>
      </div>
    </div>
  </nav>
</template>
