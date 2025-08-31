import request from '@/utils/request'


//分页加载
export function getProjects(params) {
  // params示例：{ pageNo: 1, pageSize: 15 }
  return request({
    url: '/project/loadDataList',
    method: 'post',
    data: params
  })
}

export function getProjectDetail(id) {
  return request({
    url: `/project/${id}`,
    method: 'get'
  })
}

export function addProjectWithFiles(data) {
  return request({
    url: '/project/addWithFiles',
    method: 'post',
    data
  })
}

export function deleteProject(id) {
  return request({
    url: `/project/delete/${id}`,
    method: 'delete'
  })
}
