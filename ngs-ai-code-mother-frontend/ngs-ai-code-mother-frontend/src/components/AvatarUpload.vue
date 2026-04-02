<template>
  <div class="avatar-upload">
    <a-upload
      :before-upload="handleBeforeUpload"
      :show-upload-list="false"
      :custom-request="handleCustomRequest"
      accept="image/*"
    >
      <div class="avatar-wrapper">
        <a-avatar v-if="imageUrl" :src="imageUrl" :size="size" />
        <a-avatar v-else :size="size">
          {{ userName?.charAt(0) || 'U' }}
        </a-avatar>
        <div class="upload-hint">
          <icon-camera />
          <span>点击上传</span>
        </div>
      </div>
    </a-upload>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { uploadUserAvatar } from '@/api/userController'

interface Props {
  imageUrl?: string
  userName?: string
  size?: number
}

const props = withDefaults(defineProps<Props>(), {
  imageUrl: '',
  userName: 'U',
  size: 96,
})

const emit = defineEmits<{
  (e: 'update:imageUrl', value: string): void
  (e: 'uploadSuccess', value: string): void
}>()

const loading = ref(false)

// 监听外部 imageUrl 变化
watch(
  () => props.imageUrl,
  (newVal) => {
    if (newVal) {
      emit('update:imageUrl', newVal)
    }
  },
  { immediate: true }
)

/**
 * 上传前校验
 */
const handleBeforeUpload = (file: File) => {
  // 检查文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件！')
    return false
  }

  // 检查文件大小（最大 5MB）
  const maxSize = 5 * 1024 * 1024
  if (file.size > maxSize) {
    message.error('图片大小不能超过 5MB！')
    return false
  }

  return true
}

/**
 * 自定义上传请求
 */
const handleCustomRequest = async ({ file }: any) => {
  if (!file) {
    message.error('请选择文件')
    return
  }

  loading.value = true

  try {
    const response = await uploadUserAvatar({
      file: file as File,
    })

    // 处理响应格式，从 response.data 中获取实际的响应数据
    const actualResponse = response.data || response
    
    if (actualResponse.code === 0 && actualResponse.data) {
      const avatarUrl = actualResponse.data.avatarUrl
      emit('update:imageUrl', avatarUrl)
      emit('uploadSuccess', avatarUrl)
      message.success('头像上传成功')
    } else {
      message.error(actualResponse.message || '上传失败')
    }
  } catch (error: any) {
    console.error('上传失败:', error)
    message.error(error.message || '上传失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.avatar-upload {
  display: inline-block;
}

.avatar-wrapper {
  position: relative;
  cursor: pointer;
  transition: opacity 0.3s;
}

.avatar-wrapper:hover {
  opacity: 0.8;
}

.upload-hint {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s;
}

.avatar-wrapper:hover .upload-hint {
  opacity: 1;
}

.upload-hint span {
  margin-top: 4px;
  font-size: 12px;
}
</style>
