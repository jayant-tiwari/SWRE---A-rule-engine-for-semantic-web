function uploadfile() {
    var form = $('#ontologyfile')[0];
    var data = new FormData(form);
    console.log("hello");
    console.log(data);
    $.ajax({
        type: "POST",
        url: 'webapi/DataLoader/NewOntology',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType:false,
        cache: false,
        async: true,
        timeout: 60000,
        success: function (status) {
            window.location.assign("/SWRE_war_exploded/workon.html");
        }
    });
}

function triggerUniversityOntology() {

    api = "webapi/DataLoader/University";
    $.get(api,function(status) {
        if(status==="success")
            alert(api);
        window.location.assign("/SWRE_war_exploded/workon.html");
    });
}
