$().ready(function () {
    $("#themes").change(function (event) {
        $.ajax({
            url:"/chooseTheme",
            type: "GET",
            dataType: "json",
            data: {topic: $(event.target).val()},
        })
            .done(function (data){
                setTests(data)
            })
            .fail(function (xhr, status, error){
                alert(xhr.responseText + '|\n' + status + '|\n' + error);
            });
    });
    $("#tests").change(function (event) {
        $.ajax({
            url:"/getDescription",
            type: "GET",
            dataType: "json",
            data: {test: $(event.target).val()},
        })
            .done(function (dataDescription){
                setDescription(dataDescription)
            })
            .fail(function (xhr, status, error){
                alert(xhr.responseText + '|\n' + status + '|\n' + error);
            });
    });
});

let setTests = function(data) {
    $('#tests').find('option').remove();
    $('#tests').append(new Option("Выберите тест"))
    $.each(data, function (index, value){
        $('#tests').append(new Option(value, value));
    }
    );
    $('#description').find('div').remove();
};
let setDescription = function(dataDescription) {
    $('#description').find('div').remove();
    $.each(dataDescription, function (index, value){
        let div = document.createElement('div');
        div.innerHTML = value;
        $('#description').append(div);
    });
};
