<template>
  <div class="container mx-auto p-6 bg-white rounded-lg shadow-lg max-w-md">
    <h2 class="text-2xl font-bold mb-4">用户登录</h2>
    <form @submit.prevent="login">
      <!-- 姓名 -->
      <div class="mb-4">
        <label class="block text-gray-700 mb-2" for="name">姓名</label>
        <input
          v-model="loginData.name"
          id="name"
          type="text"
          required
          class="w-full p-2 border rounded"
          placeholder="请输入您的姓名"
        />
        <small class="text-gray-500">此项为必填。</small>
      </div>

      <!-- 电子邮箱 -->
      <div class="mb-4">
        <label class="block text-gray-700 mb-2" for="email">电子邮箱</label>
        <input
          v-model="loginData.email"
          id="email"
          type="email"
          required
          class="w-full p-2 border rounded"
          placeholder="请输入您的电子邮箱"
        />
        <small class="text-gray-500"
          >请确保邮箱地址有效，以验证您的身份。</small
        >
      </div>

      <!-- 提交按钮 -->
      <button
        type="submit"
        class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 w-full"
      >
        登录
      </button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginData: {
        name: '',
        email: '',
      },
    };
  },
  methods: {
    async login() {
      try {
        const response = await fetch('/api/auth/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(this.loginData),
        });

        if (!response.ok) {
          throw new Error('登录失败，请检查姓名和邮箱是否正确');
        }

        const user = await response.json();
        alert(`欢迎回来, ${user.name}`);
        // 将用户信息存储到本地或 Vuex 中
        localStorage.setItem('user', JSON.stringify(user));
      } catch (error) {
        alert(error.message);
      }
    },
  },
};
</script>
