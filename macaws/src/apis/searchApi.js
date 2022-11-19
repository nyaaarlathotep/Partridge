import httpRequest from '../requests/base';

export function search(params) {
    return httpRequest.post("/ehentai/basic/search", params);
}