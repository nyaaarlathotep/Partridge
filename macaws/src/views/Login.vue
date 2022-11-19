<template>
    <div class="loginbody">
        <div class="logindata">
            <div class="logintext">
                <h2>Welcome</h2>
            </div>
            <div class="formdata">
                <el-form ref="form" :model="loginForm" :rules="rules">
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" clearable placeholder="请输入账号"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="loginForm.password" clearable placeholder="请输入密码" show-password></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div class="tool">
                <div>
                    <el-checkbox v-model="checked" @change="remember()">记住密码</el-checkbox>
                </div>
            </div>
            <div class="button">
                <el-button type="primary" @click.native.prevent="login(loginForm)">登录</el-button>
                <el-button class="shou" @click="register(loginForm)">注册</el-button>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive, ref } from 'vue'
import { userLogin, userRegister } from '../apis/loginApi'
export default {
    setup() {
        const loginForm = reactive({
            username: "",
            password: ""
        })
        const login = (value) => {
            userLogin(value).then((res) => {
                if(res.code === 200) {
                    this.$router.replace('/SearchPage');
                }
            })
        };
        const remember = (value) => {
            localStorage.setItem("news",JSON.stringify(value))
        };
        const register = (value) => {
            userRegister(value).then((res) => {
                if(res.code === 200) {
                    this.$router.replace('/');
                }
            })
        };
        return {
            loginForm,
            login,
            remember,
            register
        }
    }
}
</script>

<style scoped>
.loginbody {
  width: 100%;
  height: 100%;
  overflow: auto;
  background-repeat: no-repeat;
  position: fixed;
  line-height: 100%;
  padding-top: 150px;;
}
.logintext {
  margin-bottom: 20px;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
  color: white;
  text-shadow: 2px 2px 4px #000000;
}

.logindata {
  width: 400px;
  height: 300px;
  transform: translate(-50%);
  margin-left: 50%;
}

.tool {
  display: flex;
  justify-content: space-between;
  color: #606266;
}
 
.button {
  margin-top: 10px;
  text-align: center;
}
 
.shou {
  cursor: pointer;
  color: #606266;
}
.logintext {
  margin-bottom: 20px;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
  color: white;
  text-shadow: 2px 2px 4px #000000;
}
</style>
