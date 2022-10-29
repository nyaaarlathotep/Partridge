import httpRequest from '../requests/api';

export function userLogin(param) {
    return httpRequest({
        url: '/posts',
        method: 'post',
        data: param,
    })
}