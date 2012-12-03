<#include "/layout/header.ftl">

    <div class="container">
		<div class="row">
			<div class="span9">
				<h2>${gm.name}</h2>
			</div>
			<div class="span3">
				<!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
				  <button class="btn" href="#">Export</button>
				  <button class="btn" href="#">Delete</button>
				</div> -->
				
				    <ul class="nav nav-pills pull-right" style="padding-top: 20px;">
                                                <li> <a href="${basePath}/edit-grammar/${gm.id}">Edit</a></li>
						<li> <a href="${basePath}/delete-grammar/${gm.id}">Delete</a></li>
                                                <li> <a href="#">Export</a></li>
					</ul>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li><a href="${basePath}/">Home</a> <span class="divider">&gt;</span></li>
			<li class="active">Grammar</li>
		</ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div>
        <p>${gm.description!"No description."}</p>
      </div>

	  <div class="row">
		<div class="span4">
			<h3 style="padding-top: 0">Rules</h3>
		</div>
    <div class="span4">
      <form name="ruleAdd" method="Post" action="${basePath}/rule-add" class="form-inline pull-right">
        <input type="hidden" name="grammarId" value="${gm.id}">
        <input type="text" name="ruleId" placeholder="New rule name...">
        <button type="submit" class="btn">New Rule</button>
    </form>
    </div>
    <div class="span4">
      <form name="ruleSearch" method="GET" action="${basePath}/rule-search" class="form-inline pull-right">
             <input type="hidden" name="grammarId" value="${gm.id}">
             <input type="text" name="name" placeholder="Search for a Rule...">
             <button type="submit" class="btn">Search</button>
        </form>
    </div>
		
	  </div>
      
      <div class="row">
          <div class="span12">
              <div class="accordion" id="accordion">
                <table style="width: 100%">
                    <#list rules as rule>  
                    <tr>
                        <td>
                            <div class="accordion-group">

                                    <div class="accordion-heading">
                                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapse${rule.id}">
                                        ${rule.id}
                                        </a>                        
                                    </div>
                                    <div id="collapse${rule.id}" class="accordion-body collapse">
                                        <div class="accordion-inner">
                                        <pre>${rule.content?html}</pre>
                                        </div>
                                    </div>

                            </div>
                        </td>
                        <td style="vertical-align: top;">
                            <div class="btn-group pull-right"><a class="btn btn-small" href="${basePath}/grammar/${gm.id}/rule/${rule.id}"><i class="icon-file"></i></a><a class="btn btn-small" href="${basePath}/delete-rule/${gm.id}/${rule.id}"><i class="icon-remove"></i></a></div>
                        </td>
                    </tr>
                    </#list>
                </table>
              </div>
          </div>
      </div>

<#include "/layout/footer.ftl">