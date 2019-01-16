/**
 * List of modules that already exist in the DB.
 */
var existingModulesMap = [];
/**
 * List of module stubs that are dynamically created by the user.
 */
var moduleStubs = [];
/**
 * Id Counter for the created module Stubs.
 */
var nextStubId = 1;
/**
 * Counter which keeps the number of the next semester.
 * It's also used to create the semester IDs.
 */
var semesterCounter = 1;
var delBtnSemesterId = {};//object which maps the delete button ids to semester id
var listOfSemester = {};//object which maps the semester id to the semesterCounter

/**
 * "Dummy Module" that acts as a placeholder to show the user where to drop the module.
 */
var dragAndDropModulePlaceholder = $('<div></div>', {
    "class": "card text-muted dragAndDropPlaceholder",
    on: {
        dragover: function (event) {
            event.preventDefault();
        },
        drop: function (event) {
            event.preventDefault();
            //because jQuery only passes the jQuery event object instead of the browser event object
            event.dataTransfer = event.originalEvent.dataTransfer;
            var draggedElementId = event.dataTransfer.getData("text");
            testAndSetPlaceholder(draggedElementId);
            $('#' + draggedElementId).detach().appendTo(this.parent());
            this.remove();
        }
    }
}).append($('<div></div>', {
    "class": "card-header bg-white text-truncate placeholderText",
    text: dropYourModuleHere
}));

/**
 * Creates a new semester panel and adds it to the list.
 */
