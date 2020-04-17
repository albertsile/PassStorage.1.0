class HttpModule {

    status(response) {
        if (response.status >= 200 && response.status < 300) {
            return Promise.resolve(response)
        } else {
            return Promise.reject(new Error(response.statusText))
        }
    }
    json(response) {
        return response.json()
    }
    fetchOpts(opt) {
        if (opt.method === 'POST') {
            return {
                method: opt.method,
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                credentials: 'include',
                body: JSON.stringify(opt.data)
            };
        } else {
            return {
                method: opt.method,
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                credentials: 'include'
            };
        }
    }
    ;
            http(httpOptions) {
        if (httpOptions.options.params != null) {
            httpOptions.url = httpOptions.url + '?' + httpModule.queryParams(httpOptions.options.params);
        }
        let options = httpModule.fetchOpts(httpOptions.options);
        return fetch(httpOptions.url, options)
                .then(httpModule.status)
                .then(httpModule.json)
                .catch((ex) => console.log("Fetch Exception", ex));
    }
    ;
            queryParams(params) {
        return Object.keys(params)
                .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
                .join('&');
    }

}

let httpModule = new HttpModule();

export {httpModule};


