<!-- src/components/ArticleList.vue -->
<template>
  <div class="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
    <div v-for="article in articles" :key="article.id" class="bg-white shadow-lg rounded-lg p-6">
      <h2 class="text-xl font-semibold text-gray-800">{{ article.title }}</h2>
      <p class="text-gray-600 mt-2">{{ article.summary }}</p>
      <div class="mt-4">
        <router-link :to="{ name: 'ArticleDetail', params: { id: article.id }}" class="text-blue-500 hover:underline">阅读更多</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      articles: [
        { id: 1, title: "文章1", summary: "这是文章1的摘要" },
        { id: 2, title: "文章2", summary: "这是文章2的摘要" },
      ],
    };
  },
  methods: {
    openCommissionForm(id) {
      console.log(`打开文章 ${id} 的约稿表单`);
    },
  },
};
</script>
