import request from '@/utils/request' // axios 封装，如果你已经有 utils/request.js

/**
 * 获取项目文件树
 * @param {Number} projectId 
 */
export function getProjectFiles(projectId) {
  return request({
    url: `/projectFile/${projectId}/files`,
    method: 'get'
  })
}

/**
 * 获取单个文件内容
 * @param {Number} projectId 
 * @param {String} path 
 */
export function getFileContent(projectId, path) {
  return request({
    url: `/projectFile/${projectId}/file`,
    method: 'get',
    params: { path }
  })
}

export function deleteProjectFiles(projectId) {
  return request({
    url: `/projectFile/deleteByProjectFile/${projectId}`,
    method: 'delete'
  })
}
