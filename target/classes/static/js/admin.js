(function main() {
    document.addEventListener('DOMContentLoaded', DOMContentLoaded);

    function DOMContentLoaded() {
        let buttonNode = document.querySelector('.js-show-form');
        buttonNode.addEventListener('click', showForm);
    }


    function showForm() {
        let node = document.querySelector('.js-form');
        node.classList.remove('hidden');
    }
})();