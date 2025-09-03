# Changelog

## [1.1.0] - 2025-09-04
### Added
- 首页仪表盘模块：
  - 统计卡片：笔记数、项目数、最近更新、访问量
  - 饼图：文章分类比例（ECharts）
  - 折线图：文章发布趋势
  - 最近更新列表（支持跳转到笔记详情）
- 后端接口：
  - `/techNote/recentActivities` 获取最近更新
  - 分类统计接口（按 section.name 分组）
  - 文章趋势接口
- Header.vue 增加用户状态展示与退出功能

### Improved
- Mapper 保持分页查询统一实现
- 前端界面结构更清晰，首页内容模块化
- 依赖清理，修复 `vite` 启动失败问题

### Planned
- 全局搜索功能（Header 搜索框 + Search.vue） 