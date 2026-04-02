<template>
  <div id="userProfilePage">
    <a-card title="个人信息修改" class="profile-card">
      <a-form :model="form" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" @finish="handleSubmit">
        <a-form-item label="用户名" name="userName" :rules="[{ required: true, message: '请输入用户名' }]">
          <a-input v-model:value="form.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="头像" name="userAvatar">
          <div style="display: flex; align-items: center; gap: 16px">
            <AvatarUpload
              :image-url="form.userAvatar"
              :user-name="form.userName"
              :size="96"
              @update:image-url="handleAvatarUpdate"
              @upload-success="handleAvatarUploadSuccess"
            />
            <a-input
              v-model:value="form.userAvatar"
              placeholder="或输入头像 URL"
              style="width: 400px"
            />
          </div>
        </a-form-item>
        <a-form-item label="简介" name="userProfile">
          <a-textarea v-model:value="form.userProfile" placeholder="请输入个人简介" :rows="4" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 6, span: 18 }">
          <a-button type="primary" html-type="submit" :loading="loading">保存修改</a-button>
          <a-button style="margin-left: 8px" @click="resetForm">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { updateUser } from '@/api/userController'
import AvatarUpload from '@/components/AvatarUpload.vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loading = ref(false)

const form = reactive({
  id: 0,
  userName: '',
  userAvatar: '',
  userProfile: '',
})

// 初始化表单数据
const initForm = () => {
  const user = loginUserStore.loginUser
  form.id = user.id || 0
  form.userName = user.userName || ''
  form.userAvatar = user.userAvatar || ''
  form.userProfile = user.userProfile || ''
}

// 处理表单提交
const handleSubmit = async () => {
  loading.value = true
  try {
    const res = await updateUser(form)
    if (res.data.code === 0) {
      message.success('个人信息修改成功')
      // 更新登录用户信息
      await loginUserStore.fetchLoginUser()
    } else {
      message.error('修改失败：' + res.data.message)
    }
  } catch (error) {
    console.error('修改个人信息失败：', error)
    message.error('修改失败，请重试')
  } finally {
    loading.value = false
  }
}

/**
 * 处理头像更新
 */
const handleAvatarUpdate = (url: string) => {
  form.userAvatar = url
}

/**
 * 处理头像上传成功，自动调用更新接口
 */
const handleAvatarUploadSuccess = async (avatarUrl: string) => {
  try {
    // 更新表单中的头像 URL
    form.userAvatar = avatarUrl
    
    // 调用用户信息更新接口
    const res = await updateUser({
      id: form.id,
      userAvatar: avatarUrl,
    })
    
    if (res.data.code === 0) {
      message.success('头像已更新')
      // 更新登录用户信息
      await loginUserStore.fetchLoginUser()
    } else {
      message.error('更新失败：' + res.data.message)
    }
  } catch (error) {
    console.error('更新头像失败：', error)
    message.error('更新失败，请重试')
  }
}

// 重置表单
const resetForm = () => {
  initForm()
}

// 页面加载时初始化表单数据
onMounted(() => {
  // 检查用户是否登录
  if (!loginUserStore.loginUser.id) {
    message.warning('请先登录')
    router.push('/user/login')
    return
  }

  // 初始化表单数据
  initForm()
})
</script>

<style scoped>
#userProfilePage {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.profile-card {
  max-width: 800px;
  margin: 0 auto;
}
</style>
