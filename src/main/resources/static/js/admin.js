// (function main() {
//     document.addEventListener('DOMContentLoaded', DOMContentLoaded);
//
//     function DOMContentLoaded() {
//         let buttonNode = document.querySelector('.js-show-form');
//         buttonNode.addEventListener('click', showForm);
//     }
//
//
//     function showForm() {
//         let node = document.querySelector('.js-form');
//         node.classList.remove('hidden');
//     }
// })();

const usersList = document.querySelector('.users__list');
const openCreateModalButton = document.getElementById('openCreateModalButton');
const createUserModal = document.querySelector('.createUserModal__body');
const modalUserName = document.getElementById('modalUserName');
const modalUserSurname = document.getElementById('modalUserSurname');
const modalUserRole = document.getElementById('modalUserRole');
const modalUserRoleOptions = modalUserRole.innerHTML;
const modalUserLogin = document.getElementById('modalUserLogin');
const modalUserPasswordLabel = document.getElementById('modalUserPasswordLabel');
const modalTitle = document.querySelector('.createUserModal__title');
const modalUserPasswordInput = document.getElementById('modalUserPassword');
let editMode = false;
let hiddenInput = null;
const userDeleteForm = document.getElementById('userDeleteForm');
let deletedUserId = null;

function getHiddenInput(id) {
    const label = document.createElement('label');
    label.innerHTML = `<input type="hidden" name ="id"  value=${id} id="hiddenInput" name="id"/>`;
    hiddenInput = label;
    return label;
}

function setUserDataInForm({ userId, firstName, lastName, login, role: { rolesList, currentRole} }) {
    editMode = true;
    createUserModal.appendChild(getHiddenInput(userId));
    modalUserName.value = firstName;
    modalUserSurname.value = lastName;
    modalUserRole.innerHTML = rolesList.reduce( (accum, role) => {
        return accum += `<option ${role === currentRole ? 'selected' : ''}>${role}</option>`
    }, '');
    modalUserLogin.value = login;
    modalUserPasswordInput.removeAttribute('required');
    createUserModal.setAttribute('action', '/updateUser');
    openCreateModalButton.click();
}

async function getEditUserData(userId) {
    let url = new URL("/editUser");
    let params = {id : userId};
    url.search = new URLSearchParams(params).toString();
    response = await fetch(url);
    const result = await response.json();
    console.log(result);
    setUserDataInForm(result)
}

usersList.addEventListener('click', ( { target }) => {
    if (target.closest('.user-info__button_edit')) {
        const userId = target.closest('.user-info').dataset.id;
        getEditUserData(userId);
    }
    if (target.closest('.user-info__button_delete')) {
        deletedUserId =  target.closest('.user-info').dataset.id;
    }
})

userDeleteForm.addEventListener('submit', () => {
    userDeleteForm.prepend(getHiddenInput(deletedUserId))
})

function refreshUserModal() {
    editMode = false;
    modalUserName.value = '';
    modalUserSurname.value = '';
    modalUserRole.innerHTML = modalUserRoleOptions;
    modalUserLogin.value = '';
    hiddenInput.remove();
    modalUserPasswordInput.setAttribute('required', '');
    createUserModal.setAttribute('action', '/addUser');
    editMode = false;
}

openCreateModalButton.addEventListener('click', ( { isTrusted} ) => {
    modalTitle.textContent = !isTrusted ? 'Edit user data' : 'Create new user';
    if (isTrusted && editMode) {
        refreshUserModal();
    }
})