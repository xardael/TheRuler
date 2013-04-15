<#include "/layout/header.ftl">
<#escape x as x?html>

<div class="row">
    <div class="span12">
        <h2>${rc.getMessage("error")}</h2>
    </div>
</div>


<div class="row">
    <div class="span12">
        <div class="alert alert-block alert-error">
            <h4>${exception.localizedMessage!}</h4>
            <#if exception.stackTrace??>
                <#list exception.stackTrace as line>
                    ${line}<br>
                </#list>
            </#if>
        </div>
    </div>
</div>


</#escape>
<#include "/layout/footer.ftl">