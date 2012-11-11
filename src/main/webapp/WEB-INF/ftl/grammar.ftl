<#include "/layout/header.ftl">

    <div class="container">
		<div class="row">
			<div class="span10">
				<h2>Grammar name</h2>
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
			<li><a href="#">Grammars</a> <span class="divider">&gt;</span></li>
			<li><a href="#">Grammar edit</a> <span class="divider">&gt;</span></li>
			<li class="active">Search for a rule </li>
		</ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div>
        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui. </p>
      </div>

	  <div class="row">
		<div class="span4">
			<h3 style="padding-top: 0">Search results</h3>
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

      <!-- Example row of columns -->
      <div class="row">
        <div class="span4">
          <table class="table table-hover table-condensed">
            <tr><td style="border: 0;"><a href="#">Volutpat nunc urna non</a></td><td style="border: 0;"> <a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td style="width: 100%"><a href="#">Non sapien tempus lacinia</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td style="vertical-align: middle"><a href="#">Etiam vel dolor ac mi aliquam </a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td><a href="#">Duis feugiat</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
          </table>
        </div>
        <div class="span4">
          <table class="table table-hover table-condensed">
            <tr><td style="border: 0;"><a href="#">Velit vitae velit malesuada</a></td><td style="border: 0;"><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr class="info"><td style="width: 100%"><a href="#">Sed malesuada massa imperdiet</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td><a href="#">Duis lacinia accumsan eros</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td><a href="#">Mauris ac elit tortor</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
          </table>
       </div>
        <div class="span4">
          <table class="table table-hover table-condensed">
            <tr><td style="border: 0;"><a href="#">Integer bibendum ultricies liber</a></td><td style="border: 0;"><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td style="width: 100%"><a href="#">Phasellus sed lectus</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
            <tr><td><a href="#">Phasellus nec magna</a></td><td><a class="btn btn-small" href="#"><i class="icon-remove"></i></a></td></tr>
          </table>
        </div>
      </div>
	  
	  
	  
	  <textarea rows="20" style="width: 99%">
${text}
    </textarea>

    <div class="container">
      <a class="btn pull-left" href="#">Insert ruleref</a>

      <a class="btn pull-right" href="#">Save</a>
      <a class="pull-right" style="margin: 5px 20px 0 0;" href="#">Discard changes</a>
    </div>

<#include "/layout/footer.ftl">