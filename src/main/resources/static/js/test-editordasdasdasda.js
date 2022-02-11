function getQuestionHtml({ name, description, testId, questions }) {
    const question = document.createElement('div');
    question.className = 'question';
    question.dataset.id = testId;
    question.innerHTML = `
    <div class="row">
      <a class="col question__text" data-bs-toggle="collapse" href='#test${testId}' role="button" aria-expanded="false" aria-controls="collapseExample">
        ${name}
      </a>
      <button class="col-auto question__add-button" data-bs-toggle="modal" data-bs-target="#questionModal"><img src="./img/add-icon.svg" alt="Edit test question"></button>
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
const addThemeForm = document.getElementById('addThemeForm');
const addThemeFormInput = document.querySelector('.add-theme-form__input');
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
    const testItem = target.closest('.theme-item');
    if (testItem) {
        const themeId = testItem.dataset.id;
        if (target.closest('.theme-item__input')) {
            if (themeId !== currentThemeId) {
                setNewThemeTests(themeId);
            }
        }
        if (target.closest('.theme-item__edit')) {
            setThemeEditMode(testItem)
        }
    }
}

document.addEventListener('click', ({ target }) => {
    const targetClassList = target.classList;
    if (target.closest('#testThemes')) {
        testThemeClichHandler(target)
        deactivateAddThemeForm();
    }
    else if (target.closest('.sidebar-add-theme')) {
        addThemeClickHandler(target);
    }
    else if(target.closest('#detailList')) {
        refreshThemesValues();
        detailClickHandler(target);
        deactivateAddThemeForm();
    }
    /* refreshThemesValues(); */
})




function setThemeEditMode(newTheme) {
    if (prevEditedTheme) {
        prevEditedTheme.classList.remove('edit');
        prevEditedTheme.querySelector('.theme-item__input').setAttribute('readonly', '');
        prevEditedTheme.querySelector('.theme-item__input').value = prevEditedThemeValue;
    }
    prevEditedTheme = newTheme;
    if (newTheme) {
        newTheme.classList.add('edit');
        newTheme.querySelector('.theme-item__input').removeAttribute('readonly');
        newTheme.querySelector('.theme-item__input').focus();
        prevEditedThemeValue = newTheme.querySelector('.theme-item__input').value;
    }
}

function refreshThemesValues() {
    setThemeEditMode(null);
    prevEditedThemeValue = null;
}

const prevQuestion = null;

function detailClickHandler(target) {
    const question = target.closest('.question');
    if (question) {
        const questionId = question.dataset.id;
        console.log(questionId)
    }
}

function changeActiveAddThemeFormStatus() {
    addThemeForm.classList.toggle('active');
}

function addThemeClickHandler(target) {
    if (target.closest('.sidebar-add-theme__button')) {
        changeActiveAddThemeFormStatus();
    }
}

function deactivateAddThemeForm() {
    addThemeForm.reset();
    addThemeForm.classList.remove('active');
}






const activateAddTestButton = document.getElementById('activateAddTestButton');
const addTestForm = document.getElementById('addTestForm');
const addAnswerButton = document.getElementById('addAnswerButton');


// activateAddTestButton.addEventListener('click', () => {
//     addTestForm.classList.toggle('active');
// })

function clickQuestionHandler(target) {
    if(target.classList.contains('question__text')) {
        target.closest('.question').classList.toggle('open')
    } else if(target.closest('.question__add-button')) {
        console.log('button')
    }
}

detailList.addEventListener('click', ({ target }) => {
    if (target.closest('.question')) {
        clickQuestionHandler(target);
    }
})

addAnswerButton.addEventListener('click', () => {
    console.log('hello');
});