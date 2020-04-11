var if_rules=[];
var existing_rules=[];
var classes=[];
var propertis=[];
var then_rules={subject:"",predicate:"",object:""};
var api = "webapi/Rule";
$.get(api,function(rule,status){

    if(status === "success"){
        var str="";
        alert(rule);
        var l = rule.length;
        for(var i=0;i<l;i++){

            str+=`
			<div class="form-group">
				<label class="form-check-label">
					<input type="checkbox" class="form-check-input existing" value="${i}">${rule[i]}
				</label>
			</div>
		`;
        }
        $('#existingrules').html(str);
    }
});

function createRule(){
    var api = "webapi/Rule/getNode";
    $.get(api,function(create,status) {
        if (status === "success") {
           alert(create);
            alert(create.length);
                for (var i = 0; i<create[0].length; i++)
                    classes.push(create[0][i]);
                //console.log(classes);
                for (var j = 0; j< create[1].length; j++)
                    propertis.push(create[1][j]);
        }
        else
            alert("fail");
    });
    $('#existSection').hide();
    $('#ifSection').show();
    //addRule();
}
function addRule(){

            var str="";
            str+=`<div class="row mt-1">
            <div class="col-lg-3 text-center">
            <select class="form-control subject">`;
            for(var i=0;i<classes.length;i++){
                str+=` <option value="`+classes[i]+`">`+classes[i]+`</option>`;
            }
            str+=`</select></div> <div class="col-lg-3 text-center"><select class="form-control predicate">`;
            for(var i=0;i<propertis.length;i++){
                str+=` <option value="`+propertis[i]+`">`+propertis[i]+`</option>`;
            }
            str+=`</select> </div> <div class="col-lg-3 text-center"><select class="form-control object">`;

            for(var i=0;i<classes.length;i++){
                str+=` <option value="`+classes[i]+`">`+classes[i]+`</option>`;
            }
            str+=`</select> </div> <div class="col-lg-3 text-center"><select class="form-control and"><option value="AND">AND</option>
            <option value="OR">OR</option> </select></div></div>`;

            $('#ifrules').append(str);
}

function done(){
    $('#thenSection').show();
    var subject=$('.subject');
    var predicate=$('.predicate');
    var object=$('.object');
    var and=$('.and');
    console.log(subject);
    for(var i=0;i<subject.length;i++){
        var temp={subject:subject[i].value,predicate:predicate[i].value,object:object[i].value,and:and[i].value};
        if_rules.push(temp);
    }
    console.log(rules);
}
function submitE(){
    var existing=$('.existing');
    for(var i=0;i<existing.length;i++){
        if(existing[i].checked==true)
            existing_rules.push(existing[i].value);
    }
    console.log(JSON.stringify(existing_rules));
    // var data = new FormData();
    // for(var i=0;i<existing_rules.length;i++)
    // data.append("rules",existing_rules[i]);
    var data=JSON.stringify({rules:existing_rules});
    $.ajax({
        url: 'webapi/Rule/userule',
        type: "POST",
        data: data,
        //enctype:'multipart/form-data',
        processData: false,
        contentType: 'application/json',
        cache: false,
        async: true,
        timeout: 60000,
        success: function (rule,status) {
            alert(rule);
            location.reload(true);
        }

    });
}
function submit(){
    then_rules.subject=$('#then_subject').val();
    then_rules.predicate=$('#then_predicate').val();
    then_rules.object=$('#then_object').val();
    console.log(then_rules);
}
