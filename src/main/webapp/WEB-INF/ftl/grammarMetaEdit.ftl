<#include "/layout/header.ftl">


    <div class="row">
        <div class="span9">
            <h2>${rc.getMessage("grammarEdit")}</h2>
        </div>
        <div class="span3">
            <!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
              <button class="btn" href="#">Export</button>
              <button class="btn" href="#">Delete</button>
            </div> -->

            <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
                <li> <a href="${rc.contextPath}/export/${gm.id}">${rc.getMessage("export")}</a></li>
                <li class="active"> <a href="${rc.contextPath}/edit-grammar/${gm.id}">${rc.getMessage("edit")}</a></li>
                <li> <a href="${rc.contextPath}/delete-grammar/${gm.id}" class="delete">${rc.getMessage("delete")}</a></li>

            </ul>
        </div>
    </div>


    <ul class="breadcrumb">
        <li><a href="${rc.contextPath}/">${rc.getMessage("grammars")}</a> <span class="divider">&gt;</span></li>
        <li><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a> <span class="divider">&gt;</span></li>
        <li class="active">Grammar Edit</li>
    </ul>

    <div class="row">
        <div class="span12">
            <form name="grammar" class="validate" method="post" action="${rc.contextPath}/save-grammar">
                <input type="hidden" name="id" value="${gm.id}">
                <input type="hidden" name="date" value="${gm.date}">
                <div class="control-group">
                    <label class="control-label" for="name">${rc.getMessage("name")}:</label>
                    <div class="controls">
                        <input type="text" name="name" class="span6 required" id="name" value="${gm.name}">
                        <span class="help-inline" style="margin-bottom: 11px"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="description">${rc.getMessage("desc")}:</label>
                    <div class="controls">
                        <textarea name="description" id="description" rows="5" class="span6">${(gm.description)!""}</textarea>
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
                    <button type="button" class="btn pull-right discard" style="margin: 0 10px 0 0;" disabled>${rc.getMessage("cancel")}</button>
                </div>
            </form>
        </div>            
    </div>

    
    
    
    
    <div>&nbsp;</div>
<#include "/layout/footer.ftl">