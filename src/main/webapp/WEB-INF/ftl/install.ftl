<#include "/layout/header.ftl">
<#escape x as x?html>

<div class="row">
    <div class="span12">
        <h2>${rc.getMessage("grammars")}</h2>
    </div>
</div>




<ul class="breadcrumb">
    <li>${rc.getMessage("databaseInstallation")}</li>
</ul>


<form class="form-horizontal validate" id="installForm" method="post" action="${rc.contextPath}/doInstall" name="config">
    <div class="control-group" id="test">
        <label class="control-label" for="inputHost">${rc.getMessage("host")}:</label>
        <div class="controls">
            <input type="text" class="required" id="inputHost" name="inputHost" placeholder="eg. localhost/database">
            <span class="help-inline"></span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="inputUser">${rc.getMessage("user")}:</label>
        <div class="controls">
            <input type="text" class="required" id="inuptUser" name="inputUser" placeholder="eg. admin">
            <span class="help-inline"></span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label" for="inputPassword">${rc.getMessage("pass")}:</label>
        <div class="controls">
            <input type="password" class="required" id="inputPassword"  name="inputPass" placeholder="eg. w84gSAb2-kMN">
            <span class="help-inline"></span>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label" for="inputName">${rc.getMessage("dbName")}:</label>
        <div class="controls">
            <input type="text" class="required" id="inuptName"  name="inputName" placeholder="eg. grammars">
            <span class="help-inline"></span>
            <div style="margin-top: 10px"><span class="label label-warning">${rc.getMessage("warning")}!</span> ${rc.getMessage("warningMess")}</div>
        </div>

    </div>



    <div class="control-group">
        <label class="control-label" for="inputPort">${rc.getMessage("port")}:</label>
        <div class="controls">
            <input type="text" class="required" id="inuptPort"  name="inputPort" placeholder="eg. 1984" value="1984">
            <span class="help-inline"></span>
        </div>
    </div>

    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn btn-primary">${rc.getMessage("install")}</button>            
        </div>
    </div>

</form>


</#escape>
<#include "/layout/footer.ftl">