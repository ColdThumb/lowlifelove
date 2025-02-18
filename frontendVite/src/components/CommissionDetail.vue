<template>
  <div>
    <h1>表格详情</h1>
    <div v-if="commission">
      <p><strong>姓名:</strong> {{ commission.name }}</p>
      <p><strong>邮箱(已脱敏):</strong> {{ commission.email }}</p>
      <p><strong>需求详情:</strong></p>
      <p>{{ commission.demand }}</p>
    </div>
    <div v-else>
      <p>加载中...</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CommissionDetail',
  data() {
    return {
      commission: null,
    };
  },
  created() {
    const id = this.$route.params.id;
    // 发请求获取表单详情
    fetch(`/api/auth/${id}`)
      .then((res) => {
        if (!res.ok) {
          throw new Error('数据获取失败或不存在');
        }
        return res.json();
      })
      .then((data) => {
        // 后端已经对邮箱进行了脱敏，这里直接显示即可
        this.commission = data;
      })
      .catch((err) => {
        console.error('加载表格详情失败:', err);
      });
  },
};
</script>
