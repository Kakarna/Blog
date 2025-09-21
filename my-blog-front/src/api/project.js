import request from '@/utils/request'


// 获取公共项目
export function getPublicProjects(params) {
  return request({
    url: '/project/publicProjects',
    method: 'get',
    data: params
  })
}

// 获取用户项目（可以是当前用户或其他用户）
export function getMyProjects(params = {}, requireUserId = false) {
  if (requireUserId && !params.userId) {
    throw new Error('用户ID参数是必需的')
  }
  return request({
    url: '/project/privateProjects',
    method: 'get',
    params: params  // 修改为使用params传递GET参数
  })
}


export function getProjectDetail(id) {
  return request({
    url: `/project/${id}`,
    method: 'get'
  })
}

export function addProjectWithFiles(formData) {
  return request({
    url: '/project/addWithFiles',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}


export function deleteProject(id) {
  return request({
    url: `/project/delete/${id}`,
    method: 'delete'
  })
}
