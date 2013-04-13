<#include "/layout/header.ftl">

		<div class="row">
			<div class="span10">
				<h2>Rule edit</h2>
			</div>
			<div class="span2">
				<!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
				  <button class="btn" href="#">Export</button>
				  <button class="btn" href="#">Delete</button>
				</div> -->
				
				    <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
                                        <li> <a href="${rc.contextPath}/delete-rule/${gm.id}/${rule.id}" class="delete">Delete Rule</a></li>
					</ul>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li><a href="${rc.contextPath}/">Grammars</a> <span class="divider">&gt;</span></li>
                        <li><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a> <span class="divider">&gt;</span></li>
			<li class="active">${rule.id}</li>
		</ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div class="row">
          <div class="span12">
            <h3>${gm.name}</h3>
          </div>
      </div>
      <div class="row">
          <div class="span12">
              <div class="well">
                ${gm.description!"No description."}
              </div>
          </div>
      </div>

	  <div class="row">
		<div class="span12">
                    <h3 style="padding-top: 0"><span style="font-weight: normal;">Rule:</span> ${rule.id}</h3>
		</div>		
	  </div>
        <form name="grammar" method="post" action="${rc.contextPath}/save-grammar-content">
            <input name="meta.id" type="hidden" value="${gm.id}" />
            <textarea name="content" id="content" rows="20" style="width: 99%">${rule.content}</textarea>
          
            <div class="form-actions">
                <button type="button" class="btn" id="insertRuleref" data-toggle="modal"><i class="icon-plus-sign"></i> Insert ruleref</button>
                <button type="button" class="btn" id="validateXml" style="margin: 0 0 0 10px;" data-loading-text='<img src="${rc.contextPath}/res/img/loading_spinner.gif" width="15" height="15" alt=""> Validating...'><i class="icon-warning-sign"></i> Check XML format</button>

                <button type="submit" class="btn pull-right btn-primary">Save changes</button>
                <button type="button" class="btn pull-right discard" style="margin: 0 10px 0 0;">Cancel</button>
           </div>

        </form>          
        
          
          
    
<script type="text/javascript">
        function doAjaxPost() {
            // get the form values
            var name = $('#name').val();
            var education = $('#education').val();

            $.ajax({
                type: "POST",
                url: "${rc.contextPath}/AddUser.htm",
                data: "name=" + name + "&education=" + education,
                success: function(response){
                    // we have the response
                    $('#myModalLabel').html(response);
                    $('#myModal').modal('toggle')
                },
                error: function(e){
                    alert('Error: ' + e);
                }
            });
        }
        
        function checkAvailability() {
        $.getJSON("${rc.contextPath}/ajax/availability", { name: $('#name').val() }, function(gm) {
            $('#info').html(gm.name + " is not available, try ");
        });
    }
        
        function checkasdasfadAvailability() {
            //$.getJSON("${rc.contextPath}/availability", { name: $('#name').val() }, function(availability) {
            //    alert(availability);
            //});
            
            $.ajax({
                url : "${rc.contextPath}/availability",
                data : "name=" + $('#name').val() + "&education=" + $('#education').val(),
                success : function(result) {
                        //alert(result);
                        $('#info').html(result);
                }
                
//                dataType: "JSON",
//                url: "${rc.contextPath}/availability",
//                data: "name=" + $('#name').val() + "&education=" + $('#education').val(),
//                success: function(response){
//                    alert('Error: ' + response);
//                },
//                error: function(e){
//                    alert('Error: ' + e);
//                }
            });
        }
        
        function ajaxik() {
            $.ajax({"type": "POST",
                "contentType": "application/json; charset=utf-8",
                "url": "${rc.contextPath}/ajaxik",
                "data": JSON.stringify({"name": "Ricardo"}),
                "dataType": "json",
                "success": function(resp) {alert(resp.message);}
            });
            
//            $.ajax({
//                "type": "POST",
//                "contentType": "application/json; charset=utf-8",
//                "url": "${rc.contextPath}/availability",
//                "data": JSON.stringify({"name": $('#name').val()}),
//                "dataType": "json",
//                "success": function(resp) {alert(resp.message);}
//            });
        }
        
</script>
 
<#include "/layout/footer.ftl">