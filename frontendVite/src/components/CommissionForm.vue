<template>
  <div class="container mx-auto p-6 bg-white rounded-lg shadow-lg max-w-md">
    <h2 class="text-2xl font-bold mb-4">约稿表单</h2>
    <form @submit.prevent="submitForm">
      <!-- 姓名 -->
      <div class="mb-4">
        <label class="block text-gray-700 mb-2" for="name">姓名</label>
        <input
          v-model="formData.name"
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
          v-model="formData.email"
          id="email"
          type="email"
          required
          class="w-full p-2 border rounded"
          placeholder="请输入您的电子邮箱"
        />
        <small class="text-gray-500"
          >请确保邮箱地址有效，以便我们联系您。</small
        >
      </div>

      <!-- 需求描述 -->
      <div class="mb-4">
        <label class="block text-gray-700 mb-2" for="description"
          >需求描述</label
        >
        <textarea
          v-model="formData.description"
          id="description"
          required
          rows="4"
          class="w-full p-2 border rounded"
          placeholder="请输入您对约稿内容的具体需求，例如主题、风格、交付时间等"
        ></textarea>
        <small class="text-gray-500">尽量详细描述您的需求。</small>
      </div>

      <!-- 提交按钮 -->
      <button
        type="submit"
        class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 w-full"
      >
        提交
      </button>
      <small class="text-gray-500"
        >现在提交约稿表单，大概可以在 {{ dueDate }} 之前收到稿件。</small
      >
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        name: '',
        email: '',
        description: '',
      },
      dueDate: '', // 动态计算的预计交稿日期
    };
  },
  methods: {
    async fetchDueDate() {
      try {
        const response = await fetch('/api/commission/preview-due-date', {
          method: 'GET',
        });

        if (!response.ok) {
          throw new Error(`获取 dueDate 失败！状态码: ${response.status}`);
        }

        const result = await response.json();
        this.dueDate = result.dueDate; // 设置返回的预计交稿日期
      } catch (error) {
        console.error('获取预计交稿日期失败：', error);
        alert('无法获取预计交稿日期，请稍后再试。');
      }
    },
    async submitForm() {
      try {
        const token = localStorage.getItem('token'); // token をゲットする
        const response = await fetch('/api/commission', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: token ? `Bearer ${token}` : '',
          },
          body: JSON.stringify(this.formData),
        });

        if (!response.ok) {
          throw new Error(`HTTP 错误！状态码: ${response.status}`);
        }

        const result = await response.json();
        alert('表单提交成功！');
        console.log('后端返回的结果：', result);

        if (result.token) {
          localStorage.setItem('token', result.token); // 新しい token を保存する
        }

        // 清空表单数据
        this.formData.name = '';
        this.formData.email = '';
        this.formData.description = '';

        // 跳转到首页或其他页面
        this.$router.push('/');
      } catch (error) {
        console.error('表单提交失败：', error);
        alert('提交失败，请稍后重试。');
      }
    },
  },
  created() {
    // 页面加载时获取动态的 dueDate
    this.fetchDueDate();
    this.submitForm();
  },
};
</script>

<style scoped>
small {
  display: block;
  margin-top: 0.25rem;
  color: #6b7280;
}
</style>
