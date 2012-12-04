<#include "/layout/header.ftl">

		<div class="row">
			<div class="span10">
				<h2>Rule edit</h2>
			</div>
			<div class="span2">
				<!-- <div class="btn-group pull-right"  style="margin-top: 10px;">				
				  <button class="btn" href="#">Export</button>
				  <button class="btn" href="#">Delete</button>
				</div> -->
				
				    <ul class="nav nav-pills pull-right" style="margin: 15px 0 0 0">
						<li> <a href="#">Delete Rule</a></li>
					</ul>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li><a href="${basePath}/">Grammars</a> <span class="divider">&gt;</span></li>
                        <li><a href="${basePath}/grammar/${gm.id}">${gm.name}</a> <span class="divider">&gt;</span></li>
			<li class="active">${rule.id}</li>
		</ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div class="row">
          <div class="span12">
            <h3>${gm.name}</h3>
          </div>
      </div>
      <div class="row">
          <div class="span12">
            <p>${gm.description!"No description."}</p>
          </div>
      </div>

	  <div class="row">
		<div class="span4">
			<h3 style="padding-top: 0">${rule.id}</h3>
		</div>
    <div class="span4">
        <form class="form-inline pull-right" style="margin: 15px 0 0 0">
        <input type="text" placeholder="New rule name...">
        <button type="submit" class="btn">New Rule</button>
    </form>
    </div>
    <div class="span4">
      <form name="ruleSearch" method="GET" action="${basePath}/rule-search" class="form-inline pull-right" style="margin: 15px 0 0 0">
             <input type="hidden" name="grammarId" value="${gm.id}">
             <input type="text" name="name" placeholder="Search for a Rule...">
             <button type="submit" class="btn">Search</button>
        </form>
    </div>
		
	  </div>
        <form name="grammar" class="form-horizontal pull-left" method="post" action="${basePath}/save-grammar-content">
            <input name="meta.id" type="hidden" value="${gm.id}" />
	  <textarea name="content" rows="20" style="width: 99%">${rule.content}</textarea>
        

    <div class="container">
      <a class="btn pull-left" href="#">Insert ruleref</a>

      <button type="submit" class="btn pull-right">Save</button>
      <a class="pull-right" style="margin: 5px 20px 0 0;" href="#">Discard changes</a>
    </div>
</form> 
<#include "/layout/footer.ftl">