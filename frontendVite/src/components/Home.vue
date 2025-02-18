<template>
  <div class="container mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">待写稿件</h1>

    <div v-if="drafts.length === 0" class="text-gray-500">
      目前没有待写稿件。
    </div>

    <div v-else class="space-y-4">
      <div
        v-for="draft in drafts"
        :key="draft.id"
        class="p-4 bg-white shadow rounded-md flex justify-between items-center"
      >
        <!-- 稿件内容 -->
        <div>
          <h2 class="text-lg font-medium">稿件 #{{ draft.id }}</h2>
          <p class="text-sm text-gray-500">预计交稿时间：{{ draft.dueDate }}</p>
        </div>

        <!-- 操作按钮 -->
        <div class="flex space-x-2">
          <!-- 修改稿件按钮 -->
          <button
            @click="editDraft(draft.id)"
            class="px-3 py-1 bg-blue-500 text-white rounded-md hover:bg-blue-600"
          >
            修改
          </button>

          <!-- 查看稿件按钮：点击后跳转到 CommissionDetail -->
          <button
            @click="viewDraft(draft.id)"
            class="px-3 py-1 bg-blue-500 text-white rounded-md hover:bg-blue-600"
          >
            查看
          </button>

          <!-- 删除稿件按钮 -->
          <button
            @click="deleteDraft(draft.id)"
            class="px-3 py-1 bg-red-500 text-white rounded-md hover:bg-red-600"
          >
            删除
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      drafts: [], // 存储当前用户的待写稿件
    };
  },
  created() {
    // 在组件创建时获取当前用户的待写稿件数据
    this.fetchMyDrafts();
  },
  methods: {
    // 获取当前用户的待写稿件
    async fetchMyDrafts() {
      try {
        const token = localStorage.getItem('token');
        if (!token) {
          throw new Error('用户未登录');
        }

        const response = await fetch('/api/drafts/mine', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error(`获取失败: ${response.status}`);
        }

        this.drafts = await response.json();
      } catch (error) {
        console.error('加载失败:', error);
        alert('无法获取您的稿件，请稍后再试。');
      }
    },

    // 编辑稿件
    editDraft(id) {
      console.log(`编辑稿件 ID: ${id}`);
      // 触发编辑稿件逻辑或跳转到编辑页
    },

    // 查看稿件：跳转到 CommissionDetail 页
    viewDraft(id) {
      this.$router.push({ name: 'CommissionDetail', params: { id } });
    },

    // 删除稿件
    async deleteDraft(id) {
      try {
        console.log(`删除稿件 ID: ${id}`);
        const token = localStorage.getItem('token');
        if (!token) {
          throw new Error('用户未登录');
        }

        const response = await fetch(`/api/drafts/${id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });

        const data = await response.json();
        if (response.ok) {
          this.drafts = this.drafts.filter((draft) => draft.id !== id);
          alert('删除成功');
        } else {
          alert(data.message || '删除失败');
        }
      } catch (error) {
        console.error('删除请求错误', error);
        alert('请求失败，请稍后再试');
      }
    },
  },
};
</script>
