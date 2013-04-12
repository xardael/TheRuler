<#include "/layout/header.ftl">

		<div class="row">
			<div class="span12">
				<h2>Error! Error</h2>
			</div>
		</div>
		
		
	    <div class="row">
			<div class="span12">
				<div class="alert alert-block alert-error">
                                    <h4>Error!</h4>
                                    <#if exception.stackTrace??>
                                        <#list exception.stackTrace as line>
                                            ${line}<br>
                                        </#list>
                                    <#else>
                                        b
                                    </#if>
                                    ${exception.message?if_exists}
                                </div>
			</div>
		</div>

        

<#include "/layout/footer.ftl">