var currentPage = 1;

$(document).ready(function () {
    var t = $(".js-search-field");
    var e = $(".js-top-bar");
    t.on({
        "focus": function () {
            e.addClass("_search-open")
        }, "blur": function () {
            e.removeClass("_search-open")
        }, "keydown": function (e) { if (13 == e.keyCode) { t.blur(); clearQuestionsDiv(); requestData(); } }
    });
    $(".js-search-submit").on({ "click": function (e) { { t.blur(); clearQuestionsDiv(); requestData(); } } });
    $("#warning").click(function () {

        $(this).slideUp("slow");
        return false;
    });
    $("#load-more").click(function () {
        requestData();
    });
});

function requestData() {


    $.ajax({
        type: 'GET',
        url: "/search",
        data: {
            intitle: $(".js-search-field")[0].value,
            site: "stackoverflow",
            order: "desc",
            sort: "activity",
            pagesize: 3,
            page: currentPage++
        },
        dataType: 'json',
        cache: false,
        success: function (resp) {
            console.log(resp);
            processResponce(resp);
        }
    }).fail(function (e) {
        loadWarning('failed to get data ' + e.responseText);
    });
}

function clearQuestionsDiv() {
    var questionsDiv = $('#questions');
    questionsDiv[0].innerHTML = "";
    currentPage =1 ;

}

function processResponce(resp) {
    $('#load-more')[0].disabled = !resp.has_more;

    $('#questionTemplate').tmpl(resp.items).appendTo('#questions');

}

function getQuestionObject(jQuestion) {

    qSummary = document.createElement('div');
    qSummary.setAttribute('class', 'question-summary');

    qSummary.append(jQuestion.title);
    return qSummary;
}


function loadWarning(text) {

    $("#warning").html("<b>WARNING: <\/b>" + text);

    $("#warning").slideDown("slow");
};



