class HTTP {

    constructor (url) {
        this.API = url;
    }

    request (method, body) {
        return fetch(this.API, { method, body: JSON.stringify(body),
         headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json',
           'Authorization': 'Basic dXNlcm5hbWUtMzpwYXNzd29yZA=='
         } });
    }

    fetchData = () => fetch(this.API);

    createData (body) {
        return this.request('POST', body);
    }

}
