import { ElMessage } from 'element-plus'

const messageInstance = new Map()

const showMessage = (options, type) => {
  const message = typeof options === 'string' ? options : options.message
  const key = `${type}-${message}`

  if (messageInstance.has(key)) {
    return
  }

  const config = typeof options === 'string' 
    ? { message, duration: 2000, type }
    : { ...options, duration: 2000 }

  const instance = ElMessage(config)

  messageInstance.set(key, instance)

  setTimeout(() => {
    messageInstance.delete(key)
  }, 2000)
}

const message = {
  success: (options) => showMessage(options, 'success'),
  warning: (options) => showMessage(options, 'warning'),
  info: (options) => showMessage(options, 'info'),
  error: (options) => showMessage(options, 'error')
}

export default message
