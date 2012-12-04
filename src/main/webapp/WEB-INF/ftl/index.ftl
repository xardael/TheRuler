<#include "/layout/header.ftl">

		<div class="row">
			<div class="span12">
				<h2>The Ruler</h2>
			</div>
		</div>
		
		
	    <ul class="breadcrumb">
			<li>Grammars</li>
            </ul>

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div>
        <p>Maecenas rhoncus, lorem vel lobortis tincidunt, justo nisl pulvinar ipsum, eget adipiscing erat risus a quam. Sed malesuada massa imperdiet erat pretium quis fermentum nulla molestie. Pellentesque fermentum mollis nisl vel adipiscing. Aliquam ac convallis lacus. Sed vel turpis erat. Praesent non purus quis arcu sagittis facilisis. Duis lacinia accumsan eros, eget convallis tortor congue vitae. Aliquam erat volutpat. Phasellus dictum tempus tellus a venenatis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris rutrum malesuada velit eget malesuada.
        Vivamus at quam vel nisi semper vestibulum et eget enim. Mauris in lorem sed ante ultricies vulputate ac in eros. </p>
      </div>

	  <div class="row">
  		<div class="span4">
  			<h3 style="padding-top: 0">Grammars</h3>
  		</div>
      <div class="span4">
        
      </div>
      <div class="span4">
        <form name="newGrammar" method="POST" action="${basePath}/create-grammar" class="form-inline pull-right" style="margin: 15px 0 0 0;">
             <input type="text" name="name" placeholder="New Grammar Name...">
             <button type="submit" class="btn">New Grammar</button>
        </form>
      </div>
	  </div>

    <table class="table table-hover">
      <tr><th>#</th><th>Name</th><th>Description</th><th>Date</th><th></th></tr>
      <#list grammarMetas as gm>
        <tr><td>${gm.id}</td><td><a href="${basePath}/grammar/${gm.id}">${gm.name}</a></td><td>${(gm.description)!"------- No decription -------"}</td><td>${gm.date}</td><td><div class="btn-group pull-right"><a class="btn btn-small" href="#"><i class="icon-file"></i></a><a class="btn btn-small" href="${basePath}/edit-grammar/${gm.id}"><i class="icon-edit"></i></a><a class="btn btn-small" href="${basePath}/delete-grammar/${gm.id}"><i class="icon-remove"></i></a></div></td></tr>
      </#list>
    </table>

<!--        <div class="pagination pagination-centered">
          <ul>
          <li class="disabled"><a href="#">Prev</a></li>
          <li class="active"><a href="#">1</a></li>
          <li><a href="#">2</a></li>
          <li><a href="#">3</a></li>
          <li><a href="#">4</a></li>
          <li><a href="#">5</a></li>
          <li><a href="#">6</a></li>
          <li><a href="#">7</a></li>
          <li><a href="#">8</a></li>
          <li><a href="#">Next</a></li>
          </ul>
        </div>-->

<#include "/layout/footer.ftl">