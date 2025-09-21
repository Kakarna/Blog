/**
 * JWT工具函数
 * 用于解析和处理JWT token
 */

/**
 * 解析JWT token
 * @param {string} token - JWT token字符串
 * @returns {object|null} 解析后的payload对象，如果解析失败返回null
 */
export function parseJwt(token) {
  try {
    // 移除Bearer前缀（如果存在）
    const actualToken = token.replace(/^Bearer\s+/i, '')
    
    // 分割JWT的三个部分
    const parts = actualToken.split('.')
    if (parts.length !== 3) {
      console.error('Invalid JWT format: token must have 3 parts')
      return null
    }
    
    // 解码payload部分（base64url解码）
    const payload = parts[1]
    const decodedPayload = atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    
    // 解析JSON
    return JSON.parse(decodedPayload)
  } catch (error) {
    console.error('Failed to parse JWT token:', error)
    return null
  }
}

/**
 * 检查token是否过期
 * @param {string} token - JWT token字符串
 * @returns {boolean} 是否过期
 */
export function isTokenExpired(token) {
  const payload = parseJwt(token)
  if (!payload || !payload.exp) {
    return true
  }
  
  // 将过期时间戳（秒）转换为毫秒并与当前时间比较
  const currentTime = Date.now() / 1000
  return payload.exp < currentTime
}

/**
 * 获取token中的用户信息
 * @param {string} token - JWT token字符串
 * @returns {object|null} 用户信息对象
 */
export function getUserInfoFromToken(token) {
  const payload = parseJwt(token)
  if (!payload) {
    return null
  }
  
  return {
    id: payload.id,
    username: payload.username,
    nickname: payload.nickname,
    role: payload.role
  }
}