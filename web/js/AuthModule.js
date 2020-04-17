import {userModule} from './UserModule.js';
import {httpModule} from './HttpModule.js';
class AuthModule {
    printLoginForm() {
        document.getElementById('content').innerHTML =
                `<div class="w-100 d-flex justify-content-center">
               <div class="card border-primary p-2" style="max-width: 30rem;">
                  <div class="card-header text-center">Enter login and password</div>
                  <div class="card-body">
                    <p class="card-text d-flex justify-content-between">Login: <input class="ml-2" type="text" id="login"></p>
                    <p class="card-text d-flex justify-content-between">Password: <input class="ml-2" type="password" id="password"></p>
                    <p class="card-text"><button class="btn btn-primary w-100 rounded-pill" type="button" id="btnEnter">Sign in</button</p>
                  </div>
                    <p class="text-center">No account? <a id="registration" href="#">Register</a></p>
               </div>
             </div>`;
        document.getElementById('btnEnter').onclick = function () {
            authModule.auth();
        }
        document.getElementById('registration').onclick = function () {
            userModule.addNewUser();
        }
        document.getElementById('password').addEventListener("keyup", function (event) {
            if (event.keyCode === 13) {
                event.preventDefault();
                document.getElementById('btnEnter').click();
            }
        });
    }
    auth() {
        let login = document.getElementById('login').value;
        let password = document.getElementById('password').value;
        let credential = {
            "login": login,
            "password": password
        }
        httpModule.http({url: 'login', options: {method: 'POST', data: credential}})
                .then(function (response) {
                    if (response.authStatus === 'true') {
                        sessionStorage.setItem('user', JSON.stringify(response.user));
                        document.getElementById('info').innerHTML = 'You are logged in as ' + response.user.login;
                    } else {
                        document.getElementById('info').innerHTML = 'Login failed';
                        authModule.printLoginForm();
                    }
                    authModule.authMenu();
                    document.getElementById('content').innerHTML = ''
                });
    }
    ;
            systemOutput() {
        httpModule.http({url: 'logout', options: {method: 'GET'}})
                .then(function (response) {
                    if (response !== null && response.actionStatus === 'true') {
                        sessionStorage.removeItem('user');
                        authModule.authMenu();
                        document.getElementById('content').innerHTML = '';
                        document.getElementById('info').innerHTML = 'You out';
                    } else {
                        if (locaStorage.getItems('user') !== null) {
                            sessionStorage.removeItem('user');
                        }
                    }
                })
    }
    ;
            authMenu() {
        let user = null;
        if (sessionStorage.getItem('user') !== null) {
            user = JSON.parse(sessionStorage.getItem('user'));
        }
        if (user !== null) {
            document.getElementById("userProfile").style.display = 'block';
            document.getElementById("addResource").style.display = 'block';
            document.getElementById("resources").style.display = 'block';
            document.getElementById("sysout").style.display = 'block';
            document.getElementById("showLogin").style.display = 'none';
        } else {
            document.getElementById("userProfile").style.display = 'none';
            document.getElementById("addResource").style.display = 'none';
            document.getElementById("resources").style.display = 'none';
            document.getElementById("sysout").style.display = 'none';
            document.getElementById("showLogin").style.display = 'block';
        }
    }
}

let authModule = new AuthModule();
export {authModule};

