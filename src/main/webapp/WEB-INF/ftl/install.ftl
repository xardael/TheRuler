<#include "/layout/header.ftl">

		<div class="row">
			<div class="span12">
				<h2>The Ruler</h2>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li>Installation</li>
            </ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div>
        <p>This will install and configure database for TheRuler.</p>
      </div>

      <form class="form-horizontal" method="post" action="${basePath}/doInstall" name="config">
        <div class="control-group">
            <label class="control-label" for="inputHost">Host:</label>
            <div class="controls">
                <input type="text" id="inputHost" name="inputHost" placeholder="eg. localhost/database">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="inputUser">User:</label>
            <div class="controls">
                <input type="text" id="inuptUser" name="inputUser" placeholder="eg. admin">
            </div>
        </div>
        
    <div class="control-group">
    <label class="control-label" for="inputPassword">Password</label>
    <div class="controls">
    <input type="password" id="inputPassword"  name="inputPass" placeholder="eg. w84gSAb2-kMN">
    </div>
    </div>
        
        <div class="control-group warning">
            <label class="control-label" for="inputName">Database Name:</label>
            <div class="controls">
                <input type="text" id="inuptName"  name="inputName" placeholder="eg. grammars">
                <span class="help-inline">WARNING! If database already exists, it will be overwritten.</span>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label" for="inputPort">Port:</label>
            <div class="controls">
                <input type="text" id="inuptPort"  name="inputPort" placeholder="eg. 1984" value="1984">
            </div>
        </div>
        
    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn">Install</button>
        </div>
    </div>
    
    </form>

    

<#include "/layout/footer.ftl">