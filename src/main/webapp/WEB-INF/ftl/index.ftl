<#include "/layout/header.ftl">

		<div class="row">
			<div class="span12">
				<h2>The Ruler</h2>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li>Grammars</li>
            </ul>

            
	  <div class="row">
  		<div class="span4">
  			<h3 style="padding-top: 0">${rc.getMessage("grammars")}</h3>
  		</div>
      <div class="span4">
        
      </div>
      <div class="span4">
        <form name="newGrammar" method="POST" action="${rc.contextPath}/create-grammar" class="form-inline pull-right" style="margin: 15px 0 0 0;">
            <div class="control-group" id="test">

             <input type="text" name="name" placeholder="New Grammar Name...">
             <button type="submit" class="btn">New Grammar</button>

            </div>
        </form>
      </div>
	  </div>
<#if grammarMetas?size == 0>
    <div class="row">
          <div class="span12">
              
                <div class="alert"><strong>No grammars.</strong></div>
              
          </div>
    </div>
<#else>
    <table class="table table-hover">
      <tr><th>#</th><th>Name</th><th>Description</th><th>Date</th><th></th></tr>
      <#list grammarMetas as gm>
        <tr><td>${gm_index + 1}.</td><td><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a></td><td>
                <#if gm.description??>
                    <#if (gm.description?length > 100)>
                        ${gm.description?substring(0,100)}...
                    <#else>
                        ${gm.description}
                    </#if>  
                            <#else>
                            ------- No decription -------
                            
                            </#if>  
                
            </td><td>${gm.date}</td><td><div class="btn-group pull-right"><a class="btn btn-small" href="${rc.contextPath}/export/${gm.id}" title="Export"><i class="icon-share"></i></a><a class="btn btn-small" href="${rc.contextPath}/edit-grammar/${gm.id}" title="Edit"><i class="icon-pencil"></i></a><a class="btn btn-small delete" href="${rc.contextPath}/delete-grammar/${gm.id}" title="Delete"><i class="icon-remove"></i></a></div></td></tr>
      </#list>
    </table>


</#if>

<!--        <div class="pagination pagination-centered">
          <ul>
          <li class="disabled"><a href="#">Prev</a></li>
          <li class="active"><a href="#">1</a></li>
          <li><a href="#">2</a></li>
          <li><a href="#">3</a></li>
          <li><a href="#">4</a></li>
          <li><a href="#">5</a></li>
          <li><a href="#">6</a></li>
          <li><a href="#">7</a></li>
          <li><a href="#">8</a></li>
          <li><a href="#">Next</a></li>
          </ul>
        </div>-->

<#include "/layout/footer.ftl">