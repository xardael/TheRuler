<#include "/layout/header.ftl">
<#escape x as x?html>

<div class="row">
    <div class="span10">
        <h2>${rc.getMessage("ruleEdit")}</h2>
    </div>
    <div class="span2">
        <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
            <li> <a href="${rc.contextPath}/delete-rule/${gm.id}/${rule.id}" class="delete">${rc.getMessage("deleteRule")}</a></li>
        </ul>
    </div>
</div>

<ul class="breadcrumb">
    <li><a href="${rc.contextPath}/">${rc.getMessage("grammars")}</a> <span class="divider">&gt;</span></li>
    <li><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a> <span class="divider">&gt;</span></li>
    <li class="active">${rule.id}</li>
</ul>

<div class="row">
    <div class="span12">
        <h3>${gm.name}</h3>
    </div>
</div>

<div class="row">
    <div class="span12">
        <div class="well">
            ${((gm.description)!rc.getMessage("noDesc"))}
        </div>
    </div>
</div>

<div class="row">
    <div class="span12">
        <h3 style="padding-top: 0"><span style="font-weight: normal;">${rc.getMessage("rule")}:</span> ${rule.id}</h3>
    </div>		
</div>

<form name="rule" method="post" action="${rc.contextPath}/save-rule">
    <input type="hidden" name="grammarId" value="${gm.id}">
    <input name="id" type="hidden" value="${rule.id}" />
    <textarea name="content" id="content" rows="20" style="width: 99%">${rule.content}</textarea>
    <div class="form-actions">
        <button type="button" class="btn" id="insertRuleref" data-toggle="modal"><i class="icon-plus-sign"></i> ${rc.getMessage("insertRuleref")}</button>
        <button type="submit" class="btn pull-right btn-primary">${rc.getMessage("saveChanges")}</button>
    </div>
</form>          

</#escape>
<#include "/layout/footer.ftl">