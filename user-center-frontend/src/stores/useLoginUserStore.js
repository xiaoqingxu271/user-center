import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref(null)

  function setLoginUser(user) {
    if (loginUser.value) {
      loginUser.value = { ...loginUser.value, ...user }
    } else {
      loginUser.value = user
    }
    localStorage.setItem('userInfo', JSON.stringify(loginUser.value))
    console.log('setLoginUser - 最终保存:', loginUser.value)
  }

  function getLoginUser() {
    if (!loginUser.value) {
      const userInfo = localStorage.getItem('userInfo')
      if (userInfo) {
        loginUser.value = JSON.parse(userInfo)
      }
    }
    return loginUser.value
  }

  function clearLoginUser() {
    loginUser.value = null
    localStorage.removeItem('userInfo')
  }

  return {
    loginUser,
    setLoginUser,
    getLoginUser,
    clearLoginUser
  }
})
