import {authModule} from './AuthModule.js';
import {httpModule} from './HttpModule.js';


class UserModule {
    addNewUser() {
        document.getElementById('content').innerHTML =
                `<div class="w-100 d-flex justify-content-center">
            <div class="card w-50">
                <div class="card-body">
                  <h5 class="card-title w-100 text-center">Register user</h5>
                  <p class="card-text d-flex justify-content-end">Firstname: <input class="w-50 ml-3" type="text" id="firstname"></p>
                  <p class="card-text d-flex justify-content-end">Lastname: <input class="w-50 ml-3" type="text" id="lastname"></p>
                  <p class="card-text d-flex justify-content-end">User email: <input class="w-50 ml-3" type="email" id="email"></p>
                  <p class="card-text d-flex justify-content-end">Login: <input class="w-50 ml-3" type="text" id="login"></p>
                  <p class="card-text d-flex justify-content-end">Password: <input class="w-50 ml-3" type="password" id="password"></p>
                  <a href="#" id="btnAddUser" class="btn btn-primary w-100 rounded-pill">Register user</a>
                </div>
            </div>
          </div>`;
        document.getElementById('btnAddUser').onclick = function () {
            userModule.createUser();
        }


    }
    createUser() {
        let firstname = document.getElementById('firstname').value;
        let lastname = document.getElementById('lastname').value;
        let email = document.getElementById('email').value;
        let login = document.getElementById('login').value;
        let password = document.getElementById('password').value;
        let user = {
            "firstname": firstname,
            "lastname": lastname,
            "email": email,
            "login": login,
            "password": password,
        }
        httpModule.http({url: 'createUser', options: {method: 'POST', data: user}})
                .then(function (response) {
                    if (response !== null || response !== undefined) {
                        if (response.actionStatus === 'true') {
                            document.getElementById('info').innerHTML = 'User added';
                            document.getElementById('content').innerHTML = '';
                        } else {
                            document.getElementById('info').innerHTML = 'Failed to add user';
                        }
                    }
                })
    }
    printUserProfileForm() {
        let user = null;
        if (sessionStorage.getItem('user') !== null) {
            user = JSON.parse(sessionStorage.getItem('user'));
            console.log("user.login=" + user.login);
            document.getElementById('content').innerHTML =
                    `<div class="row mt-5">
                     <div class="card p-4 m-auto w-50">
                        <h1 class="h3 mb-4 font-weight-normal text-center">User profile</h1>
                        <div class="input-group mb-2">
                            <div class="input-group-prepend">
                              <span class="input-group-text">Name </span>
                            </div>
                            <input type="text" id="firstname" class="form-control"  value="${user.person.firstname}">
                        </div>
                        <div class="input-group mb-2">
                            <div class="input-group-prepend">
                              <span class="input-group-text">Surname </span>
                            </div>
                            <input type="text" id="lastname" class="form-control"  value="${user.person.lastname}">
                        </div>
                        <div class="input-group mb-2">
                            <div class="input-group-prepend">
                              <span class="input-group-text">User email </span>
                            </div>
                            <input type="text" id="email" class="form-control"  value="${user.person.email}">
                        </div>
                        <div class="input-group mb-2">
                            <div class="input-group-prepend">
                              <span class="input-group-text">Login </span>
                            </div>
                            <input type="text" id="login" class="form-control"  value="${user.login}">
                        </div>
                        <div class="input-group mb-2">
                            <div class="input-group-prepend">
                              <span class="input-group-text">New Password </span>
                            </div>
                            <input type="password" id="password" class="form-control"  value="">
                        </div>
                        <button id='btnChangeProfile' class="btn btn-primary btn-block rounded-pill" type="button">Edit</button>
                     </div>
                </div>`;
        } else {
            document.getElementById('content').innerHTML = 'No user data';
            authModule.authMenu();
        }
        document.getElementById('btnChangeProfile').onclick = function () {
            userModule.changeProfile();
        }
    }
    changeProfile() {
        let firstname = document.getElementById('firstname').value;
        let lastname = document.getElementById('lastname').value;
        let email = document.getElementById('email').value;
        let login = document.getElementById('login').value;
        let password = document.getElementById('password').value;
        if ("" === password) {
            password = null;
        }
        let user = {
            "firstname": firstname,
            "lastname": lastname,
            "email": email,
            "login": login,
            "password": password
        }
        httpModule.http({url: 'changeUserProfile', options: {method: 'POST', data: user}})
                .then(function (response) {
                    if (response !== null || response !== undefined) {
                        if (response.authStatus === 'true') {
                            if (response.user !== null) {
                                sessionStorage.setItem('user', JSON.stringify(response.user));
                                document.getElementById('info').innerHTML = 'User profile changed';
                            } else {
                                document.getElementById('info').innerHTML = 'User profile has not changed';
                                if (sessionStorage.getItem('user') !== null) {
                                    sessionStorage.removeItem('user');
                                }
                            }
                        } else {
                            document.getElementById('info').innerHTML = 'User profile has not changed';
                            if (sessionStorage.getItem('user') !== null) {
                                sessionStorage.removeItem('user');
                            }
                        }
                    } else {
                        document.getElementById('info').innerHTML = 'Server error';
                    }
                    userModule.printUserProfileForm();
                });
    }
}
let userModule = new UserModule();
export {userModule};