function addNewSemester() {
    if (semesterCounter < 1) {
        semesterCounter = 1;
    }
    var semesterId = "semester" + semesterCounter;
    var delBtnId = "deleteButton" + semesterCounter;
    var spanId = "semesterSpan" + semesterCounter;
    var semesterBodyId = "semesterbody" + semesterCounter;
    delBtnSemesterId[delBtnId] = semesterId;
    listOfSemester[semesterId] = {
        "semesterNr": semesterCounter,
        "delBtn": delBtnId
    };

    var semester = $('<div></div>', {
        draggable: true,
        "class": "card semester",
        "id": semesterId,
        draggable: true,
        on: {
            dragover: function (event) {
                event.preventDefault();
            },
            drop: function (event) {
                event.preventDefault();
                //because jQuery only passes the jQuery event object instead of the browser event object
                event.dataTransfer = event.originalEvent.dataTransfer;
                var draggedElementId = '#' + event.dataTransfer.getData("text");
                
                if ($(draggedElementId).hasClass("semester")) {
                    var targetElementId = '#' + $(event.target).parents("div.semester").attr("id");
                
                    if (draggedElementId != event.target.id) {
                        var modulesAreaDragged = $(draggedElementId).find("div.modulesArea");
                        var modulesAreaTarget = $(targetElementId).find("div.modulesArea");
                        var modulesDragged = modulesAreaDragged.find("div.module");
                        var modulesTarget = modulesAreaTarget.find("div.module");
                        modulesDragged.detach().appendTo(modulesAreaTarget);
                        modulesTarget.detach().appendTo(modulesAreaDragged);
                        checkPlaceholder(modulesAreaDragged);
                        checkPlaceholder(modulesAreaTarget);
                        function checkPlaceholder(modulesArea) {
                            if (modulesArea !== undefined && modulesArea.children().length > 1) {
                                modulesArea.find(".dragAndDropPlaceholder").remove();
                            } else if (modulesArea !== undefined && modulesArea.children().length === 0) {
                                modulesArea.append(dragAndDropModulePlaceholder.clone());
                            }
                        }
                    }
                }
            },
            dragstart: drag
        }
    });

    /**
     * Declaration of the semester body, here the modules
     * will be appended when the user drops modules on the semester.
     */
    var semesterBody = $('<div></div>', {
        id: semesterBodyId,
        "class": "card-body modulesArea px-2 pt-2 pb-0",
        on: {
            dragover: function (event) {
                event.preventDefault();
            },
            drop: function (event) {
                event.preventDefault();
                //because jQuery only passes the jQuery event object instead of the browser event object
                event.dataTransfer = event.originalEvent.dataTransfer;
                var draggedElementId = event.dataTransfer.getData("text");
                
                if ($('#' + draggedElementId).hasClass("semester")) {return;}
                
                testAndSetPlaceholder(draggedElementId);
                //semesterBody.find('.dragAndDropPlaceholder').remove();
                $(this).find('.dragAndDropPlaceholder').remove();
                $('#' + draggedElementId).detach().appendTo(this);
            },
            drag: function (event) { //child has the drag function because parent has no clickable area (stub for later use)
            }
        }
    }).append(dragAndDropModulePlaceholder.clone());

    /**
     * Construction of the semester title.
     * Also the drag and drop logic for the title gets added (see dragover and drop).
     */
    var semesterTitle = $('<div></div>', {
        "class": "card-header",
        html: "<span id=" + spanId + ">" + semesterCounter + ". Semester</span>",
        on: {
            dragover: function (event) {
                event.preventDefault();
            },
            /**
             * When the user drops modules on the title the module will be appended on its body
             */
            drop: function (event) {
                event.preventDefault();
                //because jQuery only passes the jQuery event object instead of the browser event object
                event.dataTransfer = event.originalEvent.dataTransfer;
                var draggedElementId = event.dataTransfer.getData("text");
                
                if ($('#' + draggedElementId).hasClass("semester")) {return;}
                
                testAndSetPlaceholder(draggedElementId);
                semesterBody.find('.dragAndDropPlaceholder').remove();
                $('#' + draggedElementId).detach().appendTo(semesterBody);
            }
        }
    });

    $('<button></button>', {
        "class": "btn btn-danger py-0 px-2 float-right",
        "id": delBtnId,
        on: {
            /**
             * Delete functionality, moves all modules from the semester to delete to the unmapped modules list
             * and moves all modules from the higher semesters one semester down until the semester to delete has modules from its
             * following semester.
             *
             * E.g We have 4 semesters and want to delete the 2nd. The elements from the 2nd semester will be moved to the unmapped modules list
             * and the modules from the 4th semester will be move to the 3rd and the modules from the 3rd will be moved to the 2nd semester.
             * The 4th semester which is empty now, gets deleted.
             */
            click: function (event) {
                /*
                 * Browser issue: When the span character is clicked in Chrome: Event target is a span
                 * In Firefox it is the button.
                 */
                var idToDelete = delBtnSemesterId[event.target.id || $(event.target).parent().attr('id') || $(event.target).parent().parent().attr('id')];
                var semesterToDelete = listOfSemester[idToDelete];
                var lastSemester = $("#semesterContainer").children(":last");
                var modules = {};
                var modulesLast = null;

                for (var i = semesterCounter - 1; i >= semesterToDelete.semesterNr; i--) {
                    var idThis = "#semester" + i;
                    var modulesArea = $(idThis).find("div.modulesArea");

                    modules = $(idThis).find("div.module");
                    if (i > semesterToDelete.semesterNr) {
                        if (modulesLast == null) {
                            modulesArea.empty();
                            modulesLast = modules;
                        } else {
                            modulesArea.empty().append(modulesLast);
                            modulesLast = modules;
                        }
                    }
                    else if (i == semesterToDelete.semesterNr) {
                        $("#unmappedModulesList").append(modules);
                        modulesArea.empty();
                        modulesArea.append(modulesLast);
                    }
                    if (modulesArea.children().length === 0) {
                        modulesArea.append(dragAndDropModulePlaceholder.clone());
                    }
                }

                lastSemester.remove();
                semesterCounter--;
            }
        }
    }).append($('<i></i>', {
            "class": "fa fa-trash"
        })
    ).appendTo(semesterTitle);

    semester.append(semesterTitle);
    semester.append(semesterBody);
    $("#semesterContainer").append(semester);
    semesterCounter++;
}

function allowDrag(event) {
    event.preventDefault();
}

/**
 * Drop logic for the unmapped modules list.
 */
function moduleListDrop(event) {
    event.preventDefault();
    var idDataTransfer = '#'+(event.dataTransfer.getData("text"));
    if ($(idDataTransfer).hasClass("semester")) {
        var modulesArea = $(idDataTransfer).find("div.modulesArea");
        var modules = $(idDataTransfer).find("div.module");
        modulesArea.empty();
        $("#unmappedModulesList").append(modules);
        modulesArea.append(dragAndDropModulePlaceholder.clone());
        return;
    }
    var moduleId = event.dataTransfer.getData("text");
    testAndSetPlaceholder(moduleId);
    $('#' + moduleId).detach().appendTo($('#unmappedModulesList'));
}

