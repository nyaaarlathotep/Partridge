import axios from "axios";
import {ElMessage} from 'element-plus';

const service = axios.create({
    baseURL: 'http://223.166.241.224:51320/',
    withCredentials: true, // 异步请求携带cookies
    changeOrigin: true,
    timeout: 5000
})
 
// 添加请求拦截器
service.interceptors.request.use(
    function (config) {
        // 在发送请求之前做些什么
        return config
    },
    function (error) {
        // 对请求错误做些什么
        console.log('出错了',error)
        return Promise.reject(error)
    }
)
 
// 添加响应拦截器
service.interceptors.response.use(
    function (response) {
        const dataAxios = response.data
        return dataAxios
    },
    function (error) {
        ElMessage({
            message: '接口报错了',
            type: 'success',
          })
        return Promise.reject(error)
    }
)
export default service