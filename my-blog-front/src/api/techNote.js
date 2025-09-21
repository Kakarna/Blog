// src/api/techNote.js
import request from '@/utils/request'

export function getTechNoteList(queryVO) {
  return request({
    url: '/techNote/loadDataListBySection',
    method: 'post',
    data: queryVO
  })
}

export function getTechNoteById(id) {
  return request.get(`/techNote/getById`, {
    params: { id }
  })
}

export const addTechNote = (data) => {
  return request({
    url: '/techNote/add',
    method: 'post',
    data
  })
}


export function updateTechNote(data) {
  return request({
    url: '/techNote/update',
    method: 'post',
    data
  })
}

export function deleteTechNoteById(id) {
  return request({
    url: '/techNote/delete',
    method: 'post',
    data: { id }
  })
}

export function getPrivateNotes(queryVO, requireUserId = false) {
  if (requireUserId && !queryVO.userId) {
    throw new Error('用户ID参数是必需的')
  }
  return request({
    url: '/techNote/getPrivateNotes',
    method: 'get',
    params: queryVO
  })
}

// 保留旧接口以兼容现有代码，但内部调用新接口
export function getNotesByUserId(queryVO, requireUserId = false) {
  return getPrivateNotes(queryVO, requireUserId);
}

