<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <!-- 表格 -->
    <a-table :columns="columns" :data-source="data" :pagination="pagination" @change="doTableChange">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button type="primary" style="margin-right: 8px" @click="doEdit(record)">修改</a-button>
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>

    <!-- 修改用户信息模态框 -->
    <a-modal v-model:visible="editModalVisible" title="修改用户信息" width="600px">
      <a-form :model="editForm" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="用户名" name="userName" :rules="[{ required: true, message: '请输入用户名' }]">
          <a-input v-model:value="editForm.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="头像" name="userAvatar">
          <div style="display: flex; align-items: center; gap: 16px">
            <AvatarUpload :image-url="editForm.userAvatar" :user-name="editForm.userName" :size="96"
              @update:image-url="handleEditAvatarUpdate" @upload-success="handleEditAvatarUploadSuccess" />
            <a-input v-model:value="editForm.userAvatar" placeholder="或输入头像 URL" style="width: 300px" />
          </div>
        </a-form-item>
        <a-form-item label="简介" name="userProfile">
          <a-textarea v-model:value="editForm.userProfile" placeholder="请输入个人简介" :rows="4" />
        </a-form-item>
        <a-form-item label="用户角色" name="userRole" :rules="[{ required: true, message: '请选择用户角色' }]">
          <a-select v-model:value="editForm.userRole" placeholder="请选择用户角色">
            <a-select-option value="user">普通用户</a-select-option>
            <a-select-option value="admin">管理员</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button @click="editModalVisible = false">取消</a-button>
        <a-button type="primary" @click="handleEditSubmit">确定</a-button>
      </template>
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { deleteUser, listUserVoByPage, adminUpdateUser, uploadUserAvatar } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import AvatarUpload from '@/components/AvatarUpload.vue'

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

// 展示的数据
const data = ref<API.UserVO[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

// 表格分页变化时的操作
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索数据
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 删除数据
const doDelete = async (id: number) => {
  if (!id) {
    return
  }
  const res = await deleteUser({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 编辑模态框
const editModalVisible = ref(false)

// 编辑表单数据
const editForm = reactive({
  id: 0,
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: '',
})

// 打开编辑模态框
const doEdit = (record: API.UserVO) => {
  // 填充表单数据
  editForm.id = record.id || 0
  editForm.userName = record.userName || ''
  editForm.userAvatar = record.userAvatar || ''
  editForm.userProfile = record.userProfile || ''
  editForm.userRole = record.userRole || 'user'

  // 显示模态框
  editModalVisible.value = true
}

// 处理头像更新
const handleEditAvatarUpdate = (url: string) => {
  editForm.userAvatar = url
}

/**
 * 处理编辑模态框中头像上传成功，自动调用更新接口
 */
const handleEditAvatarUploadSuccess = async (avatarUrl: string) => {
  try {
    // 更新表单中的头像 URL
    editForm.userAvatar = avatarUrl

    // 调用管理员更新用户信息接口
    const res = await adminUpdateUser({
      id: editForm.id,
      userAvatar: avatarUrl,
    })

    if (res.data.code === 0) {
      message.success('头像已更新')
      // 刷新列表数据
      fetchData()
    } else {
      message.error('更新失败：' + res.data.message)
    }
  } catch (error) {
    console.error('更新头像失败：', error)
    message.error('更新失败，请重试')
  }
}

// 处理修改用户信息提交
const handleEditSubmit = async () => {
  try {
    const res = await adminUpdateUser(editForm)
    if (res.data.code === 0) {
      message.success('修改成功')
      editModalVisible.value = false
      // 刷新数据
      fetchData()
    } else {
      message.error('修改失败：' + res.data.message)
    }
  } catch (error) {
    console.error('修改用户信息失败：', error)
    message.error('修改失败，请重试')
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
#userManagePage {
  padding: 24px;
  background: white;
  margin-top: 16px;
}
</style>
