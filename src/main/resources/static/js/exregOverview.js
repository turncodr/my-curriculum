/**
 *
 */
function showModuleDetails(event) {
    let id = (event.target.id == "") ? $(event.target).parents(".module").attr("id") : event.target.id;

    $.ajax({
        url: "/module/details/" + id,
        success: function (response) {
            if ($("#showModuleDetails").length) {
                $("#showModuleDetails").remove();
            }
            $("body").append(response);
            $("#showModuleDetails").modal();
            $("#showModuleDetails").modal("show");
        }
    });
}

