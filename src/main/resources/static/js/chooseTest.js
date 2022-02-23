const testThemeSelect = document.getElementById('testThemeSelect');
const testSelect = document.getElementById('testSelect');
const testDescription = document.getElementById('testDescription');
const startTestButton = document.getElementById('startTestButton');
let testsData = null;
const baseUrl = 'http://localhost:8080';

const changeListener = ({ target }) => {
    const value = target.querySelector('option:checked').textContent;
    console.log(value);
    updateCurrentThemeData(value);
};

const loadListener = ({}) => {
    const value = testThemeSelect.querySelector('option:checked').textContent;
    console.log(value);
    updateCurrentThemeData(value);
};

document.addEventListener('DOMContentLoaded', loadListener);
testThemeSelect.addEventListener('change', changeListener);

async function updateCurrentThemeData(themeName) {
    const formData = new FormData();
    formData.append('theme :', themeName);
    let url = new URL(baseUrl + "/user/chooseTheme");
    let params = {theme : themeName};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url);
    const result = await response.json();
    updateTestsData(result);
}

function updateTestsData(data) {
    testsData = data;
    const selectInner = data.reduce((accum, { name, testId }, index) => {
        return accum + `<option value='${testId}' data-index=${index}>${name}</option>`;
    }, '');

    testSelect.innerHTML = selectInner;
    testSelect.closest('div').classList.remove('hidden');
    startTestButton.closest('div').classList.remove('hidden');
    updateDescription();
}

testSelect.addEventListener('change', updateDescription)

function updateDescription() {
    const testOptionIndex = testSelect.querySelector('option:checked').dataset.index;
    testDescription.innerHTML = testsData[Number(testOptionIndex)].description;
    // testDescription.closest('div').classList.remove('hidden');
}

//
