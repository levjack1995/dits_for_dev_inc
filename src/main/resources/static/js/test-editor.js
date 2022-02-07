// const dataByTopicId =
//     [
//         {
//             "name": "Second test",
//             "description": "DescTest2",
//             "testId": 2,
//             "questions": [
//                 {
//                     "questionId": 2,
//                     "description": "1st quest for test2"
//                 },
//                 {
//                     "questionId": 3,
//                     "description": "2st quest for test2"
//                 },
//                 {
//                     "questionId": 4,
//                     "description": "3d quest for test2"
//                 },
//                 {
//                     "questionId": 5,
//                     "description": "4d qyest for test2"
//                 },
//                 {
//                     "questionId": 6,
//                     "description": "5d quest for test2"
//                 }
//             ]
//         },
//         {
//             "name": "TherdNameTest",
//             "description": "DescT3",
//             "testId": 19,
//             "questions": [
//
//             ]
//         }
//     ];



function getQuestionHtml({ name, description, testId, questions }) {
    const question = document.createElement('div');
    question.className = 'question';
    question.dataset.id = testId;
    question.innerHTML = `
    <div class="row">
      <a class="col question__text" data-bs-toggle="collapse" href='#test${testId}' role="button" aria-expanded="false" aria-controls="collapseExample">
        ${name}
      </a>
      <button class="col-auto question__add-button"><img src="./img/add-icon.svg" alt="Edit test question"></button>
      <div class="col-auto question__control">
        <button><img src="./img/edit-icon.svg" alt="Edit test question"></button>
        <button><img src="./img/delete-icon.svg" alt="Delete test question"></button>
      </div>
    </div>
    <div class="collapse question__list" id=test${testId}>
      ${
        questions.reduce( (accum, { questionId, description }) => {
            return accum += (`
            <div class="row align-items-center question__item" data-id=${questionId}>
              <span class="col-auto">1</span>
              <textarea class="col form-input" type="text" readonly="">
                ${description}
              </textarea>
              <div class="col-auto question-control">
                <button><img src="./img/edit-icon.svg" alt="Edit test question"></button>
                <button><img src="./img/delete-icon.svg" alt="Delete test question"></button>
              </div>
            </div>
          `)
        }, '')
    }
    </div>
  `
    return question
}


const detailList = document.getElementById('detailList');
let prevEditedTheme = null;
let prevEditedThemeValue = null;
let currentThemeId = null;

async function getTestsData(themeId) {
    let url = new URL("http://localhost:8080/getTests");
    let params = {id : themeId};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url);
    const result = await response.json();
    return result;
}

async function setNewThemeTests(themeId) {
    const testsData = await getTestsData(themeId);
    detailList.innerHTML = '';
    testsData.forEach( testData => {
        detailList.appendChild(getQuestionHtml(testData))
    })
}

function testThemeClichHandler(target) {
    const testItem = target.closest('.test-item');
    if (testItem) {
        const themeId = testItem.dataset.id;
        if (target.closest('.test-item__input')) {
            if (themeId !== currentThemeId) {
                setNewThemeTests(themeId);
            }
        }
        if (target.closest('.test-item__edit')) {
            setThemeEditMode(testItem)
        }
    }
}

document.addEventListener('click', ({ target }) => {
    const targetClassList = target.classList;
    if (target.closest('#testThemes')) {
        testThemeClichHandler(target)
    } else if(target.closest('#detailList')) {
        refreshThemesValues();
        detailClickHandler(target);
    }
    /* refreshThemesValues(); */
})




function setThemeEditMode(newTheme) {
    if (prevEditedTheme) {
        prevEditedTheme.classList.remove('edit');
        prevEditedTheme.querySelector('.test-item__input').setAttribute('readonly', '');
        prevEditedTheme.querySelector('.test-item__input').value = prevEditedThemeValue;
    }
    prevEditedTheme = newTheme;
    if (newTheme) {
        newTheme.classList.add('edit');
        newTheme.querySelector('.test-item__input').removeAttribute('readonly');
        newTheme.querySelector('.test-item__input').focus();
        prevEditedThemeValue = newTheme.querySelector('.test-item__input').value;
    }
}

function refreshThemesValues() {
    setThemeEditMode(null);
    prevEditedThemeValue = null;
}

const prevQuestion = null;

function detailClickHandler(target) {
    if (target.closest('.question')) {
        console.log('Yes')
    }
}




 const activateAddTestButton = document.getElementById('activateAddTestButton');
const addTestForm = document.getElementById('addTestForm');
activateAddTestButton.addEventListener('click', () => {
    addTestForm.classList.toggle('active');
})
detailList.addEventListener('click', ({ target }) => {
    if (target.closest('.question')) {
        clickQuestionHandler(target);
    }
})
function clickQuestionHandler(target) {
    if(target.classList.contains('question__text')) {
        target.closest('.question').classList.toggle('open')
    }
}