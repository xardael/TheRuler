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
                <p><span class="label label-info">Heads up!</span>  ${gm.description!"No description."}</p>
              </div>
          </div>
      </div>

	  <div class="row">
		<div class="span12">
			<h3 style="padding-top: 0">${rule.id}</h3>
		</div>		
	  </div>
        <form name="grammar" method="post" action="${rc.contextPath}/save-grammar-content">
            <input name="meta.id" type="hidden" value="${gm.id}" />
	  <textarea name="content" rows="20" style="width: 99%">${rule.content}</textarea>
          
      <div class="form-actions">
            <a class="btn pull-left" href="#">Insert ruleref</a>
          
            <button type="submit" class="btn pull-right btn-primary">Save changes</button>
            <button type="button" class="btn pull-right" style="margin: 0 20px 0 0;">Cancel</button>
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


<table>
            <tr><td>Enter your name : </td><td> <input type="text" id="name"><br/></td></tr>
            <tr><td>Education : </td><td> <input type="text" id="education"><br/></td></tr>
            <tr><td colspan="2"><input type="button" value="Add Users" onclick="checkAvailability()"><br/></td></tr>
            <tr><td colspan="2"><div id="info" style="color: green;"></div></td></tr>
        </table>
        <a href="/AjaxWithSpringMVC2Annotations/ShowUsers.htm">Show All Users</a>




          
          
    <div class="alert alert-info">
            <strong>Heads up!</strong>
            Navbar links must have resolvable id targets. For example, a <code>&lt;a href="#home"&gt;home&lt;/a&gt;</code> must correspond to something in the dom like <code>&lt;div id="home"&gt;&lt;/div&gt;</code>.
          </div>
     
    <!-- Button to trigger modal -->
    <a href="#myModal" role="button" class="btn" data-toggle="modal">Launch demo modal</a>
     $('#myModal').modal('toggle')
    <!-- Modal -->
    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Modal header</h3>
        </div>
        <div class="modal-body">
            <p>One fine body…</p>
        </div>
        <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        <button class="btn btn-primary">Save changes</button>
        </div>
    </div>
    

<#include "/layout/footer.ftl">