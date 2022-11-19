<template>
    <div class="searchbody">
        <el-container class="mainbody">
            <el-header>
                <el-row class="tags" justify="center">
                    <el-tag
                        v-for="item in items"
                        :key="item.label"
                        :type="item.type"
                        class="mx-1"
                        effect="dark"
                        >
                        {{ item.label }}
                    </el-tag>
                </el-row>
                <el-row :gutter="20" justify="center">
                    <el-col :span="12">
                        <el-input v-model="input" placeholder="Please input" />
                    </el-col>
                    <el-col :span="3">
                        <el-button type="primary" :icon="Search">Search</el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-main>              
                <el-table :data="tableData" stripe>
                    <el-table-column prop="date" label="Date" width="180" />
                    <el-table-column prop="type" label="Type" width="180" />
                    <el-table-column prop="description" label="Description" />
                </el-table>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref } from 'vue';
const input = ref('');
const items = ref([
  { type: '', label: 'Tag 1' },
  { type: 'success', label: 'Tag 2' },
  { type: 'info', label: 'Tag 3' },
  { type: 'danger', label: 'Tag 4' },
  { type: 'warning', label: 'Tag 5' },
])
const tableData = [
  {
    date: '2016-05-03',
    type: 'Tom',
    description: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-02',
    type: 'Tom',
    description: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-04',
    type: 'Tom',
    description: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-01',
    type: 'Tom',
    description: 'No. 189, Grove St, Los Angeles',
  },
]
</script>

<style scoped>
.tags {
  margin-top: 10px;
  margin-bottom: 10px;
}
</style>
