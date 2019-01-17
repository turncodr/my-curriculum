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
});

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
