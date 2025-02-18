<!-- src/components/CommissionForm.vue -->
<template>
    <div class="container mx-auto p-6 bg-white rounded-lg shadow-lg max-w-md">
      <h2 class="text-2xl font-bold mb-4">约稿表单</h2>
      <form @submit.prevent="submitForm">
        <div class="mb-4">
          <label class="block text-gray-700 mb-2" for="name">姓名</label>
          <input v-model="name" id="name" type="text" required class="w-full p-2 border rounded" placeholder="请输入您的姓名" />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 mb-2" for="email">电子邮箱</label>
          <input v-model="email" id="email" type="email" required class="w-full p-2 border rounded" placeholder="请输入您的电子邮箱" />
        </div>
        <div class="mb-4">
          <label class="block text-gray-700 mb-2" for="message">需求描述</label>
          <textarea v-model="message" id="message" required class="w-full p-2 border rounded" placeholder="请输入您的需求"></textarea>
        </div>
        <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">提交</button>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        name: "",
        email: "",
        message: "",
      };
    },
    methods: {
      submitForm() {
        console.log("表单提交：", this.name, this.email, this.message);
        // 可以在这里添加实际的表单提交逻辑，例如发送到后台服务器
      },
    },
  };
  </script>
  