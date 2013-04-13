<#include "/layout/header.ftl">


    <div class="row">
        <div class="span9">
            <h2>Grammar Edit</h2>
        </div>
        <div class="span3">
            <!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
              <button class="btn" href="#">Export</button>
              <button class="btn" href="#">Delete</button>
            </div> -->

            <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
                <li> <a href="${rc.contextPath}/export/${gm.id}">Export</a></li>
                <li class="active"> <a href="${rc.contextPath}/edit-grammar/${gm.id}">Edit</a></li>
                <li> <a href="${rc.contextPath}/delete-grammar/${gm.id}" class="delete">Delete</a></li>

            </ul>
        </div>
    </div>


    <ul class="breadcrumb">
        <li><a href="${rc.contextPath}/">Grammars</a> <span class="divider">&gt;</span></li>
        <li><a href="${rc.contextPath}/grammar/${gm.id}">${gm.name}</a> <span class="divider">&gt;</span></li>
        <li class="active">Grammar Edit</li>
    </ul>

    <div class="row">
        <div class="span12">
            <form name="grammar" method="post" action="${rc.contextPath}/save-grammar">
                <input type="hidden" name="id" value="${gm.id}">
                <input type="hidden" name="date" value="${gm.date}">
                <div class="control-group">
                    <label class="control-label" for="name">Name: </label>
                    <div class="controls">
                        <input type="text" name="name" class="span6" id="name" value="${gm.name}">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="description">Description: </label>
                    <div class="controls">
                        <textarea name="description" rows="5" class="span6">${(gm.description)!""}</textarea>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="date">Content: </label>
                    <div class="controls">
                        <textarea name="content" id="content" rows="15" style="width: 99%">${grammar.content}</textarea>
                    </div>
                </div>
        

            <div class="form-actions">
            <button type="button" class="btn" id="insertRuleref"><i class="icon-plus-sign"></i> Insert ruleref</button>
            <button type="button" class="btn" id="validateXml" style="margin: 0 0 0 10px;" data-loading-text='<img src="${rc.contextPath}/res/img/loading_spinner.gif" width="15" height="15" alt=""> Validating...'><i class="icon-warning-sign"></i> Validate SRGS</button>
          
            <button type="submit" class="btn pull-right btn-primary">Save changes</button>
            <button type="button" class="btn pull-right discard" style="margin: 0 10px 0 0;">Cancel</button>
            </div>
            </form>
        </div>            
    </div>

     <!-- Modal to see validation resulsts -->
    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <h3 id="myModalLabel">Validation result</h3>
        </div>
        <div class="modal-body">
        </div>
        <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        </div>
    </div>

    <div>&nbsp;</div>
<#include "/layout/footer.ftl">