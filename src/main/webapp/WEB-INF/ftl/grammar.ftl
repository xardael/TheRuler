<#include "/layout/header.ftl">

    
		<div class="row">
			<div class="span9">
				<h2>${rc.getMessage("grammar")}</h2>
			</div>
			<div class="span3">
				<!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
				  <button class="btn" href="#">Export</button>
				  <button class="btn" href="#">Delete</button>
				</div> -->
				
				    <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
                                                <li> <a href="${rc.contextPath}/export/${gm.id}">${rc.getMessage("export")}</a></li>
                                                <li> <a href="${rc.contextPath}/edit-grammar/${gm.id}">${rc.getMessage("edit")}</a></li>
                                                <li> <a href="${rc.contextPath}/delete-grammar/${gm.id}" class="delete">${rc.getMessage("delete")}</a></li>
					</ul>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li><a href="${rc.contextPath}/">${rc.getMessage("grammars")}</a> <span class="divider">&gt;</span></li>
                        <#if search == false>
                            <li class="active">${gm.name}</li>
                            <#else>
                            <li><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a> <span class="divider">&gt;</span></li>
                            <li class="active">${rc.getMessage("searchResults")}</li>
                        </#if>  
			
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
            ${gm.description!(rc.getMessage("noDesc"))}
            </div>
          </div>
      </div>

	  <div class="row">
		<div class="span4">
			<h3 style="padding-top: 0">
                            <button type="button" class="btn btn-small" id="collapseBtn" style="margin-bottom: 5px;"><i class="icon-chevron-down" style=""></i></button>
                            <#if search == false>
                            ${rc.getMessage("rules")}
                            <#else>
                            ${rc.getMessage("searchResults")}
                            </#if>  
                        </h3>
		</div>
    <div class="span4">
      <form name="ruleAdd" method="post" action="${rc.contextPath}/rule-add" class="form-inline pull-right" style="margin: 15px 0 0 0">
          <div class="input-append">
            <input type="hidden" name="grammarId" value="${gm.id}">
            <input type="text" name="ruleId" placeholder="${rc.getMessage('newRuleName')}">
            <button type="submit" class="btn">${rc.getMessage("newRule")}</button>
         </div>
    </form>
    </div>
    <div class="span4">
      <form name="ruleSearch" method="GET" class="form-inline pull-right" style="margin: 15px 0 0 0">
          <div class="input-append">
             <input type="hidden" name="search" value="true">
             <input type="text" name="name" placeholder="${rc.getMessage('searchForARule')}" 
                    <#if search == true>
                            value="${searchString}"
                            </#if>  
             >
             <button type="submit" class="btn"><i class="icon-search"></i></button>
          </div>
        </form>
    </div>
		
	  </div>
      
      <div class="row">
          <div class="span12">
              <#if rules?size == 0>
                
                <div class="alert"><strong>
                        <#if (search == true)>
                            ${rc.getMessage("noResults")}
                         <#else>
                            ${rc.getMessage("noRules")}
                        </#if>  
                </strong></div>
              </#if>
              <div class="accordion" id="accordion">
                <table id="rules" style="width: 100%">
                    <#list rules as rule>  
                    <tr>
                        <td style="width: 100%;">
                            <div class="accordion-group">

                                    <div class="accordion-heading">
                                        <a class="accordion-toggle" href="${rc.contextPath}/grammar/${gm.id}/rule/${rule.id}">
                                        ${rule.id}
                                        </a>  
                                    </div>
                                    <div id="collapse${rule.id}" class="accordion-body collapse mcollapse">
                                        <div class="accordion-inner">
                                        <pre>${rule.content?html}</pre>
                                        </div>
                                    </div>

                            </div>
                        </td>
                        <td style="vertical-align: top !important;">
                            <div class="btn-group pull-right" style="height: 32px;">
                                <a class="btn btn-small delete" style="height: 100%; width: 22px;" href="${rc.contextPath}/delete-rule/${gm.id}/${rule.id}"><i class="icon-remove" style="margin-top: 6px;"></i></a>
                            </div>
                        </td>
                    </tr>
                    </#list>
                </table>
              </div>
          </div>
      </div>

<#include "/layout/footer.ftl">