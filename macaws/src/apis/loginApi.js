import httpRequest from '../requests/base';

export function userLogin(params) {
    return httpRequest.post("/user/login", params);
}

export function userRegister(params) {
    return httpRequest.post("/user/register", params);
}