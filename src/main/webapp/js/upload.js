function uploadfile() {
    var form = $('#ontologyfile')[0];
    var data = new FormData(form);
    console.log("hello");
    $.ajax({
        type: "POST",
        url: 'webapi/Rule/fileUpload',
        enctype: 'multipart/form-data',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        async: true,
        timeout: 60000,
        success: function (ontology, status) {
            alert(ontology);
        }
    });
}