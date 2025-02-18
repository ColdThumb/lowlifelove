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
          <h2 class="text-lg font-medium">{{ draft.name }}的稿件</h2>
          <p class="text-sm text-gray-500">预计交稿时间：{{ draft.dueDate }}</p>
        </div>

        <!-- 操作按钮 -->
        <div class="flex space-x-2">
          <button
            @click="editDraft(draft.id)"
            class="px-3 py-1 bg-blue-500 text-white rounded-md hover:bg-blue-600"
          >
            修改
          </button>
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
  name: "Home",
  data() {
    return {
      drafts: [], // 存储待写稿件的数组
    };
  },
  created() {
    // 模拟获取待写稿件数据
    this.fetchDrafts();
  },
  methods: {
    fetchDrafts() {
      // 模拟 API 调用获取稿件列表
      this.drafts = [
        { id: 1, name: "小明", dueDate: "2024-01-17" },
        { id: 2, name: "小红", dueDate: "2024-01-20" },
      ];
    },
    editDraft(id) {
      console.log(`编辑稿件 ID: ${id}`);
      // 触发编辑稿件逻辑
    },
    deleteDraft(id) {
      console.log(`删除稿件 ID: ${id}`);
      // 触发删除稿件逻辑
      this.drafts = this.drafts.filter((draft) => draft.id !== id);
    },
  },
};
</script>

<style>
/* 可根据 Tailwind CSS 需求定制样式 */
</style>
