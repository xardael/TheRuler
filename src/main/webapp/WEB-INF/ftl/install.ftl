<#include "/layout/header.ftl">
    
            <div class="row">
			<div class="span12">
				<div class="alert alert-block alert-error hidden" id="error">
    <button type="button" class="close">&times;</button>
    <h4>Warning!</h4>
    Best check yo self, you're not...
    </div>
			</div>
		</div>

		<div class="row">
			<div class="span12">
				<h2>The Ruler</h2>
			</div>
		</div>


		
		
	    <ul class="breadcrumb">
			<li>Database installation</li>
            </ul>


      <form class="form-horizontal validate" id="installForm" method="post" action="${rc.contextPath}/doInstall" name="config">
          <div class="control-group" id="test">
            <label class="control-label" for="inputHost">Host:</label>
            <div class="controls">
                <input type="text" class="required" id="inputHost" name="inputHost" placeholder="eg. localhost/database">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="inputUser">User:</label>
            <div class="controls">
                <input type="text" class="required" id="inuptUser" name="inputUser" placeholder="eg. admin">
                <span class="help-inline"></span>
            </div>
        </div>
        
    <div class="control-group">
    <label class="control-label" for="inputPassword">Password:</label>
    <div class="controls">
    <input type="password" class="required" id="inputPassword"  name="inputPass" placeholder="eg. w84gSAb2-kMN">
    <span class="help-inline"></span>
    </div>
    </div>
        
        <div class="control-group">
            <label class="control-label" for="inputName">Database Name:</label>
            <div class="controls">
                <input type="text" class="required" id="inuptName"  name="inputName" placeholder="eg. grammars">
                <span class="help-inline"></span>
                <div style="margin-top: 10px"><span class="label label-warning">Warning!</span> If database already exists, it will be overwritten.</div>
            </div>
            
        </div>
        
          
          
        <div class="control-group">
            <label class="control-label" for="inputPort">Port:</label>
            <div class="controls">
                <input type="text" class="required" id="inuptPort"  name="inputPort" placeholder="eg. 1984" value="1984">
                <span class="help-inline"></span>
            </div>
        </div>

    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn btn-primary">Install</button>            
        </div>
    </div>
    
    </form>
     
      

<#include "/layout/footer.ftl">