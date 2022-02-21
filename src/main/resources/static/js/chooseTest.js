const testThemeSelect = document.getElementById('testThemeSelect');
const testSelect = document.getElementById('testSelect');
const testDescription = document.getElementById('testDescription');
const startTestButton = document.getElementById('startTestButton');
let testsData = null;
const baseUrl = 'http://localhost:8080';

testThemeSelect.addEventListener('change', ({ target }) => {
    const value = target.querySelector('option:checked').textContent;
    updateCurrentThemeData(value);
})

// testThemeSelect.addEventListener()

async function updateCurrentThemeData(themeName) {
    const formData = new FormData();
    formData.append('theme :', themeName);
    let url = new URL(baseUrl + "/chooseTheme");
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
