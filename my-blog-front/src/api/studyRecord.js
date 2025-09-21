import request from '@/utils/request'

// 公共记录接口（无需登录）
export function fetchPublicStudyRecords(queryVO) {
  return request({
    url: '/studyRecord/loadPublicDataList',
    method: 'post',
    data: queryVO
  })
}

// 私人记录接口（需登录，后端会从 token 获取 userId）
export function fetchPrivateStudyRecords(queryVO) {
  return request({
    url: '/studyRecord/loadPrivateDataList',
    method: 'post',
    data: queryVO
  })
}

export function addStudyRecord(data) {
  const userData = JSON.parse(localStorage.getItem('user') || '{}')
  const username = userData.user?.username || ''
  return request.post('/studyRecord/add', {
    ...data,
    username,
  })
}

export function updateStudyRecord(data) {
  return request.post('/studyRecord/update', data)
}

export function deleteStudyRecord(data) {
  return request.post('/studyRecord/delete', data)
}
