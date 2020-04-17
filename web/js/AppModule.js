import {authModule} from './AuthModule.js';
import {userModule} from './UserModule.js';
import {resourcesModule} from './ResourcesModule.js';

document.getElementById("userProfile").onclick = function () {
    toogleMenuActive("userProfile");
    userModule.printUserProfileForm();
};
document.getElementById("resources").onclick = function () {
    toogleMenuActive("resources");
    resourcesModule.getListResources();
};
document.getElementById("addResource").onclick = function () {
    toogleMenuActive("addResource");
    resourcesModule.printAddResourcesForm();
};

document.getElementById("showLogin").onclick = function () {
    toogleMenuActive("showLogin");
    authModule.printLoginForm();
};
document.getElementById("sysout").onclick = function () {
    toogleMenuActive("sysout");
    authModule.systemOutput();
};


function toogleMenuActive(elementId) {
    let activeElement = document.getElementById(elementId);
    let passiveElements = document.getElementsByClassName("nav-item");
    for (let i = 0; i < passiveElements.length; i++) {
        if (activeElement === passiveElements[i]) {
            passiveElements[i].classList.add("active");
        } else {
            if (passiveElements[i].classList.contains("active")) {
                passiveElements[i].classList.remove("active");
            }
        }
    }
}

