class HTTP {

    constructor (url) {
        this.API = url;
    }

    request (method, body) {
        return fetch(this.API, { method, body: JSON.stringify(body),
         headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json'
         } });
    }

    fetchData = () => fetch(this.API);

    createData (body) {
        return this.request('POST', body);
    }

}
