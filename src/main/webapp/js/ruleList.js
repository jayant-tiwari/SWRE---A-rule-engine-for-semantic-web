var api = "webapi/Rule";
$.get(api,function(rule,status){
    if(status == "success"){
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