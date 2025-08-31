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

