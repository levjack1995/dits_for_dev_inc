/**
 * TODO:
 * - rename this file
 */

const formTestItem = document.getElementById("form-test-item");

const buttonSubmit = formTestItem.querySelector("button[type=\"submit\"]");
const testAnswers = formTestItem.querySelectorAll(".answer_variant > input[type=\"checkbox\"]");

if (buttonSubmit !== null) {
    buttonSubmit.addEventListener("click", e => {
        e.preventDefault();
    
        if (testAnswers.length > 0 && isAnyAnswerSelected(testAnswers)) {
            formTestItem.submit();
        }
    });
}

function isAnyAnswerSelected(checkboxList) {
    for (let i = 0; i < checkboxList.length; i++) {
        if (checkboxList[i].checked) {
            return true;
        }
    }

    return false;
}