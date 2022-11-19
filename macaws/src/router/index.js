// import { createApp } from "vue";
import { createRouter, createWebHashHistory } from "vue-router";

const routes = [
    {
        path: "/",
        name: "Login",
        component: () => import("../views/login.vue")
    },
    {
        path: "/searchPage",
        name: "SearchPage",
        component: () => import("../views/searchPage.vue")
    }
]

const router = createRouter({

    history: createWebHashHistory(),

    routes

})

export default router