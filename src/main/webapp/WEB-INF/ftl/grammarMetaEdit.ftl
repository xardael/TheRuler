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
            <form name="gm" method="post" action="${rc.contextPath}/save-grammar">
                <input type="hidden" name="id" value="${gm.id}">
                <div class="control-group">
                    <label class="control-label" for="name">Name: </label>
                    <div class="controls">
                        <input type="text" name="name" class="span6" id="name" value="${gm.name}">
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="date">Description: </label>
                    <div class="controls">
                        <textarea name="description" rows="5" class="span6">${(gm.description)!""}</textarea>
                    </div>
                </div>

                <div class="form-actions">


                    <button type="submit" class="btn  btn-primary">Save changes</button>
                </div>
            </form>
        </div>            
    </div>



    <form name="grammar" class="form-horizontal pull-left" method="post" action="${rc.contextPath}/save-grammar-content">
            <input name="meta.id" type="hidden" value="${gm.id}" />
	  <textarea name="content" rows="15" style="width: 99%">${grammar.content}</textarea>
        

            <div class="container">
            <a class="btn pull-left" href="#">Insert ruleref</a>

            <button type="submit" class="btn pull-right">Save</button>
            <a class="pull-right" style="margin: 5px 20px 0 0;" href="#">Discard changes</a>
            </div>
    </form>
    
    <div>&nbsp;</div>
<#include "/layout/footer.ftl">