import request from '@/utils/request'


// 获取公共账号分区
export function getPublicSections() {
  return request({
    url: '/techSection/publicUser',
    method: 'get'
  })
}


export function getPrivateSections() {
  return request.get('/techSection/privateUser')
}

export function addSection(data) {
  return request.post('/techSection/add', data)
}

// 修改分区
export function updateSection(id, data) {
  return request.put(`/techSection/update/${id}`, data)
}


export function deleteSection(id) {
  return request.delete(`/techSection/delete/${id}`)
}
