import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => {
    return {
      user: {
        id: '',
        token:''
      }
    }
    }, 
    actions: {
      setUser(user) {
        this.user = user
      }
    }
  }
)
