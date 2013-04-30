<#include "/layout/header.ftl">
<#escape x as x?html>

<div class="row">
    <div class="span9">
        <h2>${rc.getMessage("grammarEdit")}</h2>
    </div>
    <div class="span3">
        <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
            <li> <a href="${rc.contextPath}/export/${grammar.id}">${rc.getMessage("export")}</a></li>
            <li class="active"> <a href="${rc.contextPath}/edit-grammar/${grammar.id}">${rc.getMessage("edit")}</a></li>
            <li> <a href="${rc.contextPath}/delete-grammar/${grammar.id}" class="delete">${rc.getMessage("delete")}</a></li>

        </ul>
    </div>
</div>


<ul class="breadcrumb">
    <li><a href="${rc.contextPath}/">${rc.getMessage("grammars")}</a> <span class="divider">&gt;</span></li>
    <li><a href="${rc.contextPath}/grammar/${grammar.id}">${grammar.name}</a> <span class="divider">&gt;</span></li>
    <li class="active">${rc.getMessage("grammarEdit")}</li>
</ul>

<div class="row">
    <div class="span12">
        <form name="grammar" class="validate" method="post" action="${rc.contextPath}/save-grammar">
            <input type="hidden" name="id" value="${grammar.id}">
            <div class="control-group">
                <label class="control-label" for="name">${rc.getMessage("name")}:</label>
                <div class="controls">
                    <input type="text" name="name" class="span6 required" id="name" value="${grammar.name}">
                    <span class="help-inline" style="margin-bottom: 11px"></span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="description">${rc.getMessage("desc")}:</label>
                <div class="controls">
                    <textarea name="description" id="description" rows="5" class="span6">${(grammar.description)!""}</textarea>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="content">${rc.getMessage("content")}: </label>
                <div class="controls">
                    <textarea name="content" id="content" rows="15" style="width: 99%">${grammar.content}</textarea>
                </div>
            </div>


            <div class="form-actions">
                <button type="button" class="btn" id="insertRuleref" data-toggle="modal"><i class="icon-plus-sign"></i> ${rc.getMessage("insertRuleref")}</button>
                <button type="button" class="btn" id="validateXml" style="margin: 0 0 0 10px;" data-loading-text='<img src="${rc.contextPath}/res/img/loading_spinner.gif" width="15" height="15" alt=""> Validating...'><i class="icon-warning-sign"></i> ${rc.getMessage("validateSrgs")}</button>

                <button type="submit" class="btn pull-right btn-primary">${rc.getMessage("saveChanges")}</button>
            </div>
        </form>
    </div>            
</div>





<div>&nbsp;</div>
</#escape>
<#include "/layout/footer.ftl">