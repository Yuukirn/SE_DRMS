export const Local = {
    // 设置永久缓存
    set(key, val) {
      window.localStorage.setItem(key, JSON.stringify(val))
    },
    // 获取永久缓存
    get(key) {
      let json = window.localStorage.getItem(key)
      return JSON.parse(json)
    },
    // 移除永久缓存
    remove(key) {
      window.localStorage.removeItem(key)
    },
    // 移除全部永久缓存
    clear() {
      window.localStorage.clear()
    }
   }
   
   export const Session = {
    // 设置临时缓存
    set(key, val) {
      window.sessionStorage.setItem(key, JSON.stringify(val))
    },
    // 获取临时缓存
    get(key) {
      let json = window.sessionStorage.getItem(key)
      return JSON.parse(json)
    },
    // 移除临时缓存
    remove(key) {
      window.sessionStorage.removeItem(key)
    },
    // 移除全部临时缓存
    clear() {
      window.sessionStorage.clear()
    }
   }