/**
 * Generic drag event which enables the data transfer of the dragged object.
 */
function drag(event) {
    //in case the event is a jQuery object and not the Browser object
    if (event.dataTransfer === undefined) {
        event = event.originalEvent;
    }
    event.dataTransfer.setData("text", event.target.id);
}

/**
 * Checks whether the parent is a semester body. If it is and the dragged module was its last child, insert the drag
 * and drop placeholder.
 */
function testAndSetPlaceholder(draggedModuleId) {
    var modulesArea = $('#' + draggedModuleId).closest(".modulesArea");
    if (modulesArea !== undefined && modulesArea.children().length === 1) {
        modulesArea.append(dragAndDropModulePlaceholder.clone());
    }
}

/**
 * Collects the data from the create stub popup and creates a jquery object from the data.
 */
function createStub() {

    if ($('#stub_title').val()) {

        //reset title field if it was marked as invalid
        if($('#stub_title').hasClass('is-invalid')){
            $('#stub_title').removeClass('is-invalid');
        }

        var stub = {
            code: $('#stub_code').val(),
            title: $('#stub_title').val(),
            areaOfStudies: areaOfStudiesMap[$('#areaOfStudies').val().replace("#","")],
            lecturers: $('#stub_lecturers').val(),
            stubId: nextStubId
        };
        moduleStubs[nextStubId] = stub;
        nextStubId++;

        $('#createModuleStub').modal('hide');
        $('#stubForm').trigger('reset');
        addStub(stub);

    } else if (!$('#stub_title').hasClass('is-invalid')) {

        //mark title field as invalid
        $('#stub_title').addClass("is-invalid");

    }

}

/**
 * Creates the card for the new stub.
 */
function addStub(stub) {
//    var areaOfStudiesColor;
//    areaOfStudiesList.forEach(function(areaOfStudies) {
//        if (areaOfStudies.id == stub.areaOfStudies) {
//            areaOfStudiesColor = areaOfStudies.colorRGB;
//        }
//    });
    $('<div></div>', {
        "class": "card module",
        "style": "background-color:rgb(" + areaOfStudiesMap[stub.areaOfStudies.id].colorRGB + ")",
        id: "modulelist_stub" + stub.stubId,
        "data-stub-id": stub.stubId,
        draggable: true,
        on: {
            dragstart: drag
        }
    }).append(
        $('<div></div>', {
            "class": "card-header text-truncate",
            html: stub.code + ' ' + stub.title
        })
    ).appendTo($('#unmappedModulesList'));
}

/**
 * Saves all mapped Module stubs and Modules.
 * Sends an AJAX request to the REST controller which then saves the data to the DB.
 */
function save() {
    var stubsToBeMapped = [];
    var modulesToBeMapped = [];

    $('#semesterContainer').children('div').each(function (semesterIndex, semester) {
        $(semester).find("div.module").each(function (moduleIndex, module) {
            if (module.hasAttribute("data-module-id")) {

                //module is an existing module, not a stub
                var moduleId = module.getAttribute("data-module-id");
                var module = existingModulesMap[moduleId];
                module.semester = semesterIndex + 1;
                modulesToBeMapped.push(module);

            } else if (module.hasAttribute("data-stub-id")) {

                //module is a stub
                var stubId = module.getAttribute("data-stub-id");
                var stub = moduleStubs[stubId];
                stub.semester = semesterIndex + 1;
                delete stub.stubId;
                stubsToBeMapped.push(stub);

            }
        });
    });

    var exregData = {
        exReg: {
            name: $('#exReg_name').val(),
            validFrom: $('#exReg_validFrom').val()
        },
        newModuleStubs: stubsToBeMapped,
        modulesToBeMapped: modulesToBeMapped
    };

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        url: '/exreg/save',
        dataType: 'json',
        data: JSON.stringify(exregData),
        success: function (data) {
            alert(data.status); //do stuff when save was successful
        }
    });
}

/**
 * Function which will be executed when the site has been loaded.
 * It will append all unmapped modules from the DB to the unmapped modules list.
 */
$(document).ready(function () {
    listOfUnmappedModules.forEach(function (module) {
        existingModulesMap[module.id] = module;
    });
    addNewSemester();
});
