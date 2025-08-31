// src/api/studyRecord.js
import request from '@/utils/request'

export function fetchStudyRecords(queryVO) {
  return request({
    url: '/studyRecord/loadDataList',
    method: 'post',
    data: queryVO // 使用data传递分页参数，因为后端使用@RequestBody接收
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
