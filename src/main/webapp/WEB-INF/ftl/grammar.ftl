<#include "/layout/header.ftl">

    <div class="container">
		<div class="row">
			<div class="span10">
				<h2>${gm.name}</h2>
			</div>
			<div class="span2">
				<!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
				  <button class="btn" href="#">Export</button>
				  <button class="btn" href="#">Delete</button>
				</div> -->
				
				    <ul class="nav nav-pills" style="padding-top: 20px;">
						<li> <a href="#">Export</a></li>
						<li> <a href="#">Delete</a></li>

					</ul>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li><a href="${basePath}/">Home</a> <span class="divider">&gt;</span></li>
			<li class="active">Grammar edit</li>
		</ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div>
        <p>${gm.description!"No description."}</p>
      </div>

	  <div class="row">
		<div class="span4">
			<h3 style="padding-top: 0">Raw edit</h3>
		</div>
    <div class="span4">
      <form class="form-inline pull-right">
        <input type="text" placeholder="New rule name...">
        <button type="submit" class="btn">New Rule</button>
    </form>
    </div>
    <div class="span4">
      <form class="form-inline pull-right">
        <input type="text" placeholder="Search for a rule...">
        <button type="submit" class="btn">Search</button>
      </form>
    </div>
		
	  </div>
        
	  <textarea rows="20" style="width: 99%">${grammarContent}</textarea>

    <div class="container">
      <a class="btn pull-left" href="#">Insert ruleref</a>

      <a class="btn pull-right" href="#">Save</a>
      <a class="pull-right" style="margin: 5px 20px 0 0;" href="#">Discard changes</a>
    </div>

<#include "/layout/footer.ftl">