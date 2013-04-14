<#include "/layout/header.ftl">

<div class="row">
    <div class="span12">
        <h2>${rc.getMessage('appName')}</h2>
    </div>
</div>


<ul class="breadcrumb">
    <li>${rc.getMessage('grammars')}</li>
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
                <div class="input-append">
                    <input type="text" name="name" placeholder="${rc.getMessage('newGrammarName')}">
                    <button type="submit" class="btn">${rc.getMessage('newGrammar')}</button>
                </div>
            </div>
        </form>
    </div>
</div>
<#if grammarMetas?size == 0>
    <div class="row">
        <div class="span12">
            <div class="alert"><strong>${rc.getMessage('newGrammar')}</strong></div>
        </div>
    </div>
<#else>
<table class="table table-hover">
    <tr><th>#</th><th>${rc.getMessage('name')}</th><th>${rc.getMessage('desc')}</th><th>${rc.getMessage('date')}</th><th></th></tr>
    <#list grammarMetas as gm>
        <tr><td>${gm_index + 1}.</td><td><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a></td><td>
            <#if gm.description??>
                <#if (gm.description?length > 100)>
                    ${gm.description?substring(0,100)}...
                <#else>
                    ${gm.description}
                </#if>  
            <#else>
                ------- ${rc.getMessage('noDesc')} -------
            </#if>  
        </td><td>${gm.date}</td><td><div class="btn-group pull-right"><a class="btn btn-small" href="${rc.contextPath}/export/${gm.id}" title="${rc.getMessage('export')}"><i class="icon-share"></i></a><a class="btn btn-small" href="${rc.contextPath}/edit-grammar/${gm.id}" title="${rc.getMessage('edit')}"><i class="icon-pencil"></i></a><a class="btn btn-small delete" href="${rc.contextPath}/delete-grammar/${gm.id}" title="${rc.getMessage('delete')}"><i class="icon-remove"></i></a></div></td></tr>
    </#list>
</table>


</#if>

<#include "/layout/footer.ftl">