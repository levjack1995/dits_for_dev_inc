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
let isReverseTest = false;

function updateResult(data) {
  if (!data) { 
    return
  }

  console.log(isReverseTest)

  const reversedData = isReverseTest ? [...data].reverse() : [...data];
  dataContainer.classList.add('active');
  resultTableBody.innerHTML = `
    ${reversedData.map( ({testName, count, avgProc, questionStatistics}, index) => {
      return `
      <div class="py-3 col-12 test" data-id="${index}">
        <div class="grid" data-bs-toggle="collapse" href='#test${index}'>
          <div class="grid__item">${index + 1}</div>
          <div class="grid__item">${testName}</div>
          <div class="text-center justify-content-center grid__item">${count}</div>
          <div class="text-center justify-content-center grid__item">${avgProc}%</div>
        </div>
      </div>
      `
    }).join('')}
  `
}

themeSelect.addEventListener('change', async ({ target }) => {
  dataContainer.classList.remove('active');
  try {
    const userId = target.value;
    const url = new URL("http:localhost:8080/getUserTestsStatistic");
    const params = {id: userId};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url);
    const result = await response.json();
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


dataContainer.addEventListener('click', (event) => {
  const { target } = event;
  if(target.closest('#sortTestsButton')) {
    isReverseTest = !isReverseTest;
    reverseTests(target);
  } 
})
  