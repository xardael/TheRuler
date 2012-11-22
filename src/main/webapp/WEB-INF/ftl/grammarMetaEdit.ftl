<#include "/layout/header.ftl">

<div class="container">
    <div class="row">
        <div class="span10">
            <h2>Grammar Meta Edit</h2>
        </div>
        <div class="span2">
            <!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
              <button class="btn" href="#">Export</button>
              <button class="btn" href="#">Delete</button>
            </div> -->

            <ul class="nav nav-pills" style="padding-top: 20px;">
                <li> <a href="#">Delete</a></li>

            </ul>
        </div>
    </div>


    <ul class="breadcrumb">
        <li><a href="${basePath}">Home</a> <span class="divider">&gt;</span></li>
        <li><a href="#">Grammar Meta Edit</a> <span class="divider">&gt;</span></li>
    </ul>

    <form name="gm" class="form-horizontal pull-left" method="post" action="${basePath}/save-grammar">
        <input type="hidden" name="id" value="${gm.id}">
        <div class="control-group">
            <label class="control-label" for="name">Name: </label>
            <div class="controls">
                <input type="text" name="name" id="name" value="${gm.name}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="date">Date: </label>
            <div class="controls">
                <input type="text" name="date" id="date" value="${gm.date}">
            </div> 
        </div>
        <div class="control-group">
            <div class="controls">
                <textarea name="description" rows="20" style="width: 99%">${(gm.description)!""}</textarea>
            </div>
        </div>
        <div class="control-group">
            <div class="container">
                <a class="btn pull-left" href="#">Insert ruleref</a>

                <button type="submit" class="btn pull-right">Save</button>
                <a class="pull-right" style="margin: 5px 20px 0 0;" href="#">Discard changes</a>
            </div>
        </div>
    </form>

    <div>&nbsp;</div>
<#include "/layout/footer.ftl">