import request from '@/utils/request'

export function getSectionList() {
  return request.get('/techSection/loadDataList')
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
