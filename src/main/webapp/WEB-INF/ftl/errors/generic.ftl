<#include "/layout/header.ftl">

		<div class="row">
			<div class="span12">
				<h2>${rc.getMessage("error")}</h2>
			</div>
		</div>
		
		
	    <div class="row">
			<div class="span12">
				<div class="alert alert-block alert-error">
                                    ${exception.message}
                                </div>
			</div>
		</div>

        

<#include "/layout/footer.ftl">