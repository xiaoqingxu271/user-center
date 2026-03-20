import request from '@/utils/request'

export const userRegister = (data) => {
  return request({
    url: '/user/authentication/register',
    method: 'post',
    data: {
      account: data.userAccount,
      password: data.userPassword,
      username: data.userName
    }
  })
}

export const userLogin = (data) => {
  return request({
    url: '/user/authentication/login',
    method: 'post',
    data: {
      account: data.userAccount,
      password: data.userPassword
    }
  })
}

export const userLogout = () => {
  return request({
    url: '/user/authentication/logout',
    method: 'post'
  })
}

export const getCurrentUserInfo = () => {
  return request({
    url: '/user/information/get',
    method: 'get'
  })
}

export const updateCurrentUserInfo = (data) => {
  return request({
    url: '/user/information/update',
    method: 'put',
    data: {
      username: data.userName,
      gender: data.gender,
      avatar: data.avatarUrl
    }
  })
}

export const resetPassword = (data) => {
  return request({
    url: '/user/information/resetPassword',
    method: 'post',
    data
  })
}

export const getUserPage = (data) => {
  return request({
    url: '/user/information/page',
    method: 'post',
    data: {
      account: data.userAccount,
      username: data.userName,
      status: data.userStatus,
      current: data.current,
      pageSize: data.pageSize
    }
  })
}

export const getUserInfoById = (id) => {
  return request({
    url: `/user/information/${id}`,
    method: 'get'
  })
}

export const startOrUpUser = (id, status) => {
  return request({
    url: `/user/information/startOrUp/${id}/${status}`,
    method: 'post'
  })
}

export const deleteUser = (ids) => {
  return request({
    url: `/user/information/delete/${ids}`,
    method: 'delete'
  })
}

export const modifyUser = (data) => {
  return request({
    url: '/user/information/modify',
    method: 'put',
    data: {
      id: data.id,
      username: data.userName,
      gender: data.gender,
      avatar: data.avatarUrl,
      role: data.userRole
    }
  })
}

export const addUser = (data) => {
  return request({
    url: '/user/information/add',
    method: 'post',
    data: {
      account: data.userAccount,
      password: data.userPassword,
      username: data.userName,
      gender: data.gender,
      avatar: data.avatarUrl,
      role: data.userRole
    }
  })
}
