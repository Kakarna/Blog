import request from '@/utils/request'

/**
 * 用户注册
 * @param {Object} data { username, password, email, code }
 */
export function registerUser(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data,
  })
}

/**
 * 发送邮箱验证码 - 注册场景
 * @param {String} email
 */
export function sendEmailCode({ email }) {
  return request({
    url: '/user/sendCodeForRegister',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: new URLSearchParams({ email }).toString(),
  })
}

/**
 * 发送邮箱验证码 - 修改邮箱场景
 * @param {String} email
 */
export function sendUpdateEmailCode({ email }) {
  return request({
    url: '/user/sendCodeForUpdate',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: new URLSearchParams({ email }).toString(),
  })
}

/**
 * 用户登录
 * @param {Object} data { username, password }
 */
export function loginUser(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data,
  }).then((res) => {
    if (res.status === 'success' && res.data) {
      localStorage.setItem('token', res.data)
    }
    return res
  })
}

/**
 * 退出登录
 */
export function logoutUser() {
  localStorage.removeItem('token')
}

/**
 * 获取用户列表
 */
export function fetchUsers(params) {
  return request({
    url: '/user/loadDataList',
    method: 'get',
    params,
  })
}

/**
 * 新增用户
 */
export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data,
  })
}

/**
 * 根据 ID 获取用户详情
 */
export function getUserById(id) {
  return request({
    url: '/user/getUserById',
    method: 'get',
    params: { id },
  })
}

/**
 * 根据 ID 更新用户
 * 可以只传需要修改的字段，例如 { id, nickname } 或 { id, password }
 */
export function updateUserById(data) {
  return request({
    url: '/user/updateUserById',
    method: 'post',
    data,
  })
}

/**
 * 根据 ID 删除用户
 */
export function deleteUserById(id) {
  return request({
    url: '/user/deleteUserById',
    method: 'post',
    data: { id },
  })
}
