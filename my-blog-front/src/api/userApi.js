import request from '@/utils/request'

// 用户注册
export function registerUser(data) {
    return request({
        url: '/user/register',
        method: 'post',
        data,
    })
}

// 用户登录
export function loginUser(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data,
  }).then(res => {
    if (res.status === 'success' && res.data) {
      localStorage.setItem('token', res.data) // 直接存 data
    }
    return res
  })
}


// 获取用户列表
export function fetchUsers(params) {
    return request({
        url: '/user/loadDataList',
        method: 'get',
        params,
    })
}

// 新增用户
export function addUser(data) {
    return request({
        url: '/user/add',
        method: 'post',
        data,
    })
}

// 根据 ID 获取用户详情
export function getUserById(id) {
    return request({
        url: '/user/getUserById',
        method: 'get',
        params: { id },
    })
}

// 根据 ID 更新用户
export function updateUserById(data) {
    return request({
        url: '/user/updateUserById',
        method: 'post',
        data,
    })
}

// 根据 ID 删除用户
export function deleteUserById(id) {
    return request({
        url: '/user/deleteUserById',
        method: 'post',
        data: { id },
    })
}
