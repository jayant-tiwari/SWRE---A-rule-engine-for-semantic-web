
var classes=[];
var propertis=[];
var query=[];
var subject,predicate,object,other;
// var result=[["mohit","studies","ds603"],["parth","teaches","ds603"]];
var api = "webapi/Rule/getNode";
$.get(api,function(create,status) {
    if (status === "success") {
        //alert(create);
        for (var i = 0; i<create[0].length; i++)
            classes.push(create[0][i]);
        for (var j = 0; j< create[1].length; j++)
            propertis.push(create[1][j]);
        node();
    }
    else
        alert("fail");
});
function node() {
    var str = "";
    str += `<div class="row mt-1"><div class="col-lg-1 text-center mt-2"><h5>Subject :</h5></div> <div class="col-lg-3 text-center"><select class="form-control" style="border-radius: 50px;"id= "subject" onchange="checkValues(this.value,1)">`;
    for (var i = 0; i < classes.length; i++) {
        str += `<option value="` + classes[i] + `">` + classes[i] + `</option>`;
    }
    str += `<option value="other">Other</option></select><input type="text" id="s1" placeholder="Enter Subject" class="form-control hide"></div> <div class="col-lg-1 text-center mt-2"><h5>Predicate:</h5>
                       </div><div class="col-lg-3 text-center"><select class="form-control" style="border-radius: 50px;" id="predicate">`;
    for (var i = 0; i < propertis.length; i++) {
        str += `<option value="` + propertis[i] + `"
>` + propertis[i] + `</option>`;
    }
    str += `</select></div><div class="col-lg-1 text-center mt-2"><h5>Object :</h5></div><div class="col-lg-3 text-center"><select class="form-control" style="border-radius: 50px;" id="object" onchange="checkValueo(this.value,1)">`;

    for (var i = 0; i < classes.length; i++) {
        str += `<option value="` + classes[i] + `">` + classes[i] + `</option>`;
    }
    str += `<option value="other">Other</option></select><input type="text" id="o1" placeholder="Enter Object" class="form-control hide"></div></div>`;
    $('#nodes').html(str);
    $('.loader').hide();
}

function go(){
    query = [];
    subject="";predicate="";object="";other="";
    $('.loader').show();
    if($('#subject').val()=== "other"){
        subject=$("#s1").val();
        other="sub";
    }
    else
        subject=$('#subject').val();
    predicate=$('#predicate').val();
    if($('#object').val()=== "other"){
        object=$("#o1").val();
        other="obj";
    }
    else
        object=$('#object').val();
    query.push(subject);query.push(predicate);query.push(object);
    var data=JSON.stringify({rules:query});
    $.ajax({
        url: 'webapi/Rule/getQuery',
        type: "POST",
        data: data,
        //enctype:'multipart/form-data',
        processData: false,
        contentType: 'application/json',
        cache: false,
        async: true,
        timeout: 60000,
        success: function (result,status) {
            alert(result);
            showResult(result,predicate);
        }

    });
}

function showResult(result,predicate){
    $("#result").show();
    var str="";
    if(other==="") {
        for (var i = 0; i < result.length; i++) {
            str += `<div class="row mt-1"><div class="col-lg-4"><p>` + result[i][0] + `</p></div><div class="col-lg-4"><p>` + predicate + `</p></div><div class="col-lg-4"><p>` + result[i][1] + `</p></div></div>`;
        }
    }
    else if(other==="sub"){
        for (var i = 0; i < result.length; i++) {
            str += `<div class="row mt-1"><div class="col-lg-4"><p>` + subject+ `</p></div><div class="col-lg-4"><p>` + predicate + `</p></div><div class="col-lg-4"><p>` + result[i]+ `</p></div></div>`;
        }
    }
    else{
        for (var i = 0; i < result.length; i++) {
            str += `<div class="row mt-1"><div class="col-lg-4"><p>` + result[i]+ `</p></div><div class="col-lg-4"><p>` + predicate + `</p></div><div class="col-lg-4"><p>` + object + `</p></div></div>`;
        }
    }
    $('.loader').hide();
    $("#showresult").html(str);

}
function checkValues(val,id) {
    console.log(id);
    if(val==="other"){
        $("#s"+id).show();
    }
    else
        $("#s"+id).hide();
}
function checkValueo(val,id) {
    if(val==="other"){
        $("#o"+id).show();
    }
    else
        $("#o"+id).hide();
}
