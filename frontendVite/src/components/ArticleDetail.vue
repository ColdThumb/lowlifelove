<!-- src/components/ArticleDetail.vue -->
<template>
  <div class="bg-white p-6 rounded-lg shadow-lg">
    <h1 class="text-3xl font-bold mb-4">{{ article.title }}</h1>
    <p class="text-gray-700 leading-relaxed">{{ article.content }}</p>
    <router-link to="/commission" class="mt-6 block text-center px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">向我约稿</router-link>
  </div>
</template>

<script>
export default {
  data() {
    return {
      article: {
        id: 1,
        title: "文章详情标题",
        content: "这是文章的详细内容。",
      },
      showCommissionForm: false,
      commissionMessage: "",
    };
  },
  methods: {
    openCommissionForm() {
      this.showCommissionForm = true;
    },
    closeCommissionForm() {
      this.showCommissionForm = false;
      this.commissionMessage = "";
    },
    submitCommission() {
      console.log("提交约稿信息：", this.commissionMessage);
      this.closeCommissionForm();
    },
  },
};
</script>
