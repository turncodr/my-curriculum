$(document).ready(() => {
    // get all the messages that should be displayed
    let messages = JSON.parse(sessionStorage.getItem('messages') || null);
    console.log(messages);
    if (messages != undefined) {
        messages.forEach((message) => {
            displayAlert(message);
        });

        // messages have been displayed, therefore delete them from storage
        sessionStorage.removeItem('messages');
    }
    var valueAttr = $('.moduletype-selection').attr('value');
    if(typeof valueAttr !== typeof undefined && valueAttr !== false && valueAttr !== "0") {
        $("#moduletype-text").toggle();
        $("#moduletype-dropdown").toggle();
        switch (valueAttr) {
            case '1': $('#moduletype-dropdown').val('1'); break;
            case '2': $('#moduletype-dropdown').val('2');
        }
        $('#module-type-checkbox').prop('checked', 'true');
    }
    $('.moduletype-selection').removeAttr("value");

    $("#module-type-checkbox").click(function() {
        $("#moduletype-text").toggle();
        $("#moduletype-dropdown").toggle();
        checkSelectedModule();
    });
    $('#moduletype-dropdown').change(function () {
        checkSelectedModule();
    })
});

function checkSelectedModule() {
    if ($("#module-type-checkbox").prop('checked') && $('#moduletype-dropdown option:selected').val() === '1') {
        $("#selected_module_type").val('PLACEHOLDER_MODULE');
    } else if ($("#module-type-checkbox").prop('checked') && $('#moduletype-dropdown option:selected').val() === '2') {
        $("#selected_module_type").val('ELECTIVE_MODULE');
    } else {
        $("#selected_module_type").val('MAIN_MODULE');
    }
}

function displayAlert({type, message}) {
    let alertClass = '';
    switch (type) {
        case 'SUCCESS':
            alertClass = 'alert-success';
            break;
        case 'WARNING':
            alertClass = 'alert-warning';
            break;
        case 'ERROR':
            alertClass = 'alert-danger';
            break;
    }

    $('<div></div>', {
        class: `alert ${alertClass}`,
        role: 'alert'
    }).text(message)
        .appendTo($('#messages'));
}
