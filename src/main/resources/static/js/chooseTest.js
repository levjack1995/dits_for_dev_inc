// $().ready(function () {
//     $("#themes").change(function (event) {
//         $.ajax({
//             url:"/chooseTheme",
//             type: "GET",
//             dataType: "json",
//             data: {topic: $(event.target).val()},
//         })
//             .done(function (data){
//                 setTests(data)
//             })
//             .fail(function (xhr, status, error){
//                 alert(xhr.responseText + '|\n' + status + '|\n' + error);
//             });
//     });
//     $("#tests").change(function (event) {
//         $.ajax({
//             url:"/getDescription",
//             type: "GET",
//             dataType: "json",
//             data: {test: $(event.target).val()},
//         })
//             .done(function (dataDescription){
//                 setDescription(dataDescription)
//             })
//             .fail(function (xhr, status, error){
//                 alert(xhr.responseText + '|\n' + status + '|\n' + error);
//             });
//     });
// });
//
// let setTests = function(data) {
//     $('#tests').find('option').remove();
//     $('#tests').append(new Option("Выберите тест"))
//     $.each(data, function (index, value){
//         $('#tests').append(new Option(value, value));
//     }
//     );
//     $('#description').find('div').remove();
// };
// let setDescription = function(dataDescription) {
//     $('#description').find('div').remove();
//     $.each(dataDescription, function (index, value){
//         let div = document.createElement('div');
//         div.innerHTML = value;
//         $('#description').append(div);
//     });
// };
// const sort = document.querySelector('.sort');
//
// sort.addEventListener('click', () => sort.classList.toggle('down'));


const testThemeSelect = document.getElementById('testThemeSelect');
const testSelect = document.getElementById('testSelect');
const testDescription = document.getElementById('testDescription');
const startTestButton = document.getElementById('startTestButton');
let testsData = null;

testThemeSelect.addEventListener('change', ({ target }) => {
    const value = target.querySelector('option:checked').textContent;
    updateCurrentThemeData(value);
})

async function updateCurrentThemeData(themeName) {
    const formData = new FormData();
    formData.append('theme :', themeName);
    let url = new URL("http://localhost:8080/chooseTheme");
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
    testDescription.closest('div').classList.remove('hidden');
}

//
