import { createApp } from "vue";
import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path: "/login",
        name: "Login",
        component: () => import("../views/Login")
    }
]

const router = createRouter({

    history: createWebHashHistory(),

    routes

})

export default router