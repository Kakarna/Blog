import request from '@/utils/request'

/**
 * 统一搜索接口
 * @param {Object} params 搜索参数
 * @param {String} params.keyword 搜索关键词
 * @param {String} params.type 搜索类型：user, note, project, all
 * @param {Number} params.pageNo 页码
 * @param {Number} params.pageSize 每页数量
 */
export function unifiedSearch(params) {
  return request({
    url: '/search',
    method: 'get',
    params
  })
}