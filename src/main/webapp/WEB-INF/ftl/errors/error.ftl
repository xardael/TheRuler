<#include "/layout/header.ftl">

		<div class="row">
			<div class="span12">
				<h2>Error! Error</h2>
			</div>
		</div>
		
		
	    <div class="row">
			<div class="span12">
				<div class="alert alert-block alert-error">
                                    <h4>Error! ${exception.localizedMessage?if_exists}</h4>
                                    <#if exception.stackTrace??>
                                        <#list exception.stackTrace as line>
                                            ${line}<br>
                                        </#list>
                                    <#else>
                                        b
                                    </#if>
                                </div>
			</div>
		</div>

        

<#include "/layout/footer.ftl">