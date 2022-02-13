let data = [
  {
    "testName": "Second test",
    "count":19,
    "avgProc":80,
    "questionStatistics": [
      {"count":3,"avgProc":100},
      {"count":10,"avgProc":90},
      {"count":10,"avgProc":60},
      {"count":10,"avgProc":70},
      {"count":19,"avgProc":84}
    ]
  },
  {
    "testName":"TherdNameTest",
    "count":0,
    "avgProc":0,
    "questionStatistics":[]
  }]

const themeSelect = document.getElementById('themeSelect');
const resultTableBody = document.getElementById('resultTableBody');
const dataContainer = document.getElementById('dataContainer');
const sortTestsButton = document.getElementById('sortTestsButton');
const token = document.head.querySelector('meta[name="_csrf"]').getAttribute('content');
let isReverseTest = false;

function updateResult(data) {
  if (!data) { 
    return
  }

  const reversedData = isReverseTest ? [...data].reverse() : data;
  dataContainer.classList.add('active');
  resultTableBody.innerHTML = `
    ${reversedData.map( ({testName, count, avgProc, questionStatistics}, index) => {
      return `
      <div class="py-3 col-12 test" data-id="${index}">
        <div class="grid" data-bs-toggle="collapse" href='#test${index}'>
          <div class="grid__item">${testName}</div>
          <div class="text-center justify-content-center grid__item">${count}</div>
          <div class="text-center justify-content-center grid__item">${avgProc}%</div>
        </div>
        <div class="collapse" id='test${index}'>
          <div class="question">
            <div class="mt-3 ps-3 question__head-row">
              <span class="question__head-text">Question name</span>
              <span></span>
              <div class="text-center question__head-text">
                <span>Correct</span>
                <button class="sort-button"><img src="./img/sort-icon.svg"></button>
              </div>
            </div>
            <div class="question__list">
              ${questionStatistics.map( ({questionDescription, avgProc}, index) => {
                return `
                  <div class="mt-3 question__item">
                    <span class="px-2">${index + 1}.</span><textarea class="py-2 px-4 question__name" rows="1" readonly>
                    ${questionDescription}</textarea><span></span><span class="text-center">${avgProc}%</span>
                  </div>
                `
              }).join('')}
            </div>
          </div>
        </div>
      </div>
      `
    }).join('')}
  `
}

themeSelect.addEventListener('change', async ({ target }) => {
  dataContainer.classList.remove('active');
  try {
    const themeId = target.value;
    const url = new URL("http://localhost:8080/getTestsStatistic");
    const params = {id: themeId};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url);
    const result = await response.json();
    // result = data
    updateResult(result);
  } catch (err) {
    console.error(err)
  }
});

function reverseTests(target) {
  target.closest('#sortTestsButton').classList.toggle('reverse');
  const reversedTests = Array.from(resultTableBody.querySelectorAll('.test')).reverse();
  resultTableBody.innerHTML = '';
  reversedTests.forEach( test => {
    resultTableBody.append(test);
  })
}

function reverseQuestions(target) {
  const questionNode = target.closest('.question')
  const questionsContainer = questionNode.querySelector('.question__list');
  const questions = questionsContainer.querySelectorAll('.question__item');
  const reversedQuestions = [...questions].reverse();
  questionsContainer.innerHTML = '';
  reversedQuestions.forEach(item => {
    questionsContainer.append(item);
  })
}

function questionClickHandler(target) {
  if (target.closest('.sort-button')) {
    reverseQuestions(target);
  }
}

dataContainer.addEventListener('click', (event) => {
  const { target } = event;
  if(target.closest('#sortTestsButton')) {
    isReverseTest = !isReverseTest;
    reverseTests(target);
  } else if (target.closest('.question')) {
    questionClickHandler(target)
  }
})
  